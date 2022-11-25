import express from 'express';
import {
  getCurrentUserHandler,
  getLoginUserHandler,
  getLogoutUserHandler,
  getUsersHandler,
  getUsersTasksHandler,
  postSignupUserHandler,
} from '../controllers/user.controller';
import validateRequest from '../middlewares/validateRequest';
import { verifyJwt } from '../middlewares/verifyJwt';
import { createUserSchema, loginUserSchema } from '../schemas/user.schema';

const router = express.Router();

router
  .route('/user/login')
  .post(validateRequest(loginUserSchema), getLoginUserHandler);

router.route('/user/logout').get(getLogoutUserHandler);

router
  .route('/user/signup')
  .post(validateRequest(createUserSchema), postSignupUserHandler);

router.route('/users').get(getUsersHandler);

router.route('/users/current').get(verifyJwt, getCurrentUserHandler);

router.route('/user/tasks').get(verifyJwt, getUsersTasksHandler);

export = router;
