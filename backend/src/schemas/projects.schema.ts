import {object, string, number} from 'yup';

const params = {
	params: object({
		id: number().required('ID is required'),
	}),
};

export const createProjectSchema = object({
	body: object({
		name: string().required('Name is required'),
		description: string()
			.notRequired(),
	}),
});

export const updateProjectSchema = object({
	...params,
});

export const deleteProjectSchema = object({
	...params,
});
