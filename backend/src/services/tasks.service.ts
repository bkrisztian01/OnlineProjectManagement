import { NotFound } from '@curveball/http-errors/dist';
import { Project } from '../models/projects.model';
import { Task } from '../models/tasks.model';
import { User } from '../models/users.model';
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
    relations: ['milestone'],
  });

  if (!task) {
    return null;
  }

  return nullCheck(task);
}

export async function getTasks(projectId: number, pageNumber?: number) {
  const query = Task.createQueryBuilder('task')
    .take(PAGE_SIZE)
    .orderBy('task.id', 'ASC')
    .where('task.project.id = :projectId', { projectId });

  if (pageNumber && pageNumber > 0) {
    const skipAmount = (pageNumber - 1) * PAGE_SIZE;
    query.skip(skipAmount);
  }

  const tasks = await query.getMany();

  return tasks;
}

export async function createTask(
  projectId: number,
  name: string,
  description: string,
  status: Status,
  deadline: string,
) {
  const project = await Project.findOne({ where: { id: projectId } });
  if (!project) {
    throw new NotFound('Project was not found');
  }

  const task = Task.create({
    name,
    description: description || '',
    deadline,
    status,
    project,
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

  const assignees = await User.createQueryBuilder('users')
    .select('users')
    .where('users.id IN (:...assigneeIds)', { assigneeIds: assigneeIds })
    .getMany();

  const prerequisiteTasks = await Task.createQueryBuilder('task')
    .select('task')
    .where('task.id IN (:...prerequisiteTaskIds)', {
      prerequisiteTaskIds: prerequisiteTaskIds,
    })
    .getMany();

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
  await Task.remove(await getTaskById(id, projectId));
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
