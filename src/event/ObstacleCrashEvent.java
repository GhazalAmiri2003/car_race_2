package event;

public class ObstacleCrashEvent extends CrashEvent {

    public ObstacleCrashEvent() {
        super("Obstacle Crash", 3);
    }

    @Override
    public String getDescription() {
        return "The car crashed into an obstacle.";
    }
}