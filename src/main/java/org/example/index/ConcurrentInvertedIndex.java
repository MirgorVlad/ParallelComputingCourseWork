package org.example.index;

import lombok.Getter;
import org.example.entity.Document;
import org.example.threadpool.CustomThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ConcurrentInvertedIndex {

    private final int THREAD_COUNT = 1;
    private final InvertedIndex invertedIndex;
    private final CustomThreadPool customThreadPool;
    @Getter
    private  List<FutureTask<?>> futureTaskList;

    private FutureTask<?> time;

    public ConcurrentInvertedIndex() {
        this.customThreadPool = new CustomThreadPool(THREAD_COUNT);
        invertedIndex = new InvertedIndex();
    }


    public void buildIndex(List<Document> documentList) {
        List<FutureTask<?>> futureTaskList = new ArrayList<>();
        int step = documentList.size() / THREAD_COUNT;
        for (int i = 0; i < THREAD_COUNT; i++) {
            List<Document> docList = i == THREAD_COUNT - 1 ? documentList.subList(i * step, documentList.size())
                    : documentList.subList(i * step, (i + 1) * step);
            futureTaskList.add(customThreadPool.execute(() -> invertedIndex.buildIndex(docList)));
        }
        this.futureTaskList = futureTaskList;
        countTime(futureTaskList);
    }

    private void countTime(List<FutureTask<?>> futureTaskList) {
        time = new FutureTask<>(() -> {
            long start = System.currentTimeMillis();
            System.out.println(start);
            futureTaskList.forEach(t -> {
                try {
                    t.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            long end = System.currentTimeMillis();
            System.out.println(end);
            return String.valueOf(end - start);
        });

        new Thread(time).start();
    }

    public String getTime() throws ExecutionException, InterruptedException {
        return (String) time.get();
    }

    public Map<String, Set<Integer>> searchQuery(String query) {
        return invertedIndex.searchQuery(query);
    }

}
