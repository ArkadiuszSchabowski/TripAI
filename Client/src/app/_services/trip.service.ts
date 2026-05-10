import { Injectable } from '@angular/core';
import { TripRequest } from '../models/trip-request';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class TripService {
    apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

    showAgentTripPlan(dto: TripRequest){
      console.log(dto);
    return this.http.post(this.apiUrl + 'trip/with-agent', dto)
  }
}
