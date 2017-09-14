import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
/**
 * This class counts the words in a text file and outputs them in some 
 * format as required by the user in the command line
 */
public class WordCounter {
    // the list that stores the non-stop words in the file
    List<String> wordList;
    // the list that stores all the words in the file
    List<String> allWordList;
    // the list that store all the stop words
    List<String> stopWordList;
    // the list that stores the WordCount of the non-stop words
    List<WordCount> wordCountList;
    // the list that stores the WordCount of all the words in the file
    List<WordCount> allWordCountList;
    
    /** default constructor */
    public WordCounter() {
        // initialize all the instance variables
        allWordList = new ArrayList<String>();
        wordList = new ArrayList<String>();
        stopWordList = new ArrayList<String>();
        wordCountList = new ArrayList<WordCount>();
        allWordCountList = new ArrayList<WordCount>();
    }
    
    /** 
     * Reads through the given file and takes the words of the file
     * one by one, adding them to the corresponding list
     */
    public void load(String filePath) {
        
        // create a file with the given file path
        File wordFile = new File(filePath);
        // create a file with the stop words
        // assuming the stopword file exists in the same directory as the class
        File stopWordFile = new File("StopWords.txt");
        Scanner scanner1 = null;
        Scanner scanner2 = null;
        
        try {
            scanner1 = new Scanner(wordFile);
            scanner2 = new Scanner(stopWordFile);
        } catch (FileNotFoundException e) {
            System.out.println("File cannot be found!");
        }
        
        // reads through the stop words file and add each stop word into the 
        // stop word list 
        while (scanner2.hasNext()) {
            String curWord = scanner2.next();
            stopWordList.add(curWord);
        }
        
        // reads through the given text file
        while (scanner1.hasNext()) {
            String curWord = scanner1.next().toLowerCase();
            // normalize the word
            curWord = format(curWord);
            // add the word to the all word list
            allWordList.add(curWord);
            // if the word is not a stop word, adds it to the
            // non-stop word list
            if (!stopWordList.contains(curWord)) {
                wordList.add(curWord);
            }
        }
    }
    
    /** 
     * normalize a string
     */
    private String format(String originalWord) {
        boolean done = false;
        
        String resultWord = originalWord;
        
        // keep looping until all the special characters at
        // the two ends of the string are removed
        while (!done) {
            // keep a record of the length of the word before formatting
            int lengthBefore = resultWord.length();
            // remove the first and last special character if there is any
            resultWord = resultWord.replaceAll("\\W$|^\\W", "");
            // if the format didn't do anything to the word,
            // the word should be already formatted so we are done
            if (resultWord.length() == lengthBefore) {
                done = true;
            }
        }
        
        return resultWord;
    }
    
    /**
     * create word count maps and adds the words into them
     */
    public void processData() {
        // create a map that stores the non-stop words
        // and a map that stores all the words
        WordCountMap wordCountMap = new WordCountMap();
        WordCountMap allWordCountMap = new WordCountMap();
        
        // adding the non-stop words into the non-stop word map
        for (String word : wordList) {
            wordCountMap.incrementCount(word);
        }
        
        // adding all the words into the all word map
        for (String word : allWordList) {
            allWordCountMap.incrementCount(word);
        }
        
        // getting the WordCountList from both maps
        wordCountList = wordCountMap.getWordCountsByCount();
        allWordCountList = allWordCountMap.getWordCountsByCount();
    }
    
    /**
     * Displays the Word and its count to the user
     */
    public void display() {
        for (WordCount word : wordCountList) {
            System.out.println(word.getWord() + ":" + word.getCount());
        }
    }
    
    /**
     * Retrieves the wordCount list for the non-stop words
     */
    public List<WordCount> getWordCountList() {
        return wordCountList;
    }
    
    /**
     * Retrieves the wordCount list for all the words
     */
    public List<WordCount> getAllWordCountList() {
        return allWordCountList;
    }
    
    /**
     * Write the given content to a file with the given name
     */
    public void writeToFile(String content, String fileName) {
        
        PrintWriter toFile = null;
        
        try {
            toFile = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("PrintWriter error opening the file " + fileName);
            System.out.println(e.getMessage());
        }
        
        if (toFile != null) {
            toFile.println(content);
        }
        // close the file after finnishing writing
        toFile.close();
    }
    
    /**
     * Reads from command line the name of the file with the words to be counted
     * and how the user want the word count map to be displayed
     * Displays the word count map as user required
     */
    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.out.println("Need more command line arguments!");
        } else {
            if (args.length == 2) {
                String filepath = args[1];
                // displays the word and its count with text
                if (args[0].equals("byCount")) {
                    WordCounter counter = new WordCounter();
            
                    counter.load(filepath);
                    counter.processData();
                    counter.display();
                }
            } else {
                // displays the word with a word cloud
                if (args[0].equals("cloud")) {
                    String inputfileName = args[1];
                    int numWordsToInclude = 0;
                    try {
                        numWordsToInclude = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Please give a number of words to include");
                    }
                    
                    String outputfileName = args[3];

                    WordCounter counter = new WordCounter();

                    counter.load(inputfileName);
                    counter.processData();
                    
                    int wordcountSize = counter.getWordCountList().size();
                    String html = "";
                    
                    // if the text file contains fewer non-stop words than the given
                    // number to include, include all words in the word cloud
                    if (numWordsToInclude > wordcountSize) {
                        html = WordCloudMaker.getWordCloudHTML(inputfileName, counter.allWordCountList);
                    } else {
                        // otherwise, only include the top given number of non-stop words
                        html = WordCloudMaker.getWordCloudHTML(inputfileName, counter.getWordCountList().subList(0, numWordsToInclude));
                    }

                    counter.writeToFile(html, outputfileName);
                }
            }
        }
    }
}