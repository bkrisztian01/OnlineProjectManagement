import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ButtonFinishComponent } from './button-finish.component';

describe('ButtonFinishComponent', () => {
  let component: ButtonFinishComponent;
  let fixture: ComponentFixture<ButtonFinishComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ButtonFinishComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ButtonFinishComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
