import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TripRequest } from '../../../models/trip-request';
import { TripService } from '../../../_services/trip.service';
import { ToastrService } from 'ngx-toastr';
import { TripDailyPlan } from '../../../models/trip-daily-plan';
import { TripInformation } from '../../../models/trip-information';
import { TripAgentResponse } from '../../../models/trip-agent-response';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  tripAgentResponse: TripAgentResponse | null = null;
  tripInformation: TripInformation | null = null;
  days: TripDailyPlan[] = [];

  form = this.fb.group({
    fromCity: ['', [Validators.required, Validators.maxLength(50)]],
    toCity: ['', [Validators.required, Validators.maxLength(50)]],
    numberOfTravelers: [
      1,
      [Validators.required, Validators.min(1), Validators.max(10)],
    ],

    dateRange: this.fb.group({
      start: ['', Validators.required],
      end: ['', Validators.required],
    }),
  });

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private tripService: TripService,
  ) {}

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
        this.tripAgentResponse = response;
        console.log(this.tripAgentResponse);
      },
      error: (error) => {
        if (error.status === 400) {
          this.toastr.error(error.error);
          this.form.reset();
        }
        this.toastr.error(error.error.message)
        console.log(error);
      },
    });
  }

  resetTrip() {
    this.tripAgentResponse = null;
    this.form.reset();
  }
}
