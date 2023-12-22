package org.example.index;

import org.example.entity.Document;
import org.example.threadpool.CustomThreadPool;

import java.util.*;
import java.util.concurrent.FutureTask;

public class ConcurrentInvertedIndex {

    private final int THREAD_COUNT = 4;
    private final InvertedIndex invertedIndex;

    private final CustomThreadPool customThreadPool;

    public ConcurrentInvertedIndex() {
        this.customThreadPool = new CustomThreadPool(THREAD_COUNT);
        invertedIndex = new InvertedIndex();
    }


    public List<FutureTask<?>> buildIndex(List<Document> documentList) {
        List<FutureTask<?>> futureTaskList = new ArrayList<>();
        int step = documentList.size() / THREAD_COUNT;

        for (int i = 0; i < THREAD_COUNT; i++) {
            List<Document> docList = i == THREAD_COUNT - 1 ? documentList.subList(i * step, documentList.size() - 1)
                    : documentList.subList(i * step, (i + 1) * step);
            futureTaskList.add(customThreadPool.execute(() -> invertedIndex.buildIndex(docList)));
        }

        return futureTaskList;
    }

    public Map<String, Set<Integer>> searchQuery(String query) {
       return invertedIndex.searchQuery(query);
    }

}
