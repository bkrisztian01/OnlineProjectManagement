import { Conflict, NotFound } from '@curveball/http-errors/dist';
import { Milestone } from '../models/milestones.model';
import { Task } from '../models/tasks.model';
import { Status } from '../util/Status';
import { getProjectById } from './projects.service';

export const milestones: Milestone[] = [];

export async function getMilestones() {
  return await Milestone.find({ relations: ['tasks', 'project'] });
}

export async function getMilestoneById(id: number) {
  return await Milestone.findOne({
    where: { id },
    relations: ['tasks', 'project'],
  });
}

export async function createMilestone(
  projectId: number,
  name: string,
  description: string,
  status: Status,
  deadline: string,
) {
  const project = await getProjectById(projectId);
  if (!project) {
    throw new NotFound('Project was not found');
  }

  const milestone = Milestone.create({
    name,
    description: description || '',
    deadline,
    project,
  });

  return await milestone.save();
}

export async function updateMilestoneById(
  id: number,
  name: string,
  description: string,
  deadline: string,
  status: Status,
  taskIds: number[],
) {
  const milestone = await getMilestoneById(id);
  if (!milestone) {
    throw new NotFound('Milestone was not found');
  }

  const tasks = await Task.createQueryBuilder('task')
    .leftJoinAndSelect('task.project', 'project')
    .select()
    .where('task.id IN (:...taskIds)', { taskIds: taskIds })
    .getMany();

  if (tasks.find(t => t.project.id != milestone.project.id)) {
    throw new Conflict('Task and milestone is not in the same project');
  }
  milestone.name = name || milestone.name;
  milestone.description = description || milestone.description;
  milestone.deadline = deadline || milestone.deadline;
  milestone.status = status || milestone.status;
  milestone.tasks = taskIds ? tasks : milestone.tasks;
  await milestone.save();

  return milestone;
}

export async function deleteMilestoneById(id: number) {
  await Milestone.delete(id);
  return;
}
