import type {Project} from '../models/projects.model';

export const projects: Project[] = [];

export function getProjectById(id: number) {
	return projects.find(project => project.id === id);
}

export function deleteProjectById(id: number) {
	const index = projects.findIndex(p => p.id === id);
	if (index === -1) {
		return false;
	}

	projects.splice(index, 1);
	return true;
}
