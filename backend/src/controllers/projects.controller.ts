import type {Request, Response, NextFunction} from 'express';
import {number} from 'yup';
import type {Project} from '../models/projects.model';
import {projects} from '../services/projects.service';

export function getProjectHandler(req: Request, res: Response, next: NextFunction) {
	res.send(projects);
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

export function getProjectByIdHandeler(req: Request, res: Response, next: NextFunction) {
	const project = projects.find(project => project.id === parseInt(req.params.id));
	if (!project) {
		res.status(404).send('Project was not found');
		return;
	}

	res.send(project);
}

export function putProjectByIdHandler(req: Request, res: Response, next: NextFunction) {
	const index = projects.findIndex(project => project.id === parseInt(req.params.id));
	if (index === -1) {
		res.status(404).send('Project was not found');
		return;
	}

	const project = projects[index];

	const newProject = {
		id: parseInt(req.params.id),
		name: req.body.name as string || project.name,
		description: req.body.description as string || project.description,
		startDate: req.body.startDate ? new Date(req.body.startDate) : project.startDate,
		endDate: req.body.endDate ? new Date(req.body.endDate) : project.endDate,
		estimatedTime: parseInt(req.params.estimatedTime) || project.estimatedTime,
		tasks: projects[index].tasks,
	};
	projects[index] = newProject as Project;

	res.status(200).send('Successful operation');
}

export function deleteProjectByIdHandler(req: Request, res: Response, next: NextFunction) {
	const index = projects.findIndex(project => project.id === parseInt(req.params.id));
	if (index === -1) {
		res.status(204).send('Project was not found');
		return;
	}

	projects.splice(index, 1);
	res.status(200).send('Successful operation');
}
