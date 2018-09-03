import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {UserinfoService} from './services/userinfo.service';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private userAuth : UserinfoService){ }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.userAuth.getUserData().isLogedin;
  }
}
