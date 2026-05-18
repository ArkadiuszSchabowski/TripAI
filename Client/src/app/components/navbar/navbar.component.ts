import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  constructor(
    private router: Router,
  ) {}

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
