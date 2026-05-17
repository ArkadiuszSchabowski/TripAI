import { Injectable } from '@angular/core';
import { TripAgentResponse } from '../models/trip-agent-response';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TripStateService {
  constructor() {}

  private tripDataSubject = new BehaviorSubject<TripAgentResponse | null>(null);
  tripData$ = this.tripDataSubject.asObservable();

  setTripData(data: TripAgentResponse | null) {
    this.tripDataSubject.next(data);
  }
}
