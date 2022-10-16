import {object, string, number} from 'yup';

const params = {
	params: object({
		id: number().required(),
	}),
};

export const createProjectSchema = object({
	body: object({
		name: string().required(),
		description: string(),
	}),
});

export const updateProjectByIdSchema = object({
	...params,
	body: object({
		name: string().required(),
		description: string(),
		startDate: string(),
		endDate: string(),
		estimatedTime: number(),
	}),
});

export const getProjectByIdSchema = object({
	...params,
});

export const deleteProjectByIdSchema = object({
	...params,
});
