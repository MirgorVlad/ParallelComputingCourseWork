package org.example;

import org.example.entity.Document;

import java.util.List;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        InvertedIndex invertedIndex = new InvertedIndex();
        List<Document> documentList = DocUtil.readDocuments("D:\\University\\documents");
        invertedIndex.buildIndex(documentList);
        System.out.println(invertedIndex.searchQuery("Harry Potter is"));

    }
}
