import { Request, Response, NextFunction } from "express";

const projects = [
    {
        id: 1,
        name: "New Facebook"
    },
    {
        id: 2,
        name: "New Twitter",
    },
]

export function getProjectHandler(req: Request, res: Response, next: NextFunction) {
    res.send(projects);
}