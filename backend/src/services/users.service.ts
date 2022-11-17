import { Conflict } from '@curveball/http-errors/dist';
import { User } from '../models/users.model';

export async function validatePassword(username: string, password: string) {
  // return users.find(u => u.username === username && u.password === password);
  return await User.findOne({
    where: {
      username,
      password,
    },
  });
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
  const q = await User.createQueryBuilder('users')
    .select('users')
    .from(User, 'user')
    .where('user.username = :username OR user.email = :email', {
      username,
      email,
    })
    .getMany();

  if (q.length > 0) {
    throw new Conflict('Username or email is already in use');
  }

  const user = User.create({
    username,
    password,
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
