package org.example;

import org.example.entity.Document;
import org.example.index.InvertedIndex;
import org.example.util.DocUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        InvertedIndex invertedIndex = new InvertedIndex();
        List<Document> documentList = DocUtil.readDocuments("D:\\University\\documents");
        int threadCount = 4;
        int step = documentList.size() / threadCount;

        for(int i = 0; i < threadCount; i++){

            List<Document> docList = i == threadCount-1 ? documentList.subList(i * step, documentList.size()-1)
                    : documentList.subList(i * step, (i+1)*step);
            executorService.submit(() -> invertedIndex.buildIndex(docList));

        }

        invertedIndex.buildIndex(documentList);
        System.out.println(invertedIndex.searchQuery("Harry Potter is"));

    }
}
