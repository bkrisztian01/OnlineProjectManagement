import type {Project} from '../models/projects.model';

export const projects: Project[] = [];

export function getProjects() {
	return projects;
}

export function createProject(
	name: string,
	description: string,
) {
	const project: Project = {
		id: projects.length + 1,
		name,
		description: description || '',
		startDate: new Date(Date.now()),
		endDate: new Date(0),
		estimatedTime: 0,
		tasks: [],
		milestones: [],
	};

	projects.push(project);
	return project;
}

export function getProjectById(id: number) {
	return projects.find(project => project.id === id);
}

// eslint-disable-next-line max-params
export function updateProjectById(
	id: number,
	name: string,
	description: string,
	startDate: Date,
	endDate: Date,
	estimatedTime: number,
) {
	const project = getProjectById(id);
	if (!project) {
		throw new Error('Project was not found');
	}

	project.name = name || project.name;
	project.description = description || project.description;
	project.startDate = isNaN(startDate.getTime()) ? project.startDate : startDate;
	project.endDate = isNaN(endDate.getTime()) ? project.endDate : endDate;
	project.estimatedTime = estimatedTime || project.estimatedTime;

	return project;
}

export function deleteProjectById(id: number) {
	const index = projects.findIndex(p => p.id === id);
	if (index === -1) {
		return false;
	}

	projects.splice(index, 1);
	return true;
}
