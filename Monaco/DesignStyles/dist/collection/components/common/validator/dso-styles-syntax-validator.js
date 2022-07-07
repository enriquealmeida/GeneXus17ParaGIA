import { validate } from '../grammar/src/DesignStylesParserFacade';
export function validateValue(value) {
    return validate(value);
}
