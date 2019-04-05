
import { Component, OnInit, ViewChild, SystemJsNgModuleLoader } from '@angular/core';
import {Router,  ActivatedRoute } from '@angular/router';

import {UserinfoService} from '../../services/userinfo.service';
import {DataService} from '../../services/data.service';
import {UrlConfig} from './../../../environments/url-config';
import {PromptMessageComponent} from '../../template/promptMessage/promptMessage.component';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  url = UrlConfig;
  ifDataAvailable:boolean;
  isGridClickable:boolean = true;
  chartDataModel:any = {
    chartData:{ 
      "chart": { "caption": "Expences Summary for All accounts","theme": "fint"},
      data:[]
  }};

  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;
  
  //userName:any = {};
  homeModel = { displayType: 2,selAccName:'', selAccType:'' }
  data:any = [];//{te:12000,td:20000,sa:8000};
  accountTypeData:any[] = [];
  selectedAccountName:string;

  constructor(private route: ActivatedRoute,private userService:UserinfoService, private dataService:DataService, private router: Router) { }
  
   ngOnInit() {
     
    this.promptMessageComponent.showLoader();
    let url,
        accName = this.route.snapshot.paramMap.get('accname'),
        accType = this.route.snapshot.paramMap.get('acctype');

    this.homeModel.selAccName = accName;
    this.homeModel.selAccType = accType;
    this.data = this.userService.getDataModel();   
    if(accName && accType && accName.length > 0 && accType.length > 0){
      // url  = UrlConfig.GET_ALL_EXPENCE+accType+"&accName=" +accName; 
      // this.isGridClickable = false;
      // this.setHomeExpOfAcc(url, false);
      this.updateView();
    }else{
      url  = UrlConfig.GET_ALL_EXPENCE+accType;
      this.setHomeData(url, true);
    }        
  } 

  setAllAcountName(data){

    let resultData = data.map (item =>{
      return {field: item['accName'], key: item['accName']}
    })
    
    this.dataService.setData(resultData);
  }
  

  updateView(){
    let url  = UrlConfig.GET_ALL_EXPENCE+this.homeModel.selAccType+"&accName=" +this.homeModel.selAccName; 
    this.isGridClickable = false;
    this.setHomeExpOfAcc(url, false);
  }

  setHomeData(url, isLinkAvailable){

    var sub = this.dataService.httpGetCall(url).subscribe(res =>{
     console.log(res);
      this.ifDataAvailable = false;
     if(res.data.expenseWithAccTypeList && res.data.expenseWithAccTypeList.length > 0){
          this.data.grdiData = res.data.expenseWithAccTypeList[0].accExpList;
          this.chartDataModel.chartData.data = this.generateChartData(this.data.grdiData, isLinkAvailable);
          sub.unsubscribe();
          this.ifDataAvailable = true;
          this.setAllAcountName(this.data.grdiData);
        }
        this.promptMessageComponent.hideLoader();
      },err => {
        
        this.setAllAcountName(this.data.grdiData);
        this.ifDataAvailable = true;
        this.chartDataModel = isLinkAvailable ? this.chartDataModel1 : this.chartDataModel2;
        this.promptMessageComponent.hideLoader();
        console.log(err);
        sub.unsubscribe();
      }

   );

  }


  setHomeExpOfAcc(url, isLinkAvailable){

    var sub = this.dataService.httpGetCall(url).subscribe(res =>{
     console.log(res);
      this.ifDataAvailable = false;
     if(res.data.content && res.data.content.length > 0){
          this.data.grdiData = res.data.content[0].data;
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
        this.accountTypeData = this.dataService.getData();
      }

   );

  }




  gridRowClicked(data){
    let url  ="detail/personalexp/"+data.accName;
    this.router.navigate([url]); 
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
                          {"label":"MF","value": "50000", "link": "detail/personalexp/MF"},
                          {"label":"LIC","value": "20000", "link":  "detail/personalexp/MF2"},
                          {"label":"PPF","value": "40000", "link":  "detail/personalexp/MF4"}]                          
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
