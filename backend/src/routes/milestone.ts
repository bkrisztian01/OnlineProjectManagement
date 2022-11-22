import express from 'express';
import {
  deleteMilestoneByIdHandler,
  getMilestoneByIdHandler,
  getMilestonesHandler,
  postMilestoneHandler,
  setArchivedMilestoneByIdHandler,
  updateMilestoneByIdHandler,
} from '../controllers/milestones.controller';
import validateRequest from '../middlewares/validateRequest';
import {
  archiveMilestoneByIdSchema,
  createMilestoneSchema,
  deleteMilestoneByIdSchema,
  getMilestoneByIdSchema,
  updateMilestoneByIdSchema,
} from '../schemas/milestones.schema';

const router = express.Router();

router
  .route('/projects/:projectId/milestones')
  .get(getMilestonesHandler)
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
