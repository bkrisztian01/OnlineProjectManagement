import {
  BaseEntity,
  Column,
  Entity,
  JoinTable,
  ManyToMany,
  OneToMany,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Task } from './tasks.model';
import { UserRole } from './userRoles.model';

@Entity({ name: 'users' })
export class User extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('varchar', { length: 50 })
  username: string;

  @Column('varchar', { length: 1000 })
  password: string;

  @Column('varchar', { length: 50 })
  fullname: string;

  @Column('varchar', { length: 50 })
  email: string;

  @ManyToMany(() => Task, task => task.assignees)
  @JoinTable()
  tasks: Task[];

  @OneToMany(() => UserRole, userRole => userRole.user)
  userRoles: UserRole[];
}
