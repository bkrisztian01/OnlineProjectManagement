import express from 'express';
import {
  deleteMilestoneByIdHandler,
  getMilestoneByIdHandler,
  getMilestonesHandler,
  postMilestoneHandler,
  setArchivedMilestoneByIdHandler,
  updateMilestoneByIdHandler,
} from '../controllers/milestone.controller';
import validateRequest from '../middlewares/validateRequest';
import {
  archiveMilestoneByIdSchema,
  createMilestoneSchema,
  deleteMilestoneByIdSchema,
  getMilestoneByIdSchema,
  getMilestonesSchema,
  updateMilestoneByIdSchema,
} from '../schemas/milestone.schema';

const router = express.Router();

router
  .route('/projects/:projectId/milestones')
  .get(validateRequest(getMilestonesSchema), getMilestonesHandler)
  .post(validateRequest(createMilestoneSchema), postMilestoneHandler);

router
  .route('/projects/:projectId/milestones/:id')
  .get(validateRequest(getMilestoneByIdSchema), getMilestoneByIdHandler)
  .put(validateRequest(updateMilestoneByIdSchema), updateMilestoneByIdHandler)
  .delete(
    validateRequest(deleteMilestoneByIdSchema),
    deleteMilestoneByIdHandler,
  );

router
  .route('/projects/:projectId/milestones/:id/archive')
  .put(
    validateRequest(archiveMilestoneByIdSchema),
    setArchivedMilestoneByIdHandler,
  );

export = router;
