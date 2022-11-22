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
  await createProject('Project #1', 'Test project', '2022-01-01', null, null);
  await createProject('MobWeb házi', 'Nagyházi', null, null, null);
  await createTask(1, 'Task #1', 'Test task', Status.NotStarted, '2022-12-12');
  await createTask(
    2,
    'Engedélyek',
    'PermissionDispatcher',
    Status.InProgress,
    null,
  );
  await createMilestone(
    2,
    'MainActivity',
    'MainActivity',
    Status.InProgress,
    '2022-11-19',
  );

  await updateMilestoneById(2, 1, null, null, null, null, [2]);
}
