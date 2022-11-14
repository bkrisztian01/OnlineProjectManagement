// import 'reflect-metadata';
import * as dotenv from 'dotenv';
import { DataSource } from 'typeorm';
import { Milestone } from './models/milestones.model';
import { Project } from './models/projects.model';
import { Task } from './models/tasks.model';
import { User } from './models/users.model';

dotenv.config();

export const AppDataSource = new DataSource({
  type: 'postgres',
  host: process.env.DB_HOST || 'localhost',
  port: parseInt(process.env.DB_PORT) || 5432,
  username: process.env.DB_USERNAME || 'test',
  password: process.env.DB_PASSWORD || 'test',
  database: process.env.DB_NAME || 'test',
  synchronize: false,
  logging: false,
  entities: [User, Milestone, Project, Task],
});
