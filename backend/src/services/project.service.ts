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

export async function getProjects(userId: number, pageNumber: number) {
  const options: any = {
    order: { id: 'ASC' },
    relations: ['userRoles', 'userRoles.user'],
  };

  if (pageNumber && pageNumber > 0) {
    const skipAmount = (pageNumber - 1) * PAGE_SIZE;
    options.skip = skipAmount;
    options.take = PAGE_SIZE;
  }

  const adminRole = await UserRole.findOne({
    where: { user: { id: userId }, role: Role.Admin },
  });

  if (!adminRole) {
    options.where = {
      userRoles: {
        user: { id: userId },
      },
    };
  }

  // Ez nagyon jank
  const projects = (await Project.find(options)).map(p => {
    console.log(p);
    return {
      id: p.id,
      name: p.name,
      description: p.description,
      status: p.status,
      startDate: p.startDate,
      endDate: p.endDate,
      estimatedTime: p.estimatedTime,
      userRoles: p.userRoles,
      userRole: adminRole
        ? adminRole.role
        : p.userRoles.find(r => r.user.id == userId).role,
    };
  });

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

export async function getProjectById(id: number, userId: number) {
  let project: any = await Project.findOne({
    where: { id },
    relations: ['userRoles', 'userRoles.user'],
  });

  let userRole = await UserRole.findOne({
    where: {
      user: { id: userId },
      role: Role.Admin,
    },
  });

  if (!userRole) {
    userRole = await UserRole.findOne({
      where: {
        project: { id },
        user: { id: userId },
      },
    });
  }

  if (userRole) {
    project.userRole = userRole.role;
  }

  return project;
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
  const project = await Project.findOne({
    where: { id },
  });

  if (project) {
    Project.remove(project);
  }
}
