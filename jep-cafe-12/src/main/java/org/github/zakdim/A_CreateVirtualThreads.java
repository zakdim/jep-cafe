package org.github.zakdim;

import java.util.concurrent.ForkJoinPool;

/**
 * Create by dmitri on 2023-06-14.
 */
public class A_CreateVirtualThreads {
    public static void main(String[] args) throws InterruptedException {

        var pthread =
                Thread.ofPlatform()
                        .unstarted(() -> System.out.println(Thread.currentThread()));

        pthread.start();
        pthread.join();

        var vthread =
                Thread.ofVirtual()
                        .unstarted(() -> System.out.println(Thread.currentThread()));

        vthread.start();
        vthread.join();

//        System.out.println("Class = " + vthread.getClass());

        var task =
                ForkJoinPool.commonPool()
                        .submit(() -> System.out.println(Thread.currentThread()));
        task.join();

    }

}
