import { NextFunction, Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';
import { RefreshToken } from '../models/refreshToken.model';

export async function handleRefreshToken(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const cookies = req.cookies;
  if (!cookies?.jwt) {
    return res.sendStatus(401);
  }
  const refreshToken = req.cookies['jwt'];

  const foundRefreshToken = await RefreshToken.findOne({
    relations: ['user'],
    where: {
      refreshToken,
    },
  });

  if (!foundRefreshToken) {
    console.log(`retek: ${foundRefreshToken}`);
    return res.sendStatus(403);
  }

  jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, decoded) => {
    if (err || foundRefreshToken.user.id !== decoded.userId) {
      console.log(`repa: ${foundRefreshToken}`);
      return res.sendStatus(403);
    }

    const accessToken = jwt.sign(
      { userId: decoded.userId },
      process.env.ACCESS_TOKEN_SECRET,
      { expiresIn: process.env.ACCESS_TOKEN_TIME || '2m' },
    );

    res.json({ accessToken });
  });
}
