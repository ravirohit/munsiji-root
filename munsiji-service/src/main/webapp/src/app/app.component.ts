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
  fontSize:number = 20;
  
  constructor(private userInfo:UserinfoService,private router: Router,  private dataService : DataService){

    this.dataSource = {
      "chart": {
          "caption": "Harry's SuperMart",
          "subCaption": "Top 5 stores in last month by revenue",
          "numberprefix": "$",
          "theme": "fint"
      },
      "data": [
          {
              "label": "Bakersfield Central",
              "value": "880000"
          },
          {
              "label": "Garden Groove harbour",
              "value": "730000"
          },
          {
              "label": "Los Angeles Topanga",
              "value": "590000"
          },
          {
              "label": "Compton-Rancho Dom",
              "value": "520000"
          },
          {
              "label": "Daly City Serramonte",
              "value": "330000"
          }
      ]
  }

  }
  ngOnInit(){
    this.userData = this.userInfo.getUserData();
  }
  logout():void{
        

        let logoutURL = UrlConfig.LOGOUT;
        let sub = this.dataService.httpPostCall(logoutURL,{}).subscribe(data =>{
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
