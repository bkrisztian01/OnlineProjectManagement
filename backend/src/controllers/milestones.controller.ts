import type { NextFunction, Request, Response } from 'express';
import {
  createMilestone,
  deleteMilestoneById,
  getMilestoneById,
  getMilestones,
  updateMilestoneById,
} from '../services/milestones.service';
import { Status } from '../util/Status';

export async function getMilestonesHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(await getMilestones(parseInt(req.params.projectId)));
}

export async function postMilestoneHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const milestone = await createMilestone(
      parseInt(req.params.projectId),
      req.body.name,
      req.body.description,
      req.body.status,
      req.body.deadline,
    );

    res.send(milestone);
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function getMilestoneByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const milestone = await getMilestoneById(
    parseInt(req.params.id),
    parseInt(req.params.projectId),
  );
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
      parseInt(req.params.projectId),
      parseInt(req.params.id),
      req.body.name,
      req.body.description,
      Status[req.body.status as keyof typeof Status],
      req.body.status,
      req.body.taskIds,
    );

    res.status(200).send(milestone);
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function deleteMilestoneByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  deleteMilestoneById(parseInt(req.params.id), parseInt(req.params.projectId));

  res.status(200).send('Successful operation');
}
