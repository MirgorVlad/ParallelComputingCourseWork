package org.example.index;

import org.example.entity.Document;
import org.example.threadpool.CustomThreadPool;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ConcurrentInvertedIndex {

    private final int THREAD_COUNT = 4;
    private final InvertedIndex invertedIndex;
    private final CustomThreadPool customThreadPool;

    private FutureTask<?> time;

    public ConcurrentInvertedIndex() {
        this.customThreadPool = new CustomThreadPool(THREAD_COUNT);
        invertedIndex = new InvertedIndex();
    }


    public List<FutureTask<?>> buildIndex(List<Document> documentList) {
        List<FutureTask<?>> futureTaskList = new ArrayList<>();
        int step = documentList.size() / THREAD_COUNT;
        for (int i = 0; i < THREAD_COUNT; i++) {
            List<Document> docList = i == THREAD_COUNT-1 ? documentList.subList(i * step, documentList.size())
                    : documentList.subList(i * step, (i + 1) * step);
            futureTaskList.add(customThreadPool.execute(() -> invertedIndex.buildIndex(docList)));
        }
        time = countTime(futureTaskList);
        return futureTaskList;
    }

    private FutureTask<?> countTime(List<FutureTask<?>> futureTaskList) {
        return customThreadPool.execute(() -> {
            double start = System.currentTimeMillis();
            System.out.println(start);
            futureTaskList.forEach(t -> {
                try {
                    t.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            double end = System.currentTimeMillis();
            System.out.println(end);
            return end - start;
        });
    }

    public Double getTime() throws ExecutionException, InterruptedException {
        return (Double) time.get();
    }

    public Map<String, Set<Integer>> searchQuery(String query) {
       return invertedIndex.searchQuery(query);
    }

}
