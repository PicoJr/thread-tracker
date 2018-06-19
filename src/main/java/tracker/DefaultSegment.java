package tracker;

import java.time.Instant;

public class DefaultSegment implements Segment {
    private final int group;
    private final String text;
    private Instant start;
    private Instant end;

    DefaultSegment(String text, int group) {
        this.text = text;
        this.group = group;
    }

    @Override
    public int getGroup() {
        return this.group;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public synchronized void start() {
        this.start = Instant.now();
    }

    @Override
    public synchronized void end() {
        this.end = Instant.now();
    }

    @Override
    public Instant getStart() {
        return start;
    }

    @Override
    public Instant getEnd() {
        return end;
    }
}
