
import { Component, OnInit, ViewChild } from '@angular/core';

import {UserinfoService} from '../services/userinfo.service';
import {DataService} from '../services/data.service';

import {UrlConfig} from './../../environments/url-config';
import {PromptMessageComponent} from '../template/promptMessage/promptMessage.component';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  getUrl = UrlConfig;
  ifDataAvailable:boolean;
  chartDataModel:any = {
    chartData:{ "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
    data:[]
  }};

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;
  
  //userName:any = {};
  homeModel = {isHeaderDisplay:false,userName: "Username", displayType: 2, rotateAngel:''}
  data:any = [];//{te:12000,td:20000,sa:8000};

  constructor(private userService:UserinfoService, private dataService:DataService) { }
  
   ngOnInit() {
     
     this.promptMessageComponent.showLoader();
     this.data = this.userService.getDataModel();
     
     this.homeModel.isHeaderDisplay = true;
     
     var sub = this.dataService.httpGetCall(this.getUrl.GET_ALL_EXPENCE).subscribe(res =>{

        this.ifDataAvailable = false;
       if(res.data.expenseWithAccTypeList && res.data.expenseWithAccTypeList.length > 0){
            this.data.grdiData = res.data.expenseWithAccTypeList[0].accExpList;
            this.chartDataModel.data = this.generateChartData(this.data.grdiData);
            sub.unsubscribe();
            this.ifDataAvailable = true;
          }
          this.promptMessageComponent.hideLoader();
        },err => {
          this.ifDataAvailable = false;
          //this.chartDataModel = this.chartDataModel1;
          this.promptMessageComponent.hideLoader();
          console.log(err);
          sub.unsubscribe();
        }

     );
  } 
  

  gridRowClicked(data){
    console.log("DATA ", data);
  }

   displayType(e){
     console.log(e.srcElement.value);
   }

   generateChartData(d:Array<any>){
    let arrData = [];
    d.forEach(element => {
      arrData.push({
        "label" : element.accName,
        "value" : element.amnt
      });
    });
    console.log("CHART DATA : ",arrData);
    return arrData;
   }



   chartDataModel1 = {"chartData":{ "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
                      "data": [
                          {"label":"MF","value": "50000", "routerLink": "?acc=MF"},
                          {"label":"LIC","value": "20000", "link": "/?acc=LIC"},
                          {"label":"PPF","value": "40000", "link": "/?acc=PPF"}]                          
   }}
  
      width="100%";
      type = 'column3d';
      dataFormat = 'json';
      title = 'Angular4 FusionCharts Sample';

    }
