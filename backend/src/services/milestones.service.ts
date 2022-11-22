import { Conflict, NotFound } from '@curveball/http-errors/dist';
import { In } from 'typeorm';
import { Milestone } from '../models/milestones.model';
import { Task } from '../models/tasks.model';
import { Status } from '../util/Status';
import { getProjectById } from './projects.service';

function nullCheck(milestone: Milestone) {
  if (!milestone.tasks) {
    milestone.tasks = [];
  }
  return milestone;
}

export async function getMilestones(projectId: number) {
  const milestones = await Milestone.find({
    relations: ['tasks'],
    where: { project: { id: projectId } },
  });

  milestones.forEach(nullCheck);

  return milestones;
}

export async function getMilestoneById(id: number, projectId: number) {
  const milestone = await Milestone.findOne({
    where: { id, project: { id: projectId } },
    relations: ['tasks'],
  });

  if (!milestone) {
    return null;
  }

  return nullCheck(milestone);
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
    status,
  });

  return nullCheck(await milestone.save());
}

export async function updateMilestoneById(
  projectId: number,
  id: number,
  name: string,
  description: string,
  deadline: string,
  status: Status,
  taskIds: number[],
) {
  const milestone = await Milestone.findOne({
    where: { id, project: { id: projectId } },
    relations: ['tasks', 'project'],
  });
  if (!milestone) {
    throw new NotFound('Milestone was not found');
  }

  const tasks = await Task.find({
    relations: ['project'],
    where: {
      id: In(taskIds),
      project: {
        id: milestone.project.id,
      },
    },
  });

  if (tasks.length == 0) {
    throw new Conflict('Task and milestone is not in the same project');
  }
  milestone.name = name || milestone.name;
  milestone.description = description || milestone.description;
  milestone.deadline = deadline || milestone.deadline;
  milestone.status = status || milestone.status;
  milestone.tasks = taskIds ? tasks : milestone.tasks;
  await milestone.save();

  return nullCheck(milestone);
}

export async function deleteMilestoneById(id: number, projectId: number) {
  await Milestone.remove(await getMilestoneById(id, projectId));
  return;
}

export async function setArchivedMilestoneById(
  id: number,
  projectId: number,
  archived: boolean,
) {
  const milestone = await getMilestoneById(id, projectId);
  if (!milestone) {
    throw new NotFound('Milestone was not found');
  }

  milestone.archived = archived;
  milestone.save();
}
