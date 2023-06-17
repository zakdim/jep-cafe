package org.github.zakdim;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

/**
 * Create by dmitri on 2023-06-14.
 */
public class C_CreateVirtualThreads {

    public static void main(String[] args) throws InterruptedException {

        ContinuationScope scope = new ContinuationScope("scope");
        Continuation continuation =
                new Continuation(
                        scope,
                        () -> {
                            System.out.println("Running");
                            Continuation.yield(scope); // yielding Continuation suspends its execution
                            System.out.println("Still running");
                        }
                );

        System.out.println("Start");
        continuation.run();
        System.out.println("Back");
        continuation.run();
        System.out.println("Done");
    }
}
