import { NextFunction, Request, Response } from 'express';
import * as jwt from 'jsonwebtoken';
import { RefreshToken } from '../models/refreshTokens.model';

export async function handleRefreshToken(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  const cookies = req.cookies;
  if (!cookies?.jwt) return res.sendStatus(401);
  const refreshToken = cookies.jwt;

  const foundRefreshToken = await RefreshToken.findOne({
    relations: ['user'],
    where: {
      refreshToken,
    },
  });

  if (!foundRefreshToken) return res.sendStatus(403);

  jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET, (err, decoded) => {
    if (err || foundRefreshToken.user.username !== decoded.username)
      return res.sendStatus(403);

    const accessToken = jwt.sign(
      { username: decoded.username },
      process.env.ACCESS_TOKEN_SECRET,
      { expiresIn: '2m' },
    );

    res.json({ accessToken });
  });
}
