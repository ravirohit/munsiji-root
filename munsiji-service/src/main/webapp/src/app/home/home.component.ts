import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import {UserinfoService} from '../services/userinfo.service';
import {DataService} from '../services/data.service';
import { ElementRef } from '@angular/core/src/linker/element_ref';

import { Observable } from 'rxjs/Rx';
import {UrlConfig} from './../../environments/url-config';
import {PromptMessageComponent} from '../template/promptMessage/promptMessage.component';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  getUrl = UrlConfig;
  chartDataModel:any;

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;
  
  //userName:any = {};
  homeModel = {isHeaderDisplay:false,userName: "Username", displayType: 2, rotateAngel:''}
  data:any = [];//{te:12000,td:20000,sa:8000};

  constructor(private userService:UserinfoService, private dataService:DataService) { }
  
   ngOnInit() {
     
     this.promptMessageComponent.showLoader();
     this.chartDataModel = this.chartDataModel1;
     this.data = this.userService.getDataModel();
     console.log(this.data);
     this.homeModel.isHeaderDisplay = true;
     
     var sub = this.dataService.httpGetCall(this.getUrl.GET_ALL_EXPENCE).subscribe(res =>{

       console.log(res.data);
       this.data.grdiData = res.data;
       this.chartDataModel = this.generateChartData(res.data);
       sub.unsubscribe();
       this.promptMessageComponent.hideLoader();
        },err => {
          this.promptMessageComponent.hideLoader();
          console.log(err);
          sub.unsubscribe();
        }

     );
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


   chartDataModel1 = {"chartData":{ "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
                      "data": [
                          {"label":"MF","value": "50000", "routerLink": "?acc=MF"},
                          {"label":"LIC","value": "20000", "link": "/?acc=LIC"},
                          {"label":"PPF","value": "40000", "link": "/?acc=PPF"}]                          
   }}

   generateChartData(d:Array<any>){
    let arrData = [];
    d.forEach(element => {
      arrData.push({
        "label" : element.accName,
        "value" : element.value
      });
    });
    console.log("CHART DATA : ",arrData);
    return arrData;
   }

  //  [
  //   {"accName":"MF","amnt":50000, "date":"2-10-2012",  "desc":"PPF deposit for me"},
  //   {"accName":"LIC","amnt":20000, "date":"2-11-2012",  "desc":"PPF deposit for me"},
  //   {"accName":"PPF","amnt":40000, "date":"2-12-2012",  "desc":"PPF deposit for me"}
  //  ] 
  
      width="100%";
      type = 'column3d';
      dataFormat = 'json';
      title = 'Angular4 FusionCharts Sample';

    }
