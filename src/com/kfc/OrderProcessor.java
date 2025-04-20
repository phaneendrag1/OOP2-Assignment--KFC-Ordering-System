
package com.kfc;

import java.util.List;
import java.util.concurrent.*;

public class OrderProcessor {
    public static void processOrdersInBackground(List<OrderLine> orders) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Void> task = () -> {
            System.out.println("Processing " + orders.size() + " order(s) in background...");
            Thread.sleep(1000); // simulate delay
            System.out.println("Orders processed!");
            return null;
        };
        executor.submit(task);
        executor.shutdown();
    }
}
