// import 'reflect-metadata';
import * as dotenv from 'dotenv';
import { DataSource } from 'typeorm';
import { Milestone } from './models/milestone.model';
import { Project } from './models/project.model';
import { RefreshToken } from './models/refreshToken.model';
import { Task } from './models/task.model';
import { User } from './models/user.model';
import { UserRole } from './models/userRole.model';

dotenv.config();

export const AppDataSource = new DataSource({
  type: 'postgres',
  host: process.env.DB_HOST || 'localhost',
  port: parseInt(process.env.DB_PORT) || 5432,
  username: process.env.DB_USERNAME || 'postgres',
  password: process.env.DB_PASSWORD || 'test',
  database: process.env.DB_NAME || 'opm',
  synchronize: false,
  logging: false,
  entities: [Milestone, Project, UserRole, User, RefreshToken, Task],
  ssl: true,
  extra: {
    ssl: {
      rejectUnauthorized: false,
    },
  },
});
