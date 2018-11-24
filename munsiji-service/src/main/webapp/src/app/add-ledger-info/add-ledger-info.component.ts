import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import {UserinfoService} from './../services/userinfo.service';

import { Router } from '@angular/router';
import { UrlConfig } from '../../environments/url-config';
import {DataService} from '../services/data.service'; 
import {PromptMessageComponent} from '../template/promptMessage/promptMessage.component';

@Component({
  selector: 'app-add-ledger-info',
  templateUrl: './add-ledger-info.component.html',
  styleUrls: ['./add-ledger-info.component.css']
})
export class AddLedgerInfoComponent implements OnInit, OnDestroy {
  URL = UrlConfig;
  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;

  expence:any; 
  arrAccountName = [];
  isMessage:boolean = false;
  
  constructor( private userService:UserinfoService, private dataService: DataService, private router: Router) { }


  ngOnInit() { 
    this.clearForm();
    //let dataMoel = this.userService.getDataModel();
    //this.arrAccountName = dataMoel.dropDown;
    //console.log(this.arrAccountName);
    this.promptMessageComponent.showLoader();
    let getAccountTypeURL = this.URL.GET_ACCOUNT_TYPE+"pexpence";

    let sub = this.dataService.httpGetCall(getAccountTypeURL).subscribe(data =>{
      this.arrAccountName = data.data.accountDetail.pexpence;
      console.log("Account names for PEXPENCE: ", this.arrAccountName);
      this.promptMessageComponent.hideLoader();
      sub.unsubscribe();
    },err =>{
      this.promptMessageComponent.hideLoader();
      sub.unsubscribe();
    })

  }
  
  ngOnDestroy(){  }

  submitExpence(){

    this.promptMessageComponent.showLoader();
    let addDataModel, index =this.expence.account.split("#")[1];

    addDataModel = {
      "accType"     : this.expence.accType,
      "accName"     : this.expence.account,
      "amount"      : this.expence.amount,
      "dateOfExpnse": this.expence.date,
      "desc"        : this.expence.desc
      };
      
    this.userService.setDataModelForAccount(this.expence, index);
    let sub = this.dataService.httpPostCall(this.URL.ADD_EXPANCE, addDataModel).subscribe( res => {
      this.isMessage = true;
      console.log(".ADD_EXPANCE http call is success", res.msg);
      alert(res.msg);
      sub.unsubscribe();
      this.promptMessageComponent.hideLoader();

    }, err => {
      this.promptMessageComponent.hideLoader();
      console.log("Error in .ADD_EXPANCE HTTP call ", err);
      sub.unsubscribe();
    });
  }

  fromReset(){
    this.isMessage = false;
    this.clearForm();
  }
  routeToHomePage(){
    this.router.navigate(['/']); 
  }

  clearForm(){
    this.expence={
      "accType":"pexpence",
      "account":"",
      "amount":0,
      "date":"",
      "desc":""
    }; 
  }

}
 