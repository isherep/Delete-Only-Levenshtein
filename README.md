# Modified-Levenshtein-Distance-Delete-Only
## Modified Levenshtein distance algorithms, to match strings by deletion and capitalization only and does not allow replacement or insertion of characters ##


After working on the spellchecking application, I learned that Levenhstein distance does not return the correct word in many cases. For example _"applllleee"_ will return _"appelle"_ instead of _"apple"_ If the word contains multiple invalid duplications of repeated characters, the Levenshtein distance algorithm will return the closest matching word, but it might be not the correct word we are looking for. It will return the word with replaced or inserted characters instead of just removed duplicated ones. Also the same happens when the word has one miscapitalized letter, if the cost of inserting different or replacing it is the smallest, it will replace it with a whole different character or delete it.

For [DeleteOnlyLevenshtein](https://github.com/isherep/Delete-Only-Levenshtein-Distance/blob/master/modifiedLevenshtein.java) method `deleteOnlyLevenshtein`

For [Delete And Replace Only Wrong Casing Levenshtein](https://github.com/isherep/Delete-Only-Levenshtein-Distance/blob/master/modifiedLevenshtein.java) method `deleteAndReplaceWrongCasing`

I modified the Levenshtein algorithm to handle these problems.

The idea is the following: Edit Distance algorithm uses the smallest edit distance between two strings to perform correction. 
If we want to allow the only deletion - then we have to make sure it is always the minimum of all choices
Assign high values to other costs so they will never be selected

1. Compare two strings:
matching if only the case mismatched character casing needs to be replaced
                              
2. Compute cost for each transformation, but instead of using real costs of insertion and replacement -  assigning higher costs to insertion and substitution, unless it's replacing of miscapitalized character.
If characters have only case mismatch - allow the original cost of replacement to the correct casing.

3. If the characters are not duplicated - assign a high value to deletion so it's not selected.

If you found this code helpful, please give a star :star:  


                                        
