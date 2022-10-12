import { Express } from "express"
import { getProjectHandler } from "./projects/projects.controller";

// const courses = [
//     {
//         id: 1,
//         name: 'course1',
//     },
//     {
//         id: 2,
//         name: 'course2',
//     },
//     {
//         id: 3,
//         name: 'course3',
//     },
// ];

function routes(app: Express) {
    app.get('/', (req, res) => {
        res.send("Hello World!");
    });
    
    app.route('/api/projects')
    .get(getProjectHandler);
    
    app.post('/courses', (req, res) => {
        if (!req.body.name || req.body.name.length < 3) {
            // 400 bad request
            res.status(400).send("Name is required and it should be minimum 3 characters");
        }
        
        const course = {
            id: courses.length + 1,
            name: req.body.name,
        };  
        courses.push(course);
        res.send(course);
    });

}

export default routes;