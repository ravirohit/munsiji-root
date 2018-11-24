import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

import { UrlConfig } from './../../environments/url-config';

import {UserinfoService} from '../services/userinfo.service'; 
import {DataService} from '../services/data.service'; 
import {PromptMessageComponent} from '../template/promptMessage/promptMessage.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  url = UrlConfig;
  isMessage:boolean = false;
  isSignup:boolean = false;
  signupModel = {  name:"",  pwd1:"",  pwd2:"",  email:""  };
  signupErrorModel = {  name:"",  pwd1:"",  pwd2:"",  email:"", commonMessage:false };

  constructor(private userService:UserinfoService, private router: Router, private dataService : DataService) { }

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;

  ngOnInit() { }  

  loginFn(userID, userPWD){
    this.isMessage=false;
        
    if(userID.value.length > 3 && userPWD.value.length > 3){

      let loginURL:string = this.url.LOGIN, data:any = {};
      data = {
        "userName": userID.value,	
        "pwd": userPWD.value
      };
      this.promptMessageComponent.showLoader();
      let sub = this.dataService.httpPostCall(loginURL,data).subscribe(data =>{
        sub.unsubscribe();
        alert("LOGIN IS sUCCESSFUL...");
        let userObj = {
                        isLogedin : true, 
                        name:"Raj kumar singh",
                        k:data.msg
                      };
        this.userService.setUSerData(userObj);
        this.router.navigate(['/munsiji-service']);   
        this.promptMessageComponent.hideLoader();
      },(err) =>{
        console.log("Error in LOGIN HTTP call ", err);
        sub.unsubscribe();
        this.promptMessageComponent.showLoader();
      });
      }else{
        console.log("USERID OR PASSWORD is not correct.");

        let userObj = {
          isLogedin : true, 
          name:"Raj kumar singh",
          k:"@@@@@@@@@@@@@@@"
        };
      this.userService.setUSerData(userObj);
      
      this.router.navigate(['/munsiji-service']);  
      }
    }
 
  

/***
 * Signup methtod ;
 * 
 */
  signUp(){

    this.isMessage=true;
    if(this.signupModel.pwd1 !== this.signupModel.pwd2){
      this.signupErrorModel.commonMessage = true;
    }else{
      let signUpUrl = this.url.REGISTER_USER, data = {};
      data = { emailId : this.signupModel.email,
              mobileNo: this.signupModel.name,
              pwd     : this.signupModel.pwd1,
              role    : ""
            };
      this.signupErrorModel.commonMessage = false;

      let sub = this.dataService.httpPostCall(signUpUrl, data).subscribe( res => {
        
        this.isMessage=true;
        this.isSignup = !this.isSignup;
        sub.unsubscribe();
      
      }, err => {
        console.log("Error in SIGNUP HTTP call ", err);
        sub.unsubscribe();
      });

    }
    console.log(this.url.REGISTER_USER);
   
  }

}
