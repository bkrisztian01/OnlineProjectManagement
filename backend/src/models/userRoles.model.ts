import {
  BaseEntity,
  Column,
  Entity,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Role } from '../util/Role';
import { Project } from './projects.model';
import { User } from './users.model';

@Entity({ name: 'user_roles' })
export class UserRole extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({
    type: 'enum',
    enum: Role,
    default: Role.Member,
  })
  role: Role;

  @ManyToOne(() => Project, project => project.userRoles)
  project: Project;

  @ManyToOne(() => User, user => user.userRoles)
  user: User;
}