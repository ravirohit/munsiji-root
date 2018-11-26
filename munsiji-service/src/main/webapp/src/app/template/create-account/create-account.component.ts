import { Component, OnInit, ViewChild } from '@angular/core';
import {UserinfoService} from './../../services/userinfo.service'

import { Router } from '@angular/router';
import { UrlConfig } from './../../../environments/url-config';
import {DataService} from '../../services/data.service'; 
import {PromptMessageComponent} from './../promptMessage/promptMessage.component';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent implements OnInit {

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;

  URL = UrlConfig;

  //name = "dfdf";
  acc:any = {name:"",type:"",desc:"",date:new Date() }

  model = {isMessage: false}
  constructor( private userService:UserinfoService, private dataService:DataService, private router: Router) { }

  ngOnInit() { }

  createAccount():void{
    this.promptMessageComponent.showLoader();
    let createAccountUrl = this.URL.CREATE_ACCOUNT,
        createDataModel = {
                            name : this.acc.name, 
                            type: 'personalexp', 
                            investedAmnt : this.acc.bal, 
                            crteDate : this.acc.date,
                            desc  : this.acc.desc
                          };
    let sub = this.dataService.httpPostCall(createAccountUrl, createDataModel).subscribe( res => {
                      console.log("CREATE_ACCOUNT http call is success", res.msg);
                      
                      //this.acc.resMSG = res.msg;
                      this.model.isMessage = true;
                      sub.unsubscribe();
                      this.promptMessageComponent.hideLoader();
                      if(res.status != 403){
                        this.router.navigate(['munsiji-service']);
                      }
                    }, err => {
                      console.log("Error in CREATE_ACCOUNT HTTP call ", err);
                      this.promptMessageComponent.hideLoader();
                      sub.unsubscribe();
                    });
    
  }

  fromReset(){
    this.model.isMessage = false;
    this.acc = {name:"",type:"",desc:"",date:new Date() 
  }
  }
  routeToHomePage(){
    this.router.navigate(['munsiji-service']); 
  }
}
