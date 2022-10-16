import type {Request, Response, NextFunction} from 'express';
import {getTaskById, tasks} from '../services/tasks.service';
import {Status} from '../util/Status';
import {getProjectById, projects} from '../services/projects.service';
import {getUserById, users} from '../services/users.service';

export function getTaskHandler(req: Request, res: Response, next: NextFunction) {
	res.send(tasks);
}

export function postTaskHandler(req: Request, res: Response, next: NextFunction) {
	const project = getProjectById(parseInt(req.body.projectId));
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

export function getTaskByIdHandler(req: Request, res: Response, next: NextFunction) {
	const task = getTaskById(parseInt(req.params.id));
	if (!task) {
		res.status(404).send('Task was not found');
		return;
	}

	res.send(task);
}

export function updateTaskByIdHandler(req: Request, res: Response, next: NextFunction) {
	const task = getTaskById(parseInt(req.params.id));
	if (!task) {
		res.status(404).send('Task was not found');
		return;
	}

	if (req.body.assigneeIds) {
		const assignees = [];
		for (const userId of req.body.assigneeIds) {
			const user = getUserById(userId);
			if (!user) {
				res.status(404).send('User was not found');
				return;
			}

			assignees.push(user);
		}

		task.assignees = assignees;
	}

	task.name = req.body.name as string || task.name;
	task.description = req.body.description as string || task.description;
	task.status = Status[req.body.status as keyof typeof Status] as Status || task.status;
	task.deadline = req.body.deadline ? new Date(req.body.deadline) : task.deadline;
	task.prerequisiteTaskIds = req.body.prerequisiteTaskIds as number[] || task.prerequisiteTaskIds;

	res.send('Successful operation');
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
