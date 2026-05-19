import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TripStateService } from '../../_services/trip-state.service';
import { TripAgentResponse } from '../../models/trip-agent-response';
import { LoaderService } from '../../_services/loader.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  tripAgentResponse: TripAgentResponse | null = null;
  constructor(
    public loader: LoaderService,
    private router: Router,
    private tripStateService: TripStateService
  ) {}
  ngOnInit(): void {
    this.tripStateService.tripData$.subscribe({
      next: (response) => {
        ((this.tripAgentResponse = response), console.log(response));
      },
    });
  }

  get isHomePage(): boolean {
    return this.router.url === '/';
  }

  get isItineraryPage(): boolean {
    return this.router.url === '/itinerary';
  }

  scrollToSection(sectionId: string): void {
    const el = document.getElementById(sectionId);

    if (el) {
      el.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
      });
    }
  }
}
