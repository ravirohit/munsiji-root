import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {UserinfoService} from './services/userinfo.service';

import { UrlConfig } from '../environments/url-config';
import {DataService } from '../app/services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isVisble = true;
  userData = {isLogedin:false};
  
  constructor(private userInfo:UserinfoService,private router: Router,  private dataService : DataService){
  }
    
  ngOnInit(){
    this.userData = this.userInfo.getUserData();
  }
  logout():void{
    localStorage.setItem("ui",JSON.stringify({}));
    this.userInfo.setUSerData({});
    this.router.navigate(['/']); 

        let logoutURL = UrlConfig.LOGOUT;
        let sub = this.dataService.httpGetCall(logoutURL).subscribe(data =>{
            sub.unsubscribe();    
            alert("LOGOUT IS sUCCESSFUL...");
            localStorage.setItem("ui",JSON.stringify({}));
            this.userInfo.setUSerData({});
            this.router.navigate(['/']); 
  
        },(err) =>{
          console.log("Error in LOGOUT HTTP call ", err);
          sub.unsubscribe();
        });
  }


  toogleMenu(e):void{
    console.log(this.isVisble+"   asds" + e);
    this.isVisble = !this.isVisble;
  }
    id = 'chart1';
    width = 600;
    height = 400;
    type = 'column2d';
    dataFormat = 'json';
    dataSource;
    title = 'Angular4 FusionCharts Sample';

}
