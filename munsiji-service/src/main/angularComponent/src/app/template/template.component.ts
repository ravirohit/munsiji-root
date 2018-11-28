import { Component, OnInit, OnDestroy } from '@angular/core';
import {UserinfoService} from '../services/userinfo.service';
import {DataService} from '../services/data.service';

@Component({
    selector : 'app-formTemplate',
    templateUrl:'./formTemplate.component.html',
    styleUrls: ['./formTemplate.component.css']
  })
  export class formTemplateComponent implements OnInit, OnDestroy{
    isSelectedSd:string = 'cm';
    toRenderData:any;
    constructor(private ds:DataService){}
    ngOnInit():void{
      console.log("Initialize");
      this.toRenderData = this.ds.getData(); 
    }
    ngOnDestroy():void{
      console.log("Destroy");
      this.toRenderData = {};
    }
    assetDropDown(e):void{
      this.isSelectedSd = e.target.value;
    }
    assetViewData(e):void{
      console.log("Clicked to display data")
    }
  }
  