package event;

public class CurveCrashEvent extends CrashEvent {

    public CurveCrashEvent() {
        super("Curve Crash", 2);
    }

    @Override
    public String getDescription() {
        return "The car failed to turn correctly in the curve.";
    }
}