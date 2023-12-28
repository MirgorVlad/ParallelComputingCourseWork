package org.example.util;

import org.example.entity.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocUtil {

    public static List<Document> readDocuments(String folderPath) {
        int count = 0;
        List<Document> contentList = new ArrayList<>();
        File file = new File(folderPath);
        int i = 0;
        for (File dir : file.listFiles()) {
            for (File filePath : dir.listFiles()) {
                Path path = Paths.get(filePath.getAbsolutePath());
                count++;
                try {
                    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                    String content = String.join("\n", lines);
                    contentList.add(new Document(++i, content));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Files: " + count);
        return contentList;

    }
}
