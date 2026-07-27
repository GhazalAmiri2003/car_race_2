package driver;

import car.Car;

public class UserDriver extends Driver implements UserInteraction {

    private boolean fastModeRequested;
    private boolean jumpRequested;
    private boolean turnRequested;

    public UserDriver(String name) {
        super(name);
        this.fastModeRequested = false;
        this.jumpRequested = false;
        this.turnRequested = false;
    }

    @Override
    public void pressFastKey() {
        fastModeRequested = true;
    }

    @Override
    public void pressSafeKey() {
        fastModeRequested = false;
    }

    @Override
    public void pressJumpKey() {
        jumpRequested = true;
    }

    @Override
    public void pressTurnKey() {
        turnRequested = true;
    }

    @Override
    public void controlCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null.");
        }

        if (fastModeRequested) {
            car.activateFastDrive();
        } else {
            car.activateSafeDrive();
        }
    }

    @Override
    public boolean wantsToJump() {
        boolean result = jumpRequested;
        jumpRequested = false;
        return result;
    }

    @Override
    public boolean wantsToTurn() {
        boolean result = turnRequested;
        turnRequested = false;
        return result;
    }
}