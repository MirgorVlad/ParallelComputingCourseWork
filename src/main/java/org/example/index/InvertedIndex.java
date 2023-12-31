package org.example.index;

import lombok.Data;
import org.example.map.CustomConcurrentHashMap;
import org.example.entity.Document;

import java.util.*;

@Data
public class InvertedIndex {
    private final Random random = new Random();
    private CustomConcurrentHashMap<String, Set<String>> indexMap = new CustomConcurrentHashMap<>();
    private final Object object = new Object();

    public boolean buildIndex(List<Document> documentList, int start, int end) {
        for (int i = start; i < end; i++) {
            Document document = documentList.get(i);
            String content = document.getContent();
            for (String word : content.split("\\s+")) {
                Set<String> strings = indexMap.get(word);
                if(strings == null) {
                    strings = new HashSet<>();
                }
                strings.add(document.getId());
                indexMap.put(word, strings);
            }
        }
        return true;
    }

    public Map<String, Set<String>> searchQuery(String query) {
        Map<String, Set<String>> map = new HashMap<>();
        for (String word : query.split("\\s+")) {
            Set<String> index = indexMap.get(word);
            map.put(word, Objects.requireNonNullElseGet(index, HashSet::new));
        }
        return map;
    }
}
