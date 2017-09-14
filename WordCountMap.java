import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;
import java.util.Stack;
/**
 * This class uses a tree structure to store words and their counts in 
 * a given input
 *
 * @author: Yitong Chen
 * @reference: Anna Rafferty
 */
public class WordCountMap {
    /** Creates a root node of the tree*/
    private Node root;
    
    /**
     * Constructs an empty WordCountMap.
     * Initializes the root node.
     */         
    public WordCountMap() {
        root = new Node();
    }

    /**
     * Adds 1 to the existing count for word, or adds word to the WordCountMap
     * with a count of 1 if it was not already present. Adding an empty string is not
     * allowed.
     * @param the new word
     */
    public void incrementCount(String word) {
        if (word.length() > 0) {
            // create a list of all the characters in the given word
            List<String> wordList = transformStringToList(word);
            incrementCount(wordList, root);
        }
    }

    /**
     * Adds 1 to the existing count for word, or adds word to the WordCountMap
     * with a count of 1 if it was not already present.
     * @param a list of the characters in the target word
     * @rootNode the rootNode of the current sub-tree
     */
    private void incrementCount(List<String> word, Node rootNode) {
        
        if (!word.isEmpty()) {
            // get the children of the current root node
            List<Node> children = rootNode.children;
            
            // if the current root node is a leaf, the given word
            // is not in the list and thus we add the word to the list
            if (children.isEmpty()) {
                createNewWord(word, rootNode);
            } else {
                String character = word.remove(0);
                Node newRoot = null;
                
                for (Node child : children) {
                    if (child.data.getWord().equals(character)) {
                        newRoot = child;
                    }
                }
                
                if (newRoot == null) {
                    word.add(0, character);
                    createNewWord(word, rootNode);
                } else {
                    incrementCount(word, newRoot);
                }
            }
        } else {
            rootNode.data.incrementCount();
        }
    }
    
    /**
     * Adds a new word to the list with a count 1;
     * @param a list of the remaining characters of the new word
     * @param the current parent node
     */
    private void createNewWord(List<String> word, Node rootNode) {
        if (!word.isEmpty()) {
            String curWord = word.remove(0);
            // creates a new node with the next character in the word
            Node newNode = new Node(curWord);
            // adds the new node as a children of the current parent node
            rootNode.children.add(newNode);
            // keep calling createword until all the remaining characters 
            // has been added to the tree
            createNewWord(word, newNode);
        } else {
            // the word has been added to the tree
            // increment the count of the word by one
            rootNode.data.incrementCount();
        }
    }
    
    /**
     * Remove 1 to the existing count for word. If word is not present, does
     * nothing. If word is present and this decreases its count to 0, removes
     * any nodes in the tree that are no longer necessary to represent the
     * remaining words.
     */
    public void decrementCount(String word) {
        boolean contains = contains(word);
        
        // Process the word if it is in the tree
        if (contains) {
            // create two queues that contain all the characters of the word
            Queue<String> wordQueue = transformStringToQueue(word);
            Queue<String> wordQueue1 = transformStringToQueue(word);
            
            // call the helper method, get the count ater decrementation
            int afterDecrementation = decrementCount(wordQueue, root);
            
            // remove the unused nodes
            if (afterDecrementation == 0) {
                removeUnused(wordQueue1, root);
            } 
        }
    }


    /**
     * Remove 1 to the existing count for a word.
     * @param the queue of all the characters in the word
     * @param the rootnode of the current subtree
     * @return the count of the target word after decrementation
     */
    private int decrementCount(Queue<String> wordQueue, Node rootNode) {
        // keep iterating until reaching the last character
        if (!wordQueue.isEmpty()) {
            // get the children of the current rootnode
            List<Node> children = rootNode.children;
            
            Node targetNode= null;
            
            String word = wordQueue.remove();
            
            // find the node of the next character
            for (Node child : children) {
                if (child.data.getWord().equals(word)) {
                    targetNode = child;
                }
            }
            // targetNode is guaranteed to be not null at this point
            // because there is we know this target word is stored in the map
            return decrementCount(wordQueue, targetNode);
        } else {
            // decrement count on the node of the last character
            rootNode.data.decrementCount();
            return rootNode.data.getCount();
        }
    }
    
    /**
     * Removes the unused nodes
     * @param a list of all the characters in the target word
     * @param the rootnode of the current subtree
     */
    private void removeUnused(Queue<String> wordList, Node rootNode) {
        // keep iterating until reaching the last character
        if (!rootNode.children.isEmpty()) {
    
            List<Node> children = rootNode.children;
            
            Node targetNode = null;
            
            String word = wordList.remove();
            
            // find the node of the next character 
            for (Node child : children) {
                if (child.data.getWord().equals(word)) {
                    targetNode = child;
                }
            }
            // targetNode is guaranteed to be not null at this point
            // because there is we know this target word is stored in the map
            removeUnused(wordList, targetNode);
            
            // going from bottom-up, clearing the children of each unused nodes
            if (!isUseful(rootNode)) {
                rootNode.children.clear();
            }
        } 
    }
    
    /**
     * Tells whether the node is still useful by looking at its descendants
     * @param the targetNode
     * @return true if the node is still useful
     *         false if the node is no longer useful
     */
    private boolean isUseful(Node rootNode) {
        // keep iterating until reaching a leaf
        if (!rootNode.children.isEmpty()) {
            boolean result = false;
            for (Node child : rootNode.children) {
                // if there is one useful child, then the node is useful
                if (child.data.getCount() > 0) {
                    result = true;
                } else {
                    result = isUseful(child);
                }
            }
            return result;
        } else {
            // if reaching a zero-count leaf, the node is no longer useful
            if (rootNode.data.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }
    
    /**
     * Returns true if word is stored in this WordCountMap with
     * a count greater than 0, and false otherwise.
     */
    public boolean contains(String word) {
        int count = getCount(word);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the count of word, or -1 if word is not in the WordCountMap.
     */
    public int getCount(String word) {
        // create a queue to store all the characters in the given word
        Queue<String> wordList = transformStringToQueue(word);
        
        int result = getCount(wordList, root);
        
        return result;
        
    }
    
    /**
     * returns the count of word, or -1 if word is not in the WordCountMap
     */
    private int getCount(Queue<String> wordList, Node rootNode) {
        if (wordList.isEmpty()) {
            // return the count of the last character
            return rootNode.data.getCount();
        } else {
        	List<Node> children = rootNode.children;
            
            // still have remaining characters but we have reached a 
            // leaf node, so the word is not in the map
            if (children.isEmpty()) {
                return -1;
            } else {
                String target = wordList.remove();
            	Node nextRoot = null;
            	// finds the node of the next character
                for (Node child : children) {
                    if (child.data.getWord().equals(target)) {
                    	nextRoot = child;
                    }
                }
                // if cannot find the node of the next character
                // the word is not in the map
                if (nextRoot != null) {
                	return getCount(wordList, nextRoot);
                } else {
                	return -1;
                }
            }
        }
    }
    
    /**
     * Retrieves each character of a string and make it into a queue
     * @param the word string
     * @return an queue of characters in the string
     */
    private Queue<String> transformStringToQueue(String word) {
        Queue<String> result = new LinkedList<String>();
        for (int i = 0; i < word.length(); i ++) {
            result.add(word.substring(i, i + 1));
        }
        return result;
    }
    
    /**
     * Retrieves each character of a string and make it into a list
     * @param the word string
     * @return a list of characters in the string
     */
    private List<String> transformStringToList(String word) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < word.length(); i ++) {
            result.add(word.substring(i, i + 1));
        }
        return result;
    }

    /** 
     * Returns a list of WordCount objects, one per word stored in this 
     * WordCountMap, sorted in decreasing order by count. 
     */
    public List<WordCount> getWordCountsByCount() {
        List<WordCount> words = new ArrayList<WordCount>();
        getWordCounts(words, root, root.data.getWord());
        sortList(words);
        return words;
    }
    
    /**
     * Uses quick sort to sort the word count list in descending order
     * Sorting the list in place
     * @param the list to be sorted
     */
    private void sortList(List<WordCount> wordList) {
        // create an array with all the wordCounts
        WordCount[] wordArray = wordList.toArray(new WordCount[wordList.size()]);
        // sort the array
        Quicksort.quicksort(wordArray);
        // put the wordCount objects back into the arraylist
        for (int i = 0; i < wordList.size(); i ++) {
            wordList.set(i, wordArray[i]);
        }
    }
    
    /**
     * Retrieves all the words and their counts in the map
     * Creates a WordCount object for each word and adds to the result list
     * @param the result list of all the wordCount objects
     * @param the root node of the current subtree
     * @param the word string at this point of the traversal
     */
    private void getWordCounts(List<WordCount> resultList, Node rootNode, String curString) {
        if (!rootNode.children.isEmpty()) {
            if (rootNode.data.getCount() != 0) {
                int count = rootNode.data.getCount();
                String word = curString + rootNode.data.getWord();
                WordCount wordCount = new WordCount(word, count);
                resultList.add(wordCount);
            } 
            
            List<Node> children = rootNode.children;
                
            for (Node child : children) {
                getWordCounts(resultList, child, curString + rootNode.data.getWord());
            }
        } else {
            int count = rootNode.data.getCount();
            String word = curString + rootNode.data.getWord();
            WordCount wordCount = new WordCount(word, count);
            resultList.add(wordCount);
        }
    }

    /** 
     * Returns a count of the total number of nodes in the tree.
     * A tree with only a root is a tree with one node; a tree that represents 
     * no words has 1 node (the root).
     */
    public int getNodeCount() {
        // increment by 1 because the root itself is not counted
        int count = getNodeCount(root) + 1;
        return count;
     }
    
    /**
     * Returns a count of the number of nodes in the tree
     * rooted at the given node.
     */
    private int getNodeCount(Node rootNode) {
        // keep iterating until reaching a leaf
        if (!rootNode.children.isEmpty()) {
            List<Node> children = rootNode.children;
            
            int sum = 0;
            
            // counting in the nodes in each subtree of the root node
            for (Node child : children) {
                sum = sum + 1 + getNodeCount(child);
            }
            
            return sum;
        } else {
            return 0;
        }
    }
    
    /**
     * Display each word and its count stored in the map
     */
    private static void display(List<WordCount> list) {
        for (WordCount word : list) {
            System.out.println(word.getWord() + ": " + word.getCount());
        }
    }
    
    /**
     * The node class stores both a CharCount data and a list 
     * of the children nodes of the node
     */
    private class Node {
        private CharCount data;
        private List<Node> children;
        
        /** Create a default node object */
        private Node() {
            this("", null);
        }
        
        /** Create a node with a given word to store in it*/
        private Node(String word) {
            this(word, null);
        }
        
        /** Create a node with a given word and a list of child nodes*/
        private Node(String word, List<Node> children) {
            this.data = new CharCount(word);
            if (children == null) {
                this.children = new ArrayList<Node>();
            } else {
                this.children = children;
            }
        }
        
        /** Overrides the toString() method so that the node can be printed */
        public String toString() {
            String result = data.getWord() + ": " + data.getCount();
            return result;
        }
    }
    
    /** 
     * The charCount class stores a character and a count
     * The count is always zero for a character that is not the last
     * character of a word
     */
    private class CharCount {
        // a character in a word
        private String word;
        // the count associated with this character
        private int count;

        /**
         * Constructs an empty CharCount object
         */
        private CharCount() {
            this("", 0);
        }

        /**
         * Constructs a CharCount object with the given character
         */
        private CharCount(String word) {
            this(word, 0);
        }

        /**
         * Constructs a CharCount object with the given character and count
         */
        private CharCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        /**
         * Gets the word stored by this WordCount
         */
        private String getWord() {
            return word;
        }

        /** 
         * Gets the count stored by this WordCount
         */
        private int getCount() {
            return count;
        }
        
        /** increments the count by 1 */
        private void incrementCount() {
            count ++;
        }

        /** decrements the count by 1 */
        private void decrementCount() {
            count --;
        }
    }

    /** the main method tests each of the methods above*/
    public static void main(String[] args) {
        
        System.out.println("Constructing a new WordCountMap...... ");
        
        WordCountMap newMap = new WordCountMap();
        
        // Tests the methods on an empty WordCountMap
        System.out.println("The WordCountMap should be empty now.");
        System.out.println("Calling decrementCount() should not do anything.");
        newMap.decrementCount("Yitong");
        System.out.println("The map should not contain the string 'Yitong' "
                           + "and the contains method returns: " +
                           newMap.contains("Yitong"));
        System.out.println("Getting the count of a word from an empty list returns: "
                          + newMap.getCount("Yitong"));
        System.out.println("The map should contain a single root node "
                           + "and getNodeCount() returns: " + 
                           newMap.getNodeCount());
        System.out.println("-------------------------------");
        
        // Tests adding a new word
        String word1 = "Yitong";
        System.out.println("Adding the word 'Yitong' to the WordCountMap......");
        newMap.incrementCount(word1);
        System.out.println("The map should contain the string 'Yitong' and " 
                           + "the contains method returns: " +
                           newMap.contains("Yitong"));
        System.out.println("Getting the count of 'Yitong' returns: "
                          + newMap.getCount("Yitong"));
        System.out.println("The map should contain 7 nodes and getNodeCount() returns: " 
                           + newMap.getNodeCount());
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
        System.out.println("-------------------------------");
        
        // Tests adding the same word one more time
        String word2 = "Yitong";
        System.out.println("Adding another 'Yitong' to the WordCountMap......");
        newMap.incrementCount(word2);
        System.out.println("Getting the count of 'Yitong' returns: "
                          + newMap.getCount("Yitong"));
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
        System.out.println("-------------------------------");
        
        // Tests adding a different word to the list
        String word3 = "Shaocheng";
        System.out.println("Adding 'Shaocheng' to the WordCountMap......");
        newMap.incrementCount(word3);
        System.out.println("The map should still contain 16 nodes and getNodeCount() returns: " 
                           + newMap.getNodeCount());
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
        System.out.println("-------------------------------");
        
        // Tests adding a word that shares some nodes with previous words
        String word4 = "YitongChen";
        System.out.println("Adding 'YitongChen' to the WordCountMap......");
        newMap.incrementCount(word4);
        System.out.println("Getting the count of 'YitongChen' returns: "
                          + newMap.getCount("YitongChen"));
        System.out.println("The map should contain 20 nodes and getNodeCount() returns: " 
                           + newMap.getNodeCount());
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
        System.out.println("-------------------------------");
        
        // Tests removing a word with a single count from the tree
        // Unused nodes should be removed
        System.out.println("Removing 'YitongChen' from the WordCountMap......");
        newMap.decrementCount("YitongChen");
        System.out.println("Getting the count of 'YitonChen' returns: "
                          + newMap.getCount("YitongChen"));
        System.out.println("The map should no longer contains 'YitongChen' and contains() returns: " + 
                          newMap.contains("YitongChen"));
        System.out.println("Unused nodes should have been removed and getNodeCount() returns: " 
                           + newMap.getNodeCount());
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());    
        System.out.println("-------------------------------");
        
        // Tests removing a word with more than 1 count
        System.out.println("Removing 'Yitong' from the WordCountMap......");
        newMap.decrementCount("Yitong");
        System.out.println("Getting the count of 'Yitong' returns: "
                          + newMap.getCount("Yitong"));
        System.out.println("No nodes should have been removed and getNodeCount() returns: " 
                           + newMap.getNodeCount());
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
        System.out.println("-------------------------------");
        
        // Tests adding an empty string 
        String word5 = "";
        System.out.println("Adding empty string to the WordCountMap......");
        newMap.incrementCount(word5);
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
        System.out.println("-------------------------------");
        
        // Tests adding a non-capticalized word
        String word6 = "yitong";
        System.out.println("Adding another non-capitalized 'yitong' to the WordCountMap......");
        newMap.incrementCount(word6);
        System.out.println("The map should differentiate be case-sensitive.");
        System.out.println("Calling getWordCountsByCount() returns: ");
        display(newMap.getWordCountsByCount());
    }
}