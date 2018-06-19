package tracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadTracker implements ThreadTracker {

    private final AtomicLong atomicSegmentId;
    private final ConcurrentMap<Long, Segment> segments;

    DefaultThreadTracker() {
        this.atomicSegmentId = new AtomicLong(0);
        this.segments = new ConcurrentHashMap<>();
    }

    @Override
    public long createSegment(String text, int group) {
        Segment segment = new DefaultSegment(text, group);
        segments.put(atomicSegmentId.get(), segment);
        return atomicSegmentId.getAndIncrement();
    }

    @Override
    public void startSegment(long id) {
        if (segments.containsKey(id)) {
            segments.get(id).start();
        }
    }

    @Override
    public void endSegment(long id) {
        if (segments.containsKey(id)) {
            segments.get(id).end();
        }
    }

    @Override
    public void dump(Path jsonPath) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("time_unit", "ms");
        JsonArray segmentsArray = new JsonArray();
        for (Map.Entry<Long, Segment> entry : this.segments.entrySet()) {
            Segment segment = entry.getValue();
            JsonObject segmentObject = new JsonObject();
            segmentObject.addProperty("group", segment.getGroup());
            segmentObject.addProperty("text", segment.getText());
            segmentObject.addProperty("time_start", segment.getStart().toEpochMilli());
            segmentObject.addProperty("time_end", segment.getEnd().toEpochMilli());
            segmentsArray.add(segmentObject);
        }
        jsonObject.add("segments", segmentsArray);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonObject);
        Files.write(jsonPath, json.getBytes());
    }
}
