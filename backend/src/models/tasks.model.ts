import {
  BaseEntity,
  Column,
  Entity,
  JoinColumn,
  JoinTable,
  ManyToMany,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Status } from '../util/Status';
import { Milestone } from './milestones.model';
import { Project } from './projects.model';
import { User } from './users.model';

@Entity({ name: 'task' })
export class Task extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('varchar', { length: 50 })
  name: string;

  @Column('text')
  description: string;

  @Column({
    type: 'enum',
    enum: Status,
    default: Status.NotStarted,
  })
  status: Status;

  @Column({ type: 'date', nullable: true })
  deadline: string;

  @ManyToMany(() => User)
  @JoinTable()
  assignees: User[];

  @ManyToMany(() => Task)
  @JoinTable()
  prerequisiteTasks: Task[];

  @Column({ default: false })
  archived: boolean;

  @ManyToOne(() => Milestone, milestone => milestone.tasks, {
    onDelete: 'SET NULL',
  })
  milestone: Milestone;

  @ManyToOne(() => Project, project => project.tasks, { onDelete: 'CASCADE' })
  @JoinColumn({
    name: 'projectId',
  })
  project: Project;
}
