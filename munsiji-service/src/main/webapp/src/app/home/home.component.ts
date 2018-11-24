import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
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

  //@ViewChild(PromptMessageComponent) PromptMessageComponent:PromptMessageComponent;
  
  userName:any = {};
  homeModel = {isHeaderDisplay:false,userName: "Username", displayType: 2, rotateAngel:''}
  data:any = [];//{te:12000,td:20000,sa:8000};

  constructor(private userService:UserinfoService, private dataService:DataService) { }
  
   ngOnInit() {
     this.userName = this.userService.getUserData();
     this.data = this.userService.getDataModel();
     console.log(this.data);
     this.homeModel.isHeaderDisplay = true;
     console.log(this.getUrl.GET_ACCOUNT);
     var sub = this.dataService.httpGetCall(this.getUrl.GET_ACCOUNT).subscribe(data =>{
       console.log(data);
       sub.unsubscribe();
        },err => {
          console.log(err);
          sub.unsubscribe();
        }

     );
     //this.generateChartData();
  } 
  clickChart(e){
    console.log("Chart palete clicked" ,  e);
  }

  gridRowClicked(data){
    console.log("DATA ", data);
  }

   toggle(value:string, opacity, index=-1):void{
    let aElements:any =  document.querySelectorAll(".view-container");
    for(let i = 0; i <  aElements.length; i++){
      let el = aElements[i];

      if(index === -1 || i != index){
        
        if(opacity === 0){
          el.style.opacity = opacity;
          this.createTransitionEffect(el,"display", "none",500); 
          
        }else{
          
          el.style.display= value; 
          this.createTransitionEffect(el,"opacity", "1",500);
        }
      }
    }
   }
   
   rowClick(a):void{
     let targetEl = document.getElementById("view-container-"+a),
         style = targetEl.style.display;
     this.toggle("none", 0, a);
    
     if( style === "block" ){
       
      targetEl.style.opacity = "0";
      this.createTransitionEffect(targetEl, "display", "none",500);
      
     }else{ 
       
     targetEl.style.display= "block"; 
     this.createTransitionEffect(targetEl,"opacity", "1",500);
           
     }
   }

   createTransitionEffect(el,prop, displayPro, duration){
     setTimeout( ()=> {
      el.style[prop] = displayPro; 
     }, duration);
   }



   displayType(e){
     console.log(e.srcElement.value);
   }

   getData(){

   }


   chartDataModel = {"chartData":{ "chart": { "caption": "PPF Summary","theme": "fint"},
                      "data": [
                          {"label":"MF","value": "5000", "routerLink": "?acc=MF"},
                          {"label":"LIC","value": "2000", "link": "/?acc=LIC"},
                          {"label":"PPF","value": "5000", "link": "/?acc=PPF"}]                          
   }}


  
      width="100%";
      type = 'column3d';
      dataFormat = 'json';
      title = 'Angular4 FusionCharts Sample';

    }
