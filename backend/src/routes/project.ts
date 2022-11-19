import express from 'express';
import {
  deleteProjectByIdHandler,
  getProjectByIdHandler,
  getProjectsHandler,
  postProjectHandler,
  updateProjectByIdHandler,
} from '../controllers/projects.controller';
import validateRequest from '../middlewares/validateRequest';
import {
  createProjectSchema,
  deleteProjectByIdSchema,
  getProjectByIdSchema,
  updateProjectByIdSchema,
} from '../schemas/projects.schema';

const router = express.Router();

router
  .route('/projects')
  .get(getProjectsHandler)
  .post(validateRequest(createProjectSchema), postProjectHandler);

router
  .route('/projects/:id')
  .get(validateRequest(getProjectByIdSchema), getProjectByIdHandler)
  .put(validateRequest(updateProjectByIdSchema), updateProjectByIdHandler)
  .delete(validateRequest(deleteProjectByIdSchema), deleteProjectByIdHandler);

export = router;
