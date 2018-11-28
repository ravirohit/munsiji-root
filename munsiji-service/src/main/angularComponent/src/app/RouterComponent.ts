import {Routes} from "@angular/router";

import {AuthGuard} from './auth.guard';
import {HomeComponent} from './home/home.component';
import {TourComponent} from './tour/tour.component';
import {ProfileComponent} from './profile/profile.component';
import {GroupComponent} from './group/group.component';
import {AddLedgerInfoComponent} from './add-ledger-info/add-ledger-info.component';
import {CreateAccountComponent} from './template/create-account/create-account.component';
//import {ChildComponent} from './home/child/child.component';

export const applicationRouter : Routes = [
    {path: '',canActivate:[AuthGuard] ,component: HomeComponent },
   // {path: 'munsiji-service/login', component: LoginComponent},
    {path: 'tour',canActivate:[AuthGuard] , component: TourComponent,data: { title: '' }},
    {path: 'group',canActivate:[AuthGuard] , component: GroupComponent},
    {path: 'profile',canActivate:[AuthGuard] , component: ProfileComponent},
    {path: 'add',canActivate:[AuthGuard] , component: AddLedgerInfoComponent},
    {path: 'c_account',canActivate:[AuthGuard] , component: CreateAccountComponent},
   // { path: '',   redirectTo: '/munsiji-service', pathMatch: 'full' },
   {path: 'detail/:acctype/:accname', component: HomeComponent}
    //{path: '**', redirectTo: 'munsiji-service'}
   ];