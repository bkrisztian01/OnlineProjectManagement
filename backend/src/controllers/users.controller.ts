import type { NextFunction, Request, Response } from 'express';
import {
  createUser,
  getUsers,
  validatePassword,
} from '../services/users.service';

export async function getLoginUserHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const username = req.body.username as string;
  const password = req.body.password as string;

  const user = await validatePassword(username, password);
  if (!user) {
    res.status(401).send('Invalid credentials');
    return;
  }

  res.send(user);
}

export async function getLogoutUserHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send('Successful operation');
}

export async function postSignupUserHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const username = req.body.username as string;
  const password = req.body.password as string;
  const fullname = req.body.fullname as string;
  const email = req.body.email as string;

  try {
    const user = await createUser(username, password, fullname, email);
    res.send(user);
  } catch (e: any) {
    res.status(e.httpStatus).send(e.message);
  }
}

export async function getUsersHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(await getUsers());
}
