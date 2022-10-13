import type {Task} from '../models/tasks.model';
import {Status} from '../util/Status';

export const tasks: Task[] = [
	{
		id: 1,
		name: 'API',
		description: 'REST api',
		status: Status.Done,
		deadline: new Date(Date.now()),
		assignees: [],
		prerequisiteTaskIds: [],
	},
];
