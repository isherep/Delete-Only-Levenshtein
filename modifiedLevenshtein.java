     /* This allows finding the word that matches given word including only deletions. 
     *
     * Words like "AaaaAApppppLLLLLeeee" will be corrected to "apple".
     * 
     * @param lhs the character sequence matched against 
     * @param rhs the character sequence we are matching
     * 
     * @return the minimal edit distance to turn sequence lrs into sequence rhs
     */
    public int modifiedLevenshteinDistance (CharSequence lhs, CharSequence rhs) {
     
         // a large number to assign to dissallowed actions like insert and replace
        static final int DISALLOW = 1000000;
                                  
        int len0 = lhs.length() + 1;                                                     
        int len1 = rhs.length() + 1;                                                     

        // the array of distances                                                       
        int[] cost = new int[len0];                                                     
        int[] newcost = new int[len0];                                                  
        char last = 0;
        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0-1; i++) cost[i] = i;                                     
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
                // computing cost for each transformation. assigning higher costs to insertion and replacement other that replacing casing 
                // cost of replacemnt: if the characters have only case mismatch - allow original replace cost,
                //if they characters are different  - assign high cost of insertion to make disallowed.
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

        // the distance is the cost for transforming all letters in both strings        
        return cost[len0 - 1];                                                          
    }