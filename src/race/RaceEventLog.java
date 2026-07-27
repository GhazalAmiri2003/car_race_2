package race;

public class RaceEventLog {

    private final int tick;
    private final String message;

    public RaceEventLog(int tick, String message) {
        this.tick = tick;
        this.message = message;
    }

    public int getTick() {
        return tick;
    }

    public String getMessage() {
        return message;
    }
}