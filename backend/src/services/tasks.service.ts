import { Task } from '../models/tasks.model';
import { User } from '../models/users.model';
import { Status } from '../util/Status';
import { getProjectById } from './projects.service';

export async function getTaskById(id: number) {
  return await Task.findOne({
    where: { id },
    relations: ['project', 'milestone'],
  });
}

export async function getTasks() {
  return await Task.find({
    relations: ['project', 'milestone'],
  });
}

export async function createTask(
  projectId: number,
  name: string,
  description: string,
  deadline: string,
) {
  const project = await getProjectById(projectId);
  if (!project) {
    throw new Error('Project was not found');
  }

  const task = Task.create({
    name,
    description: description || '',
    deadline,
    project,
  });

  return await task.save();
}

// eslint-disable-next-line max-params
export async function updateTaskById(
  taskId: number,
  name: string,
  description: string,
  status: Status,
  deadline: string,
  assigneeIds: number[],
  prerequisiteTaskIds: number[],
) {
  const task = await getTaskById(taskId);
  if (!task) {
    throw new Error('Task was not found');
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

  return task;
}

export async function deleteTaskById(id: number) {
  await Task.remove(await getTaskById(id));
}

export async function setArchivedTaskById(id: number, archived: boolean) {
  const task = await getTaskById(id);
  if (!task) {
    throw new Error('Task was not found');
  }

  task.archived = archived;
  task.save;
}
