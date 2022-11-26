import type { NextFunction, Request, Response } from 'express';
import {
  createTask,
  deleteTaskById,
  getTaskById,
  getTasks,
  setArchivedTaskById,
  updateTaskById,
} from '../services/task.service';

export async function getTaskHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(
    await getTasks(
      parseInt(req.params.projectId),
      Number(req.query.pageNumber),
    ),
  );
}

export async function postTaskHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const task = await createTask(
      parseInt(req.params.projectId),
      req.body.name,
      req.body.description,
      req.body.status,
      req.body.deadline,
      req.body.assigneeIds,
      req.body.prerequisiteTaskIds,
    );
    res.send(task);
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function getTaskByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const task = await getTaskById(
    parseInt(req.params.id),
    parseInt(req.params.projectId),
  );
  if (!task) {
    res.status(404).send('Task was not found');
    return;
  }

  res.send(task);
}

export async function updateTaskByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const task = await updateTaskById(
      parseInt(req.params.projectId),
      parseInt(req.params.id),
      req.body.name,
      req.body.description,
      req.body.status,
      req.body.deadline,
      req.body.assigneeIds,
      req.body.prerequisiteTaskIds,
    );

    res.send(task);
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function deleteTaskByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    await deleteTaskById(
      parseInt(req.params.id),
      parseInt(req.params.projectId),
    );
    res.sendStatus(200);
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function setArchivedTaskByIdHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    await setArchivedTaskById(
      parseInt(req.params.id),
      parseInt(req.params.projectId),
      req.body.archived,
    );
    res.sendStatus(200);
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}
