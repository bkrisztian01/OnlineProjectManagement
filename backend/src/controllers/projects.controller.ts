import type {Request, Response, NextFunction} from 'express';
import {createProject, deleteProjectById, getProjectById, getProjects, updateProjectById} from '../services/projects.service';

export function getProjectsHandler(req: Request, res: Response, next: NextFunction) {
	res.send(getProjects());
}

export function postProjectHandler(req: Request, res: Response, next: NextFunction) {
	const project = createProject(req.body.name, req.body.description);
	res.send(project);
}

export function getProjectByIdHandeler(req: Request, res: Response, next: NextFunction) {
	const project = getProjectById(parseInt(req.params.id));
	if (!project) {
		res.status(404).send('Project was not found');
		return;
	}

	res.send(project);
}

export function updateProjectByIdHandler(req: Request, res: Response, next: NextFunction) {
	try {
		updateProjectById(
			parseInt(req.params.id),
			req.body.name,
			req.body.description,
			new Date(req.body.startDate),
			new Date(req.body.endDate),
			req.body.estimatedTime,
		);

		res.send('Successful operation');
	} catch (e: unknown) {
		res.status(404).send((e as Error).message);
	}
}

export function deleteProjectByIdHandler(req: Request, res: Response, next: NextFunction) {
	if (!deleteProjectById(parseInt(req.params.id))) {
		res.status(204).send('Project was not found');
		return;
	}

	res.status(200).send('Successful operation');
}
