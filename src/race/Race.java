package race;

import car.Car;
import driver.Driver;
import driver.UserInteraction;
import event.CurveCrashEvent;
import event.ObstacleCrashEvent;
import event.OutOfFuelEvent;
import event.RaceEvent;
import track.Curve;
import track.Obstacle;
import track.RaceTrack;

import java.util.*;

public class Race {

    private final Car playerCar;
    private final Car opponentCar;
    private final RaceTrack track;
    private final RaceEventHistory eventHistory;

    private final Map<Car, Set<Obstacle>> handledObstacles;
    private final Map<Car, Set<Curve>> handledCurves;

    private boolean finished;
    private String result;

    public Race(Car playerCar, Car opponentCar, RaceTrack track) {
        if (playerCar == null || opponentCar == null || track == null) {
            throw new IllegalArgumentException("Race arguments cannot be null.");
        }

        this.playerCar = playerCar;
        this.opponentCar = opponentCar;
        this.track = track;
        this.eventHistory = new RaceEventHistory(5);
//to build hash map for each car
        this.handledObstacles = new HashMap<>();
        this.handledCurves = new HashMap<>();

        this.handledObstacles.put(playerCar, new HashSet<>());
        this.handledObstacles.put(opponentCar, new HashSet<>());
        //separate set for each car to handle
        this.handledCurves.put(playerCar, new HashSet<>());
        this.handledCurves.put(opponentCar, new HashSet<>());

        this.finished = false;
        this.result = "";
    }

    public Car getPlayerCar() {
        return playerCar;
    }

    public Car getOpponentCar() {
        return opponentCar;
    }

    public RaceTrack getTrack() {
        return track;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getResult() {
        return result;
    }

    public String getLastEventMessage() {
        return eventHistory.getLastMessage();
    }

    public List<RaceEventLog> getEventLogs() {
        return eventHistory.getEvents();
    }

    public UserInteraction getPlayerInput() {
        if (!(playerCar.getDriver() instanceof UserInteraction)) {
            throw new IllegalStateException("Player driver does not support user interaction.");
        }
        //casting needed coz we need interaction methods not just driver
        return (UserInteraction) playerCar.getDriver();
    }

            //updating


    public void updateStep() {
        if (finished) {
            return;
        }

        updateCar(playerCar);
        updateCar(opponentCar);

        checkWinner();
    }

    private void updateCar(Car car) {
        //driver decide for fast/safe mode
        Driver driver = car.getDriver();
        boolean wasStopped = car.isStopped();//B4 update

        driver.controlCar(car);
        car.updateOneTick();
        //no event effect on stopped car until tick finish
        if (wasStopped || car.isStopped()) {
            return;
        }

        handleFuelEvent(car);
        if (car.isStopped()) {
            return;
        }

        handleObstacleEvent(car, driver);
        if (car.isStopped()) {
            return;
        }

        handleCurveEvent(car, driver);
    }



     //handle events

    private void handleFuelEvent(Car car) {
        if (!car.needsFuelStop()) {
            return;
        }

        RaceEvent event = new OutOfFuelEvent(car.getFuelStopDuration());
        event.applyTo(car);
        log(car, event.getDescription());
    }

    private void handleObstacleEvent(Car car, Driver driver) {
        Obstacle obstacle = track.getPassedObstacle(
                car.getPreviousFrontPosition(),
                car.getFrontPosition()
        );

        if (obstacle == null) {
            return;
        }
        //check if it is new or repeated
        if (handledObstacles.get(car).contains(obstacle)) {
            return;
        }
        //get value for car and add new obstacle to set
        handledObstacles.get(car).add(obstacle);

        if (driver.wantsToJump()) {
            car.startJump(8);
            log(car, "Jump success!");
            return;
        }

        RaceEvent event = new ObstacleCrashEvent();
        event.applyTo(car);
        log(car, event.getDescription());
    }

    private void handleCurveEvent(Car car, Driver driver) {
        Curve curve = track.getEnteredCurve(
                car.getPreviousPosition(),
                car.getCurrentPosition()
        );

        if (curve == null) {
            return;
        }

        if (handledCurves.get(car).contains(curve)) {
            return;
        }

        handledCurves.get(car).add(curve);

        if (driver.wantsToTurn()) {
            log(car, "Turn success! Curve direction: " + curve.getDirection());
            return;
        }

        RaceEvent event = new CurveCrashEvent();
        event.applyTo(car);
        log(car, event.getDescription());
    }



    private void log(Car car, String message) {

        eventHistory.add(car.getCurrentTime(), car.getDriver().getName() + ": " + message);
    }



    private void checkWinner() {
        boolean playerFinished = track.hasFinished(playerCar.getFrontPosition());
        boolean opponentFinished = track.hasFinished(opponentCar.getFrontPosition());

        if (playerFinished && opponentFinished) {
            finished = true;
            result = "Draw!";
        } else if (playerFinished) {
            finished = true;
            result = "Player wins!";
        } else if (opponentFinished) {
            finished = true;
            result = "Opponent wins!";
        }
    }
}