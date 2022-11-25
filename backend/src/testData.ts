import {
  createMilestone,
  updateMilestoneById,
} from './services/milestone.service';
import { createProject } from './services/project.service';
import { createTask } from './services/task.service';
import { createUser } from './services/user.service';
import { Status } from './util/Status';

export async function initTestData() {
  await createUser('testuser', 'test123', 'Test User', 'testuser@example.com');
  await createUser(
    'mariska',
    'mariska123',
    'Kovács Mariann',
    'kovimari@gmail.com',
  );
  await createProject(
    'Project #1',
    'Test project',
    '2022-01-01',
    null,
    Status.NotStarted,
    null,
    1,
    [2],
  );
  await createProject(
    'MobWeb házi',
    'Nagyházi',
    null,
    null,
    Status.InProgress,
    null,
    2,
    [1],
  );
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
