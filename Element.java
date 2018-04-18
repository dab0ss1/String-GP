import java.util.Random;

/**
 * Created by Jonathan Qassis on 2/12/2018.
 */

/**
 * @author Jonathan Qassis
 * This class represents the implementation for an Element representing a string and it's fitness in
 * relation to another string.
 */

public class Element {
	private int MIN = 32; // Lowest ASCII character used
	private int MAX = 126; // Highest ASCII character used
	
	private char[] phrase; // Character representation of a phrase
	private double fitness; // How close to actual phrase this Element is (high is better)
	
	
	/**
	 * Constructs a new Element
	 * 
	 * @param phrase character array representation of a string
	 * @modifies Nothing
	 * @effects Constructs a new Element with the phrase given and an initial fitness value of 0.0
	 * @return Nothing
	 */
	public Element(char[] phrase){
		this.phrase = phrase;
		this.fitness = 0.0;
	}
	
	/**
	 * Calculates the fitness of an element
	 * 
	 * @param str is a String that we use for comparison 
	 * @modifies this (fitness)
	 * @effects Sets fitness to a new value based off this Elements comparison with the string str
	 * @return Nothing
	 */
	public void calcFitness(String str){
		double score = 0.0;
		for(int i = 0; i < phrase.length; i++){
			if(phrase[i] == str.charAt(i)){
				score += 1.0;
			}
		}
		// Using Math.pow(score, 3) to make sure more fit Elements have a better chance of surviving
		// to the next generation.
		fitness = Math.pow(score, 3) / Math.pow(str.length(), 3);
	}
	
	/**
	 * Returns a string representation of this element
	 * 
	 * @modifies Nothing
	 * @effects Creates a string out of the character array phrase and returns it
	 * @return a String representation of the phrase array
	 */
	public String getPhrase(){
		return new String(phrase);
	}
	
	/**
	 * Returns the fitness of this element
	 * 
	 * @modifies Nothing
	 * @effects Nothing
	 * @return The fitness value associated with this Element
	 */
	public double getFitness(){
		return fitness;
	}
	
	/**
	 * Creates a new element with mixed characters
	 * 
	 * @param elm is an Element
	 * @param random is a random number generator 
	 * @modifies Nothing
	 * @effects Creates a new element and returns it to the user
	 * @return A new Element that is a mix of this Element and another called elm
	 */
	public Element crossOver(Element elm, Random random){
		char[] newPhrase = phrase.clone();
		String elmstr = elm.getPhrase();
		for(int i = random.nextInt(phrase.length); i < phrase.length; i++){
			newPhrase[i] = elmstr.charAt(i);
		}
		return new Element(newPhrase);
	}
	
	/**
	 * Mutates characters (possibly)
	 * 
	 * @param random is a random number generator
	 * @param mutationRate is the probability that a single character will be mutated 
	 * @modifies this (phrase)
	 * @effects possibly mutates some of the character in the phrase
	 * @return Nothing
	 */
	public void mutation(Random random, double mutationRate){
		for(int i = 0; i < phrase.length; i++){
			if(random.nextDouble() < mutationRate){
				phrase[i] = (char)(random.nextInt(MAX - MIN) + MIN);
			}
		}
	}
}
