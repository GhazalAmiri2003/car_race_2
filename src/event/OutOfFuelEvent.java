package event;

import car.Car;
import car.StopReason;

public class OutOfFuelEvent extends RaceEvent {

    public OutOfFuelEvent(int stopDuration) {
        super("Out Of Fuel", stopDuration);
    }

    @Override
    public String getDescription() {
        return "The car ran out of fuel and stopped for refueling.";
    }

    @Override
    public void applyTo(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null.");
        }

        car.stopForTicks(getStopDuration(), StopReason.FUEL);
        car.refillFuel();
    }
}