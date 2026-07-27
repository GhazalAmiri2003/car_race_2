package driver;

import car.Car;

public abstract class Driver {

    private final String name;

    protected Driver(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Driver name cannot be empty.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // تصمیم راننده برای mode حرکت
    //abstract methods
    public abstract void controlCar(Car car);


    public abstract boolean wantsToJump();


    public abstract boolean wantsToTurn();

//    void activateSafeDrive();
}