import type { NextFunction, Request, Response } from 'express';
import {
  createMilestone,
  deleteMilestoneById,
  getMilestoneById,
  getMilestones,
  updateMilestoneById,
} from '../services/milestones.service';

export async function getMilestonesHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(await getMilestones());
}

export async function postMilestoneHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const milestone = await createMilestone(
      req.body.projectId,
      req.body.name,
      req.body.description,
      req.body.status,
      req.body.deadline,
    );

    res.send(milestone);
  } catch (e: any) {
    res.status(e.httpStatus).send(e.message);
  }
}

export async function getMilestoneByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const milestone = await getMilestoneById(parseInt(req.params.id));
  if (!milestone) {
    res.status(404).send('Milestone was not found');
    return;
  }

  res.send(milestone);
}

export async function updateMilestoneByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const milestone = await updateMilestoneById(
      parseInt(req.params.id),
      req.body.name,
      req.body.description,
      req.body.deadline,
      req.body.status,
      req.body.taskIds,
    );

    res.status(200).send(milestone);
  } catch (e: any) {
    res.status(e.httpStatus).send(e.message);
  }
}

export async function deleteMilestoneByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  deleteMilestoneById(parseInt(req.params.id));

  res.status(200).send('Successful operation');
}
