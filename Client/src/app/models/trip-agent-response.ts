import { FlightDetails } from "./flight-details";
import { TripCostEstimate } from "./trip-cost-estimate";
import { TripDailyPlan } from "./trip-daily-plan";
import { TripInformation } from "./trip-information";

export interface TripAgentResponse {
  tripIntro: string;
  closingFeeling: string;
  costs: TripCostEstimate;
  details: FlightDetails;
  information: TripInformation;
  dailyPlan: TripDailyPlan[];
}