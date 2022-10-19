import type {Request, Response, NextFunction} from 'express';
import {createUser, getUsers, validatePassword} from '../services/users.service';

export function getLoginUserHandler(req: Request, res: Response, next: NextFunction) {
	const username = req.body.username as string;
	const password = req.body.password as string;

	const user = validatePassword(username, password);
	if (!user) {
		res.status(401).send('Invalid credentials');
		return;
	}

	res.send(user);
}

export function getLogoutUserHandler(req: Request, res: Response, next: NextFunction) {
	res.send('Successful operation');
}

export function postSignupUserHandler(req: Request, res: Response, next: NextFunction) {
	const username = req.body.username as string;
	const password = req.body.password as string;
	const fullname = req.body.fullname as string;
	const email = req.body.email as string;

	try {
		const user = createUser(username, password, fullname, email);
		res.send(user);
	} catch (e: unknown) {
		res.status(409).send((e as Error).message);
	}
}

export function getUsersHandler(req: Request, res: Response, next: NextFunction) {
	res.send(getUsers());
}
