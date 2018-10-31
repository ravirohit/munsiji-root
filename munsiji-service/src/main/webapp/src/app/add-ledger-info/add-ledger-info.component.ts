import { Component, OnInit, OnDestroy, OnChanges } from '@angular/core';
import {UserinfoService} from './../services/userinfo.service';

import { UrlConfig } from '../../environments/url-config';
import {DataService} from '../services/data.service'; 

@Component({
  selector: 'app-add-ledger-info',
  templateUrl: './add-ledger-info.component.html',
  styleUrls: ['./add-ledger-info.component.css']
})
export class AddLedgerInfoComponent implements OnInit, OnDestroy {
  URL = UrlConfig;
  expence:any={
    "accType":"pexpence",
    "account":"",
    "amount":0,
    "date":"",
    "desc":""
  }; 
  arrAccountName = [{}];

  constructor( private userService:UserinfoService, private dataService: DataService) { }


  ngOnInit() { 
    let dataMoel = this.userService.getDataModel();
    this.arrAccountName = dataMoel.dropDown;
    console.log(this.arrAccountName);
  }

  ngOnDestroy(){  }

  submitExpence(){
    let addDataModel, index =this.expence.account.split("#")[1];

    addDataModel = {
      "accType"     : this.expence.accType,
      "accName"     : this.expence.account,
      "amount"      : this.expence.amount,
      "dateOfExpnse": this.expence.date,
      "desc"        : this.expence.desc
      }

    console.log(index);
    this.userService.setDataModelForAccount(this.expence, index);

    let sub = this.dataService.httpPostCall(this.URL.ADD_EXPANCE, addDataModel).subscribe( res => {
      console.log(".ADD_EXPANCE http call is success", res.msg);
      alert(res.msg);
      this.expence  ={
                      "accType":"pexpence",
                      "account":"",
                      "amount":0,
                      "date":"",
                      "desc":""
                    }; 
      sub.unsubscribe();
    }, err => {
      console.log("Error in .ADD_EXPANCE HTTP call ", err);
      sub.unsubscribe();
    });
  }
}
 