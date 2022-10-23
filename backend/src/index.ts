import cors from 'cors';
import express from 'express';
import routes from './routes';
import {initTestData} from './testData';

const app = express();
app.use(express.json());
app.use(cors());
const port = process.env.PORT ?? 5000;

routes(app);

initTestData();

app.listen(port, () => {
	console.log(`Listening on http://localhost:${port}`);
});
