import type {Express} from 'express';
import {deleteMilestoneByIdHandler, getMilestoneByIdHandler, getMilestonesHandler, postMilestoneHandler, updateMilestoneByIdHandler} from './controllers/milestones.controller';
import {getProjectsHandler, deleteProjectByIdHandler, postProjectHandler, updateProjectByIdHandler, getProjectByIdHandeler} from './controllers/projects.controller';
import {deleteTaskByIdHandler, getTaskByIdHandler, getTaskHandler, postTaskHandler, setArchivedTaskByIdHandler, updateTaskByIdHandler} from './controllers/tasks.controller';
import {getLoginUserHandler, getLogoutUserHandler, getUsersHandler, postSignupUserHandler} from './controllers/users.controller';
import validateRequest from './middlewares/validateRequest';
import {createMilestoneSchema, deleteMilestoneByIdSchema, getMilestoneByIdSchema, updateMilestoneByIdSchema} from './schemas/milestones.schema';
import {createProjectSchema, deleteProjectByIdSchema, getProjectByIdSchema, updateProjectByIdSchema} from './schemas/projects.schema';
import {archiveTaskByIdSchema, createTaskSchema, deleteTaskByIdSchema, getTaskByIdSchema, updateTaskByIdSchema} from './schemas/tasks.schema';
import {createUserSchema, loginUserSchema} from './schemas/users.schema';

function routes(app: Express): void {
	app.get('/', (req, res) => {
		res.send('Hey! :)');
	});

	//
	// Projects
	//
	app.route('/projects')
		.get(getProjectsHandler)
		.post(validateRequest(createProjectSchema), postProjectHandler);

	app.route('/projects/:id')
		.get(validateRequest(getProjectByIdSchema), getProjectByIdHandeler)
		.put(validateRequest(updateProjectByIdSchema), updateProjectByIdHandler)
		.delete(validateRequest(deleteProjectByIdSchema), deleteProjectByIdHandler);

	//
	// User
	//
	app.route('/user/login')
		.get(validateRequest(loginUserSchema), getLoginUserHandler);

	app.route('/user/logout')
		.get(getLogoutUserHandler);

	app.route('/user/signup')
		.post(validateRequest(createUserSchema), postSignupUserHandler);

	app.route('/users')
		.get(getUsersHandler);

	//
	// Tasks
	//
	app.route('/tasks')
		.get(getTaskHandler)
		.post(validateRequest(createTaskSchema), postTaskHandler);

	app.route('/tasks/:id')
		.get(validateRequest(getTaskByIdSchema), getTaskByIdHandler)
		.put(validateRequest(updateTaskByIdSchema), updateTaskByIdHandler)
		.delete(validateRequest(deleteTaskByIdSchema), deleteTaskByIdHandler);

	app.route('/tasks/:id/archive')
		.put(validateRequest(archiveTaskByIdSchema), setArchivedTaskByIdHandler);

	//
	// Milestones
	//
	app.route('/milestones')
		.get(getMilestonesHandler)
		.post(validateRequest(createMilestoneSchema), postMilestoneHandler);

	app.route('/milestones/:id')
		.get(validateRequest(getMilestoneByIdSchema), getMilestoneByIdHandler)
		.put(validateRequest(updateMilestoneByIdSchema), updateMilestoneByIdHandler)
		.delete(validateRequest(deleteMilestoneByIdSchema), deleteMilestoneByIdHandler);
}

export default routes;
