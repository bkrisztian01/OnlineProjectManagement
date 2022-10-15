import type {User} from '../models/users.model';

export const users: User[] = [
	{
		id: 1,
		username: 'kissbela',
		password: 'beluska123',
		fullname: 'Kiss Béla',
		email: 'kissbela@gmail.com',
	},
	{
		id: 2,
		username: 'mariska',
		password: '12jelszo34',
		fullname: 'Kovács Marianna',
		email: 'mariska1980@gmail.com',
	},
];

export function getUserById(id: number) {
	return users.find(user => user.id === id);
}
