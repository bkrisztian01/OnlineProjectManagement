import type {Milestone} from '../models/milestones.model';
import {Status} from '../util/Status';
import {projects} from './projects.service';

export const milestones: Milestone[] = [];

export function getMilestoneById(id: number) {
	return milestones.find(milestone => milestone.id === id);
}

export function deleteMilestoneById(id: number) {
	const index = milestones.findIndex(m => m.id === id);
	if (index === -1) {
		return false;
	}

	projects.forEach(project => {
		const milestoneIndex = project.milestones.findIndex(milestone => milestone.id === id);
		if (milestoneIndex > -1) {
			project.milestones.splice(milestoneIndex, 1);
		}
	});

	milestones.splice(index, 1);
	return true;
}
