import type {Project} from '../models/projects.model';

export const projects: Project[] = [
	{
		id: 1,
		name: 'New Facebook',
		description: 'New social media',
		startDate: new Date(2022, 8, 12),
		endDate: new Date(0),
		estimatedTime: 0,
		tasks: [],
	},
	{
		id: 2,
		name: 'New Twitter',
		description: 'New social media',
		startDate: new Date(2021, 10, 2),
		endDate: new Date(2022, 6, 25),
		estimatedTime: 0,
		tasks: [],
	},
];

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
