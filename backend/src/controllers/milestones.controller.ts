import type {Request, Response, NextFunction} from 'express';
import type {Milestone} from '../models/milestones.model';
import {deleteMilestoneById, getMilestoneById, milestones} from '../services/milestones.service';
import {getProjectById} from '../services/projects.service';
import {getTaskById} from '../services/tasks.service';
import {Status} from '../util/Status';

export function getMilestonesHandler(req: Request, res: Response, next: NextFunction) {
	res.send(milestones);
}

export function postMilestoneHandler(req: Request, res: Response, next: NextFunction) {
	const project = getProjectById(parseInt(req.body.projectId));
	if (!project) {
		res.status(404).send('Project was not found');
		return;
	}

	const milestone: Milestone = {
		id: milestones.length + 1,
		name: req.body.name as string,
		description: req.body.description as string || '',
		status: Status[req.body.status as keyof typeof Status] as Status || Status.NotStarted,
		deadline: req.body.deadline ? new Date(req.body.deadline) : new Date(0),
		tasks: [],
	};

	milestones.push(milestone);
	project.milestones.push(milestone);
	res.send(milestone);
}

export function getMilestoneByIdHandler(req: Request, res: Response, next: NextFunction) {
	const milestone = getMilestoneById(parseInt(req.params.id));
	if (!milestone) {
		res.status(404).send('Milestone was not found');
		return;
	}

	res.send(milestone);
}

export function updateMilestoneByIdHandler(req: Request, res: Response, next: NextFunction) {
	const milestone = getMilestoneById(parseInt(req.params.id));
	if (!milestone) {
		res.status(404).send('Milestone was not found');
		return;
	}

	if (req.body.taskIds) {
		const tasks = [];
		for (const taskId of req.body.taskIds) {
			const task = getTaskById(taskId);
			if (!task) {
				res.status(404).send('Task was not found');
				return;
			}

			tasks.push(task);
		}

		milestone.tasks = tasks;
	}

	milestone.name = req.body.name as string || milestone.name;
	milestone.description = req.body.description as string || milestone.description;
	milestone.deadline = req.body.deadline ? new Date(req.body.deadline) : milestone.deadline;
	milestone.status = Status[req.body.status as keyof typeof Status] as Status || milestone.status;

	res.status(200).send('Successful operation');
}

export function deleteMilestoneByIdHandler(req: Request, res: Response, next: NextFunction) {
	if (!deleteMilestoneById(parseInt(req.params.id))) {
		res.status(204).send('Project was not found');
		return;
	}

	res.status(200).send('Successful operation');
}
