import { Component, AfterContentInit, OnInit, OnChanges } from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';
import {UserinfoService} from './services/userinfo.service';

import { UrlConfig } from '../environments/url-config';
import {DataService } from '../app/services/data.service';


import * as d3 from 'd3';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterContentInit, OnChanges {
  isVisble = true;
  headerTitle:string = "";
  userData = {isLogedin:false};
  
  constructor(private userInfo:UserinfoService,private router: Router,  private dataService : DataService){

    router.events.forEach((event) => {
      if(event instanceof NavigationEnd) {
        this.headerTitle = "Route end"+event;
        this.isVisble = false;
        switch(event.url){
          case "/" : this.headerTitle = this.userData.isLogedin ? " Home " : ''; break;          
          case "/add" : this.headerTitle = " Add Expences "; break;
          case "/profile" : this.headerTitle = " Manage Profile "; break;
          case "/c_account" : this.headerTitle = " Create Account "; break;
          default : this.headerTitle = " Expence Details "; break;
        }
      }
      // NavigationEnd
      // NavigationCancel
      // NavigationError
      // RoutesRecognized
    });
  }
    
  ngOnInit(){
    this.userData = this.userInfo.getUserData();
  }

  ngOnChanges(){
    alert("dsfsdfsd");

  }
  ngAfterContentInit(){
    setTimeout(()=>{
      d3.select("li").style("color", "darkgrey");
    },0)

  }
  logout():void{  
        this.headerTitle = "";
        let logoutURL = UrlConfig.LOGOUT;
        let sub = this.dataService.httpGetCall(logoutURL).subscribe(data =>{
            sub.unsubscribe();    
            this.userInfo.setUSerData({});
            this.router.navigate(['']); 
  
        },(err) =>{
          this.userInfo.setUSerData({});
          console.log("Error in LOGOUT HTTP call ", err);
          sub.unsubscribe();
          this.router.navigate(['']);
        });
  }


  toogleMenu(e):void{
    
    this.isVisble = !this.isVisble;
  }
  

}
