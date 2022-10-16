import type {AnySchema} from 'yup';
import type {Request, Response, NextFunction} from 'express';

export const validate = (schema: AnySchema) => async (
	req: Request,
	res: Response,
	next: NextFunction,
) => {
	try {
		await schema.validate({
			// eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
			body: req.body,
			query: req.query,
			params: req.params,
		});

		next();
		return;
	} catch (err: unknown) {
		res.status(400).send(err);
	}
};

export default validate;
