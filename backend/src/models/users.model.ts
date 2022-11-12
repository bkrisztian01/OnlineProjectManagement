import { Column, Entity, JoinTable, ManyToMany, ManyToOne, PrimaryGeneratedColumn } from "typeorm";
import { Task } from "./tasks.model";

@Entity({ name: 'user' })
export class User {
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

	@ManyToMany(() => Task, (task) => task.assignees)
	@JoinTable()
	tasks: Task[]
};
