/**
 * Created by Jonathan Qassis on 2/12/2018.
 */

/**
 * @author Jonathan Qassis
 * This class runs the GP.
 */

public class RunGP {
	public static void main(String args[]){
		String phrase = "This is a testing string. This is a testing string. This is a testing string."; 
		
		int pop = 200;
		double cross_over = 1.0; // Number between 0.0 - 1.0 (inclusive)
		double mutation = 0.1/phrase.length(); 
		
		GP gp = new GP(phrase, pop, cross_over, mutation);
		
		// Starts the program
		gp.InitializeGP();		
	}
}