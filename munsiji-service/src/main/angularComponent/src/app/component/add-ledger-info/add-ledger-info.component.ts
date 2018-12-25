import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';

import { Router } from '@angular/router';
import { UrlConfig } from '../../../environments/url-config';
import {DataService} from '../../services/data.service'; 
import {PromptMessageComponent} from '../../template/promptMessage/promptMessage.component';

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

  public options: Pickadate.DateOptions = {
    format: 'dddd, dd mmm, yyyy',
    formatSubmit: 'yyyy-mm-dd',
  };
  
  constructor(  private dataService: DataService, private router: Router) { }


  ngOnInit() { 
    this.clearForm();
    //let dataMoel = this.userService.getDataModel();
    //this.arrAccountName = dataMoel.dropDown;
    //console.log(this.arrAccountName);
    this.promptMessageComponent.showLoader();
    let getAccountTypeURL = this.URL.GET_ACCOUNT_TYPE+"personalexp";

    let sub = this.dataService.httpGetCall(getAccountTypeURL).subscribe(data =>{
      this.arrAccountName = data.data.accountDetail.personalexp;
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
      
    //this.userService.setDataModelForAccount(this.expence, index);
    let sub = this.dataService.httpPostCall(this.URL.ADD_EXPANCE, addDataModel).subscribe( res => {
      this.isMessage = true;
      this.promptMessageComponent.hideLoader();
      sub.unsubscribe();

    }, err => {
      this.promptMessageComponent.hideLoader();
      sub.unsubscribe();
    });
  }

  fromReset(){
    this.isMessage = false;
    this.clearForm();
  }
  routeToHomePage(){
    this.router.navigate(['']); 
  }

  clearForm(){
    this.expence={
      "accType":"personalexp",
      "account":"",
      "amount":null,
      "date":"",
      "desc":""
    }; 
  }

}
 