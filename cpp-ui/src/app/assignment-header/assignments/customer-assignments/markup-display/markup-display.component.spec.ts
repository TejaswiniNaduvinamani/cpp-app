import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { MarkupDisplayComponent } from './markup-display.component';

describe('MarkupDisplayComponent', () => {
  let component: MarkupDisplayComponent;
  let fixture: ComponentFixture<MarkupDisplayComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MarkupDisplayComponent]
    }).overrideTemplate(MarkupDisplayComponent, '');

    fixture = TestBed.createComponent(MarkupDisplayComponent);
    component = fixture.componentInstance;
  });

  it('should create Markup Display Component', () => {
    expect(component).toBeTruthy();
  });
});
