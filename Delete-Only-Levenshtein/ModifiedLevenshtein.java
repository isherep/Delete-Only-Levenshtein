
/**
 * Write a description of class ModifiedLevenshtein here.
 *
 * @author Iryna Sherepot
 * @version 3/16/18
 */
public class ModifiedLevenshtein{
    
    /**
     * Levenshtein algorithm returns the minimum edit distance between two words. 
     * Implements modification of Levenshtein distance to match strings by deletion only
     * 
     * Used when the string contains just invalid duplicated characters.

     * Minimum edit distance -  is the minimum number of single-character edits (i.e., insertions, deletions or substitutions) 
     * required to change one word into the other.
     * 
     * Original Levenshtein code has a cost of replacement, insertion, and deletion as 0 and 1.
     * Modified Levenshtein assigns the high values to the insertion and replacement,
     * If the characters are repeating, allow deletion - otherwise disallow.
     * This ensures that only deletion to be the minimal distance option of edit
     * 
     * This allows finding the word that matches given word including only deletions.
     * 
     * @param lhs the character sequence matched against 
     * @param rhs the character sequence we are matching
     * 
     * @return the minimal edit distance to turn sequence lrs into sequence rhs
     */
    public static int deleteOnlyLevenshtein(CharSequence lhs, CharSequence rhs){

        final int DISALLOW = 1000000;
        int len0 = lhs.length() + 1;                                                     
        int len1 = rhs.length() + 1;                                                     

        // the array of distances                                                       
        int[] cost = new int[len0];                                                     
        int[] newcost = new int[len0];                                                  
        char last = 0;
        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0 - 1; i++) {
            cost[i] = i; 
        }
        // dynamically computing the array of distances                                  
        // transformation cost for each letter in s1                                    
        for (int j = 1; j < len1; j++) {                                                
            // initial cost of skipping prefix in String s1                             
            newcost[0] = j;                                                             
            // transformation cost for each letter in s0                                
            for(int i = 1; i < len0; i++) {
                // matching if the two characters are trepeating                           
                boolean repeat = lhs.charAt(i - 1) == last;            
                // matching current letters in both strings
                boolean match = lhs.charAt(i - 1) == rhs.charAt(j - 1);
               //if characters in both strings match  - pass otherwise dissallow
                int cost_replace =  cost[i - 1] + (match ? 0 : DISALLOW); 
                int cost_insert  = cost[i] + 1 +   DISALLOW;
                //calculation cost of deletion. Id the characters are not duplicatied - assignn high value to disallow deletion
                int cost_delete  = newcost[i - 1] + (repeat ? 1 : DISALLOW);                                  
                // keep minimum cost                                                    
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);

                last = lhs.charAt(i - 1);
            }                                                                           

            // swap cost/newcost arrays                                                 
            int[] swap = cost; cost = newcost; newcost = swap;                          
        }                                                                               

        return cost[len0 - 1];
    }

    /**
     * Implements modification of Levenshtein distance to find a matching string based on deletion and replacing a character 
     * with proper cased character
     * Original Levenshtein algorithm returns the minimum edit distance between two words. 
     * Minimum edit distance -  is the minimum number of single-character edits (i.e., insertions, deletions or substitutions) 
     * required to change one word into the other.
     * 
     * Original Levenshtein code has a cost of replacement, insertion, and deletion as 0 and 1.
     * Modified Levenshtein assigns the high values to the insertion and replacement unless the character case is wrong,
     * than it allows replacement of the character with the same one of a correct case.
     * If the characters are repeating, allow deletion - otherwise disallow.
     * This ensures that only deletion to be the minimal distance option of edit
     * 
     * This allows finding the word that matches given word based on only deletions and replacing miscapitalized characters. 
     * 
     * @param lhs the character sequence matched against 
     * @param rhs the character sequence we are matching
     * 
     * @return the minimal edit distance to turn sequence lrs into sequence rhs
     */

    public static int deleteAndReplaceWrongCasing(CharSequence lhs, CharSequence rhs){

        final int DISALLOW = 1000000;

        int len0 = lhs.length() + 1;                                                     
        int len1 = rhs.length() + 1;                                                     

        // the array of distances                                                       
        int[] cost = new int[len0];                                                     
        int[] newcost = new int[len0];                                                  
        char last = 0;
        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0-1; i++) {
            cost[i] = i; 
        }
        // dynamically computing the array of distances                                  
        // transformation cost for each letter in s1                                    
        for (int j = 1; j < len1; j++) {                                                
            // initial cost of skipping prefix in String s1                             
            newcost[0] = j;                                                             
            // transformation cost for each letter in s0                                
            for(int i = 1; i < len0; i++) {
                // matching if the two characters are repeating                           
                boolean repeat = Character.toLowerCase(lhs.charAt(i - 1)) == Character.toLowerCase(last);            
                // matching current letters in both strings
                boolean match = lhs.charAt(i - 1) == rhs.charAt(j - 1);
                //matching if only the case mismatched character casing needs to be replaced
                boolean caseMismatch = !match && (Character.toLowerCase(lhs.charAt(i - 1)) == Character.toLowerCase(rhs.charAt(j - 1)));               

                //if they characters are different  - assign high cost of insertion to make disallowed, unless its case mismatch
                int cost_replace = cost[i - 1] + (match ? 0 : (caseMismatch ? 1 : DISALLOW)); 
                int cost_insert  = cost[i] + DISALLOW;
                //calculation cost of deletion. Id the characters are not duplicatied - assignn high value to disallow deletion
                int cost_delete  = newcost[i - 1] + (repeat ? 1 : DISALLOW);                                  
                // keep minimum cost                                                    
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);

                last = lhs.charAt(i - 1);
            }                                                                           

            // swap cost/newcost arrays                                                 
            int[] swap = cost; cost = newcost; newcost = swap;                          
        }

        return cost[len0 - 1];
    }

   
}
