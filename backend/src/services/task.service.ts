import { NotFound } from '@curveball/http-errors/dist';
import { Project } from '../models/project.model';
import { Task } from '../models/task.model';
import { User } from '../models/user.model';
import { Status } from '../util/Status';

const PAGE_SIZE = 5;

function nullCheck(task: Task) {
  if (!task.assignees) {
    task.assignees = [];
  }
  if (!task.prerequisiteTasks) {
    task.prerequisiteTasks = [];
  }
  if (!task.assignees) {
    task.assignees = [];
  }
  return task;
}

export async function getTaskById(id: number, projectId: number) {
  const task = await Task.findOne({
    where: { id, project: { id: projectId } },
    relations: ['milestone', 'assignees', 'prerequisiteTasks'],
  });

  if (!task) {
    return null;
  }

  return nullCheck(task);
}

export async function getTasks(projectId: number, pageNumber?: number) {
  let options: any = {
    where: {
      project: { id: projectId },
    },
    order: { id: 'ASC' },
    relations: ['assignees', 'prerequisiteTasks', 'milestone'],
  };

  if (pageNumber && pageNumber > 0) {
    const skipAmount = (pageNumber - 1) * PAGE_SIZE;
    options.skip = skipAmount;
    options.take = PAGE_SIZE;
  }

  const tasks = await Task.find(options);

  return tasks;
}

export async function createTask(
  projectId: number,
  name: string,
  description: string,
  status: Status,
  deadline: string,
  assigneeIds: number[],
  prerequisiteTaskIds: number[],
) {
  const project = await Project.findOne({ where: { id: projectId } });
  if (!project) {
    throw new NotFound('Project was not found');
  }

  let assignees = [];
  if (assigneeIds) {
    if (assigneeIds.length === 0) {
      assignees = [];
    } else {
      assignees = await User.createQueryBuilder('users')
        .select('users')
        .where('users.id IN (:...assigneeIds)', { assigneeIds: assigneeIds })
        .getMany();
    }
  }

  let prerequisiteTasks = [];
  if (prerequisiteTaskIds) {
    if (prerequisiteTaskIds.length === 0) {
      prerequisiteTasks = [];
    } else {
      prerequisiteTasks = await Task.createQueryBuilder('task')
        .select('task')
        .where('task.id IN (:...prerequisiteTaskIds)', {
          prerequisiteTaskIds: prerequisiteTaskIds,
        })
        .getMany();
    }
  }

  const task = Task.create({
    name,
    description: description || '',
    deadline,
    status,
    project,
    assignees,
    prerequisiteTasks,
  });

  return nullCheck(await task.save());
}

export async function updateTaskById(
  projectId: number,
  taskId: number,
  name: string,
  description: string,
  status: Status,
  deadline: string,
  assigneeIds: number[],
  prerequisiteTaskIds: number[],
) {
  const task = await getTaskById(taskId, projectId);
  if (!task) {
    throw new NotFound('Task was not found');
  }

  let assignees;
  if (assigneeIds) {
    if (assigneeIds.length === 0) {
      assignees = [];
    } else {
      assignees = await User.createQueryBuilder('users')
        .select('users')
        .where('users.id IN (:...assigneeIds)', { assigneeIds: assigneeIds })
        .getMany();
    }
  }

  let prerequisiteTasks;
  if (prerequisiteTaskIds) {
    if (prerequisiteTaskIds.length === 0) {
      prerequisiteTasks = [];
    } else {
      prerequisiteTasks = await Task.createQueryBuilder('task')
        .select('task')
        .where('task.id IN (:...prerequisiteTaskIds)', {
          prerequisiteTaskIds: prerequisiteTaskIds,
        })
        .getMany();
    }
  }

  task.name = name || task.name;
  task.description = description || task.description;
  task.status = status || task.status;
  task.deadline = deadline || task.deadline;
  task.prerequisiteTasks = prerequisiteTaskIds
    ? prerequisiteTasks
    : task.prerequisiteTasks;
  task.assignees = assigneeIds ? assignees : task.assignees;

  task.save();

  return nullCheck(task);
}

export async function deleteTaskById(id: number, projectId: number) {
  const task = await getTaskById(id, projectId);
  if (task) {
    Task.remove(task);
  }
}

export async function setArchivedTaskById(
  id: number,
  projectId: number,
  archived: boolean,
) {
  const task = await getTaskById(id, projectId);
  if (!task) {
    throw new NotFound('Task was not found');
  }

  task.archived = archived;
  task.save();
}

export async function getTaskStats(projectId: number) {
  const stats = await Task.createQueryBuilder('task')
    .where('task.project.id = :projectId', { projectId })
    .groupBy('task.status')
    .select(['task.status as status', 'COUNT(task.id) as count'])
    .getRawMany();

  stats.map(stat => (stat.count = Number(stat.count)));

  if (!stats.find(item => item.status == Status.Done)) {
  }

  Object.values(Status).forEach(s => {
    if (!stats.find(stat => stat.status == s)) {
      stats.push({
        status: s,
        count: 0,
      });
    }
  });

  return stats;
}
