import type { NextFunction, Request, Response } from 'express';
import {
  createMilestone,
  deleteMilestoneById,
  getMilestoneById,
  getMilestones,
  getMilestoneTasks,
  setArchivedMilestoneById,
  updateMilestoneById,
} from '../services/milestone.service';

export async function getMilestonesHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(
    await getMilestones(
      parseInt(req.params.projectId),
      Number(req.query.pageNumber),
    ),
  );
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
      req.body.taskIds,
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
      req.body.deadline,
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

  res.json();
}

export async function setArchivedMilestoneByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    await setArchivedMilestoneById(
      parseInt(req.params.id),
      parseInt(req.params.projectId),
      req.body.archived,
    );
    res.json();
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function getMilestoneTasksHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    res.send(
      await getMilestoneTasks(
        parseInt(req.params.id),
        parseInt(req.params.projectId),
      ),
    );
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}
