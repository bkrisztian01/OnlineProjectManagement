import {object, string, number} from 'yup';

const params = {
	params: object({
		id: number().required(),
	}),
};

export const createMilestoneSchema = object({
	body: object({
		projectId: number().required(),
		name: string().required(),
		description: string(),
		status: string(),
		deadline: string(),
	}),
});

export const getMilestoneByIdSchema = object({
	...params,
});

export const deleteMilestoneByIdSchema = object({
	...params,
});
