import { NotFound } from '@curveball/http-errors/dist';
import { Project } from '../models/projects.model';
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
) {
  const project = Project.create({
    name,
    description: description || '',
    startDate,
    endDate,
    status,
    estimatedTime,
  });
  await project.save();
  return nullCheck(project);
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
  await Project.remove(await getProjectById(id));
}
