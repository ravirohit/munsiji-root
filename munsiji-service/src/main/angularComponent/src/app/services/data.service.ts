import { Injectable, OnDestroy, OnInit } from '@angular/core';

import { HttpClient, HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
  

import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

@Injectable()
export class DataService implements OnDestroy,OnInit{
    data:any[] = []; 
    constructor(private http: HttpClient){}
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

}