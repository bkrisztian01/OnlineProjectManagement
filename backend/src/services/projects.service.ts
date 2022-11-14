import { Project } from '../models/projects.model';

export async function getProjects() {
  return await Project.find({
    relations: ['tasks', 'milestones'],
  });
}

export async function createProject(name: string, description: string) {
  const project = Project.create({
    name,
    description,
  });
  await project.save();
  return project;
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
    throw new Error('Project was not found');
  }

  project.name = name || project.name;
  project.description = description || project.description;
  project.startDate = startDate || project.startDate;
  project.endDate = endDate || project.endDate;
  project.estimatedTime = estimatedTime || project.estimatedTime;
  project.save();

  return project;
}

export async function deleteProjectById(id: number) {
  await Project.remove(await getProjectById(id));
}
