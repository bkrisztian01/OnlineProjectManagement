import type {Request, Response, NextFunction} from 'express';
import {number} from 'yup';
import type {Project} from '../models/projects.model';
import {deleteProjectById, getProjectById, projects} from '../services/projects.service';

export function getProjectsHandler(req: Request, res: Response, next: NextFunction) {
	res.send(projects);
}

export function postProjectHandler(req: Request, res: Response, next: NextFunction) {
	// TODO: refactor these
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

export function getProjectByIdHandeler(req: Request, res: Response, next: NextFunction) {
	const project = getProjectById(parseInt(req.params.id));
	if (!project) {
		res.status(404).send('Project was not found');
		return;
	}

	res.send(project);
}

export function updateProjectByIdHandler(req: Request, res: Response, next: NextFunction) {
	const project = getProjectById(parseInt(req.params.id));
	if (!project) {
		res.status(404).send('Project was not found');
		return;
	}

	// TODO: refactor these
	project.name = req.body.name as string || project.name;
	project.description = req.body.description as string || project.description;
	project.startDate = req.body.startDate ? new Date(req.body.startDate) : project.startDate;
	project.endDate = req.body.endDate ? new Date(req.body.endDate) : project.endDate;
	project.estimatedTime = parseInt(req.body.estimatedTime) || project.estimatedTime;

	res.status(200).send('Successful operation');
}

export function deleteProjectByIdHandler(req: Request, res: Response, next: NextFunction) {
	if (!deleteProjectById(parseInt(req.params.id))) {
		res.status(204).send('Project was not found');
		return;
	}

	res.status(200).send('Successful operation');
}
