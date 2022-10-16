import type {Status} from '../util/Status';
import type {Task} from './tasks.model';

export type Milestone = {
	id: number;
	name: string;
	description: string;
	deadline: Date;
	status: Status;
	tasks: Task[];
};
