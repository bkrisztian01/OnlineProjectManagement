import {object, string, number} from 'yup';

const credentials = {
	username: string().required(),
	password: string().required(),
};

export const loginUserSchema = object({
	body: object({
		...credentials,
	}),
});

export const createUserSchema = object({
	body: object({
		...credentials,
		fullname: string().required(),
		email: string().required(),
	}),
});
