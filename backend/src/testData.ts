import {milestones} from './services/milestones.service';
import {projects} from './services/projects.service';
import {tasks} from './services/tasks.service';
import {users} from './services/users.service';
import {Status} from './util/Status';

export function initTestData() {
	projects.push(
		{
			id: 1,
			name: 'New Facebook',
			description: 'New social media',
			startDate: new Date(2022, 8, 12),
			endDate: new Date(0),
			estimatedTime: 0,
			tasks: [],
			milestones: [],
		},
		{
			id: 2,
			name: 'New Twitter',
			description: 'New social media',
			startDate: new Date(2021, 10, 2),
			endDate: new Date(2022, 6, 25),
			estimatedTime: 0,
			tasks: [],
			milestones: [],
		});

	tasks.push(
		{
			id: 1,
			name: 'API',
			description: 'REST api',
			status: Status.Done,
			deadline: new Date(Date.now()),
			assignees: [],
			prerequisiteTaskIds: [],
		},
		{
			id: 2,
			name: 'Angular',
			description: 'Idk',
			status: Status.InProgress,
			deadline: new Date(Date.now()),
			assignees: [],
			prerequisiteTaskIds: [],
		});

	users.push(
		{
			id: 1,
			username: 'kissbela',
			password: 'beluska123',
			fullname: 'Kiss Béla',
			email: 'kissbela@gmail.com',
		},
		{
			id: 2,
			username: 'mariska',
			password: '12jelszo34',
			fullname: 'Kovács Marianna',
			email: 'mariska1980@gmail.com',
		});

	milestones.push(
		{
			id: 1,
			name: 'Version 1.0.0',
			description: 'Very big update',
			status: Status.Done,
			deadline: new Date(Date.now()),
			tasks: [],
		});

	projects[0].tasks.push(tasks[0]);
	projects[0].milestones.push(milestones[0]);
	tasks[0].assignees.push(users[1]);
	tasks[1].prerequisiteTaskIds.push(tasks[0].id);
	milestones[0].tasks.push(tasks[0]);
}
