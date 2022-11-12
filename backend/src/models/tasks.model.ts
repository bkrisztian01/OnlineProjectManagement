import {User} from './users.model';
import {Status} from '../util/Status';
import { Milestone } from './milestones.model';
import { Column, Entity, JoinTable, ManyToMany, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { Project } from './projects.model';

@Entity({ name: 'task' })
export class Task {
	@PrimaryGeneratedColumn()
	id: number;

	@Column('varchar', { length: 50 })
	name: string;
	
	@Column('text')
	description: string;
	
	@Column({
		type: 'enum',
		enum: Status,
		default: Status.NotStarted
	})
	status: Status;
	
	@Column({ type: 'date' })
	deadline: string;
	
	@ManyToMany(() => User, (user) => user.tasks)
	@JoinTable()
	assignees: User[];
	
	@ManyToMany(() => Task)
	@JoinTable()
	prerequisiteTasks: Task[];
	
	@Column()
	archived: boolean;

	@ManyToOne(() => Milestone, (milestone) => milestone.tasks)
	milestone: Milestone;

	@ManyToOne(() => Project, (project) => project.tasks)
	project: Project;
};
