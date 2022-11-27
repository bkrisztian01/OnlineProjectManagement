import { NotFound } from '@curveball/http-errors/dist';
import { In, SelectQueryBuilder } from 'typeorm';
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

export async function getProjects(userId: number, pageNumber?: number) {
  const query: SelectQueryBuilder<any> = Project.createQueryBuilder(
    'project',
  ).orderBy('project.id', 'ASC');

  const adminRole = await UserRole.findOne({
    where: { user: { id: userId }, role: Role.Admin },
  });

  if (!adminRole) {
    query
      .leftJoinAndSelect('project.userRoles', 'userRole')
      .leftJoinAndSelect('userRole.user', 'user')
      .where('user.id = :userId', { userId })
      .select([
        'project.id',
        'project.name',
        'project.description',
        'project.status',
        'project.startDate',
        'project.endDate',
        'project.estimatedTime',
        'userRole.role as userRole',
      ]);
  }

  if (pageNumber && pageNumber > 0) {
    const skipAmount = (pageNumber - 1) * PAGE_SIZE;
    query.skip(skipAmount);
    query.take(PAGE_SIZE);
  }

  // Ez nagyon jank
  const projects = ((await query.execute()) as any[]).map(item => {
    return {
      id: item.project_id,
      name: item.project_name,
      description: item.project_description,
      status: item.project_status,
      startDate: item.project_startDate,
      endDate: item.project_endDate,
      estimatedTime: item.project_estimatedTime,
      userRole: adminRole ? adminRole.role : item.userrole,
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
