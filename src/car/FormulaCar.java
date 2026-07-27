package car;

import driver.Driver;

public class FormulaCar extends Car {

    public FormulaCar(Driver driver) {
        super(driver);

        setBaseSpeed(44);
        setFastMaxSpeed(54);
        setAccelerationRate(9.0);
        setFuelCapacity(300);
        setFuelConsumptionRate(16.0);
        setFuelStopDuration(2);
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

        double speedIncrease = Math.random()
                * getAccelerationRate()
                * 1.25
                * (speedGap / targetSpeed);

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

        double newFuel = getCurrentFuel() - (getFuelConsumptionRate() * 1.1);

        if (newFuel < 0) {
            newFuel = 0;
        }

        setCurrentFuelValue(newFuel);
    }


}