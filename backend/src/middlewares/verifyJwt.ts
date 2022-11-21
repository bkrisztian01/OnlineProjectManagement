import { NextFunction, Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';

export const verifyJwt = (req: Request, res: Response, next: NextFunction) => {
  const authHeader = req.headers['authorization'];
  if (!authHeader?.startsWith('Bearer ')) return res.sendStatus(401);

  const token = authHeader.split(' ')[1];
  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, decoded: any) => {
    if (err) {
      return res.sendStatus(403);
    }

    res.locals.jwt = decoded.username;
    next();
  });
};
