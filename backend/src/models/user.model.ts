import {
  BaseEntity,
  Column,
  Entity,
  ManyToMany,
  OneToMany,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Task } from './task.model';
import { UserRole } from './userRole.model';

@Entity({ name: 'users' })
export class User extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('varchar', { length: 50 })
  username: string;

  @Column('varchar', { length: 1000, select: false })
  password: string;

  @Column('varchar', { length: 50 })
  fullname: string;

  @Column('varchar', { length: 50 })
  email: string;

  @ManyToMany(() => Task, task => task.assignees)
  tasks: Task[];

  @OneToMany(() => UserRole, userRole => userRole.user)
  userRoles: UserRole[];
}
