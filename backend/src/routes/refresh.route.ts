import express from 'express';
import { handleRefreshToken } from '../controllers/refreshToken.controller';

const router = express.Router();

router.get('/refresh', handleRefreshToken);

export = router;
