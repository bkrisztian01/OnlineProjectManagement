// import {AppDataSource} from './data-source';
import cors from 'cors';
import * as dotenv from 'dotenv';
import express from 'express';
import 'reflect-metadata';
import { AppDataSource } from './data-source';
import routes from './routes';

dotenv.config();

AppDataSource.initialize()
  .then(async () => {
    const app = express();
    app.use(express.json());
    app.use(cors());
    const port = process.env.PORT || 5000;

    routes(app);

    app.listen(port, () => {
      console.log(`Listening on http://localhost:${port}`);
    });

    console.log('Created PostgreSQL connection:', AppDataSource.isInitialized);
    AppDataSource.synchronize(true);
  })
  .catch(error => {
    console.log(error);
  });
