import * as moment from 'moment';
import { NgbDateStruct, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { DATE_FORMAT_YYYYMMDD } from './../../shared';

export class DateUtils {

  public static formatDate(date: any): string {
    if (date) {
      return moment({
        year: date.year,
        month: date.month - 1,
        date: date.day
      }).format(DATE_FORMAT_YYYYMMDD);
    }
  }

  public static parseDate(ngbDate: NgbDateStruct): string {
    if (ngbDate) {
        const parsedDate = this.formatDate(ngbDate);
        return parsedDate;
    }
    return null;
  }

  public static convertDateToNgbDateStruct(date: Date): NgbDateStruct {
    const tempDate = new Date(date);
    const finalDate = new Date(tempDate.getTime() + tempDate.getTimezoneOffset() * 60000);
    return {
        year: finalDate.getFullYear(),
        month: finalDate.getMonth() + 1,
        day: finalDate.getDate()
    };

}

  constructor() {}
}
