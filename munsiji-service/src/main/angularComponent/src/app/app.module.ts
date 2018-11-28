import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {HttpClientModule,  HttpClientJsonpModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {RouterModule,Routes   } from "@angular/router";
import { FusionChartsModule } from 'angular4-fusioncharts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {CustomHttpInterceptorService} from './services/httpInterceptorService';

import { AngularFontAwesomeModule } from 'angular-font-awesome';



import * as FusionCharts from 'fusioncharts';
import * as Charts from 'fusioncharts/fusioncharts.charts';
import * as FintTheme from 'fusioncharts/themes/fusioncharts.theme.fint';


import {UserinfoService} from './services/userinfo.service';
import {DataService} from './services/data.service';
import {applicationRouter} from './RouterComponent';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { TourComponent } from './tour/tour.component';
import { GroupComponent } from './group/group.component';

import { HomeComponent} from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { AddLedgerInfoComponent } from './add-ledger-info/add-ledger-info.component';
import {AuthGuard} from './auth.guard';
import {formTemplateComponent} from './template/template.component';
import { CreateAccountComponent } from './template/create-account/create-account.component';

import {PromptMessageComponent} from './../app/template/promptMessage/promptMessage.component';
//import { ChildComponent } from '../app/home/child/child.component';

FusionChartsModule.fcRoot(FusionCharts, Charts, FintTheme);

@NgModule({
  declarations: [
    AppComponent,HeaderComponent, LoginComponent, TourComponent,  GroupComponent, HomeComponent, ProfileComponent,
    formTemplateComponent, AddLedgerInfoComponent, CreateAccountComponent, PromptMessageComponent
  ],
  imports: [   
    BrowserModule,FusionChartsModule,BrowserAnimationsModule,AngularFontAwesomeModule,
    HttpClientModule,FormsModule,
    RouterModule.forRoot(applicationRouter)
  ],
  providers: [UserinfoService, AuthGuard,DataService,  { provide: HTTP_INTERCEPTORS, useClass: CustomHttpInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }


