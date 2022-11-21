// import {AppDataSource} from './data-source';
import cookieParser from 'cookie-parser';
import cors from 'cors';
import * as dotenv from 'dotenv';
import express from 'express';
import 'reflect-metadata';
import { AppDataSource } from './dataSource';
import { verifyJwt } from './middlewares/verifyJwt';
import milestoneRoutes from './routes/milestone';
import projectRoutes from './routes/project';
import refreshTokenRoutes from './routes/refresh';
import taskRoutes from './routes/task';
import userRoutes from './routes/user';
import { initTestData } from './testData';

dotenv.config();

AppDataSource.initialize()
  .then(async () => {
    const app = express();
    app.use(express.json());
    app.use(cors());
    app.use(cookieParser());

    app.get('/', (req, res) => {
      res.send('Hello! :)');
    });

    app.post('/resettestdata', async (req, res) => {
      await AppDataSource.synchronize(true);
      initTestData();
      res.send();
    });

    app.use(userRoutes);
    app.use(refreshTokenRoutes);
    app.use(verifyJwt, projectRoutes);
    app.use(verifyJwt, taskRoutes);
    app.use(verifyJwt, milestoneRoutes);

    const port = process.env.PORT || 5000;

    app.listen(port, () => {
      console.log(`Listening on http://localhost:${port}`);
    });

    console.log('Created PostgreSQL connection:', AppDataSource.isInitialized);
  })
  .catch(error => {
    console.log(error);
  });
