import type {User} from './users.model';
import type {Status} from './Status';

export class Task {
	id!: number;
	name!: string;
	description!: string;
	status!: Status;
	deadline!: Date;
	assignees!: User[];
	prerequisiteTaskIds!: number[];
};
