import type {Task} from '../models/tasks.model';
import {Status} from '../util/Status';
import {getProjectById, projects} from './projects.service';
import {getUserById} from './users.service';

export const tasks: Task[] = [];

let autoIncrement = 0;

export function getTaskById(id: number) {
	return tasks.find(task => task.id === id);
}

export function getTasks() {
	return tasks;
}

export function createTask(projectId: number, name: string, description: string, deadline: Date) {
	const project = getProjectById(projectId);
	if (!project) {
		throw new Error('Project was not found');
	}

	const task = {
		id: autoIncrement++,
		name,
		description: description || '',
		status: Status.NotStarted,
		deadline,
		assignees: [],
		prerequisiteTaskIds: [],
	};

	tasks.push(task);
	project.tasks.push(task);
	return task;
}

// eslint-disable-next-line max-params
export function updateTaskById(
	taskId: number,
	name: string,
	description: string,
	status: Status,
	deadline: Date,
	assigneeIds: number[],
	prerequisiteTaskIds: number[],
) {
	const task = getTaskById(taskId);
	if (!task) {
		throw new Error('Task was not found');
	}

	const assignees = [];
	if (assigneeIds) {
		for (const userId of assigneeIds) {
			const user = getUserById(userId);
			if (!user) {
				throw new Error('User was not found');
			}

			assignees.push(user);
		}
	}

	if (prerequisiteTaskIds) {
		for (const taskId of prerequisiteTaskIds) {
			if (!getTaskById(taskId)) {
				throw new Error('Prerequisite task was not found');
			}
		}
	}

	task.name = name || task.name;
	task.description = description || task.description;
	task.status = status || task.status;
	task.deadline = isNaN(deadline.getTime()) ? task.deadline : deadline;
	task.prerequisiteTaskIds = prerequisiteTaskIds || task.prerequisiteTaskIds;
	task.assignees = assigneeIds ? assignees : task.assignees;

	return task;
}

export function deleteTaskById(id: number) {
	const index = tasks.findIndex(task => task.id === id);
	if (index === -1) {
		throw new Error('Task was not found');
	}

	projects.forEach(project => {
		const taskIndex = project.tasks.findIndex(task => task.id === id);
		if (taskIndex > -1) {
			project.tasks.splice(taskIndex, 1);
		}
	});

	tasks.forEach(task => {
		const taskIndex = task.prerequisiteTaskIds.findIndex(i => i === id);
		if (taskIndex > -1) {
			task.prerequisiteTaskIds.splice(taskIndex, 1);
		}
	});

	tasks.splice(index, 1);
}
