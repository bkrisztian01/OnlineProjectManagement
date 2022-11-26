import express from 'express';
import {
  deleteTaskByIdHandler,
  getTaskByIdHandler,
  getTaskHandler,
  postTaskHandler,
  setArchivedTaskByIdHandler,
  updateTaskByIdHandler,
} from '../controllers/task.controller';
import { getUsersTasksHandler } from '../controllers/user.controller';
import validateRequest from '../middlewares/validateRequest';
import { verifyJwt } from '../middlewares/verifyJwt';
import {
  archiveTaskByIdSchema,
  createTaskSchema,
  deleteTaskByIdSchema,
  getTaskByIdSchema,
  getTasksSchema,
  updateTaskByIdSchema,
} from '../schemas/task.schema';

const router = express.Router();

router
  .route('/projects/:projectId/tasks')
  .get(validateRequest(getTasksSchema), getTaskHandler)
  .post(validateRequest(createTaskSchema), postTaskHandler);

router
  .route('/projects/:projectId/tasks/:id')
  .get(validateRequest(getTaskByIdSchema), getTaskByIdHandler)
  .put(validateRequest(updateTaskByIdSchema), updateTaskByIdHandler)
  .delete(validateRequest(deleteTaskByIdSchema), deleteTaskByIdHandler);

router
  .route('/projects/:projectId/tasks/:id/archive')
  .put(validateRequest(archiveTaskByIdSchema), setArchivedTaskByIdHandler);

router.route('/user/tasks').get(verifyJwt, getUsersTasksHandler);

export = router;
