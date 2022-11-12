import {Entity, Column, PrimaryColumn, PrimaryGeneratedColumn, OneToMany, ManyToOne} from 'typeorm';
import {Status} from '../util/Status';
import { Project } from './projects.model';
import {Task} from './tasks.model';

@Entity({ name: 'milestone' })
export class Milestone {
	@PrimaryGeneratedColumn()
	id: number;

	@Column('varchar', { length: 50 })
	name: string;

	@Column('text')
	description: string;

	@Column({ type: 'date' })
	deadline: string;

	@Column({
		type: 'enum',
		enum: Status,
		default: Status.NotStarted
	})
	status: Status;

	@OneToMany(() => Task, (task) => task.milestone)
	tasks: Task[];
	
	@ManyToOne(() => Project, (project) => project.milestones)
	project: Project;
}
