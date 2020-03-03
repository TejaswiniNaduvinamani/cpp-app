import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ToggleComponent } from './toggle.component';

describe('Toggle Component', () => {

  let component: ToggleComponent;
  let fixture: ComponentFixture<ToggleComponent>;
  const formBuilder = new FormBuilder;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ToggleComponent],
      providers: []
    }).overrideTemplate(ToggleComponent, '');

    fixture = TestBed.createComponent(ToggleComponent);
    component = fixture.componentInstance;
  });


  it('should set toggle on - onload', () => {
    // THEN
   expect(component).toBeTruthy();
  });

  it('should set toggle off - onload', () => {
    // GIVEN
    component.toggleForm = formBuilder.group({
      selectedValue: [false],
    });
    component.toggleDefaultState = true;

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.toggleForm.controls['selectedValue'].value).toBeTruthy();
  });

  it('should set toggle on onload', () => {
    // GIVEN
    component.toggleForm = formBuilder.group({
      selectedValue: [true],
    });
    component.toggleDefaultState = false;

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(component.toggleForm.controls['selectedValue'].value).toBeFalsy();
  });

  it('should switch toggle to on onchange', () => {
    // GIVEN
    component.toggleForm = formBuilder.group({
      selectedValue: [false],
    });
    component.toggleDefaultState = false;
    const event = { target : { checked : true }};

    // WHEN
    component.checkState(event);

    // THEN
    expect(component.toggleForm.controls['selectedValue'].value).toBeTruthy();
  });

  it('should switch toggle to off onchange', () => {
    // GIVEN
    component.toggleForm = formBuilder.group({
      selectedValue: [true],
    });
    component.toggleDefaultState = false;
    const event = { target : { checked : false }};

    // WHEN
    component.checkState(event);

    // THEN
    expect(component.toggleForm.controls['selectedValue'].value).toBeFalsy();
  });

})
