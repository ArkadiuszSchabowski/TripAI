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
  tripAgentResponse: TripAgentResponse | null = {
    tripIntro:
      'Discover the industrial charm and modern energy of Katowice on this delightful 6-day cultural escape from Berlin.',
    closingFeeling:
      'We hope your journey to Katowice leaves you with wonderful memories of vibrant city life, rich history, and the unique architecture of Upper Silesia.',
    information: {
      originCity: 'Berlin',
      destinationCity: 'Katowice',
      departureDateFrom: '2026-05-19',
      departureDateTo: '2026-05-24',
      numberOfPeople: '2',
      currency: 'EUR',
    },
    costs: {
      estimatedTicketCost: '122.78',
      estimatedAccommodationCost: '500',
      estimatedFoodCost: '400',
      estimatedTotalTripCost: '1022.78',
    },
    details: {
      recommendedAirline: 'Duffel Airways',
      outboundDeparture: '19.05.2026 13:56',
      outboundArrival: '19.05.2026 15:06',
      returnDeparture: '24.05.2026 17:48',
      returnArrival: '24.05.2026 18:58',
    },
    dailyPlan: [
      {
        dayNumber: 1,
        morningPlan: 'Flight departure from Berlin at 13:56',
        morningWhy:
          'A convenient mid-day departure allowing time to reach the airport comfortably.',
        afternoonPlan: 'Arrival in Katowice and transfer to hotel',
        afternoonWhy: 'Checking in and freshening up after your quick flight.',
        tonightPlan: 'Casual dinner in the city center',
        tonightWhy:
          'Ease into the city atmosphere with some local Polish cuisine.',
      },
      {
        dayNumber: 2,
        morningPlan: 'Explore the Cultural Zone',
        morningWhy:
          'Visit the iconic Silesian Museum located on the grounds of a former coal mine.',
        afternoonPlan: 'Visit the International Congress Centre',
        afternoonWhy: 'Admire the modern architecture and green rooftop paths.',
        tonightPlan: 'Evening walk at Spodek',
        tonightWhy: 'See the famous UFO-shaped arena lit up at night.',
      },
      {
        dayNumber: 3,
        morningPlan: 'Nikiszowiec Historic District tour',
        morningWhy:
          "A unique historical worker's estate with charming red-brick architecture.",
        afternoonPlan: 'Cafe hopping in the district',
        afternoonWhy:
          'Experience the authentic local lifestyle in this heritage area.',
        tonightPlan: 'Dinner in a local Silesian restaurant',
        tonightWhy: 'Taste traditional regional dishes like Rouladen.',
      },
      {
        dayNumber: 4,
        morningPlan: 'Park Śląski visit',
        morningWhy:
          'One of the largest city parks in Europe, perfect for a relaxing morning stroll.',
        afternoonPlan: 'Silesian Planetarium',
        afternoonWhy:
          'Discover science and astronomy in a beautifully modernized facility.',
        tonightPlan: 'Casual relaxation',
        tonightWhy: 'Enjoy a quiet evening in the city center.',
      },
      {
        dayNumber: 5,
        morningPlan: 'Shopping and art galleries',
        morningWhy:
          'Explore the vibrant retail scene and local art galleries in the center.',
        afternoonPlan: 'Modern art gallery exhibition',
        afternoonWhy:
          'Immerse yourselves in the local contemporary creative scene.',
        tonightPlan: 'Farewell dinner',
        tonightWhy: 'Celebrate your last full evening with fine dining.',
      },
      {
        dayNumber: 6,
        morningPlan: 'Slow morning in the city center',
        morningWhy:
          'Enjoy a final coffee and souvenirs shopping before departure.',
        afternoonPlan: 'Transfer to Katowice Airport',
        afternoonWhy:
          'Ensuring arrival at the airport in time for the 17:48 flight.',
        tonightPlan: 'Return flight to Berlin',
        tonightWhy: 'Reflect on your trip while traveling home.',
      },
    ],
  };

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
