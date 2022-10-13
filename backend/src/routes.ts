import type {Express} from 'express';
import {getProjectHandler, deleteProjectByIdHandler, postProjectHandler, putProjectByIdHandler, getProjectByIdHandeler} from './controllers/projects.controller';
import {deleteTaskByIdHandler, getTaskByIdHandler, getTaskHandler, postTaskHandler} from './controllers/tasks.controller';
import {getLoginUserHandler, getLogoutUserHandler, postSignupUserHandler} from './controllers/users.controller';
import validateRequest from './middlewares/validateRequest';
import {createProjectSchema, updateProjectSchema} from './schemas/projects.schema';
import {createUserSchema, loginUserSchema} from './schemas/users.schema';

export function routes(app: Express): void {
	//
	// Projects
	//
	app.route('/projects')
		.get(getProjectHandler)
		.post(validateRequest(createProjectSchema), postProjectHandler);

	app.route('/projects/:id')
		.get(getProjectByIdHandeler)
		.put(validateRequest(updateProjectSchema), putProjectByIdHandler)
		.delete(deleteProjectByIdHandler);

	//
	// User
	//
	app.route('/user/login')
		.get(validateRequest(loginUserSchema), getLoginUserHandler);

	app.route('/user/logout')
		.get(getLogoutUserHandler);

	app.route('/user/signup')
		.post(validateRequest(createUserSchema), postSignupUserHandler);

	//
	// Tasks
	//
	app.route('/tasks')
		.get(getTaskHandler)
		.post(postTaskHandler);

	app.route('/tasks/:id')
		.get(getTaskByIdHandler)
		.delete(deleteTaskByIdHandler);
}

export default routes;
