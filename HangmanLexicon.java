/* The program reads a much larger word list from a file to implement in Hangman. 
 */
import acm.util.*;

import java.io.*;
import java.util.*;

public class HangmanLexicon {
	
    
	//Returns how many words in the word bank.
	public static int getWordCount() {	
		return (wordBank.size());
	}
	
	//Returns a single word. 
	public String getWord(int i) {
		return(wordBank.get(i));
	}
	
	public HangmanLexicon() {
		
		//Puts the words on the file into an array list.
		try {
			//Opens the file
			BufferedReader hangmanWords = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			
			//Reads the file
			while(true) {
				String line = hangmanWords.readLine();
				if(line == null) break;
				wordBank.add(line);
			}
			//Closes the file
			hangmanWords.close();
			
		} catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	//An array that holds all the word strings.
	private static ArrayList <String> wordBank = new ArrayList <String> ();
}
