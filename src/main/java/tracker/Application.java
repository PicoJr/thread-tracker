package tracker;

import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final int TASKS = 16;
    private static final int THREADS = 4;
    private static Random random = new Random();

    public static void main(String[] args) throws Exception {
        ThreadTracker threadTracker = new DefaultThreadTracker();
        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < TASKS; i++) {
            long segmentId = threadTracker.createSegment("thread " + i, i);
            Runnable runnable = () -> {
                threadTracker.startSegment(segmentId);
                try {
                    Thread.sleep(200 + 10 * random.nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadTracker.endSegment(segmentId);
            };
            service.submit(runnable);
        }
        service.shutdown();
        service.awaitTermination(5000, TimeUnit.MILLISECONDS);
        threadTracker.dump(Paths.get("out.json"));
    }
}
