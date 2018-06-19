package tracker;

import java.time.Instant;

public interface Segment {

    int getGroup();

    String getText();

    void start();

    void end();

    Instant getStart();

    Instant getEnd();
}
