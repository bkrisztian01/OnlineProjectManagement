import express from 'express';
import {
  deleteMilestoneByIdHandler,
  getMilestoneByIdHandler,
  getMilestonesHandler,
  getMilestoneTasksHandler,
  postMilestoneHandler,
  setArchivedMilestoneByIdHandler,
  updateMilestoneByIdHandler,
} from '../controllers/milestone.controller';
import { modifyProject, viewProject } from '../middlewares/permission/project';
import validateRequest from '../middlewares/validateRequest';
import {
  archiveMilestoneByIdSchema,
  createMilestoneSchema,
  deleteMilestoneByIdSchema,
  getMilestoneByIdSchema,
  getMilestonesSchema,
  getMilestoneTasksSchema,
  updateMilestoneByIdSchema,
} from '../schemas/milestone.schema';

const router = express.Router();

router
  .route('/projects/:projectId/milestones')
  .get(validateRequest(getMilestonesSchema), viewProject, getMilestonesHandler)
  .post(
    validateRequest(createMilestoneSchema),
    modifyProject,
    postMilestoneHandler,
  );

router
  .route('/projects/:projectId/milestones/:id')
  .get(
    validateRequest(getMilestoneByIdSchema),
    viewProject,
    getMilestoneByIdHandler,
  )
  .put(
    validateRequest(updateMilestoneByIdSchema),
    modifyProject,
    updateMilestoneByIdHandler,
  )
  .delete(
    validateRequest(deleteMilestoneByIdSchema),
    modifyProject,
    deleteMilestoneByIdHandler,
  );

router
  .route('/projects/:projectId/milestones/:id/archive')
  .put(
    validateRequest(archiveMilestoneByIdSchema),
    modifyProject,
    setArchivedMilestoneByIdHandler,
  );

router
  .route('/projects/:projectId/milestones/:id/tasks')
  .get(
    validateRequest(getMilestoneTasksSchema),
    viewProject,
    getMilestoneTasksHandler,
  );

export = router;
