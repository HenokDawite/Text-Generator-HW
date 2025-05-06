/**
 * Frequency List of word/# of occurrence pairs
 * @author Henok Dawite
 **/
package wordsGeneric;

import java.util.HashMap;
import java.util.ArrayList;

public class FreqList {
    // Map that stores each word and the number of times it has been added
    protected HashMap<String, Integer> frequencyMap;
    
    /**
     * constructor
     */
    public FreqList() {
        frequencyMap = new HashMap<String, Integer>();
    }
    
    /**
     * Add a reference to a word to our list
     *
     * @param word the word to track
     */
    public void add(String word) {
        if (frequencyMap.containsKey(word)) {
            frequencyMap.put(word, frequencyMap.get(word) + 1); // increase count if it exists
        } else {
            frequencyMap.put(word, 1); // first appearance
        }
    }
    
    /**
     * Return a word from the FreqList based on a 0-1 value
     *
     * @param prob a number between 0 and 1
     * @return a String from the list, weighted by occurrence
     */
    public String get(double prob) {
        ArrayList<String> weightedWords = new ArrayList<>();
        
        if (prob < 0 || prob > 1) {
            throw new IllegalArgumentException("Invalid probability");
        }
        
        for (String item : frequencyMap.keySet()) {
            int times = frequencyMap.get(item);
            for (int j = 0; j < times; j++) {
                weightedWords.add(item);
            }
        }
        
        double totalItems = weightedWords.size();
        if (totalItems == 0) {
            return "";
        }
        
        if (prob == 1.0) {
            return weightedWords.get(weightedWords.size() - 1);
        }
        
        int index = (int) (prob * totalItems);
        return weightedWords.get(index);
    }
    
    /**
     * String representation of the list
     *
     * @return a string showing each word and how often it appeared
     */
    public String toString() {
        String result = "Frequency List: ";
        for (String label : frequencyMap.keySet()) {
            result += "<" + label + "=" + frequencyMap.get(label) + ">";
        }
        return result;
    }
    
    /**
     * Test method for FreqList
     */
    public static void main(String[] args) {
        FreqList example = new FreqList();
        example.add("apple");
        example.add("banana");
        example.add("apple");
        example.add("carrot");
        example.add("apple");
        example.add("banana");
        example.add("date");
        example.add("egg");
        example.add("fig");
        example.add("grape");
        
        System.out.println(example);
        
        // Test get method at multiple probabilities
        double[] probsToTest = {0.0, 0.1, 0.25, 0.33, 0.5, 0.66, 0.75, 0.9, 1.0};
        for (double prob : probsToTest) {
            System.out.printf("Word at p=%.2f: %s%n", prob, example.get(prob));
        }
    }
}