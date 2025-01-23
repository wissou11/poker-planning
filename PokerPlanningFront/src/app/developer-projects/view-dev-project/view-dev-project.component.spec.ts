import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDevProjectComponent } from './view-dev-project.component';

describe('ViewDevProjectComponent', () => {
  let component: ViewDevProjectComponent;
  let fixture: ComponentFixture<ViewDevProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewDevProjectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewDevProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
