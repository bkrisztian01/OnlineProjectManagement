import { array, bool, number, object, string } from 'yup';
import { Status } from '../util/Status';

const id = {
  id: string().required(),
};

const projectId = {
  projectId: string().required(),
};

const deadline = {
  deadline: string().test(
    'is-date',
    d => `${d.path} is not a valid date`,
    value => value === undefined || !isNaN(new Date(value).getTime()),
  ),
};

const status = {
  status: string().test(
    'is-status',
    d => `${d.path} is not a valid status`,
    value =>
      value === undefined || Status[value as keyof typeof Status] !== undefined,
  ),
};

export const createTaskSchema = object({
  params: object({
    ...projectId,
  }),
  body: object({
    name: string().required(),
    description: string(),
    ...deadline,
  }),
});

export const updateTaskByIdSchema = object({
  params: object({
    ...id,
    ...projectId,
  }),
  body: object({
    name: string(),
    description: string(),
    ...deadline,
    ...status,
    assigneeIds: array().of(number()),
    prerequisiteTaskIds: array().of(number()),
  }),
});

export const getTaskByIdSchema = object({
  params: object({
    ...id,
    ...projectId,
  }),
});

export const deleteTaskByIdSchema = object({
  params: object({
    ...id,
    ...projectId,
  }),
});

export const archiveTaskByIdSchema = object({
  params: object({
    ...id,
    ...projectId,
  }),
  body: object({
    archived: bool().required(),
  }),
});
