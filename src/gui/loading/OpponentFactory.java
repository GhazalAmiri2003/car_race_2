package gui.loading;

import car.Car;
import car.DefaultCar;
import car.ElectricCar;
import car.FormulaCar;
import driver.DefaultOpponentDriver;

import driver.ProfessionalOpponentDriver;

import java.util.Random;

public final class OpponentFactory {

    private static final Random RANDOM = new Random();

    private OpponentFactory() {
    }

    public static Car createRandomOpponent() {
        int choice = RANDOM.nextInt(6);

        return switch (choice) {
            case 0 -> new DefaultCar(new DefaultOpponentDriver("Opponent"));
            case 1 -> new ElectricCar(new DefaultOpponentDriver("Opponent"));
            case 2 -> new FormulaCar(new DefaultOpponentDriver("Opponent"));
            case 3 -> new DefaultCar(new ProfessionalOpponentDriver("Opponent"));
            case 4 -> new ElectricCar(new ProfessionalOpponentDriver("Opponent"));
            default -> new FormulaCar(new ProfessionalOpponentDriver("Opponent"));
        };
    }
}