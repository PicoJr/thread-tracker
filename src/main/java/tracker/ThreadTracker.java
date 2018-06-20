package tracker;

import java.io.IOException;
import java.nio.file.Path;

interface ThreadTracker {

    /** Create a new segment
     * thread-safe
     * @param text for segment
     * @param group of segment
     * @return segment id
     */
    long createSegment(String text, long group);

    /** Start segment with id id
     * id obtained from {@link #createSegment}
     * thread-safe
     * @param id of segment to start
     */
    void startSegment(long id);

    /** End segment with id id
     * id obtained from {@link #createSegment}
     * thread-safe
     * @param id of segment to end
     */
    void endSegment(long id);

    /** dump segments to json at path
     * not thread-safe
     * @param jsonPath dump
     * @throws IOException
     */
    void dump(Path jsonPath) throws IOException;
}
