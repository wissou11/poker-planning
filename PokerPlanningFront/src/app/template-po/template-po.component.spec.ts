import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplatePOComponent } from './template-po.component';

describe('TemplatePOComponent', () => {
  let component: TemplatePOComponent;
  let fixture: ComponentFixture<TemplatePOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TemplatePOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplatePOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
