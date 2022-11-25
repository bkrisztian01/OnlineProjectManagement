import express from 'express';
import {
  deleteTaskByIdHandler,
  getTaskByIdHandler,
  getTaskHandler,
  postTaskHandler,
  setArchivedTaskByIdHandler,
  updateTaskByIdHandler,
} from '../controllers/task.controller';
import { modifyProject, viewProject } from '../middlewares/permission/project';
import validateRequest from '../middlewares/validateRequest';
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
  .get(validateRequest(getTasksSchema), viewProject, getTaskHandler)
  .post(validateRequest(createTaskSchema), modifyProject, postTaskHandler);

router
  .route('/projects/:projectId/tasks/:id')
  .get(validateRequest(getTaskByIdSchema), viewProject, getTaskByIdHandler)
  .put(
    validateRequest(updateTaskByIdSchema),
    modifyProject,
    updateTaskByIdHandler,
  )
  .delete(
    validateRequest(deleteTaskByIdSchema),
    modifyProject,
    deleteTaskByIdHandler,
  );

router
  .route('/projects/:projectId/tasks/:id/archive')
  .put(
    validateRequest(archiveTaskByIdSchema),
    modifyProject,
    setArchivedTaskByIdHandler,
  );

export = router;
