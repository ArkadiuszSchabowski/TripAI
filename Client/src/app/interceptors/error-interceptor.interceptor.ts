import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private toastr: ToastrService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler,
  ): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (!error) {
          return throwError(() => error);
        }

        switch (error.status) {
          case 0:
            console.log(error);
            this.toastr.error(
              'Cannot connect to the server. Please check your internet connection or try again later.',
            );
            break;

          case 400:
            if (error.error.message) {
              this.toastr.error(error.error.message);
            }
            break;

          default:
            if (error.error.message) {
              this.toastr.error(error.error.message);
            } else {
              this.toastr.error('Unexpected server error. Try again later.');
            }
            break;
        }

        return throwError(() => error);
      }),
    );
  }
}
