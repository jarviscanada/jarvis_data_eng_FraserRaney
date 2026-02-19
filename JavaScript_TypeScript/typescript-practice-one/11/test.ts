import {ArrayElement, IsTypeEqual, typeAssert} from '../type-assertions';
import {nameDecorators} from './index';

typeAssert<
    IsTypeEqual<
        ArrayElement<typeof nameDecorators>,
        (input: string) => string
    >
>();
