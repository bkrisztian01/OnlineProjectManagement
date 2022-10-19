/* eslint-disable max-params */
import type {Milestone} from '../models/milestones.model';
import type {Status} from '../util/Status';
import {getProjectById, projects} from './projects.service';
import {getTaskById} from './tasks.service';

export const milestones: Milestone[] = [];

export function getMilestones() {
	return milestones;
}

export function getMilestoneById(id: number) {
	return milestones.find(milestone => milestone.id === id);
}

export function createMilestone(
	projectId: number,
	name: string,
	description: string,
	status: Status,
	deadline: Date,
) {
	const project = getProjectById(projectId);
	if (!project) {
		throw new Error('Project was not found');
	}

	const milestone: Milestone = {
		id: milestones.length + 1,
		name,
		description: description || '',
		status,
		deadline,
		tasks: [],
	};

	milestones.push(milestone);
	project.milestones.push(milestone);
	return milestone;
}

export function updateMilestoneById(
	id: number,
	name: string,
	description: string,
	deadline: Date,
	status: Status,
	taskIds: number[],
) {
	const milestone = getMilestoneById(id);
	if (!milestone) {
		throw new Error('Milestone was not found');
	}

	const tasks = [];
	if (taskIds) {
		for (const taskId of taskIds) {
			const task = getTaskById(taskId);
			if (!task) {
				throw new Error('Task was not found');
			}

			tasks.push(task);
		}
	}

	milestone.name = name || milestone.name;
	milestone.description = description || milestone.description;
	milestone.deadline = deadline || milestone.deadline;
	milestone.status = status || milestone.status;
	milestone.tasks = taskIds ? tasks : milestone.tasks;

	return milestone;
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
