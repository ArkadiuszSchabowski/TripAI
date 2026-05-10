import { TripDailyPlan } from "./trip-daily-plan";
import { TripInformation } from "./trip-information";

export interface TripAgentResponse {
  information: TripInformation;
  dailyPlan: TripDailyPlan[];
}