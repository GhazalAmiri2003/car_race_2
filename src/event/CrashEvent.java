package event;

import car.Car;
import car.StopReason;

public abstract class CrashEvent extends RaceEvent {

    protected CrashEvent(String eventName, int stopDuration) {
        super(eventName, stopDuration);
    }

    @Override
    public void applyTo(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null.");
        }

        car.stopForTicks(getStopDuration(), StopReason.CRASH);
    }
}