import type { NextFunction, Request, Response } from 'express';
import {
  createProject,
  deleteProjectById,
  getProjectById,
  getProjects,
  updateProjectById,
} from '../services/projects.service';

export async function getProjectsHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(await getProjects(Number(req.query.pageNumber)));
}

export async function postProjectHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const project = await createProject(
    req.body.name,
    req.body.description,
    req.body.startDate,
    req.body.endDate,
    req.body.status,
    req.body.estimatedTime,
  );
  res.send(project);
}

export async function getProjectByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const project = await getProjectById(parseInt(req.params.id));
  if (!project) {
    res.status(404).send('Project was not found');
    return;
  }

  res.send(project);
}

export async function updateProjectByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const project = await updateProjectById(
      parseInt(req.params.id),
      req.body.name,
      req.body.description,
      req.body.startDate,
      req.body.endDate,
      req.body.status,
      parseInt(req.body.estimatedTime),
    );

    res.send(project);
  } catch (e: any) {
    res.status(e.httpStatus).send(e.message);
  }
}

export async function deleteProjectByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  await deleteProjectById(parseInt(req.params.id));

  res.status(204).send('No content');
}
