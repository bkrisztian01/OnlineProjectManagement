import {
  BaseEntity,
  Column,
  Entity,
  OneToMany,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Status } from '../util/Status';
import { Milestone } from './milestone.model';
import { Task } from './task.model';
import { UserRole } from './userRole.model';

@Entity({ name: 'project' })
export class Project extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('varchar', { length: 50 })
  name: string;

  @Column('text', { default: '' })
  description: string;

  @Column({
    type: 'enum',
    enum: Status,
    default: Status.NotStarted,
  })
  status: Status;

  @Column({
    type: 'date',
    nullable: true,
    default: new Date(Date.now()).toLocaleString('en-CA').split(',')[0],
  })
  startDate: string;

  @Column({ type: 'date', nullable: true })
  endDate: string;

  @Column({ type: 'int', nullable: true })
  estimatedTime: number;

  @OneToMany(() => Task, task => task.project)
  tasks: Task[];

  @OneToMany(() => Milestone, milestone => milestone.project)
  milestones: Milestone[];

  @OneToMany(() => UserRole, userRole => userRole.project)
  userRoles: UserRole[];
}
