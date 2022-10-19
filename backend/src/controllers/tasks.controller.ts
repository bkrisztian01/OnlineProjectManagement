import type {Request, Response, NextFunction} from 'express';
import {createTask, deleteTaskById, getTaskById, getTasks, updateTaskById} from '../services/tasks.service';
import {Status} from '../util/Status';

export function getTaskHandler(req: Request, res: Response, next: NextFunction) {
	res.send(getTasks());
}

export function postTaskHandler(req: Request, res: Response, next: NextFunction) {
	try {
		const task = createTask(
			req.body.projectId,
			req.body.name,
			req.body.description,
			new Date(req.body.deadline),
		);
		res.send(task);
	} catch (e: unknown) {
		res.status(404).send((e as Error).message);
	}
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
	try {
		const task = updateTaskById(
			parseInt(req.params.id),
			req.body.name,
			req.body.description,
			Status[req.body.status as keyof typeof Status],
			new Date(req.body.deadline),
			req.body.assigneeIds,
			req.body.prerequisiteTaskIds,
		);

		res.send(task);
	} catch (e: unknown) {
		res.status(404).send((e as Error).message);
	}
}

export function deleteTaskByIdHandler(req: Request, res: Response, next: NextFunction) {
	try {
		deleteTaskById(parseInt(req.params.id));
		res.send('Successful operation');
	} catch (e: unknown) {
		res.status(404).send((e as Error).message);
	}
}
