import type {Request, Response, NextFunction} from 'express';
import {createMilestone, deleteMilestoneById, getMilestoneById, getMilestones, updateMilestoneById} from '../services/milestones.service';

export function getMilestonesHandler(req: Request, res: Response, next: NextFunction) {
	res.send(getMilestones);
}

export function postMilestoneHandler(req: Request, res: Response, next: NextFunction) {
	try {
		const milestone = createMilestone(
			req.body.projectId,
			req.body.name,
			req.body.description,
			req.body.status,
			req.body.deadline,
		);

		res.send(milestone);
	} catch (e: unknown) {
		res.status(404).send((e as Error).message);
	}
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
	try {
		const milestone = updateMilestoneById(
			parseInt(req.params.id),
			req.body.name,
			req.body.description,
			req.body.deadline,
			req.body.status,
			req.body.taskIds,
		);

		res.status(200).send(milestone);
	} catch (e: unknown) {
		res.status(404).send((e as Error).message);
	}
}

export function deleteMilestoneByIdHandler(req: Request, res: Response, next: NextFunction) {
	if (!deleteMilestoneById(parseInt(req.params.id))) {
		res.status(204).send('Project was not found');
		return;
	}

	res.status(200).send('Successful operation');
}
