import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import {UserinfoService} from '../services/userinfo.service';
import {DataService} from '../services/data.service';
import { ElementRef } from '@angular/core/src/linker/element_ref';

import { Observable } from 'rxjs/Rx';
import {UrlConfig} from './../../environments/url-config';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  getUrl = UrlConfig;
  userName:any = {};
  homeModel = {isHeaderDisplay:false,userName: "Username", displayType: 2}
  data:any = [];//{te:12000,td:20000,sa:8000};

  constructor(private userService:UserinfoService, private httpCall:DataService) { }
   ngOnInit() {
     this.userName = this.userService.getUserData();
     this.data = this.userService.getDataModel();
     this.homeModel.isHeaderDisplay = true;
     console.log(this.getUrl.GET_ACCOUNT);
     var sub = this.httpCall.httpGetCall(this.getUrl.GET_ACCOUNT).subscribe(data =>{
       console.log(data);
       sub.unsubscribe();
     },err => {
      console.log(err);
      sub.unsubscribe();
     }

     );
     //this.generateChartData();
   } 

   toggle(value:string):void{
    var aElements:any =  document.querySelectorAll(".view-container");
    for(let i = 0; i <  aElements.length; i++){
      var t = aElements[i];
      t.style.display = value;
    }
   }
   rowClick(a):void{
     let targetEl = document.getElementById("view-container-"+a),
         style = targetEl.style.display;
     this.toggle("none");
     targetEl.style.display= style === "block" ? "none" : "block";
   }

   displayType(e){
     console.log(e.srcElement.value);
   }

   getData(){

   }



  
      width="100%";
      type = 'column3d';
      dataFormat = 'json';
      title = 'Angular4 FusionCharts Sample';

    }
