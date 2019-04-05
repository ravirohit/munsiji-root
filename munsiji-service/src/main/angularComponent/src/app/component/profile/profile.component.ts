import { Component, OnInit,ViewChild } from '@angular/core';
import { DataService } from '../../services/data.service';
import { UrlConfig } from './../../../environments/url-config';
import { PromptMessageComponent } from '../../template/promptMessage/promptMessage.component';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  
  @ViewChild(PromptMessageComponent) promptMessageComponent:PromptMessageComponent;
  
  disManageAccount: boolean = false;
  resetPwd:boolean = false;

  resetPwdModel:ResetPwdModel;

  
  profileModel:ResultDataModel<AccountTypes<AccountInfo>>;

  constructor(
    private dataService:DataService, 
    ) {
      this.resetPwdModel = new ResetPwdModel();
     };

  ngOnInit() {
    this.promptMessageComponent.showLoader();
    let sub = this.dataService.httpGetCall(UrlConfig.GET_PROFILE_INFO).subscribe((res)=>{
      sub.unsubscribe();
      console.log("Success:",res);
      this.profileModel = <ResultDataModel<AccountTypes<AccountInfo>>>res["data"];
      this.generateChartData(this.profileModel.accountInfo.personalexp);
      this.promptMessageComponent.hideLoader();
      
    },(err)=>{
      sub.unsubscribe();
      this.promptMessageComponent.hideLoader();
      console.log("ERROR: ",err);                            
      this.promptMessageComponent.showToastMessage(err["msg"],"red",2000);

      this.profileModel ={
        "userName": 'RAJ',
        "emailId": "fdlkj aldsjfljsdalkfjlksadjlf",
        "mobNo": "121312312312",
        "accountInfo": {
            "personalexp": [
                {
                    "accName": "acc1",
                    "amnt": 10000,
                    "date": "26-12-2018",
                    "desc": "acc1 desc",
                    "status": true
                },
                {
                  "status": true,
                  "accName": "acc3",
                  "amnt": 2000,
                  "date": "26-12-2015",
                  "desc": "acc3 desc"
              },
              {
                "status": true,
                "accName": "acc4",
                "amnt": 50000,
                "date": "26-12-2013",
                "desc": "acc4 desc"
            },
                {
                    "status": true,
                    "accName": "acc2",
                    "amnt": 20000,
                    "date": "27-12-2016",
                    "desc": "acc2 desc"
                }
            ]
        }
      }  
      this.generateChartData(this.profileModel.accountInfo.personalexp);   
    });
  }

  updateAccount(){

    let sub, payloadData = {}; 
    this.profileModel.accountInfo.personalexp.forEach((item,i) =>{
      payloadData[item["accName"]] = item["status"]
      });

      sub = this.dataService.httpPostCall(UrlConfig.UPDATE_ACC_SCOPE,payloadData).subscribe(res=>{
        sub.unsubscribe();
        console.log("Success -> ", res);
        this.promptMessageComponent.showToastMessage(res.msg,"green",2000);
      },err=>{
        console.log("ERROR -> ", err);
        sub.unsubscribe();
        this.promptMessageComponent.showToastMessage("Error: Can not Update profile. Please try after some time. ","red",2000);

      });
    //UPDATE_ACC_SCOPE
  }

  generateChartData(personalexp){
    let result = personalexp.map((item)=>{
      if(item.status)
              return {
                "label"  : item.accName,
                "value"  : item.amnt,
                "tooltip": item.desc,
                "date"   : item.date
              }
        else
              return {}
    });
    this.myDataSource.data = result;
  }

  switchChange(e){
    this.generateChartData(this.profileModel.accountInfo.personalexp);
  }

  passwordReset(){

    this.promptMessageComponent.showLoader();
    let sub = this.dataService.httpPostCall(UrlConfig.RESET_PASSWORD, this.resetPwdModel).subscribe((res)=>{
      
      this.clearPWD();
      sub.unsubscribe();
      this.resetPwd = false;
      console.log("Success:",res);
      this.promptMessageComponent.hideLoader();      
      this.promptMessageComponent.showToastMessage(res.msg,"green",2000);
      
    },(err)=>{
      
      this.clearPWD();
      sub.unsubscribe();
      this.resetPwd = false;
      this.promptMessageComponent.hideLoader();
      console.log("ERROR: ",err);                            
      this.promptMessageComponent.showToastMessage("Error: Can not create account now. Please try after some time. ","red",2000);
      
    });
  }

  cancelPWD(){
    this.resetPwd = !this.resetPwd
    this.clearPWD();
  }

  myDataSource = {
    "chart": {
      "caption": "Personal Expences Dashboard:",
      //"subcaption": "For a net-worth of $1M",
      

      "showtooltip": "1",
                              
      "dataFormat": "json",
      "showvalues": "1",
      "showpercentintooltip": "0",
      "numberprefix": "Rs. ",
      "enablemultislicing": "1",
      "theme": "fusion"
    },
    "data": []
  };

  clearPWD(){
    this.resetPwdModel =  new ResetPwdModel();
  }
}

export class ResetPwdModel{

  currentPwd:string;
  newPwd1:string ;
  newPwd2:string ; 
}

export interface AccountInfo{

  accName:string;
  amnt:number ;
  date:string ;
  desc:string ; 
  status: boolean;
}

export interface AccountTypes<PE>{
  personalexp:PE[];
  //Tourexpence:TE;
}


export interface ResultDataModel<ExpenceType>{

  userName:String;
  emailId:string;
  mobNo:string;
  accountInfo:ExpenceType;

}

// "data": {
//   "userName": null,
//   "emailId": "ravi",
//   "mobNo": "ravi",
//   "accountInfo": {
//       "personalexp": [
//           {
//               "accName": "acc1",
//               "amnt": null,
//               "date": "26-12-2018",
//               "desc": "acc1 desc"
//           },
//           {
//               "accName": "acc2",
//               "amnt": null,
//               "date": "27-12-2018",
//               "desc": "acc2 desc"
//           }
//       ]
//   }
// }