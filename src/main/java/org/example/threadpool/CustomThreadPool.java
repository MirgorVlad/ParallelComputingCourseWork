package org.example.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CustomThreadPool {
    private final CustomQueue<FutureTask<?>> taskQueue;
    private final List<WorkerThread> threadList;


    public CustomThreadPool(int poolSize) {
        this.taskQueue = new CustomQueue<>(poolSize);
        this.threadList = new ArrayList<>();

        for (int i = 0; i < poolSize; i++) {
            WorkerThread thread = new WorkerThread();
            threadList.add(thread);
            thread.start();
        }
    }

    public FutureTask<?> execute(Callable<?> task) {
        FutureTask<?> futureTask = new FutureTask<>(task);
        try {
            taskQueue.add(futureTask);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return futureTask;
    }

    public void shutdown() {
        for (WorkerThread thread : threadList) {
            thread.stopThread();
        }
    }

    private class WorkerThread extends Thread {
        private volatile boolean isStopped = false;

        public void run() {
            while (!isStopped) {
                try {
                    FutureTask<?> task = taskQueue.remove();
                    task.run();
                } catch (InterruptedException e) {
                    if (isStopped) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void stopThread() {
            isStopped = true;
            this.interrupt();
        }
    }
}
