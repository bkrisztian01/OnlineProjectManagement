import type {User} from '../models/users.model';

export const users: User[] = [];

export function validatePassword(username: string, password: string) {
	return users.find(u => u.username === username && u.password === password);
}

export function getUserById(id: number) {
	return users.find(user => user.id === id);
}

export function createUser(
	username: string,
	password: string,
	fullname: string,
	email: string,
) {
	let user = users.find(u => u.email === email);
	if (user) {
		throw new Error('E-mail address is already in use');
	}

	user = users.find(u => u.username === username);
	if (user) {
		throw new Error('Username is already in use');
	}

	user = {
		id: users.length + 1,
		username,
		password,
		fullname,
		email,
	};
	users.push(user);
	return user;
}

export function getUsers() {
	return users;
}
