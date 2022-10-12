import express from 'express'
import routes from './routes';



const app = express()
app.use(express.json())
var port = process.env.PORT || 3000;

routes(app);

app.listen(port, () => {
    console.log(`Listening on http://localhost:${port}`);
});