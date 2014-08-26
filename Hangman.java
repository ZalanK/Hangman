/* Implementation of Hangman
 * 
 * Created by Zalan Khan, Finished July 5, 2014
 * 
 * Assignment # 4 of Stanford CS 106A.
 * 
 * Starter file and some logic used from hand-out.
 */
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram{

/** Number of guesses to uncover word */	
	public static int GUESSES_LEFT = 8;
	
	public void init() {
		setSize(650, 900);
		canvas = new HangmanCanvas();
		add(canvas);
	}
	
	public void run() {
		setup();
		play();
	}
	
	public void setup() {
		canvas.reset(); //The scaffold is added to the canvas.
		welcomeMessage();
	}
	
	private void welcomeMessage() {
		println("Welcome to Hangman!");
		println("Your word now looks like this: "+dashedWord+" ");
		println("You have "+GUESSES_LEFT+" guesses left.");
	}
	
	/* The word chosen from the library becomes hidden.
	 * The number of characters in the word becomes the number of dashes.
	 */
	private String getHiddenWord() {
		
		/* Temporary string class that starts off with nothing inside.
		 * Depending on the word length, this method will get the appropriate
		 * number of dashes to hide the word.
		 */
		String str = ""; 
	
		for(int i = 0; i < word.length(); i++) {
			str = str + "-";
		}
		return(str);
	}
	
	private void play() {
		while(GUESSES_LEFT > 0) {
			enterGuess();
			checkIfGuessMatches();
			if(dashedWord.equals(word))break; //The user has guessed the correct word.
		}	
		displayOutcome(); // Displays win or lose message.
	}
	
	//This method will ask for an appropriate letter and format.
	private void enterGuess() {
		String str = readLine("Your guess: "); //This is a string that will contain one character
		
		if(str.length() != 1) { //This will check if one letter was entered.
			println("Invalid, enter one letter.");
			str = readLine("Your guess: ");
		}

		/* Remember the string and char have different properties!
		 * Changes the letter entered to a char type since we are going to be
		 * comparing each character one by one. The string can also hold one 
		 * character but has properties more difficult for comparison.
		 */
		guessedLetter = str.charAt(0); 
		
		/* The lexicon only displays upper case letters, so if the user enters
		 * a guess with a lower case, we must change that word to upper case.
		 */
		guessedLetter = Character.toUpperCase(guessedLetter);
	}

	private void checkIfGuessMatches() {
		int letterCounter = 0; //Used to display correct or incorrect guess messages.
		
		/* The for loop will check to see if the guessed letter corresponds to
		 * the word.
		 * Since strings are immutable, we need to create a new string which
		 * is a combination of the guessed letters and the dashed word.
		 */
		
		for (int i = 0; i < word.length(); i++) {
			
			if(guessedLetter == word.charAt(i)) {
				
				if(i == 0) {
					/* If the guessed letter is the first character in the word, 
					 * all we need to do is tack on the guessed letter and then
					 * add the dashed lines.
					 */
					dashedWord = guessedLetter + dashedWord.substring(1);
				}
				if(i > 0) {
					/* If the guessed letter is the anything but the first letter
					 * the general format is to add string before, the guessed letter
					 * where specified and the after string.
					 */
					dashedWord = dashedWord.substring(0, i) + guessedLetter 
							+ dashedWord.substring(i+1);
				}
				letterCounter = letterCounter + 1; //Correct guess, thus add one.
			}
		}
		
		//The guess was correct, display correct message.
		if(letterCounter > 0) { 
			displayCorrectMessage();
			
			canvas.displayWord(dashedWord);  //Displays the dashed word on the canvas
		}
		
		//The guess was incorrect, display incorrect message and remove from guess counter.
		else { 
			GUESSES_LEFT = GUESSES_LEFT - 1;
			displayIncorrectMessage();
			
			canvas.noteIncorrectGuess(guessedLetter); //Displays all the incorrect letter guesses to the canvas
			canvas.displayWord(dashedWord); //Displays the dashed word on the canvas
		}
	}
		
	private void displayCorrectMessage() {
		println("That guess is correct.");
		println("Your word looks like this: "+dashedWord+"");
		println("You have "+GUESSES_LEFT+" guesses left.");
	}
	
	private void displayIncorrectMessage() {
		println("There are no "+guessedLetter+"'s in the word.");
		println("Your word looks like this: "+dashedWord+"");
		println("You have "+GUESSES_LEFT+" guesses left.");
	}
	
	private void displayOutcome() {
		if(dashedWord.equals(word)){
			println("You guessed the word: "+word+"");
			println("YOU WIN!");
			canvas.displayWin(); //Displays you win on graphics canvas
		}
		if(GUESSES_LEFT == 0) {
			println("You're completely hung.");
			println("The word was: "+word+"");
			println("YOU LOSE!");
			canvas.displayLose(); //Displays you lose on graphics canvas
		}
	}
	
	
	
	/* A lexicon is a collection of words used for the purpose of picking
	 * out words for the Hangman program.
	 */
	private HangmanLexicon hangmanWords = new HangmanLexicon();
	
	/* A random word needs to be picked from the lexicon. */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Initializing actualWord to get random word from lexicon. */
		private String word = 
				hangmanWords.getWord(rgen.nextInt(HangmanLexicon.getWordCount()));
	
	/* The string that is hidden which the user has to guess. 
	 * It will begin to display itself after correct guesses.
	 */
	private String dashedWord = getHiddenWord();
	
	/* This is the letter the user inputs. */
	private char guessedLetter;
	
	/*  Individual canvas object. */
	private HangmanCanvas canvas;
}
