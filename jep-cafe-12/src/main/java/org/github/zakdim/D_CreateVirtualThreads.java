package org.github.zakdim;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Create by dmitri on 2023-06-14.
 */
public class D_CreateVirtualThreads {

    public static final Pattern WORKER_PATTERN = Pattern.compile("worker-\\d+");
    public static final Pattern POOL_PATTERN = Pattern.compile("ForkJoinPool-\\d+");

    public static void main(String[] args) throws InterruptedException {

        Set<String> poolNames = ConcurrentHashMap.newKeySet();
        Set<String> pThreadNames = ConcurrentHashMap.newKeySet();

        var threads = IntStream.range(0, 10) // try with 10_000_000 - took 8376 ms
                .mapToObj(i -> Thread.ofVirtual()
                        .unstarted(() -> {
                            String poolName = readPoolName();
                            poolNames.add(poolName);
                            String workerName = readWorkerName();
                            pThreadNames.add(workerName);
                        }))
                .toList();

        Instant begin = Instant.now();
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        Instant end = Instant.now();
        System.out.println("Time = " + Duration.between(begin, end).toMillis() + " ms");
        System.out.println("# cores = " + Runtime.getRuntime().availableProcessors());
        System.out.println("# Pools: " + poolNames.size() + ", poolNames = " + poolNames);
        System.out.println("# Platform threads: " + pThreadNames.size() + ", pTheadNames = " + pThreadNames);
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

    private static String readPoolName() {
        String name = Thread.currentThread().toString();
//        System.out.println("Pool name = " + name);
        Matcher poolMatcher = POOL_PATTERN.matcher(name);
        if (poolMatcher.find()) {
            return poolMatcher.group();
        }
        return "pool not found";
    }
}
