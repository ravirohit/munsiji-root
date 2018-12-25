import { Component, OnInit, ViewChild } from '@angular/core';
import {UserinfoService} from './../../services/userinfo.service';

import {  MzModalModule  } from 'ngx-materialize';

@Component({
  selector: 'app-promptMessage',
  templateUrl: './promptMessage.component.html',
  styleUrls: ['./promptMessage.component.css']
})
export class PromptMessageComponent implements OnInit {

  @ViewChild('PromptModal') PromptModal:MzModalModule;
 
  dataModel = {
    isMsgToast:false,
    isLoader:false,
    
  };

  model:any ={
    title:"",
    desc:"",
    noBtn:"",
    yesBtn:""
  };

  constructor( private userService:UserinfoService) { }

  ngOnInit() {
  }

  showLoader(){
    this.dataModel.isLoader = true;
  } 

  hideLoader(){
    this.dataModel.isLoader = false;
  } 
  
  setModelInfo(model:any){
    this.model = {...model};
    //this.PromptModal.openModal();
  }
}
