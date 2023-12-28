package org.example.index;

import lombok.Data;
import org.example.entity.Document;

import java.util.*;

@Data
public class InvertedIndex {
    private final Random random = new Random();
    private Map<String, Set<Integer>> indexMap = new HashMap<>();
    private final Object object = new Object();

    public boolean buildIndex(List<Document> documentList) {
        for (Document document : documentList) {
            String content = document.getContent();
            for (String word : content.split("\\s+")) {
                synchronized (object) {
                    indexMap.computeIfAbsent(word, k -> new HashSet<>()).add(document.getId());
                }
            }
        }
        return true;
    }

    public Map<String, Set<Integer>> searchQuery(String query) {
        Map<String, Set<Integer>> map = new HashMap<>();
        for (String word : query.split("\\s+")) {
            Set<Integer> index = indexMap.get(word);
            map.put(word, Objects.requireNonNullElseGet(index, HashSet::new));
        }
        return map;
    }
}
