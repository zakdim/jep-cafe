package org.github.zakdim;

import java.util.stream.IntStream;

/**
 * Create by dmitri on 2023-06-14.
 */
public class B_CreateVirtualThreads {

    public static void main(String[] args) throws InterruptedException {
        var threads = IntStream.range(0, 10)
                .mapToObj(index -> Thread.ofVirtual().unstarted(() -> {
                    if (index == 0) {
                        System.out.println(Thread.currentThread());
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (index == 0) {
                        System.out.println(Thread.currentThread());
                    }
                }))
                .toList();

        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
    }
}
