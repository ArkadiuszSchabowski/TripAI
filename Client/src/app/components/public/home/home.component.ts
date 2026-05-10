import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TripRequest } from '../../../models/trip-request';
import { TripService } from '../../../_services/trip.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  
constructor(private fb: FormBuilder, private toastr: ToastrService, private tripService: TripService){

}

form = this.fb.group({
  fromCity: ['', [Validators.required, Validators.maxLength(50)]],
  toCity: ['', [Validators.required, Validators.maxLength(50)]],

  dateRange: this.fb.group({
    start: ['', Validators.required],
    end: ['', Validators.required]
  }),

  numberOfTravelers: [1, [Validators.required, Validators.min(1), Validators.max(10)]]
});

  submitTrip() {
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
        console.log(response);
        this.toastr.success('Registered successfully.');
      },
      error: (error) => {
        if (error.status === 400) {
          this.toastr.error(error.error);
          this.form.reset();
        }
        console.log(error);
      },
    });
}
}