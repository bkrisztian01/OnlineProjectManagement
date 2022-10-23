import type {Status} from './Status';
import type {Task} from './tasks.model';

export class Milestone {
	id!: number;
	name!: string;
	description!: string;
	deadline!: Date;
	status!: Status;
	tasks!: Task[];
};
