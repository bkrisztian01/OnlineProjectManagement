import type {Request, Response, NextFunction} from 'express';
import type {Task} from '../models/tasks.model';
import {tasks} from '../services/tasks.service';
import {Status} from '../util/Status';
import {projects} from '../services/projects.service';

export function getTaskHandler(req: Request, res: Response, next: NextFunction) {
	res.send(tasks);
}

export function getTaskByIdHandler(req: Request, res: Response, next: NextFunction) {
	const task = tasks.find(task => task.id === parseInt(req.params.id));
	if (!task) {
		res.status(404).send('Task was not found');
		return;
	}

	res.send(task);
}

export function postTaskHandler(req: Request, res: Response, next: NextFunction) {
	const project = projects.find(project => project.id === parseInt(req.body.projectId));
	if (!project) {
		res.status(404).send('Project was not found');
		return;
	}

	const task = {
		id: tasks.length + 1,
		name: req.body.name as string,
		description: req.body.description as string || '',
		status: Status.NotStarted,
		deadline: req.body.deadline ? new Date(req.body.deadline) : new Date(0),
		assignees: [],
		prerequisiteTaskIds: [],
	};

	project.tasks.push(task);
	tasks.push(task);
	res.send(task);
}

export function postProjectHandler(req: Request, res: Response, next: NextFunction) {
	const project = {
		id: projects.length + 1,
		name: req.body.name as string,
		description: req.body.description as string || '',
		startDate: new Date(Date.now()),
		endDate: new Date(0),
		estimatedTime: 0,
		tasks: [],
	};
	projects.push(project);

	res.send(project);
}

export function deleteTaskByIdHandler(req: Request, res: Response, next: NextFunction) {
	const index = tasks.findIndex(task => task.id === parseInt(req.params.id));
	if (index === -1) {
		res.status(404).send('Task was not found');
		return;
	}

	projects.forEach(project => {
		const taskIndex = project.tasks.findIndex(task => task.id === parseInt(req.params.id));
		if (taskIndex > -1) {
			project.tasks.splice(taskIndex, 1);
		}
	});
	tasks.splice(index, 1);

	res.send('Successful operation');
}
