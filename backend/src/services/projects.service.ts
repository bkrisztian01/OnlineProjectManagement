import { NotFound } from '@curveball/http-errors/dist';
import { Project } from '../models/projects.model';

function nullCheck(project: Project) {
  if (!project.tasks) {
    project.tasks = [];
  }
  if (!project.milestones) {
    project.milestones = [];
  }
  if (!project.userRoles) {
    project.userRoles = [];
  }
  return project;
}

export async function getProjects() {
  const projects = await Project.find({
    relations: ['tasks', 'milestones'],
  });

  projects.forEach(nullCheck);

  return projects;
}

export async function createProject(name: string, description: string) {
  const project = Project.create({
    name,
    description: description || '',
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
  estimatedTime: number,
) {
  const project = await getProjectById(id);
  if (!project) {
    throw new NotFound('Project was not found');
  }

  project.name = name || project.name;
  project.description = description || project.description;
  project.startDate = startDate || project.startDate;
  project.endDate = endDate || project.endDate;
  project.estimatedTime = estimatedTime || project.estimatedTime;
  project.save();

  return nullCheck(project);
}

export async function deleteProjectById(id: number) {
  await Project.remove(await getProjectById(id));
}
