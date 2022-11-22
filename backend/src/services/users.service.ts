import { Conflict, Unauthorized } from '@curveball/http-errors/dist';
import * as bcrypt from 'bcrypt';
import * as jwt from 'jsonwebtoken';
import { RefreshToken } from '../models/refreshTokens.model';
import { User } from '../models/users.model';

export async function validatePassword(username: string, password: string) {
  const user = await User.findOne({
    where: {
      username,
    },
  });

  const match = await bcrypt.compare(password, user.password);
  if (!match) {
    throw new Unauthorized('Unauthorized');
  }

  const accessToken = jwt.sign(
    { username: username },
    process.env.ACCESS_TOKEN_SECRET,
    { expiresIn: '2m' },
  );

  const refreshToken = jwt.sign(
    { username: username },
    process.env.REFRESH_TOKEN_SECRET,
    { expiresIn: '1d' },
  );

  const refreshTokenObject = await RefreshToken.findOne({
    where: {
      user: {
        id: user.id,
      },
    },
  });

  if (!refreshTokenObject) {
    RefreshToken.create({
      refreshToken,
      user,
    }).save();
  } else {
    refreshTokenObject.refreshToken = refreshToken;
    refreshTokenObject.save();
  }

  return { accessToken, refreshToken };
}

export async function getUserById(id: number) {
  return await User.createQueryBuilder('users')
    .select('users')
    .from(User, 'user')
    .where('user.id = :id', { id })
    .getOne();
}

export async function createUser(
  username: string,
  password: string,
  fullname: string,
  email: string,
) {
  const q = await User.findOne({
    where: [{ username }, { email }],
  });

  console.log(q);

  if (q) {
    throw new Conflict('Username or email is already in use');
  }

  const hashedPwd = await bcrypt.hash(password, 10);

  const user = User.create({
    username,
    password: hashedPwd,
    fullname,
    email,
  });
  await user.save();

  return user;
}

export async function getUsers() {
  return await User.find();
}

export async function deleteUserById(id: number) {
  await User.delete(id);
}

export async function logoutUser(refreshToken: string) {
  RefreshToken.delete({
    refreshToken,
  });
}
