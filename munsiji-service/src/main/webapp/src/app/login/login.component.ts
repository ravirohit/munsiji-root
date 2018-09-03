import { Component,Input, Output, OnInit, HostListener, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { UrlConfig } from './../../environments/url-config';

import {UserinfoService} from '../services/userinfo.service'; 
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

  constructor(private userService:UserinfoService, private router: Router) { }
  ngOnInit() { }  
  loginFn(e){
      let userObj = {isLogedin : true, name:"Raj kumar singh",  id:'A-0001',  email:'singhraj.swd@gmail.com',  cont:'9342276957'};
      this.userService.setUSerData(userObj);
      this.router.navigate(['/']);   
  }
  @Input()  pre;
  @Input() size : number;
  @Output() sizeChange =  new EventEmitter();
  
  resize(d:number){
    this.size += d;
    this.sizeChange.emit(this.size);
  }
/***
 * Signup methtod ;
 * 
 */
  signUp(){

    if(this.signupModel.pwd1 !== this.signupModel.pwd2){
      this.signupErrorModel.commonMessage = true;
    }else{
      this.signupErrorModel.commonMessage = false;
    }
    console.log(this.url.REGISTER_USER);
  }

}
