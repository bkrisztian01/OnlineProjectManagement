// import {AppDataSource} from './data-source';
import 'reflect-metadata';
import cors from 'cors';
import express from 'express';
import routes from './routes';
import {initTestData} from './testData';
import * as dotenv from 'dotenv';
import { Milestone } from './models/milestones.model';
import { Project } from './models/projects.model';
import { Task } from './models/tasks.model';
import { User } from './models/users.model';
import { DataSource } from 'typeorm';
import { AppDataSource } from './data-source';

dotenv.config();

// export const AppDataSource = new DataSource({
// 	type: 'postgres',
// 	host:  process.env.DB_HOST || 'localhost',
// 	port: parseInt(process.env.DB_PORT) || 5432,
// 	username: process.env.DB_USERNAME || 'test',
// 	password: process.env.DB_PASSWORD || 'test',
// 	database: process.env.DB_NAME || 'test',
// 	synchronize: true,
// 	logging: false,
// 	entities: [User, Milestone, Project, Task],
// 	migrations: [],
// 	subscribers: [],
// });

AppDataSource.initialize().then(async () => {
	// console.log('Inserting a new user into the database...');
	// const user = new User();
	// user.firstName = 'Timber';
	// user.lastName = 'Saw';
	// user.age = 25;
	// await AppDataSource.manager.save(user);
	// console.log('Saved a new user with id: ' + user.id);

	// console.log('Loading users from the database...');
	// const users = await AppDataSource.manager.find(User);
	// console.log('Loaded users: ', users);

	// console.log('Here you can setup and run express / fastify / any other framework.');

	const app = express();
	app.use(express.json());
	app.use(cors());
	const port = process.env.PORT || 5000;
	
	// routes(app);
	
	// initTestData();
	
	app.listen(port, () => {
		console.log(`Listening on http://localhost:${port}`);
	});
	
    console.log("created connection", AppDataSource.isConnected)
	await AppDataSource.synchronize(true);
}).catch(error => {
	console.log(error);
});
