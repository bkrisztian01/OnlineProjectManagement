import {object, string, number} from 'yup';

const params = {
	params: object({
		id: string().required(),
	}),
};

const dates = {
	startDate: string().test(
		'is-date',
		d => `${d.path} is not a valid date`,
		value => value === undefined || !isNaN(new Date(value).getTime()),
	),
	endDate: string().test(
		'is-date',
		d => `${d.path} is not a valid date`,
		value => value === undefined || !isNaN(new Date(value).getTime()),
	),
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
		...dates,
		estimatedTime: number(),
	}),
});

export const getProjectByIdSchema = object({
	...params,
});

export const deleteProjectByIdSchema = object({
	...params,
});
