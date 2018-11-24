import { Injectable, OnDestroy, OnInit, Inject } from '@angular/core';
import { HttpClient, HttpClientModule, HttpClientJsonpModule, HttpInterceptor, HttpRequest, HttpHeaders, HttpHandler,
  HttpHeaderResponse, HttpSentEvent, HttpProgressEvent,  HttpResponse, HttpUserEvent, HTTP_INTERCEPTORS, HttpEvent , HttpErrorResponse } from '@angular/common/http';
import { DOCUMENT } from "@angular/common";
import { Observable } from 'rxjs/Rx';


@Injectable()
export class UserinfoService implements OnDestroy, OnInit{
  public userData:any  = {'isLogedin':false};
  constructor() { 
    
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
  }

  setDataModelForAcctType(data){
    let  obj = {"header" : data, "content" : []};
    this.dataModel.arrData.push(obj);
    this.dataModel.dropDown.push({"key": data.title.replace(/ /g, "_"), "label" : data.title});
  }


  dataModel:any = {
    
                      "colTitle" : { "amnt": "Amount", "date":"Start Date", "desc":"Details", "accName": "Received"},
                      "th"        : ["accName","amnt", "date",  "desc"],

                      grdiData : {     
                        "header":{"title":"Other Expences", "bal":6000, key:"user_111"},
                        "content":  [
                                      {"accName":"MF","amnt":50000, "date":"2-10-2012",  "desc":"PPF deposit for me"},
                                      {"accName":"LIC","amnt":20000, "date":"2-11-2012",  "desc":"PPF deposit for me"},
                                      {"accName":"PPF","amnt":40000, "date":"2-12-2012",  "desc":"PPF deposit for me"}
                                     ]                        
                          }
                        }
}

