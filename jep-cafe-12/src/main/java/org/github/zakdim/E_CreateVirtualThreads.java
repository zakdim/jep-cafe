package org.github.zakdim;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Create by dmitri on 2023-06-14.
 */
public class E_CreateVirtualThreads {

    public static final Pattern WORKER_PATTERN = Pattern.compile("worker-\\d+");
    public static final Pattern POOL_PATTERN = Pattern.compile("ForkJoinPool-\\d+");

    private static int counter = 0;
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Set<String> pThreadNames = ConcurrentHashMap.newKeySet();

        ChronoUnit delay = ChronoUnit.MICROS;

        var threads = IntStream.range(0, 100) // try with 10_000_000 - took 8376 ms
                .mapToObj(index -> Thread.ofVirtual()
                        .unstarted(() -> {
                            try {
                                if (index == 0) {
                                    System.out.println(Thread.currentThread());
                                }
                                pThreadNames.add(readWorkerName());
                                synchronized (lock) {
                                    Thread.sleep(Duration.of(1, delay));
                                    counter++;
                                }
                                if (index == 0) {
                                    System.out.println(Thread.currentThread());
                                }
                                pThreadNames.add(readWorkerName());
                                synchronized (lock) {
                                    Thread.sleep(Duration.of(1, delay));
                                    counter++;
                                }
                                if (index == 0) {
                                    System.out.println(Thread.currentThread());
                                }
                                pThreadNames.add(readWorkerName());
                                synchronized (lock) {
                                    Thread.sleep(Duration.of(1, delay));
                                    counter++;
                                }
                                if (index == 0) {
                                    System.out.println(Thread.currentThread());
                                }
                                pThreadNames.add(readWorkerName());
                            } catch (Exception e) {

                            }

                        }))
                .toList();

        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        synchronized (lock) {
            System.out.println("# counter = " + counter);
        }
        System.out.println("# Platform threads = " + pThreadNames.size());
    }

    private static String readWorkerName() {
        String name = Thread.currentThread().toString();
//        System.out.println("Worker name = " + name);
        Matcher workerMatcher = WORKER_PATTERN.matcher(name);
        if (workerMatcher.find()) {
            return workerMatcher.group();
        }
        return "not found";
    }
}
