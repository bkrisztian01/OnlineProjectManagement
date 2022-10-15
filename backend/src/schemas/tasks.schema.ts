import {object, string, number, array} from 'yup';

const params = {
	params: object({
		id: number().required(),
	}),
};

export const createTaskSchema = object({
	body: object({
		projectId: number().required(),
		name: string().required(),
		description: string(),
		deadline: string(),
	}),
});

export const updateTaskByIdSchema = object({
	...params,
	body: object({
		name: string(),
		description: string(),
		deadline: string(),
		status: string(),
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
