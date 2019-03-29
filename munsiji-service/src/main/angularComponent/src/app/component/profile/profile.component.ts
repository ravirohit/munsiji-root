import { Component, OnInit,ViewChild } from '@angular/core';
import { MzCollectionModule } from 'ngx-materialize'
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
  resetPwd:boolean = false;

  resetPwdModel = {
                  currentPwd:'',
                  newPwd1:'',
                  newPwd2:''
                };
  profileModel = {
    "userName": null,
    "emailId": "ravi",
    "mobNo": "ravi",
    "accountInfo": {
        "personalexp": [
            {
                "accName": "acc1",
                "amnt": null,
                "date": "26-12-2018",
                "desc": "acc1 desc"
            },
            {
                "accName": "acc2",
                "amnt": null,
                "date": "27-12-2018",
                "desc": "acc2 desc"
            }
        ]
    }
  }

  constructor(
    private dataService:DataService, 
    ) { }

  ngOnInit() {
    this.promptMessageComponent.showLoader();
    let sub = this.dataService.httpGetCall(UrlConfig.GET_PROFILE_INFO).subscribe((res)=>{
      sub.unsubscribe();
      console.log("Success:",res);
      this.profileModel = res["data"];
      this.promptMessageComponent.hideLoader();
      
    },(err)=>{
      sub.unsubscribe();
      this.promptMessageComponent.hideLoader();
      console.log("ERROR: ",err);                            
      this.promptMessageComponent.showToastMessage(err["msg"],"red",3000);
      
    });
  }

  passwordReset(){

    this.promptMessageComponent.showLoader();
    let sub = this.dataService.httpPostCall(UrlConfig.RESET_PASSWORD, this.resetPwdModel).subscribe((res)=>{
      sub.unsubscribe();
      this.resetPwd = false;
      console.log("Success:",res);
      this.promptMessageComponent.hideLoader();      
      this.promptMessageComponent.showToastMessage(res.msg,"green",3000);
      
    },(err)=>{
      sub.unsubscribe();
      this.resetPwd = false;
      this.promptMessageComponent.hideLoader();
      console.log("ERROR: ",err);                            
      this.promptMessageComponent.showToastMessage("Error: Can not create account now. Please try after some time. ","red",3000);
      
    });
  }
  cancelPWD(){

  }

  myDataSource = {
    "chart": {
      "caption": "Recommended Portfolio Split",
      "subcaption": "For a net-worth of $1M",
      "showvalues": "1",
      "showpercentintooltip": "0",
      "numberprefix": "$",
      "enablemultislicing": "1",
      "theme": "fusion"
    },
    "data": [
      {
        "label": "Equity",
        "value": "300000"
      },
      {
        "label": "Debt",
        "value": "230000"
      },
      {
        "label": "Bullion",
        "value": "180000"
      },
      {
        "label": "Real-estate",
        "value": "270000"
      },
      {
        "label": "Insurance",
        "value": "20000"
      }
    ]
  };

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