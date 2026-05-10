export interface TripRequest {
    fromDepartureDate: string | null;
    toDepartureDate: string | null;
    originCity: string | null;
    destinationCity: string | null;
    numberOfPeople: number | null;
}