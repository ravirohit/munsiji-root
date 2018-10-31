import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { UrlConfig } from './../../environments/url-config';
import {LoginModule} from './../Model/loginModel/loginModule';

import {UserinfoService} from '../services/userinfo.service'; 
import {DataService} from '../services/data.service'; 

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  url = UrlConfig;
  isSignup = false;
  signupModel = {  name:"",  pwd1:"",  pwd2:"",  email:""  };
  signupErrorModel = {  name:"",  pwd1:"",  pwd2:"",  email:"", commonMessage:false };

  constructor(private userService:UserinfoService, private router: Router, private dataService : DataService) { }

  ngOnInit() { }  

  loginFn(e){
      let userObj = {isLogedin : true, name:"Raj kumar singh",  id:'A-0001',  email:'singhraj.swd@gmail.com',  cont:'9342276957'};
      this.userService.setUSerData(userObj);
      this.router.navigate(['/']);   

    





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
              mobileNo: this.signupModel.name,
              pwd     : this.signupModel.pwd1,
              role    : ""
            };
      this.signupErrorModel.commonMessage = false;

      let sub = this.dataService.httpPostCall(signUpUrl, data).subscribe( res => {
        console.log("Signup http call is success", res.msg);
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
