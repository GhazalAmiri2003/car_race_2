package car;

public class InvalidVehicleConfigException extends RuntimeException {

    public InvalidVehicleConfigException(String message) {
        super(message);
    }
}