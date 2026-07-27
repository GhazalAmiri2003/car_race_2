package car;

import driver.Driver;

public class ElectricCar extends Car {

    public ElectricCar(Driver driver) {
        super(driver);

        setBaseSpeed(26);
        setFastMaxSpeed(36);
        setAccelerationRate(4.5);
        setFuelCapacity(500);
        setFuelConsumptionRate(18.0);
        setFuelStopDuration(4);
        setCarLength(36);

        finishConfiguration();
    }

    @Override
    protected void accelerateIfNeeded() {
        if (isStopped()) {
            return;
        }

        if (!isFastModeEnabled()) {
            setCurrentSpeedValue(getBaseSpeed());
            return;
        }

        if (getCurrentSpeed() < getBaseSpeed()) {
            setCurrentSpeedValue(getBaseSpeed());
        }

        double targetSpeed = getTargetSpeed();
        double speedGap = targetSpeed - getCurrentSpeed();
        double speedIncrease = Math.random() * getAccelerationRate() * 0.85;

        if (speedIncrease > speedGap) {
            speedIncrease = speedGap;
        }

        double newSpeed = getCurrentSpeed() + speedIncrease;

        if (newSpeed > targetSpeed) {
            newSpeed = targetSpeed;
        }

        setCurrentSpeedValue(newSpeed);
    }

    @Override
    protected void consumeFuel() {
        if (isStopped()) {
            return;
        }

        double newFuel = getCurrentFuel() - (getFuelConsumptionRate() * 0.9);

        if (newFuel < 0) {
            newFuel = 0;
        }

        setCurrentFuelValue(newFuel);
    }
}