package race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaceEventHistory {

    private final List<RaceEventLog> events = new ArrayList<>();
    private final int maxSize;

    public RaceEventHistory(int maxSize) {
        this.maxSize = Math.max(1, maxSize);
    }

    public void add(int tick, String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        events.add(new RaceEventLog(tick, message));

        while (events.size() > maxSize) {
            events.remove(0);
        }
    }

    public String getLastMessage() {
        if (events.isEmpty()) {
            return "";
        }
        return events.get(events.size() - 1).getMessage();
    }

    public List<RaceEventLog> getEvents() {
        return Collections.unmodifiableList(events);
    }
}