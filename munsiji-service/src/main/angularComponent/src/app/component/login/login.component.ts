import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';

import { UrlConfig } from './../../../environments/url-config';

import {UserinfoService} from '../../services/userinfo.service'; 
import {DataService} from '../../services/data.service'; 
import {PromptMessageComponent} from '../../template/promptMessage/promptMessage.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  forgetPassword : boolean = false;
  url = UrlConfig;
  //isMessage:boolean = false;
  isSignup:boolean = false;
  signupModel = {  mobileNo:"",  pwd1:"",  pwd2:"",  email:""  };
  signupErrorModel = {  name:"",  pwd1:"",  pwd2:"",  email:"", commonMessage:false };

  constructor(private userService:UserinfoService, private router: Router, private dataService : DataService) { }

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;

  ngOnInit() { 
    
    this.userService.setUSerData({});
  }  

  loginFn(userID, userPWD){

    if(userID.value.length > 3 && userPWD.value.length > 3){

      let loginURL:string = this.url.LOGIN, data:any = {};
      data = {
        "userName": userID.value,	
        "pwd": userPWD.value
      };
      this.promptMessageComponent.showLoader();
      let sub = this.dataService.httpPostCall(loginURL,data).subscribe(res =>{
        sub.unsubscribe();
       
        if(res.status != 403){
            let userObj = {
              isLogedin : true, 
              name:"Raj kumar singh",
              k:res.msg
            };
            this.userService.setUSerData(userObj);
            this.router.navigate(['']);   
            this.promptMessageComponent.hideLoader();
        }else{
          console.log("ERROR",res);
          this.promptMessageComponent.showToastMessage(res["msg"],"red",3000);
        }
      },(err) =>{
        console.log("Error in LOGIN HTTP call ", err);
        sub.unsubscribe();
        this.promptMessageComponent.showToastMessage(err["msg"],"red",3000);        
        this.promptMessageComponent.hideLoader();
      });
      }
      else{
        console.log("USERID OR PASSWORD is not correct.");

        let userObj = {
          isLogedin : true, 
          name:"Raj kumar singh",
          k:"@@@@@@@@@@@@@@@"
        };
      this.userService.setUSerData(userObj);
      
      this.router.navigate(['']);  
      }
    }
 
  

/***
 * Signup methtod ;
 * 
 */
  signUp(){
    
    if(this.signupModel.pwd1 !== this.signupModel.pwd2){
      this.signupErrorModel.commonMessage = true;
    }else{
      let signUpUrl = this.url.REGISTER_USER, data = {};
      data = { emailId : this.signupModel.email,
              mobileNo: this.signupModel.mobileNo,
              pwd     : this.signupModel.pwd1,
              role    : ""
            };
      this.signupErrorModel.commonMessage = false;

      let sub = this.dataService.httpPostCall(signUpUrl, data).subscribe( res => {
        this.promptMessageComponent.showToastMessage(res["msg"],"green",3000);
        //this.isMessage=true;
        this.isSignup = !this.isSignup;

        sub.unsubscribe();
      
      }, err => {
        
        this.isSignup = !this.isSignup;
        console.log("Error in SIGNUP HTTP call ", err);
        this.promptMessageComponent.showToastMessage(err["msg"],"red",3000);
        sub.unsubscribe();
      });

    }
    console.log(this.url.REGISTER_USER);
   
  }

  resetPWD(el){
    console.log(el);
    let emailId = el.value;
    let forgetUrl = this.url.FORGETPWD + emailId;
    let sub = this.dataService.httpGetCall(forgetUrl).subscribe(res => {
      console.log("response:"+res);
      this.forgetPassword = !this.forgetPassword;      
      this.promptMessageComponent.showToastMessage(res["msg"],"green",3000);
      sub.unsubscribe();
    }, err => {
      console.log("Error in FORGETPWD HTTP call ", err);
      sub.unsubscribe();      
      this.promptMessageComponent.showToastMessage(err["msg"],"red",3000);
    });
    //this.forgetPassword = false;
  }

}
