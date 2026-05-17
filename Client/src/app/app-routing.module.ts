import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/public/home/home.component';
import { ItineraryComponent } from './components/public/itinerary/itinerary.component';

const routes: Routes = [
  {path: "", component: HomeComponent},
  {path: "itinerary", component: ItineraryComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
