import express from 'express';
import {
  getLoginUserHandler,
  getLogoutUserHandler,
  getUsersHandler,
  postSignupUserHandler,
} from '../controllers/users.controller';
import validateRequest from '../middlewares/validateRequest';
import { createUserSchema, loginUserSchema } from '../schemas/users.schema';

const router = express.Router();

router
  .route('/user/login')
  .get(validateRequest(loginUserSchema), getLoginUserHandler);

router.route('/user/logout').get(getLogoutUserHandler);

router
  .route('/user/signup')
  .post(validateRequest(createUserSchema), postSignupUserHandler);

router.route('/users').get(getUsersHandler);

export = router;
