import { Component, OnInit } from '@angular/core';
import {UserinfoService} from './../../services/userinfo.service'

import { UrlConfig } from './../../../environments/url-config';
import {DataService} from '../../services/data.service'; 

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent implements OnInit {

  URL = UrlConfig;

  name = "dfdf";
  acc:any = {title:"",type:"",desc:"",date:"" }

  model = {isMessage: false}
  constructor( private userService:UserinfoService, private dataService:DataService) { }

  ngOnInit() {
    // {
    //   "name": "SIP",
    //   "type": "Bank",
    //   "investedAmnt": 6000,
    //   "desc":"future plan",
    //   "crteDate": "01-10-2018 10:12:15"
    //  }
  }

  createAccount():void{

    let createAccountUrl = this.URL.CREATE_ACCOUNT,
        createDataModel = {
                            name : this.acc.title, 
                            type: 'pexpence', 
                            investedAmnt : this.acc.bal, 
                            crteDate : this.acc.date
                          };

    console.log(this.acc.name," > ",this.acc.type," > ",this.acc.date," > ",this.acc.bal," > ",this.acc.desc);


    this.userService.setDataModelForAcctType(this.acc);
    


    let sub = this.dataService.httpPostCall(createAccountUrl, createDataModel).subscribe( res => {
      console.log("CREATE_ACCOUNT http call is success", res.msg);
      this.acc.resMSG = res.msg;
      this.model.isMessage = true;
      sub.unsubscribe();
    }, err => {
      console.log("Error in CREATE_ACCOUNT HTTP call ", err);
      sub.unsubscribe();
    });
    
  }
}
