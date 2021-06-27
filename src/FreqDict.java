import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FreqDict {
    LinkedHashMap<String, Integer> frequencyDictionary; // Declares a LinkedHashMap for the character frequencies

    /**
     * This method initializes the LinkedHashMap we declared earlier
     * */
    public FreqDict() {
        this.frequencyDictionary = new LinkedHashMap<>();
    }

    /**
     * @param text This is the text the user wants encoded
     * @return Returns a LinkedHashMap of the characters mapped to their frequencies in the text
     */
    public LinkedHashMap<String, Integer> makeFrequencyDictionary(String text) {
        for (String word : text.split("")) {
            if (frequencyDictionary.containsKey(word)) {
                frequencyDictionary.put(word, frequencyDictionary.get(word) + 1);
            }
            if (!frequencyDictionary.containsKey(word)) {
                frequencyDictionary.put(word, 1);
            }
        }
        return frequencyDictionary;
    }

    /**
     * This is something I found on stack overflow that sorts the frequency hash map
     */
    public <K, V> void orderByValue(@NotNull LinkedHashMap<K, V> m, Comparator<? super V> c) {
        List<Map.Entry<K, V>> entries = new ArrayList<>(m.entrySet());
        m.clear();
        entries.stream()
                .sorted(Map.Entry.comparingByValue(c))
                .forEachOrdered(e -> m.put(e.getKey(), e.getValue()));
    }
}
