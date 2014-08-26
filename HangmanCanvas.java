/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */
import java.awt.Color;

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	
	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 19;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	private int moveX = 0; //Used to update the space between each letter
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		displayScaffold();
	}
	
/**
 * Updates the word on the screen to correspond to the current
 * state of the game. The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		double initialX = (getWidth() / 2); 
		double initialY = (getHeight() / 2) + (2 * BEAM_LENGTH);
		
		GLabel dashedWord = new GLabel(""+word+"", initialX, initialY);
		dashedWord.setFont("Times New Roman-25");
		
		if(getElementAt(initialX, initialY) != null) { //Removes the old label
			remove (getElementAt(initialX, initialY));
		}
		add(dashedWord);
	}
	
/**
 * Updates the display to correspond to an incorrect guess by the
 * user. Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		if(Hangman.GUESSES_LEFT == 7) {
			displayHead();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 6) {
			displayBody();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 5) {
			displayLeftArm();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 4) {
			displayRightArm();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 3) {
			displayLeftLeg();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 2) {
			displayRightLeg();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 1) {
			displayLeftFoot();
			displayIncorrectLetter(letter);
		}
		else if(Hangman.GUESSES_LEFT == 0) {
			displayRightFoot();
			displayIncorrectLetter(letter);
		}
	}
	
	public void displayWin() {
		double initialX = (getWidth() / 2);
		double initialY = (getHeight() / 2) - (2 * BEAM_LENGTH);
		
		GLabel win = new GLabel("YOU WIN!", initialX, initialY);
		win.move(-(win.getWidth()), -(win.getHeight() / 2)); //Centralizes the label
		win.setFont("Times New Roman-20");
		win.setColor(Color.GREEN);
		
		add(win);
	}
	
	public void displayLose() {
		double initialX = (getWidth() / 2);
		double initialY = (getHeight() / 2) - (2 * BEAM_LENGTH);
		
		GLabel lose = new GLabel("YOU LOSE!", initialX, initialY);
		lose.move(-(lose.getWidth()), -(lose.getHeight() / 2)); //Centralizes the label
		lose.setFont("Times New Roman-20");
		lose.setColor(Color.RED);
		
		add(lose);
	}
	
	private void displayScaffold() {
		scaffold = new GCompound();
		putScaffoldTogether();
		add(scaffold); //Adds the scaffold to the actual canvas
	}
	
	/* Adds base, beam and rope to create full scaffold.
	 * Proportions and measurements approximated using display given.
	 * 
	 * IMPORTANT: The reference point for this graphics drawing is the
	 * centerX, centerY which is the bottom of the body.
	 */
	private void putScaffoldTogether() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the scaffold base.
		double baseBottomX = centerX - BEAM_LENGTH; 
		double baseBottomY = centerY + LEG_LENGTH + ARM_OFFSET_FROM_HEAD; //Added Arm_offset to make base larger than the man
		double baseTopX = centerX - BEAM_LENGTH;
		double baseTopY = centerY + LEG_LENGTH - SCAFFOLD_HEIGHT;
		
		//Coordinates of the beam.
		double beamLeftX = centerX - BEAM_LENGTH;
		double beamLeftY = (centerY + LEG_LENGTH - SCAFFOLD_HEIGHT);
		double beamRightX = centerX;
		double beamRightY = (centerY + LEG_LENGTH - SCAFFOLD_HEIGHT);
		
		//Coordinates of the rope.
		double ropeTopX = centerX;
		double ropeTopY = (centerY + LEG_LENGTH - SCAFFOLD_HEIGHT);
		double ropeBottomX = centerX;
		double ropeBottomY =  centerY + LEG_LENGTH - SCAFFOLD_HEIGHT + ROPE_LENGTH;
		
		scaffoldBase = new GLine(baseBottomX, baseBottomY, baseTopX, baseTopY ); 
		scaffold.add(scaffoldBase); //Adds the base to scaffold canvas
		
		beam = new GLine(beamLeftX, beamLeftY, beamRightX, beamRightY);
		scaffold.add(beam); //Adds beam to scaffold canvas
		
		rope = new GLine(ropeTopX, ropeTopY, ropeBottomX, ropeBottomY); 
		scaffold.add(rope); //Adds rope to scaffold canvas
	}
	
	/* Adds the head to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayHead() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the head.
		double headX = centerX;
		double headY = centerY + LEG_LENGTH - SCAFFOLD_HEIGHT + ROPE_LENGTH;
		
		head = new GOval (headX, headY, (HEAD_RADIUS * 2), (HEAD_RADIUS * 2));
		head.move(-HEAD_RADIUS, 0); //The head needs to be centralized, move to the left.
		add(head);
	}
	
	/* Adds the body to the canvas.
	 * Proportions and measurements approximated using display given.
	 * The body length and arm off set from head create the actual body length.
	 */
	private void displayBody() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the body.
		double bodyTopX = centerX;
		double bodyTopY = centerY - BODY_LENGTH - ARM_OFFSET_FROM_HEAD;
 		double bodyBottomX = centerX;
		double bodyBottomY = centerY;
		
		GLine body = new GLine(bodyTopX, bodyTopY, bodyBottomX, bodyBottomY);
		add(body); 
	}
	
	/* Adds the left arm to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayLeftArm() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinate of where the arm is attached with respect to the body 
		double bodyArmX = centerX;
		double bodyArmY = centerY - BODY_LENGTH;
		
		//Coordinates of the left elbow
		double leftElbowX = centerX - UPPER_ARM_LENGTH;
		double leftElbowY = centerY - BODY_LENGTH;
		
		//Coordinates of the left hand
		double leftHandX = centerX - UPPER_ARM_LENGTH;
		double leftHandY = centerY - BODY_LENGTH + LOWER_ARM_LENGTH;
		
		GLine leftLowerArm = new GLine(bodyArmX, bodyArmY, leftElbowX, leftElbowY);
		add(leftLowerArm);
		
		GLine leftUpperArm = new GLine(leftElbowX, leftElbowY, leftHandX, leftHandY);
		add(leftUpperArm);
	}
	
	/* Adds the right arm to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayRightArm() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinate of where the arm is attached with respect to the body 
		double bodyArmX = centerX;
		double bodyArmY = centerY - BODY_LENGTH;
		
		//Coordinates of the right elbow
		double rightElbowX = centerX + UPPER_ARM_LENGTH;
		double rightElbowY = centerY - BODY_LENGTH;
		
		//Coordinates of the right hand
		double rightHandX = centerX + UPPER_ARM_LENGTH;
		double rightHandY = centerY - BODY_LENGTH + LOWER_ARM_LENGTH;
		
		GLine rightUpperArm = new GLine(bodyArmX, bodyArmY, rightElbowX, rightElbowY);
		add(rightUpperArm);
		
		GLine lowerArm = new GLine(rightElbowX, rightElbowY, rightHandX, rightHandY);
		add(lowerArm);
	}
	
	/* Adds the left leg to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayLeftLeg() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the left hip
		double leftHipX = centerX - HIP_WIDTH;
		double leftHipY = centerY;
		
		//Coordinates of the left heel
		double leftHeelX = centerX - HIP_WIDTH;
		double leftHeelY = centerY + LEG_LENGTH;
		
		//Remember that we use the bottom of the body as centerX, centerY.
		GLine leftHip = new GLine(centerX, centerY, leftHipX, leftHipY);
		add(leftHip);
		
		GLine leftLeg = new GLine(leftHipX, leftHipY, leftHeelX, leftHeelY);
		add(leftLeg);
	}

	/* Adds the right leg to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayRightLeg() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the right hip
		double rightHipX = centerX + HIP_WIDTH;
		double rightHipY = centerY;
		
		//Coordinates of the right heel
		double rightHeelX = centerX + HIP_WIDTH;
		double rightHeelY = centerY + LEG_LENGTH;
		
		//Remember that we use the bottom of the body as centerX, centerY.
		GLine rightHip = new GLine(centerX, centerY, rightHipX, rightHipY);
		add(rightHip);
		
		GLine rightLeg = new GLine(rightHipX, rightHipY, rightHeelX, rightHeelY);
		add(rightLeg);
	}
	
	/* Adds the left foot to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayLeftFoot() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the left heel
		double leftHeelX = centerX - HIP_WIDTH;
		double leftHeelY = centerY + LEG_LENGTH;
				
		//Coordinates of the left toes
		double leftToesX =  centerX - HIP_WIDTH - FOOT_LENGTH;
		double leftToesY = centerY + LEG_LENGTH;
		
		GLine leftFoot = new GLine(leftHeelX, leftHeelY, leftToesX, leftToesY);
		add(leftFoot);
	}
	
	/* Adds the right foot to the canvas.
	 * Proportions and measurements approximated using display given.
	 */
	private void displayRightFoot() {
		double centerX = getWidth() / 2;
		double centerY = getHeight() / 2;
		
		//Coordinates of the right heel
		double rightHeelX = centerX + HIP_WIDTH;
		double rightHeelY = centerY + LEG_LENGTH;
				
		//Coordinates of the right toes
		double rightToesX =  centerX + HIP_WIDTH + FOOT_LENGTH;
		double rightToesY = centerY + LEG_LENGTH;
		
		GLine leftFoot = new GLine(rightHeelX, rightHeelY, rightToesX, rightToesY);
		add(leftFoot);
	}
	
	private void displayIncorrectLetter(char letter) {
		double initialX = (getWidth() / 2) - BEAM_LENGTH; 
		double initialY = (getHeight() / 2) + (SCAFFOLD_HEIGHT / 2);
		
		GLabel singleLetter = new GLabel(" "+letter+"", initialX, initialY); //Holds the incorrect letter
		singleLetter.move(moveX, 0);
		singleLetter.setFont("Times New Roman-18");
		add(singleLetter);
		
		moveX = moveX + getWidth() / 8; //Updates the space for the next letter in the GLabel
	}
	
	/* Private instance variables */
	
	private GCompound scaffold;
	private GLine scaffoldBase;
	private GLine beam;
	private GLine rope;
	private GOval head;
}	
