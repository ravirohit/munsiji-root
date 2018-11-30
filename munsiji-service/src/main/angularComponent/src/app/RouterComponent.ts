import {Routes} from "@angular/router";

import {AuthGuard} from './auth.guard';
import {HomeComponent} from './component/home/home.component';

import {ProfileComponent} from './component/profile/profile.component';
import {GroupComponent} from './group/group.component';
import {AddLedgerInfoComponent} from './component/add-ledger-info/add-ledger-info.component';
import {CreateAccountComponent} from './component/create-account/create-account.component';



export const applicationRouter : Routes = [
    {path: '',canActivate:[AuthGuard] ,component: HomeComponent },
    {path: 'group',canActivate:[AuthGuard] , component: GroupComponent},
    {path: 'profile',canActivate:[AuthGuard] , component: ProfileComponent},
    {path: 'add',canActivate:[AuthGuard] , component: AddLedgerInfoComponent},
    {path: 'c_account',canActivate:[AuthGuard] , component: CreateAccountComponent},
   // { path: '',   redirectTo: '/munsiji-service', pathMatch: 'full' },
   {path: 'detail/:acctype/:accname', component: HomeComponent}
    //{path: '**', redirectTo: 'munsiji-service'}
   ];