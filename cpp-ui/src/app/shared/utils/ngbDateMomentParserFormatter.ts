import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';

export class NgbDateMomentParserFormatter extends NgbDateParserFormatter {
    constructor(private momentFormat: string) {
        super();
    };

    format(date: NgbDateStruct): string {
      if (date === null) {
          return '';
      }
      const dateObj = moment({
          year: date.year,
          month: date.month - 1,
          date: date.day
      });
      return dateObj.isValid() ? dateObj.format(this.momentFormat) : '';
  }

  parse(value: string): NgbDateStruct {
      if (!value) {
          return null;
      }
      const date = moment(value, this.momentFormat);
      return date.isValid() ? {
          year: date.year(),
          month: date.month() + 1,
          day: date.date()
      } : null;
  }
}
