// import { Inject } from '@angular/core';

// import {
//   HttpClient, HttpInterceptor, HttpRequest, HttpHeaders, HttpHandler,
//   HttpHeaderResponse, HttpSentEvent, HttpProgressEvent, HttpResponse, HttpUserEvent,HttpEvent, HttpErrorResponse
// } from '@angular/common/http';

// import 'rxjs/add/operator/do';
// import { Observable } from 'rxjs/Rx';
// import 'rxjs/add/operator/map';

// import { retry, catchError } from 'rxjs/operators';




// export class CustomHttpInterceptorService implements HttpInterceptor {

//   constructor( ) {
//   }
  
//   intercept(req: HttpRequest<any>, next: HttpHandler):
//     Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {
//     const nextReq = req.clone({
//       headers: req.headers.set('Cache-Control', 'no-cache')
//         .set('Pragma', 'no-cache')
//         //.set('Expires', 'Sat, 01 Jan 2000 00:00:00 GMT')
//         .set('If-Modified-Since', '0')
//     });

//     return next.handle(nextReq).pipe(
//       retry(2), // retry a failed request up to 3 times
      
//       catchError((error: HttpErrorResponse)=> {
//         if (error.error instanceof ErrorEvent) {
//           console.error('An error occurred:', error.error.message);
//         } else {
//           console.error(
//             `Backend returned code ${error.status}, ` +
//             `body was: ${error.error}`);
//         }       
//         return throwError(
//           'Something bad happened; please try again later.');
//       }) // then handle the error
      
//     ).do(
//       (event: HttpEvent<any>) => {      
//         if (event instanceof HttpResponse) {
//           this.sharedService.removeBusyIndicatior();
//           return event;
//         }
//       },
//       (err) => {
//         console.log("http errror ....", err);
//         this.sharedService.removeBusyIndicatior();
//       });
//   }

 
// }
