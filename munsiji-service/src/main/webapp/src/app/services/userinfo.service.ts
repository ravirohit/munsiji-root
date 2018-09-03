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
      debugger
      this.dataModel.arrData[i].header.bal = totalBal;
      title = this.dataModel.arrData[i].header.title + " ( "+totalBal +" ) ";
      chartData.chart = {"caption" :  title , "theme":"fint"};
      this.dataModel.arrData[i].chartData = chartData;
    }    
  }

  dataModel:any = {
                      "dropDown" : [
                        {"key":"room_exp", "label": "Room Expences"},
                        {"key":"offi_exp", "label": "Office Expences"},
                        {"key":"othe_exp", "label": "Other Expences"},
                      ],
                      "colTitle" : { "amount": "Amount", "date":"Date", "desc":"Details", "account": "Received"},
                      "th"        : ["account","amount", "date",  "desc"],
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
                                                                ] /*,
                                          "chartData":{ "chart": { "caption": "PPF Summary","theme": "fint"},
                                                        "data": [
                                                            {"label":"2-10-2012","value": "5000"},
                                                            {"label":"2-11-2012","value": "2000"},
                                                            {"label":"2-10-2012","value": "5000"}
                                                                                
                                                        ]} */                         
                                        }
                                  ]
                  };
}

/*
@Injectable()
export class CustomHttpInterceptorService implements HttpInterceptor {

 // private router:Router;
  constructor( @Inject(DOCUMENT) private document: any) {  
   }

    intercept(req: HttpRequest<any>, next: HttpHandler):
      Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {
      const nextReq = req.clone({
        headers: req.headers.set('Cache-Control', 'no-cache')
          .set('Pragma', 'no-cache')
          //.set('Expires', 'Sat, 01 Jan 2000 00:00:00 GMT')
          .set('If-Modified-Since', '0')
      });

      return next.handle(nextReq).do(
        (event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
                return event;
            }
        },
        (err) => {
          //location.href = "https://news.google.co.in/";
          //let msg = err.status +"|"+err.statusText;
          //if(err.status ==  401)
          //  this.router.navigate(['cm_authorization','msg']);
          console.log("http interceptor... Finding errror ....");
            // if (err instanceof HttpErrorResponse) {
            //    // if (err.status === 401) {
            //         this.document.location.href =
            //         `${this.document.location.protocol}//${this.document.location.hostname}:${this.document.location.port}/markit%20edmw/Default.aspx`;
            //     }
            // //}
        });
  }
}*/