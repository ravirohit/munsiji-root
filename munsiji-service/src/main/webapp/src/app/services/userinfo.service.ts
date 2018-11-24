import { Injectable, OnDestroy, OnInit, Inject } from '@angular/core';
import { HttpClient, HttpClientModule, HttpClientJsonpModule, HttpInterceptor, HttpRequest, HttpHeaders, HttpHandler,
  HttpHeaderResponse, HttpSentEvent, HttpProgressEvent,  HttpResponse, HttpUserEvent, HTTP_INTERCEPTORS, HttpEvent , HttpErrorResponse } from '@angular/common/http';
import { DOCUMENT } from "@angular/common";
import { Observable } from 'rxjs/Rx';


@Injectable()
export class UserinfoService implements OnDestroy, OnInit{
  public userData:any  = {'isLogedin':false};
  constructor() { 
    this.generateChartData();
  }

  getUserData(){
    let tmpObj = JSON.parse(localStorage.getItem("ui"));
    if(tmpObj && tmpObj.isLogedin){      
      this.userData.isLogedin = tmpObj.isLogedin;
    };
    return this.userData;
  }
  setUSerData(userInfo){
    this.userData.isLogedin = userInfo.isLogedin;
    localStorage.setItem("ui",JSON.stringify(userInfo));
  }

  ngOnInit(){
    let tmpObj = JSON.parse(localStorage.getItem("ui"));
    if(tmpObj && tmpObj.isLogedin){      
      this.userData.isLogedin = tmpObj.isLogedin;
    };
   
  }

  ngOnDestroy(){
    console.log("Service is Destroyed...");
    this.dataModel = [];
  }

  getDataModel(){
    return this.dataModel;
  }
  setDataModelForAccount(obj, arrIndex=1){

    this.dataModel.arrData[arrIndex].content.push(JSON.parse(JSON.stringify(obj)));
    this.generateChartData();
  }

  setDataModelForAcctType(data){
    let  obj = {"header" : data, "content" : []};
    this.dataModel.arrData.push(obj);
    this.dataModel.dropDown.push({"key": data.title.replace(/ /g, "_"), "label" : data.title});
  }

  generateChartData(){

    for(let i=0, l =  this.dataModel.arrData.length ; i < l ; i++){
      
      var chartData = {chart: {}, data:[]}, title = "", totalBal:number=0,
          arrData = this.dataModel.arrData[i].content;

      for(let j=0, l2 =   arrData.length ; j < l2 ; j++){        
        chartData.data.push({"label": arrData[j].date, "value": arrData[j].amount});     
        totalBal +=    Number.parseFloat(arrData[j].amount);
      }
      
      this.dataModel.arrData[i].header.bal = totalBal;
      title = this.dataModel.arrData[i].header.title + " ( "+totalBal +" ) ";
      chartData.chart = {"caption" :  title , "theme":"fint"};
      this.dataModel.arrData[i].chartData = chartData;
    }    
  }

  dataModel:any = {
                      "dropDown" : [
                        "Room Expences",
                        "Office Expences",
                        "Other Expences",
                      ],
                      "colTitle" : { "amount": "Amount", "date":"Start Date", "desc":"Details", "account": "Received"},
                      "th"        : ["account","amount", "date",  "desc"],


                      grdiData : {     
                        "header":{"title":"Other Expences", "bal":6000, key:"user_111"},
                        "content":  [
                                      {"account":"MF","amount":50000, "date":"2-10-2012",  "desc":"PPF deposit for me"},
                                      {"account":"LIC","amount":20000, "date":"2-11-2012",  "desc":"PPF deposit for me"},
                                      {"account":"PPF","amount":40000, "date":"2-12-2012",  "desc":"PPF deposit for me"}
                                     ]                        
                          },

                      arrData:[
                                   
                                      {
                                        "header":{"title":"Room Expences", "bal":24000,key:"user_333"},
                                        "content": []
                                    },{
                                        "header":{"title":"Office Expences ", "bal":15000,key:"user_222"},
                                        "content": [{"account":"HDFC","amount":4000, "date":"2-10-2012",  "desc":"PPF deposit for me"}, {"account":"HDFC","amount":5000, "date":"2-11-2012",  "desc":"PPF deposit for me"}]
                                    },
                                   
                                    {     
                                      "header":{"title":"Other Expences", "bal":6000, key:"user_111"},
                                      "content":  [
                                                    {"account":"HDFC","amount":1000, "date":"2-10-2012",  "desc":"PPF deposit for me"},
                                                    {"account":"IDFC","amount":2000, "date":"2-11-2012",  "desc":"PPF deposit for me"},
                                                    {"account":"AXIS","amount":3000, "date":"2-12-2012",  "desc":"PPF deposit for me"}
                                                   ]                        
                                        }
                                  ]
                  };
}

