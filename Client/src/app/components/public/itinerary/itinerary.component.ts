import { Component } from '@angular/core';
import { TripStateService } from '../../../_services/trip-state.service';
import { TripAgentResponse } from '../../../models/trip-agent-response';
import { Router } from '@angular/router';

@Component({
  selector: 'app-itinerary',
  templateUrl: './itinerary.component.html',
  styleUrls: ['./itinerary.component.scss'],
})
export class ItineraryComponent {
  tripAgentResponse: TripAgentResponse | null = null;

  constructor(
    private router: Router,
    private tripStateService: TripStateService,
  ) {}

  ngOnInit() {
    this.tripStateService.tripData$.subscribe((data) => {
      if (data) {
        this.tripAgentResponse = data;
        console.log(this.tripAgentResponse);
      }
    });
  }
  backToHome() {
    this.tripStateService.setTripData(null);

    this.router.navigateByUrl('/').then(() => {
      window.scrollTo({
        top: 0,
        behavior: 'smooth',
      });
    });
  }
}
