package driver;

import car.Car;

public abstract class OpponentDriver extends Driver {

    private double obstacleFailChance;
    private double turnFailChance;
    private boolean prefersFastMode;

    protected OpponentDriver(String name,
                             double obstacleFailChance,
                             double turnFailChance,
                             boolean prefersFastMode) {
        super(name);

        if (obstacleFailChance < 0 || obstacleFailChance > 1) {
            throw new IllegalArgumentException("Obstacle fail chance must be between 0 and 1.");
        }

        if (turnFailChance < 0 || turnFailChance > 1) {
            throw new IllegalArgumentException("Turn fail chance must be between 0 and 1.");
        }

        this.obstacleFailChance = obstacleFailChance;
        this.turnFailChance = turnFailChance;
        this.prefersFastMode = prefersFastMode;
    }



    @Override
    public void controlCar(Car car) {
        if (prefersFastMode) {
            car.activateFastDrive();
        } else {
            car.activateSafeDrive();
        }
    }

    @Override
    public boolean wantsToJump() {
        return Math.random() > obstacleFailChance;
    }

    @Override
    public boolean wantsToTurn() {
        return Math.random() > turnFailChance;
    }
}