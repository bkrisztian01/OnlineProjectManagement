import type {Milestone} from './milestones.model';
import type {Task} from './tasks.model';

export type Project = {
	id: number;
	name: string;
	description: string;
	startDate: Date;
	endDate: Date;
	estimatedTime: number;
	tasks: Task[];
	milestones: Milestone[];
};
