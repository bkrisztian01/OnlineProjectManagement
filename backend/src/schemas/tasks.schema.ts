import {object, string, number, array, bool} from 'yup';
import {Status} from '../util/Status';

const params = {
	params: object({
		id: string().required(),
	}),
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
		value => value === undefined || Status[value as keyof typeof Status] !== undefined,
	),
};

export const createTaskSchema = object({
	body: object({
		projectId: number().required(),
		name: string().required(),
		description: string(),
		...deadline,
	}),
});

export const updateTaskByIdSchema = object({
	...params,
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
	...params,
});

export const deleteTaskByIdSchema = object({
	...params,
});

export const archiveTaskByIdSchema = object({
	...params,
	body: object({
		archived: bool().required(),
	}),
});
