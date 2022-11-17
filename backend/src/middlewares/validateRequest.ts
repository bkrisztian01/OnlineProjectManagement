import type { NextFunction, Request, Response } from 'express';
import type { AnySchema } from 'yup';

export const validate =
  (schema: AnySchema) =>
  async (req: Request, res: Response, next: NextFunction) => {
    try {
      await schema.validate(
        {
          // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
          body: req.body,
          query: req.query,
          params: req.params,
        },
        { strict: true },
      );

      next();
      return;
    } catch (e: any) {
      res.status(400).send(e.message);
    }
  };

export default validate;
