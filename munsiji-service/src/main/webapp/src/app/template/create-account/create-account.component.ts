import { Component, OnInit } from '@angular/core';
import {UserinfoService} from './../../services/userinfo.service'

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent implements OnInit {
  name = "dfdf";
  acc:any = {title:"",type:"",desc:"",date:"" }

  model = {isMessage: false}
  constructor( private userService:UserinfoService) { }

  ngOnInit() {
  }

  createAccount():void{
    console.log(this.acc.name," > ",this.acc.type," > ",this.acc.date," > ",this.acc.bal," > ",this.acc.desc);
    this.userService.setDataModelForAcctType(this.acc);
    this.model.isMessage = true;
    
  }
}
