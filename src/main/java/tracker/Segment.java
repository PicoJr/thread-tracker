package tracker;

import java.time.Instant;

public interface Segment {

    long getGroup();

    String getText();

    void start();

    void end();

    Instant getStart();

    Instant getEnd();
}
