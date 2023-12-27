package org.example;

import org.example.entity.Document;
import org.example.index.ConcurrentInvertedIndex;
import org.example.index.InvertedIndex;
import org.example.util.DocUtil;

import java.util.List;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        ConcurrentInvertedIndex concurrentInvertedIndex = new ConcurrentInvertedIndex();
        List<Document> documents = DocUtil.readDocuments("/home/thingsboard563/Private/documents/pos");
        List<FutureTask<?>> futureTaskList = concurrentInvertedIndex.buildIndex(documents);

        for(FutureTask<?> task : futureTaskList){
            task.get();
        }

        System.out.println(concurrentInvertedIndex.searchQuery("Vlad"));
    }
}
