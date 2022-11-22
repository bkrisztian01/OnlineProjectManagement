import {
  BaseEntity,
  Column,
  Entity,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Status } from '../util/Status';
import { Project } from './projects.model';
import { Task } from './tasks.model';

@Entity({ name: 'milestone' })
export class Milestone extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('varchar', { length: 50 })
  name: string;

  @Column('text', { default: '' })
  description: string;

  @Column({ type: 'date', nullable: true })
  deadline: string;

  @Column({
    type: 'enum',
    enum: Status,
    default: Status.NotStarted,
  })
  status: Status;

  @Column({ default: false })
  archived: Boolean;

  @OneToMany(() => Task, task => task.milestone)
  tasks: Task[];

  @ManyToOne(() => Project, project => project.milestones, {
    onDelete: 'CASCADE',
  })
  project: Project;
}
