package car;

import driver.Driver;

public abstract class Car implements Controllable {

//composition
    private final Driver driver;

    private double baseSpeed;
    private double fastMaxSpeed;
    private double accelerationRate;
    private double fuelCapacity;
    private double fuelConsumptionRate;
    private int fuelStopDuration;
    private double carLength;

    private double currentSpeed;
    private double currentPosition;
    private double previousPosition;
    private int currentTime;
    private double currentFuel;

    private boolean fastModeEnabled;

    private CarState state;
    private StopReason stopReason;
    private int remainingStateTicks;
    //ریکاوری کمک می‌کنه بازی کمی forgivingتر بشه.
    private int recoveryTicksAfterCrash;

    private int jumpTotalTicks;
    private int jumpPeakHeight;

    public Car(Driver driver) {
        if (driver == null) {
            throw new InvalidVehicleConfigException("Driver cannot be null.");
        }

        this.driver = driver;

        this.currentPosition = 0;
        this.previousPosition = 0;
        this.currentTime = 0;

        this.fastModeEnabled = false;

        this.state = CarState.NORMAL;
        this.stopReason = StopReason.NONE;
        this.remainingStateTicks = 0;
        this.recoveryTicksAfterCrash = 0;

        this.jumpTotalTicks = 8;
        this.jumpPeakHeight = 22; //4 render
    }

    protected void finishConfiguration() {
        validateConfiguration();
        this.currentFuel = fuelCapacity;
        this.currentSpeed = baseSpeed;
    }

    private void validateConfiguration() {
        if (baseSpeed <= 0) {
            throw new InvalidVehicleConfigException("Base speed must be positive.");
        }
        if (fastMaxSpeed < baseSpeed) {
            throw new InvalidVehicleConfigException("Fast max speed cannot be smaller than base speed.");
        }
        if (accelerationRate <= 0) {
            throw new InvalidVehicleConfigException("Acceleration rate must be positive.");
        }
        if (fuelCapacity <= 0) {
            throw new InvalidVehicleConfigException("Fuel capacity must be positive.");
        }
        if (fuelConsumptionRate <= 0) {
            throw new InvalidVehicleConfigException("Fuel consumption rate must be positive.");
        }
        if (fuelStopDuration < 0) {
            throw new InvalidVehicleConfigException("Fuel stop duration cannot be negative.");
        }
        if (carLength <= 0) {
            throw new InvalidVehicleConfigException("Car length must be positive.");
        }
    }

    public Driver getDriver() {
        return driver;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getFastMaxSpeed() {
        return fastMaxSpeed;
    }

    public double getAccelerationRate() {
        return accelerationRate;
    }


    public double getCurrentFuel() {
        return currentFuel;
    }

    public double getFuelConsumptionRate() {
        return fuelConsumptionRate;
    }

    public int getFuelStopDuration() {
        return fuelStopDuration;
    }


    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public double getPreviousPosition() {
        return previousPosition;
    }

    //TO REACH THE OBSTACLE AND CURVE
    public double getFrontPosition() {
        return currentPosition + carLength;
    }

    public double getPreviousFrontPosition() {
        return previousPosition + carLength;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public boolean isFastModeEnabled() {
        return fastModeEnabled;
    }



    public StopReason getStopReason() {
        return stopReason;
    }

    public int getRemainingStopTicks() {
        if (!isStopped()) {
            return 0;
        }
        return remainingStateTicks;
    }



    public boolean isStopped() {
        return state == CarState.CRASHED || state == CarState.REFUELING;
    }

    public boolean isJumping() {
        return state == CarState.JUMPING;
    }

    public boolean isIgnoringHazards() {
        return state == CarState.RECOVERING;
    }



    protected void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    protected void setFastMaxSpeed(double fastMaxSpeed) {
        this.fastMaxSpeed = fastMaxSpeed;
    }

    protected void setAccelerationRate(double accelerationRate) {
        this.accelerationRate = accelerationRate;
    }

    protected void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    protected void setFuelConsumptionRate(double fuelConsumptionRate) {
        this.fuelConsumptionRate = fuelConsumptionRate;
    }

    protected void setFuelStopDuration(int fuelStopDuration) {
        this.fuelStopDuration = fuelStopDuration;
    }

    protected void setCarLength(double carLength) {
        this.carLength = carLength;
    }

    protected void setCurrentSpeedValue(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    protected void setCurrentFuelValue(double currentFuel) {
        this.currentFuel = currentFuel;
    }

    @Override
    public void activateSafeDrive() {
        fastModeEnabled = false;
    }

    @Override
    public void activateFastDrive() {
        fastModeEnabled = true;
    }

    protected double getTargetSpeed() {
        return fastModeEnabled ? fastMaxSpeed : baseSpeed;
    }

    protected void accelerateIfNeeded() {
        if (isStopped()) {
            return;
        }

        if (!fastModeEnabled) {
            currentSpeed = baseSpeed;
            return;
        }

        if (currentSpeed < baseSpeed) {
            currentSpeed = baseSpeed;
        }

        currentSpeed += accelerationRate;

        if (currentSpeed > getTargetSpeed()) {
            currentSpeed = getTargetSpeed();
        }
    }

    protected void consumeFuel() {
        if (isStopped()) {
            return;
        }

        currentFuel -= fuelConsumptionRate;

        if (currentFuel < 0) {
            currentFuel = 0;
        }
    }

    protected void move() {
        if (isStopped()) {
            return;
        }

        previousPosition = currentPosition;
        currentPosition += currentSpeed * 0.25;//4 GAME EQUILIBRIUM
        currentTime++;
    }

    public void refillFuel() {
        currentFuel = fuelCapacity;
    }
    //overload
    public void stopForTicks(int ticks) {
        stopForTicks(ticks, StopReason.OTHER);
    }

    public void stopForTicks(int ticks, StopReason reason) {
        StopReason safeReason = reason;

        if (safeReason == null) {
            safeReason = StopReason.OTHER;
        }

        this.stopReason = safeReason;
        //apply
        this.remainingStateTicks = Math.max(1, ticks);

        if (safeReason == StopReason.FUEL) {
            this.state = CarState.REFUELING;
        } else {
            this.state = CarState.CRASHED;
        }

        resetSpeedAfterStop();
    }

    protected void resetSpeedAfterStop() {
        currentSpeed = baseSpeed;
    }



    public boolean needsFuelStop() {
        return currentFuel <= 0;
    }

    public void startJump(int ticks) {
        if (ticks <= 0 || isStopped()) {
            return;
        }

        this.state = CarState.JUMPING;
        this.remainingStateTicks = ticks;
        this.jumpTotalTicks = ticks;
    }



    private void updateStateTimer() {
        if (state == CarState.NORMAL) {
            return;
        }

        if (remainingStateTicks > 0) {
            remainingStateTicks--;
        }

        if (remainingStateTicks > 0) {
            return;
        }
            //to apply recovery 4 cars after crash
        if (state == CarState.CRASHED && recoveryTicksAfterCrash > 0) {
            state = CarState.RECOVERING;
            remainingStateTicks = recoveryTicksAfterCrash;
            recoveryTicksAfterCrash = 0;
            stopReason = StopReason.NONE;
            resetSpeedAfterStop();
            return;
        }

        if (state == CarState.REFUELING) {
            refillFuel();
        }

        state = CarState.NORMAL;
        remainingStateTicks = 0;
        recoveryTicksAfterCrash = 0;
        stopReason = StopReason.NONE;
        resetSpeedAfterStop();
    }

    public int getJumpOffsetY() {
        if (!isJumping()) {
            return 0;
        }

        int passed = jumpTotalTicks - remainingStateTicks;

        if (passed < 0) {
            passed = 0;
        }
        if (passed > jumpTotalTicks) {
            passed = jumpTotalTicks;
        }

        int half = Math.max(1, jumpTotalTicks / 2);

        if (passed <= half) {
            return (int) ((passed / (double) half) * jumpPeakHeight);
        }

        int fallingTicks = passed - half;
        int fallingTotal = Math.max(1, jumpTotalTicks - half);

        return (int) (((fallingTotal - fallingTicks) / (double) fallingTotal) * jumpPeakHeight);
    }

    public void updateOneTick() {
        updateStateTimer();

        if (isStopped()) {
            currentTime++;
            return;
        }

        accelerateIfNeeded();
        move();
        consumeFuel();
    }
}