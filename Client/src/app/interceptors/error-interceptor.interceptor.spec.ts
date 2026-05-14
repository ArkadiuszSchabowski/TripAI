import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpHandler, HttpRequest } from '@angular/common/http';
import { ErrorInterceptor } from './error-interceptor.interceptor';
import { ToastrService } from 'ngx-toastr';
import { HttpClientTestingModule } from '@angular/common/http/testing';

class MockToastrService {
  error = jasmine.createSpy('error');
}

describe('ErrorInterceptor', () => {
  let http: HttpClient;
  let toastr: MockToastrService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
    providers: [
        ErrorInterceptor,
        HttpClient,
        { provide: ToastrService, useClass: MockToastrService },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true,
        },
      ],
    });

    http = TestBed.inject(HttpClient);
    toastr = TestBed.inject(ToastrService) as any;
  });

  it('should be created', () => {
    const interceptor = TestBed.inject(ErrorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});