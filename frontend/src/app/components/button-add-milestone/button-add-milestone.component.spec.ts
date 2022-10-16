import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ButtonAddMilestoneComponent } from './button-add-milestone.component';

describe('ButtonAddMilestoneComponent', () => {
  let component: ButtonAddMilestoneComponent;
  let fixture: ComponentFixture<ButtonAddMilestoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ButtonAddMilestoneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ButtonAddMilestoneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
