import { Component, OnInit, OnDestroy, OnChanges } from '@angular/core';
import {UserinfoService} from './../services/userinfo.service';

@Component({
  selector: 'app-add-ledger-info',
  templateUrl: './add-ledger-info.component.html',
  styleUrls: ['./add-ledger-info.component.css']
})
export class AddLedgerInfoComponent implements OnInit, OnDestroy {
  expence:any={
    "accType":"p_expence",
    "account":"",
    "amount":0,
    "date":"",
    "desc":""
  }; 
  arrAccountName = [{}];

  constructor( private userService:UserinfoService) { }


  ngOnInit() { 
    let dataMoel = this.userService.getDataModel();
    this.arrAccountName = dataMoel.dropDown;
    console.log(this.arrAccountName);
  }

  ngOnDestroy(){  }

  submitExpence(){
    let index =this.expence.account.split("#")[1];
    console.log(index);
    this.userService.setDataModelForAccount(this.expence, index);
  }
}
 