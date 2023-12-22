package org.example.threadpool;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class  CustomQueue<T> {

    private final int capacity;
    private final Queue<T> queue = new LinkedList<>();

    public CustomQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void add(T element) throws InterruptedException {
        while (queue.size() == capacity){
            wait();
        }
        queue.offer(element);
        notifyAll();
    }

    public synchronized T remove() throws InterruptedException {
        while (queue.isEmpty()){
            wait();
        }

        T t = queue.poll();
        notifyAll();
        return t;
    }
}
