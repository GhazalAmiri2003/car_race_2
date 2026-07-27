package event;

import car.Car;

public abstract class RaceEvent {

    private final String eventName;
    private final int stopDuration;

    protected RaceEvent(String eventName, int stopDuration) {
        if (eventName == null || eventName.isBlank()) {
            throw new IllegalArgumentException("Event name cannot be empty.");
        }

        if (stopDuration < 0) {
            throw new IllegalArgumentException("Stop duration cannot be negative.");
        }

        this.eventName = eventName;
        this.stopDuration = stopDuration;
    }


    public int getStopDuration() {
        return stopDuration;
    }

    public abstract String getDescription();

    public abstract void applyTo(Car car);
}