import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TripRequest } from '../../../models/trip-request';
import { TripService } from '../../../_services/trip.service';
import { TripDailyPlan } from '../../../models/trip-daily-plan';
import { TripInformation } from '../../../models/trip-information';
import { Router } from '@angular/router';
import { TripStateService } from '../../../_services/trip-state.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit{
  tripInformation: TripInformation | null = null;
  days: TripDailyPlan[] = [];

  form = this.fb.group({
    fromCity: ['', [Validators.required, Validators.maxLength(50)]],
    toCity: ['', [Validators.required, Validators.maxLength(50)]],
    numberOfTravelers: [
      0,
      [Validators.required, Validators.min(1), Validators.max(10)],
    ],

    dateRange: this.fb.group({
      start: ['', Validators.required],
      end: ['', Validators.required],
    }),
  });

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private tripService: TripService,
    private tripStateService: TripStateService
  ) {}
  ngOnInit(): void {
    console.log(this.tripStateService.tripData$)
  }

  loadTrip() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    let dto: TripRequest = {
      originCity: this.form.get('fromCity')!.value,
      destinationCity: this.form.get('toCity')!.value,
      fromDepartureDate: this.form.get('dateRange')!.get('start')!.value,
      toDepartureDate: this.form.get('dateRange')!.get('end')!.value,
      numberOfPeople: this.form.get('numberOfTravelers')!.value,
    };

    this.tripService.showAgentTripPlan(dto).subscribe({
      next: (response) => {
        this.tripStateService.setTripData(response);
        this.router.navigateByUrl('/itinerary');
      },
    });
  }
}
