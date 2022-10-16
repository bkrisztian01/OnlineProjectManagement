import type {User} from '../models/users.model';

export const users: User[] = [];

export function getUserById(id: number) {
	return users.find(user => user.id === id);
}
