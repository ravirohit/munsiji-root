import { Component, AfterContentInit, OnInit, OnChanges } from '@angular/core';
import {Router} from '@angular/router';
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
  userData = {isLogedin:false};
  
  constructor(private userInfo:UserinfoService,private router: Router,  private dataService : DataService){
  }
    
  ngOnInit(){
    this.userData = this.userInfo.getUserData();
  }

  ngOnChanges(){
    alert("dsfsdfsd");

  }
  ngAfterContentInit(){
    setTimeout(()=>{
      d3.select("li").style("color", "red");
    },0)

  }
  logout():void{  

        let logoutURL = UrlConfig.LOGOUT;
        let sub = this.dataService.httpGetCall(logoutURL).subscribe(data =>{
            sub.unsubscribe();    
            this.userInfo.setUSerData({});
            this.router.navigate(['munsiji-service']); 
  
        },(err) =>{
          this.userInfo.setUSerData({});
          console.log("Error in LOGOUT HTTP call ", err);
          sub.unsubscribe();
        });
        this.router.navigate(['munsiji-service']);
  }


  toogleMenu(e):void{
    
    this.isVisble = !this.isVisble;
  }
  

}
