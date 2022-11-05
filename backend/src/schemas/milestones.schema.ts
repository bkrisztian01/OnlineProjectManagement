import {object, string, number, array} from 'yup';
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

export const createMilestoneSchema = object({
	body: object({
		projectId: number().required(),
		name: string().required(),
		description: string(),
		...status,
		...deadline,
	}),
});

export const getMilestoneByIdSchema = object({
	...params,
});

export const deleteMilestoneByIdSchema = object({
	...params,
});

export const updateMilestoneByIdSchema = object({
	...params,
	body: object({
		name: string(),
		description: string(),
		...deadline,
		...status,
		taskIds: array().of(number()),
	}),
});
