import { NotFound } from '@curveball/http-errors/dist';
import { In } from 'typeorm';
import { AppDataSource } from '../dataSource';
import { Project } from '../models/project.model';
import { User } from '../models/user.model';
import { UserRole } from '../models/userRole.model';
import { Role } from '../util/Role';
import { Status } from '../util/Status';

const PAGE_SIZE = 5;

function nullCheck(project: Project) {
  if (!project.tasks) {
    project.tasks = [];
  }
  if (!project.milestones) {
    project.milestones = [];
  }
  return project;
}

export async function getProjects(pageNumber?: number) {
  const query = Project.createQueryBuilder('project')
    .take(PAGE_SIZE)
    .orderBy('project.id', 'ASC');

  if (pageNumber && pageNumber > 0) {
    const skipAmount = (pageNumber - 1) * PAGE_SIZE;
    query.skip(skipAmount);
  }
  const projects = await query.getMany();

  return projects;
}

export async function createProject(
  name: string,
  description: string,
  startDate: string,
  endDate: string,
  status: Status,
  estimatedTime: number,
  managerId: number,
  memberIds: number[],
) {
  const manager = await User.findOne({
    where: { id: managerId },
  });
  if (!manager) {
    throw new NotFound(`User with id ${managerId} was not found.`);
  }

  const uniqueMemberIds = [...new Set(memberIds)];
  const members = await User.find({
    where: {
      id: In(uniqueMemberIds),
    },
  });

  if (members.length < uniqueMemberIds.length) {
    throw new NotFound(`Some users was not found in memberIds`);
  }

  let result;
  await AppDataSource.transaction(async transactionalEntityManager => {
    const project = Project.create({
      name,
      description: description || '',
      startDate,
      endDate,
      status,
      estimatedTime,
    });

    result = await transactionalEntityManager.save(project);

    const managerRole = UserRole.create({
      project,
      user: manager,
      role: Role.Manager,
    });

    await transactionalEntityManager.save(managerRole);

    members.forEach(async member => {
      const memberRole = UserRole.create({
        project,
        user: member,
        role: Role.Member,
      });

      await transactionalEntityManager.save(memberRole);
    });
  });

  return nullCheck(result);
}

export async function getProjectById(id: number) {
  return await Project.findOne({
    where: { id },
    relations: ['tasks', 'milestones'],
  });
}

export async function updateProjectById(
  id: number,
  name: string,
  description: string,
  startDate: string,
  endDate: string,
  status: Status,
  estimatedTime: number,
) {
  const project = await Project.findOne({
    where: { id },
  });
  if (!project) {
    throw new NotFound('Project was not found');
  }

  project.name = name || project.name;
  project.description = description || project.description;
  project.startDate = startDate || project.startDate;
  project.endDate = endDate || project.endDate;
  project.status = status || project.status;
  project.estimatedTime = estimatedTime || project.estimatedTime;
  project.save();

  return project;
}

export async function deleteProjectById(id: number) {
  const project = await getProjectById(id);
  if (project) {
    Project.remove(project);
  }
}
