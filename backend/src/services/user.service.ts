import { Conflict, Unauthorized } from '@curveball/http-errors/dist';
import * as bcrypt from 'bcrypt';
import * as jwt from 'jsonwebtoken';
import { RefreshToken } from '../models/refreshToken.model';
import { Task } from '../models/task.model';
import { User } from '../models/user.model';

export async function validatePassword(username: string, password: string) {
  const user = await User.findOne({
    where: {
      username,
    },
    select: ['id', 'username', 'password'],
  });

  if (!user) {
    throw new Unauthorized('Unauthorized');
  }
  const match = await bcrypt.compare(password, user.password);
  if (!match) {
    throw new Unauthorized('Unauthorized');
  }

  const accessToken = jwt.sign(
    { userId: user.id },
    process.env.ACCESS_TOKEN_SECRET,
    { expiresIn: '2m' },
  );

  const refreshToken = jwt.sign(
    { userId: user.id },
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
  return await User.findOne({ where: { id } });
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
  const user = await getUserById(id);
  if (user) {
    User.remove(user);
  }
}

export async function logoutUser(refreshToken: string) {
  RefreshToken.delete({
    refreshToken,
  });
}

export async function getUsersTasks(userId: number) {
  const tasks = Task.find({
    where: {
      assignees: {
        id: userId,
      },
    },
  });

  return tasks;
}
