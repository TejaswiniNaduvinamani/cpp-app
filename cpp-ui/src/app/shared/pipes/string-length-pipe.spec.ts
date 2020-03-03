import { StringLengthPipe } from 'app/shared';

describe('StringLengthPipe', () => {
    let pipe: StringLengthPipe;

    beforeEach(() => {
        pipe = new StringLengthPipe();
    });

    it('should return when no value is provided', () => {
        // WHEN
        pipe.transform(null, 10, false);
    });

    it('should return value', () => {
        // WHEN
        pipe.transform('add', 4, false);

        // THEN
        expect(pipe).toBeTruthy();
    });

    it('should return substring', () => {
        expect(pipe.transform('adding', 4, false)).toBeTruthy();
    });

    it('should change value if ellipsis true', () => {
        expect(pipe.transform('adding', 4, true)).toBeTruthy();
    });

});
