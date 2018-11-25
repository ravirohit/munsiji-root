import {Routes} from "@angular/router";

import {AuthGuard} from './auth.guard';
import {HomeComponent} from './home/home.component';
import {TourComponent} from './tour/tour.component';
import {ProfileComponent} from './profile/profile.component';
import {GroupComponent} from './group/group.component';
import {AddLedgerInfoComponent} from './add-ledger-info/add-ledger-info.component';
import {CreateAccountComponent} from './template/create-account/create-account.component';

export const applicationRouter : Routes = [
    {path: 'munsiji-service',canActivate:[AuthGuard] ,component: HomeComponent },
   // {path: 'munsiji-service/login', component: LoginComponent},
    {path: 'munsiji-service/tour',canActivate:[AuthGuard] , component: TourComponent,data: { title: '' }},
    {path: 'munsiji-service/group',canActivate:[AuthGuard] , component: GroupComponent},
    {path: 'munsiji-service/profile',canActivate:[AuthGuard] , component: ProfileComponent},
    {path: 'munsiji-service/add',canActivate:[AuthGuard] , component: AddLedgerInfoComponent},
    {path: 'munsiji-service/c_account',canActivate:[AuthGuard] , component: CreateAccountComponent},
    //{path: '', redirectTo: 'munsiji-service/', pathMatch: 'full'} ,
   
    {path: '**', redirectTo: 'munsiji-service/'}
   ];