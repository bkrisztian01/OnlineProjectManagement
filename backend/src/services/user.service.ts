import { Conflict, Unauthorized } from '@curveball/http-errors/dist';
import * as bcrypt from 'bcrypt';
import * as jwt from 'jsonwebtoken';
import { Between, Not } from 'typeorm';
import { RefreshToken } from '../models/refreshToken.model';
import { Task } from '../models/task.model';
import { User } from '../models/user.model';
import { Status } from '../util/Status';
import { createProject } from './project.service';

export async function validatePassword(username: string, password: string) {
  const user = await User.findOne({
    where: {
      username,
    },
    select: ['id', 'username', 'password', 'email', 'fullname'],
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
    { expiresIn: process.env.ACCESS_TOKEN_TIME || '2m' },
  );

  const refreshToken = jwt.sign(
    { userId: user.id },
    process.env.REFRESH_TOKEN_SECRET,
    { expiresIn: process.env.REFRESH_TOKEN_TIME || '1d' },
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

  // Temp fix for frontend, when user doesn't have any projects
  await createProject(
    user.fullname,
    '',
    new Date(Date.now()).toLocaleString('en-CA').split(',')[0],
    null,
    Status.NotStarted,
    null,
    user.id,
    [],
  );

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
  const date = new Date(Date.now());
  date.setDate(date.getDate() + 14);
  const dateString = date.toLocaleString('en-CA').split(',')[0];
  const tasks = Task.find({
    where: {
      assignees: {
        id: userId,
      },
      deadline: Between(
        new Date(Date.now()).toLocaleString('en-CA').split(',')[0],
        dateString,
      ),
      status: Not<Status.Done>(Status.Done),
    },
    relations: ['project'],
  });

  return tasks;
}
