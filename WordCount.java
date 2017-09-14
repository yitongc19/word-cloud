/**
 * This class allows easy retrieval of a word and its count
 * @author: Yitong Chen
 */
class WordCount {
    // create the word and its count
    private String word;
    private int count;
    
    /**
     * Constructs an empty wordCount object
     */
    public WordCount() {
        this("", 0);
    }
    
    /**
     * Constructs a wordCount object with a given word
     */
    public WordCount(String word) {
        this(word, 0);
    }
    
    /**
     * Constructs a wordCount object with a given word and its count.
     */
    public WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }
    
    /**
     * Gets the word stored by this WordCount
     */
    public String getWord() {
        return word;
    }

    /** 
     * Gets the count stored by this WordCount
     */
    public int getCount() {
        return count;
    }
}