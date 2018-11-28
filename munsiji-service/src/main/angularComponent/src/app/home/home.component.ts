
import { Component, OnInit, ViewChild } from '@angular/core';

import {UserinfoService} from '../services/userinfo.service';
import {DataService} from '../services/data.service';
import {  ActivatedRoute } from '@angular/router';
import {UrlConfig} from './../../environments/url-config';
import {PromptMessageComponent} from '../template/promptMessage/promptMessage.component';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  ifDataAvailable:boolean;
  chartDataModel:any = {
    chartData:{ 
      "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
      data:[]
  }};

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;
  
  //userName:any = {};
  homeModel = { displayType: 2}
  data:any = [];//{te:12000,td:20000,sa:8000};

  constructor(private route: ActivatedRoute,private userService:UserinfoService, private dataService:DataService) { }
  
   ngOnInit() {
     
    let flag:boolean = true, url,accName = this.route.snapshot.paramMap.get('accname'),
        accType = this.route.snapshot.paramMap.get('acctype');

    console.log(accName, " ---> ", accType);

     this.promptMessageComponent.showLoader();
     this.data = this.userService.getDataModel();   
     if(accName && accType && accName.length > 0 && accType.length > 0){
        url  = UrlConfig.GET_ALL_EXPENCE+accType+"&accName="+accName; 
        flag = false;
     }else{
        url  = UrlConfig.GET_ALL_EXPENCE+"personalexp";
        
     }   
     console.log("URL - > " , url);
     this.setHomeData(url, flag);
  } 
  

  setHomeData(url, isLinkAvailable){

    var sub = this.dataService.httpGetCall(url).subscribe(res =>{

      this.ifDataAvailable = false;
     if(res.data.expenseWithAccTypeList && res.data.expenseWithAccTypeList.length > 0){
          this.data.grdiData = res.data.expenseWithAccTypeList[0].accExpList;
          this.chartDataModel.chartData.data = this.generateChartData(this.data.grdiData, isLinkAvailable);
          sub.unsubscribe();
          this.ifDataAvailable = true;
        }
        this.promptMessageComponent.hideLoader();
      },err => {
        this.ifDataAvailable = true;
        this.chartDataModel = isLinkAvailable ? this.chartDataModel1 : this.chartDataModel2;
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

   generateChartData(d:Array<any>, isLinkAvailable:boolean){

    let arrData = [];
    d.forEach(element => {
      let link = isLinkAvailable ?  "detail/personalexp/"+element.accName : '';
      arrData.push({
        "label" : element.accName,
        "value" : element.amnt,
        link
      });
    });
    console.log("CHART DATA ****** : ",arrData);
    return arrData;
   }



   chartDataModel1 = {"chartData":{ "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
                      "data": [
                          {"label":"MF","value": "50000", "link": "detail/acc/MF"},
                          {"label":"LIC","value": "20000", "link":  "detail/acc2/MF2"},
                          {"label":"PPF","value": "40000", "link":  "detail/acc4/MF4"}]                          
   }}

   chartDataModel2 = {"chartData":{ "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
   "data": [
       {"label":"MF","value": "50000", "link" :  ""},
       {"label":"LIC","value": "20000", "link":  ""},
       {"label":"PPF","value": "40000", "link":  ""}]                          
}}
  
      width="100%";
      type = 'column3d';
      dataFormat = 'json';
      title = 'Angular4 FusionCharts Sample';

    }
