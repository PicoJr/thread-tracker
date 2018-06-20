package tracker;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Application {

    private static final int TASKS = 256;
    private static final int THREADS = 16;
    private static Random random = new Random();

    public static void main(String[] args) throws Exception {
        ThreadTracker threadTracker = new DefaultThreadTracker();
        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        List<Callable<Integer>> tasks = new LinkedList<>();
        for (int i = 0; i < TASKS; i++) {
            Callable<Integer> task = () -> {
                long threadId = Thread.currentThread().getId();
                long segmentId = threadTracker.createSegment("thread " + threadId, threadId);
                threadTracker.startSegment(segmentId);
                try {
                    Thread.sleep(5 + 40 * random.nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadTracker.endSegment(segmentId);
                return 0;
            };
            tasks.add(task);
        }
        List<Future<Integer>> results = service.invokeAll(tasks);
        for(Future<Integer> result: results) {
            result.get();
        }
        service.shutdown();
        threadTracker.dump(Paths.get("out.json"));
    }
}
