import { Component, OnInit, ViewChild } from '@angular/core';
import {UserinfoService} from './../../services/userinfo.service'

import { Router } from '@angular/router';
import { UrlConfig } from './../../../environments/url-config';
import {DataService} from '../../services/data.service'; 
import {PromptMessageComponent} from '../../template/promptMessage/promptMessage.component';


@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent implements OnInit {

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;

  URL = UrlConfig;

  public options: Pickadate.DateOptions = {
    format: 'dddd, dd mmm, yyyy',
    formatSubmit: 'dd-mm-yyyy',
  };

  //name = "dfdf";
  acc:any = {name:"",type:"",desc:"",date:'' }

  model = {isMessage: false}
  constructor(  private userService:UserinfoService,
                private dataService:DataService, 
                private router: Router) { }

  ngOnInit() { }

  createAccount():void{
    this.promptMessageComponent.showLoader();
    let date = this.acc.date;//this.formatDate(this.acc.date);
    let createAccountUrl = this.URL.CREATE_ACCOUNT,
        createDataModel = {
                            name : this.acc.name, 
                            type: 'personalexp', 
                            investedAmnt : this.acc.bal, 
                            crteDate : date,
                            desc  : this.acc.desc
                          };
    
    let sub = this.dataService.httpPostCall(createAccountUrl, createDataModel).subscribe( res => {

                      console.log("CREATE_ACCOUNT http call is success", res.msg);  
                      this.model.isMessage = true;
                      sub.unsubscribe();
                      this.promptMessageComponent.hideLoader();                      
                      this.promptMessageComponent.showToastMessage(res.msg,"green",3000);
                    }, err => {
                      console.log("Error in CREATE_ACCOUNT HTTP call ", err);
                      this.promptMessageComponent.hideLoader();                      
                      this.promptMessageComponent.showToastMessage("Error: Can not create account now. Please try after some time. ","red",3000);
                      sub.unsubscribe();
                    });
    
  }
  formatDate(d:Date){   // from "2018-12-20" to "dd-mm-yyyy"
  
    return  d.getDate()+"-"+ d.getMonth() +"-"+d.getFullYear();
  }
  fromReset(){
    this.model.isMessage = false;
    this.acc = {name:"",type:"",desc:"",date:new Date() 
  }
  }
  routeToHomePage(){
    this.router.navigate(['']); 
  }
}
