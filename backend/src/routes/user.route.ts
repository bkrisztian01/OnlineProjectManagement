import express from 'express';
import {
  getCurrentUserHandler,
  getLoginUserHandler,
  getLogoutUserHandler,
  getUsersHandler,
  postSignupUserHandler,
} from '../controllers/user.controller';
import validateRequest from '../middlewares/validateRequest';
import { verifyJwt } from '../middlewares/verifyJwt';
import { createUserSchema, loginUserSchema } from '../schemas/user.schema';

const router = express.Router();

router
  .route('/user/login')
  .get(validateRequest(loginUserSchema), getLoginUserHandler);

router.route('/user/logout').get(getLogoutUserHandler);

router
  .route('/user/signup')
  .post(validateRequest(createUserSchema), postSignupUserHandler);

router.route('/users').get(getUsersHandler);

router.route('/users/current').get(verifyJwt, getCurrentUserHandler);

export = router;
