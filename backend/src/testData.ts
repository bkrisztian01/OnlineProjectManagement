import {
  createMilestone,
  updateMilestoneById,
} from './services/milestones.service';
import { createProject } from './services/projects.service';
import { createTask } from './services/tasks.service';
import { createUser } from './services/users.service';
import { Status } from './util/Status';

export async function initTestData() {
  await createUser('testuser', 'test123', 'Test User', 'testuser@example.com');
  await createUser(
    'mariska',
    'mariska123',
    'Kovács Mariann',
    'kovimari@gmail.com',
  );
  await createProject('Project #1', 'Test project');
  await createProject('MobWeb házi', 'Nagyházi');
  await createTask(1, 'Task #1', 'Test task', '2022-12-12');
  await createTask(2, 'Engedélyek', 'PermissionDispatcher', null);
  await createMilestone(
    2,
    'MainActivity',
    'MainActivity',
    Status.InProgress,
    '2022-11-19',
  );

  await updateMilestoneById(1, null, null, null, null, [2]);
}
