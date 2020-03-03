import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'stringLength' })
export class StringLengthPipe implements PipeTransform {
    transform(value: string, length: number = 20, ellipsis: boolean = true): string {
        let returnString = null;

        if (value === undefined || value === null) {
            return;
        }

        if (value.length > length) {
            returnString = value.substr(0, length);

            if (ellipsis) {
                returnString = `${returnString}...`;
            }
        } else {
            returnString = value;
        }
        return returnString;
    }
}
