import type { NextFunction, Request, Response } from 'express';
import {
  createUser,
  getUserById,
  getUsers,
  getUsersTasks,
  logoutUser,
  validatePassword,
} from '../services/user.service';

export async function getLoginUserHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const username = req.body.username as string;
  const password = req.body.password as string;

  try {
    const tokens = await validatePassword(username, password);

    res.cookie('jwt', tokens.refreshToken, {
      httpOnly: true,
      maxAge: 24 * 60 * 60 * 1000,
      sameSite: 'none',
      // secure: true,
    });
    res.json({ accessToken: tokens.accessToken });
  } catch (e: any) {
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function getLogoutUserHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const cookies = req.cookies;
  if (!cookies?.jwt) return res.sendStatus(204);

  logoutUser(cookies.jwt);

  res.clearCookie('jwt', { httpOnly: true, sameSite: 'none', secure: true });

  res.json();
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
    res.status(e.httpStatus || 500).send(e.message);
  }
}

export async function getUsersHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(await getUsers());
}

export async function getCurrentUserHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const user = await getUserById(parseInt(res.locals.jwt));
  if (user) {
    res.send({
      id: user.id,
      username: user.username,
      fullname: user.fullname,
      email: user.email,
    });
  }
}

export async function getUsersTasksHandler(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  res.send(await getUsersTasks(res.locals.jwt));
}
