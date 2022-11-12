import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { Milestone } from './milestones.model';
import {Task} from './tasks.model';

@Entity({ name: 'project' })
export class Project {
	@PrimaryGeneratedColumn()
	id: number;
	
	@Column('varchar', { length: 50 })
	name: string;

	@Column('text')	
	description: string;

	@Column({ type: 'date' })
	startDate: string;
	
	@Column({ type: 'date' })
	endDate: string;
	
	@Column('bigint')
	estimatedTime: number;
	
	@OneToMany(() => Task, (task) => task.project)
	tasks: Task[];

	@OneToMany(() => Milestone, (milestone) => milestone.project)
	milestones: Milestone[];
};
