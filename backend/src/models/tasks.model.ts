import type {User} from './users.model';
import type {Status} from '../util/Status';

export type Task = {
	id: number;
	name: string;
	description: string;
	status: Status;
	deadline: Date;
	assignees: User[];
	prerequisiteTaskIds: number[];
	archived: boolean;
};
