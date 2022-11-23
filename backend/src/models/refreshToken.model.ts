import {
  BaseEntity,
  Column,
  Entity,
  JoinColumn,
  OneToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { User } from './user.model';

@Entity({ name: 'refresh_token' })
export class RefreshToken extends BaseEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('text')
  refreshToken: string;

  @OneToOne(() => User)
  @JoinColumn()
  user: User;
}
