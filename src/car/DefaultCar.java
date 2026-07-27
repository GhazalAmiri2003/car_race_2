package car;
import driver.Driver;
public class DefaultCar extends Car {

    public DefaultCar(Driver driver) {
        super(driver);

        setBaseSpeed(34);
        setFastMaxSpeed(54);
        setAccelerationRate(6.0);
        setFuelCapacity(350);
        setFuelConsumptionRate(14.0);
        setFuelStopDuration(4);
        setCarLength(36);

        finishConfiguration();
    }
}