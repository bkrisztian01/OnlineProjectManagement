import { number, object, string } from 'yup';
import { Status } from '../util/Status';

const params = {
  params: object({
    id: string().required(),
  }),
};

const dates = {
  startDate: string()
    .nullable()
    .test(
      'is-date',
      d => `${d.path} is not a valid date`,
      value => value === undefined || !isNaN(new Date(value).getTime()),
    ),
  endDate: string()
    .nullable()
    .test(
      'is-date',
      d => `${d.path} is not a valid date`,
      value =>
        value === undefined ||
        value === null ||
        !isNaN(new Date(value).getTime()),
    ),
};

const status = {
  status: string().test(
    'is-status',
    d => `${d.path} is not a valid status`,
    value =>
      value === undefined ||
      value === null ||
      (<any>Object).values(Status).includes(value),
  ),
};

export const createProjectSchema = object({
  body: object({
    name: string().required(),
    description: string(),
    ...status,
    ...dates,
    estimatedTime: number().nullable(),
  }),
});

export const updateProjectByIdSchema = object({
  ...params,
  body: object({
    name: string().required(),
    description: string(),
    ...status,
    ...dates,
    estimatedTime: number().nullable(),
  }),
});

export const getProjectByIdSchema = object({
  ...params,
});

export const getProjectsSchema = object({
  query: object({
    pageNumber: string().test(
      'convertable-to-number',
      d => `${d.path} must be a number`,
      value => value === undefined || !isNaN(Number(value)),
    ),
  }),
});

export const deleteProjectByIdSchema = object({
  ...params,
});
