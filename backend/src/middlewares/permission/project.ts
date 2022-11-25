import { NextFunction, Request, Response } from 'express';
import { In } from 'typeorm';
import { UserRole } from '../../models/userRole.model';
import { Role } from '../../util/Role';

export const viewProject = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  const userId = parseInt(res.locals.jwt);
  const adminRole = await UserRole.findOne({
    where: {
      user: {
        id: userId,
      },
      role: Role.Admin,
    },
  });
  const role = await UserRole.findOne({
    where: {
      project: {
        id: parseInt(req.params.projectId),
      },
      user: {
        id: userId,
      },
      role: In([Role.Manager, Role.Member]),
    },
  });

  if (!role && !adminRole) {
    res.sendStatus(403);
    return;
  }
  next();
};

export const modifyProject = async (
  req: Request,
  res: Response,
  next: NextFunction,
) => {
  const userId = res.locals.jwt;
  const adminRole = await UserRole.findOne({
    where: {
      user: {
        id: userId,
      },
      role: Role.Admin,
    },
  });
  const role = await UserRole.findOne({
    where: {
      project: {
        id: parseInt(req.params.projectId),
      },
      user: {
        id: userId,
      },
      role: Role.Manager,
    },
  });

  if (!role && !adminRole) {
    res.sendStatus(403);
    return;
  }
  next();
};
