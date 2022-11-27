import {
  BaseEntity,
  Column,
  Entity,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Role } from '../util/Role';
import { Project } from './project.model';
import { User } from './user.model';

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

  @ManyToOne(() => Project, project => project.userRoles, {
    onDelete: 'CASCADE',
  })
  project: Project;

  @ManyToOne(() => User, user => user.userRoles, { onDelete: 'CASCADE' })
  user: User;
}
