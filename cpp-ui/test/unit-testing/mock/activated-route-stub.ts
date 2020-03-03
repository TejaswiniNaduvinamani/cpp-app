import { Observable } from 'rxjs/Rx';
import { Subject } from 'rxjs/Subject';

export class ActivatedRouteStub {

  params: Observable<any> = Observable.empty();
  private subject = new Subject();

  public push(value) {
    this.subject.next(value);
  }

  get queryParams() {
    return this.subject.asObservable();
  }

}
