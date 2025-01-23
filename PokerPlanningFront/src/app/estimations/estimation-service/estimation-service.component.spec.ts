import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstimationServiceComponent } from './estimation-service.component';

describe('EstimationServiceComponent', () => {
  let component: EstimationServiceComponent;
  let fixture: ComponentFixture<EstimationServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EstimationServiceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstimationServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
