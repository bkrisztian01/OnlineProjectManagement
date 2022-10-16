import type {Task} from '../models/tasks.model';
import {Status} from '../util/Status';

export const tasks: Task[] = [];

export function getTaskById(id: number) {
	return tasks.find(task => task.id === id);
}
