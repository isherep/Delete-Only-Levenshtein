
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.lang.*;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class OriginalLevenshtein{

/**
 * The SpellChecker behaves as a spell checking program using the text file dictionary with the correct spelling of words.
 * 
 * It will perform two types of corrections on the incoming word and then return the corrected word:
 * It fixes bad casing: england” → “England”
 * It removes invalid repeating characters: “tabble” → “table”
 * If the incoming word is already correct, the function should return the original word.
 * If no correction can be found,  the string “No Correction Found” is returned.
 * 
 * Assumes that any number of character duplication can be made, duplication can occur more than once, 
 * bad casing can occur more than one time in the word at any letter, word can have both casing and duplication errors at the same time
 * If the two possible correct spellings of the word is possible, the function picks first one.
 * 
 * SpellChecker corrects the word in a floowing way. 
 * It removes capitalization and any consecutive character duplication from the dictioaries words, puts them in the map as as keys. 
 * Removing capitalization and any consecutive character duplication from the misspelled word. 
 * that allows to select only the words from the dictionary that have the same sequence of characters as the misspelled word.
 * 
 * Each key of of map will have a value a set of possible permutations of the key word according to dictionary.
 * 
 * Using the smallest edit distance(modified Levenhstein algorithm) the function matches each permutation of the key to the misspelled word.
 * The smallest edit distance is the correct spell of the word. 
 *
 * 
 * @author Iryna Sherepot
 * @version 03/14/2019
 */

    // distance for disallowed character manipulation - insert, replace other thatn lower/uppercase
    //private static final int DISALLOW = 1000000; 
    // a multimap for a dictionary. Keys - words with no any duplication
    // map value - a set of possible words in a dictionary that can be made from a key. 
    private Map<String, Set<String>> dictionary;   

    /**
     * Constructs a MultiMap representing dictionary of words from the given file.
     * 
     * The keys in a map are dictionary words with no repeated characters and lowercased.
     * The values in a mp are the list of words that can be made of the key.
     * 
     * @param file the dictionary file of correct words
     */
    public OriginalLevenshtein(String file) throws IOException {
        this.dictionary= new TreeMap();        
        try(BufferedReader reader = new BufferedReader(new FileReader("words.txt"))){

            String current;
            //if(reader.ready()){                       
            while((current = reader.readLine())!=null){

                String canonicCurrent = makeCanonical(current);
                Set<String> candidates;
                if (!dictionary.containsKey(canonicCurrent)) {  // first time we've seen this string
                    // map add the word and create a new set for it               
                    candidates = new HashSet<String>();
                    dictionary.put(canonicCurrent, candidates);
                    //add candidates to the set
                }else {
                    //add to the existing set
                    candidates = dictionary.get(canonicCurrent);
                }
                candidates.add(current);                           
            }

        } 
    }

    /**
     * The function behaves as follows:
     * Performs two types of corrections on the incoming word and then return the corrected word:
     * It fixes bad casing: “england” → “England”
     * It removes invalid repeating characters:“tabble” → “table”
     * If the incoming word is already correct, the function returns the original word.
     * If no correction can be found, the function returns the string “No Correction Found”.
     * 
     * @param wordToCheck the word to be spell checked for bad casing and invalid character duplication
     * 
     * @return the corrected word if the given word was misspelled;  the original word the incoming word is already correct; "No Corrections Found" if no correct word is found
     */
    public String checkWord(String wordToCheck ){
        //creating default set, to handle the case when there is no key in the map
        Set<String> noCorr = new HashSet();        
        noCorr.add("No Correction Found");
        
        Set<String> candidates = dictionary.getOrDefault(makeCanonical(wordToCheck), noCorr);
        String canonizedWord = makeCanonical(wordToCheck);
        String correct = "";

        if(candidates.contains(wordToCheck)){
            return wordToCheck;  
            // else - compare map keys to the if map keys contains the canonized word
        } else if(dictionary.keySet().contains(canonizedWord)){
            // perform two types of corrections on the incoming word and then return the corrected word:
            correct = findClosestMatchingWord(wordToCheck);
            
            if(findClosestMatchingWord(wordToCheck) !=null){
                return correct;
            } //else {
            //correct = "No Correction Found";
            //} 
        }else {
            correct = "No Correction Found";
        }
        return correct;
    }
    

    /**
     * Uses Levenshtein distance modified to allow only deletions to be made in order to match two words.
     * Creates a key of a map that is a word without any duplicated characters.
     * Iterates through the set of possible candidates for that key and selects a word with a minimum Levenshtein distance
     * 
     * @param word the word to be looked the closest edit distance for. 
     * 
     * @return the word with the minimum Levenshtein edit distance - the correct word
     */
    public String findClosestMatchingWord(String word){
        //removes all the duplicated characters from the word
        String canonizedWord =makeCanonical(word);
        //constructs a set of candidate words 
        Set<String> candidates = dictionary.get(canonizedWord);
       // int min = DISALLOW;
        String corrected = null;
        //looks for a word with a minimum distance
        
        for(String candidate : candidates) {
            
                int distance = levenshteinDistance(word, candidate);
                
                //find the word with the minimum distance
                /*if (distance < min) {
                    min = distance;
                    corrected = candidate;*/
                    
            }
            int minDistance = Math.min(distance);
        
        }

        return corrected;
    }

    /**
     * Removed duplicated consecutive characters from the word
     * 
     * @param word the word to have duplication removed
     * 
     * @return the word that doesn't contain any consecutive duplicated characters
     */
    public static String makeCanonical(String word){
        String lowCaseWord = word.toLowerCase();
        StringBuilder sb = new StringBuilder(word.length());
        for(int i = 0; i< lowCaseWord .length(); i++){

            if(i == 0 || lowCaseWord .charAt(i) !=lowCaseWord .charAt(i-1)){
                sb.append(lowCaseWord .charAt(i));
            }

        }
        return sb.toString();
    }

    /**
     * Implements modification of Levenshtein distance.
     * Levenshtein algorithm returns the minimum edit distance between two words. 
     * Minimum edit distance -  is the minimum number of single-character edits (i.e., insertions, deletions or substitutions) 
     * required to change one word into the other.
     * 
     * Original Levenshtein code has a cost of replacement, insertion, and deletion as 0 and 1.
     * Modified Levenshtein assigns the high values to the insertion and replacement,
     * ensuring that only deletion to be the minimal distance option of edit
     * 
     * This allows finding the word that matches given word including only deletions. 
     * 
     * @param lhs the character sequence matched against 
     * @param rhs the character sequence we are matching
     * 
     * @return the minimal edit distance to turn sequence lrs into sequence rhs
     */
       public static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {   
                          
       int len0 = lhs.length() + 1;                                                     
       int len1 = rhs.length() + 1;                                                     
                                                                                       
       // the array of distances                                                       
       int[] cost = new int[len0];                                                     
       int[] newcost = new int[len0];                                                  
                                                                                       
       // initial cost of skipping prefix in String s0                                 
       for (int i = 0; i < len0; i++) cost[i] = i;                                     
                                                                                       
       // dynamically computing the array of distances                                  
                                                                                       
       // transformation cost for each letter in s1                                    
       for (int j = 1; j < len1; j++) {                                                
           // initial cost of skipping prefix in String s1                             
           newcost[0] = j;                                                             
                                                                                       
           // transformation cost for each letter in s0                                
           for(int i = 1; i < len0; i++) {                                             
               // matching current letters in both strings                             
               int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;             
                                                                                       
               // computing cost for each transformation                               
               int cost_replace = cost[i - 1] + match;                                 
               int cost_insert  = cost[i] + 1;                                         
               int cost_delete  = newcost[i - 1] + 1;                                  
                                                                                       
               // keep minimum cost                                                    
               newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
           }                                                                           
                                                                                       
           // swap cost/newcost arrays                                                 
           int[] swap = cost; cost = newcost; newcost = swap;                          
       }                                                                               
                                                                                       
       // the distance is the cost for transforming all letters in both strings        
       return cost[len0 - 1];                                                          
   }
    /**
     * Counts number of lines in the text file.
     * Used to test if all the words form the file were inserted into the data stucture
     * 
     * @param filename the file to be read
     * 
     * @return number of lines in the test file
     */
    public static int countLinesInTextFile(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
   
   
   public static void main(String[] args){
      System.out.println(levenshteinDistance("apple", "apple"));
      
      System.out.println(levenshteinDistance("appppple", "apple"));
      System.out.println(levenshteinDistance("appppple", "axle"));
      System.out.println(levenshteinDistance("apple", "apple"));
      
   }
   
}