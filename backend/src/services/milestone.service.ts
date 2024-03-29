import { Conflict, NotFound } from '@curveball/http-errors/dist';
import { In } from 'typeorm';
import { Milestone } from '../models/milestone.model';
import { Project } from '../models/project.model';
import { Task } from '../models/task.model';
import { Status } from '../util/Status';

const PAGE_SIZE = 5;

function nullCheck(milestone: Milestone) {
  if (!milestone.tasks) {
    milestone.tasks = [];
  }
  return milestone;
}

export async function getMilestones(projectId: number, pageNumber?: number) {
  const options: any = {
    where: {
      project: { id: projectId },
    },
    order: { id: 'ASC' },
    relations: ['tasks'],
  };

  if (pageNumber && pageNumber > 0) {
    const skipAmount = (pageNumber - 1) * PAGE_SIZE;
    options.skip = skipAmount;
    options.take = PAGE_SIZE;
  }

  const milestones = await Milestone.find(options);

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

  return milestone;
}

export async function createMilestone(
  projectId: number,
  name: string,
  description: string,
  status: Status,
  deadline: string,
  taskIds: number[],
) {
  const project = await Project.findOne({ where: { id: projectId } });
  if (!project) {
    throw new NotFound('Project was not found');
  }

  let tasks;
  if (taskIds) {
    if (taskIds.length === 0) {
      tasks = [];
    } else {
      tasks = await Task.find({
        relations: ['project'],
        where: {
          id: In(taskIds),
          project: {
            id: project.id,
          },
        },
      });
    }

    if (tasks.length < taskIds.length && taskIds.length > 0) {
      throw new Conflict('Task and milestone is not in the same project');
    }
  }

  const milestone = Milestone.create({
    name,
    description: description || '',
    deadline,
    project,
    status,
    tasks,
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

  let tasks;
  if (taskIds) {
    if (taskIds.length === 0) {
      tasks = [];
    } else {
      tasks = await Task.find({
        relations: ['project'],
        where: {
          id: In(taskIds),
          project: {
            id: milestone.project.id,
          },
        },
      });
    }

    if (tasks.length < taskIds.length && taskIds.length > 0) {
      throw new Conflict('Task and milestone is not in the same project');
    }
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
  const milestone = await getMilestoneById(id, projectId);
  if (milestone) {
    Milestone.remove(milestone);
  }
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

export async function getMilestoneTasks(id: number, projectId: number) {
  const milestone = await getMilestoneById(id, projectId);
  if (!milestone) {
    throw new NotFound('Milestone was not found');
  }

  const tasks = await Task.find({
    where: {
      milestone: { id },
      project: { id: projectId },
    },
    order: {
      id: 'ASC',
    },
  });

  return tasks;
}
