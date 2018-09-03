import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLedgerInfoComponent } from './add-ledger-info.component';

describe('AddLedgerInfoComponent', () => {
  let component: AddLedgerInfoComponent;
  let fixture: ComponentFixture<AddLedgerInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddLedgerInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddLedgerInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
