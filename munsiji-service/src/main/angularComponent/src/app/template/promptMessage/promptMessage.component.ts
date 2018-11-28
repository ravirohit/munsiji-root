import { Component, OnInit } from '@angular/core';
import {UserinfoService} from './../../services/userinfo.service'

@Component({
  selector: 'app-promptMessage',
  templateUrl: './promptMessage.component.html',
  styleUrls: ['./promptMessage.component.css']
})
export class PromptMessageComponent implements OnInit {
 
  dataModel = {
    isMsgToast:false,
    isLoader:false
  }

  constructor( private userService:UserinfoService) { }

  ngOnInit() {
  }

  showLoader(){
    this.dataModel.isLoader = true;
  } 

  hideLoader(){
    this.dataModel.isLoader = false;
  } 
  
}
