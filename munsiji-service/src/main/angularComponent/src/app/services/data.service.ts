import { Injectable, OnDestroy, OnInit, NgZone } from '@angular/core';

import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
  

import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import { UrlConfig } from '../../environments/url-config';

@Injectable()
export class DataService implements OnDestroy,OnInit{
    data:any[] = []; 

    SSE:any;

    
    constructor(private http: HttpClient, private zone:NgZone){
      let  EventSource=window['EventSource'];UrlConfig
      this.SSE = new EventSource(UrlConfig.SSE_CALL);
    }

    ngOnInit(){}

    ngOnDestroy(){ this.data = [];}
    setData(data:any){this.data = data;}
    getData(){return this.data;}

    
    httpPostCall(url:string,data):Observable<any> {
        let head    = new HttpHeaders( {  "Content-Type": 'application/json'});
        return this.http.post(url,data,{headers:head}).map(res => res);
    }


    httpGetCall(url):Observable<any>{
      return this.http.get(url).map(res=>res); 
    }


// var EventSource=window['EventSource'];
// this.sse=new EventSource('http://localhost:3002/api/data');

getMessages():Observable<any> {
    return new Observable<any>(observer=> {   
    this.SSE.onmessagec= evt => {
      this.zone.run(() =>observer.next(evt.data));
    };
    return () => this.SSE.close();
  });
   
  }

    triggerSSECall(){
      let url = UrlConfig.SSE_CALL + JSON.parse(localStorage.getItem("ui")).k;

      let sub = this.httpGetCall(url).subscribe( res =>{
        console.log("SSSEEE----SUCCESS-->", res);
      }, err =>{
        console.log("SSSEEE---ERROR--->", err);
        sub.unsubscribe();
      });
      window.setInterval( ()=>{
        this.keepSeesionAlive();
      },28000);
    }

    keepSeesionAlive(){
      let url = UrlConfig.KEEP_ALIVE + JSON.parse(localStorage.getItem("ui")).k;
      let sub = this.httpGetCall(url).subscribe( res =>{
        sub.unsubscribe();
        console.log("KEEPALIVE----SUCCESS-->", res);
      }, err =>{
        console.log("KEEPALIVE---ERROR--->", err);
        sub.unsubscribe();
      });
    }

}