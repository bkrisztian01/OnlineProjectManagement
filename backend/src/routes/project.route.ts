import express from 'express';
import {
  deleteProjectByIdHandler,
  getProjectByIdHandler,
  getProjectsHandler,
  postProjectHandler,
  updateProjectByIdHandler,
} from '../controllers/project.controller';
import { modifyProject, viewProject } from '../middlewares/permission/project';
import validateRequest from '../middlewares/validateRequest';
import {
  createProjectSchema,
  deleteProjectByIdSchema,
  getProjectByIdSchema,
  getProjectsSchema,
  updateProjectByIdSchema,
} from '../schemas/project.schema';

const router = express.Router();

router
  .route('/projects')
  .get(validateRequest(getProjectsSchema), getProjectsHandler)
  .post(validateRequest(createProjectSchema), postProjectHandler);

router
  .route('/projects/:projectId')
  .get(
    validateRequest(getProjectByIdSchema),
    viewProject,
    getProjectByIdHandler,
  )
  .put(
    validateRequest(updateProjectByIdSchema),
    modifyProject,
    updateProjectByIdHandler,
  )
  .delete(
    validateRequest(deleteProjectByIdSchema),
    modifyProject,
    deleteProjectByIdHandler,
  );

export = router;
