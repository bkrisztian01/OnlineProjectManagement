import type {Request, Response, NextFunction} from 'express';
import type {User} from '../models/users.model';
import {users} from '../services/users.service';

export function getLoginUserHandler(req: Request, res: Response, next: NextFunction) {
	const username = req.body.username as string;
	const password = req.body.password as string;
	if (!username || !password) {
		res.status(401).send('Invalid credentials');
		return;
	}

	const user = users.find(u => u.username === username && u.password === password);
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
	if (!username) {
		res.status(400).send('Missing username');
		return;
	}

	if (!password) {
		res.status(400).send('Missing password');
		return;
	}

	if (!fullname) {
		res.status(400).send('Missing full name');
		return;
	}

	if (!email) {
		res.status(400).send('Missing email');
		return;
	}

	let user = users.find(u => u.email === email);
	if (user) {
		res.status(409).send('E-mail address is already in use');
		return;
	}

	user = users.find(u => u.username === username);
	if (user) {
		res.status(409).send('Username is already in use');
		return;
	}

	user = {
		id: users.length + 1,
		username,
		password,
		fullname,
		email,
	};
	users.push(user);
	res.send(user);
}

export function getUsersHandler(req: Request, res: Response, next: NextFunction) {
	res.send(users);
}
