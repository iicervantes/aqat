import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AqatcontainerComponent } from './aqatcontainer.component';

describe('AqatcontainerComponent', () => {
  let component: AqatcontainerComponent;
  let fixture: ComponentFixture<AqatcontainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AqatcontainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AqatcontainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
