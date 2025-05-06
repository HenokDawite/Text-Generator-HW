/**
 * class to represent a pair of strings for use in trigram text generation
 * 
 * @author Henok Dawite
 */
package wordsGeneric;

public class StringPair {
    String first;
    String second;

    /**
     * creates a new string pair with the given words
     * @param first the first word in the pair
     * @param second the second word in the pair
     */
    public StringPair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    /**
     * returns a string representation of the pair in the format <first,second>
     * @return string representation of the pair
     */
    @Override
    public String toString() {
        return "<" + first + "," + second + ">";
    }

    /**
     * checks if this pair is equal to another object
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        StringPair other = (StringPair) obj;
        return first.equals(other.first) && second.equals(other.second);
    }

    /**
     * generates a hash code for this pair based on both strings
     * @return hash code for this pair
     */
    @Override
    public int hashCode() {
        return first.hashCode() * 31 + second.hashCode();
    }

    /**
     * main method to test the StringPair class
     * tests equality and hash map functionality
     */
    public static void main(String[] args) {
        // Test cases
        StringPair pair1 = new StringPair("hello", "world");
        StringPair pair2 = new StringPair("hello", "world");
        StringPair pair3 = new StringPair("goodbye", "world");

        // Test equals
        System.out.println("pair1.equals(pair2): " + pair1.equals(pair2)); // Should be true
        System.out.println("pair1.equals(pair3): " + pair1.equals(pair3)); // Should be false

        // Test HashMap
        java.util.HashMap<StringPair, String> map = new java.util.HashMap<>();
        map.put(pair1, "test1");
        System.out.println("map.get(pair2): " + map.get(pair2)); // Should be "test1"
        System.out.println("map.get(pair3): " + map.get(pair3)); // Should be null
    }
}
