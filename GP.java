import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Random;


/**
 * Created by Jonathan Qassis on 2/12/2018.
 */

/**
 * @author Jonathan Qassis
 * This class represents the implementation for a genetic program to produce a given phrase.
 */
public class GP {	
	private int MIN = 32; // Lowest ASCII character used
	private int MAX = 126; // Highest ASCII character used
	
	private static String phrase; // Target phrase
	private double best_fitness; // The current best fitness value obtained
	private String best_fit; // The current best phrase associated with the best fitness value (above)
	
	private Random rand; // Random number generator
	
	private int pop; // Initial population
	private double cross_over; // Cross over probability
	private double mutation; // Mutation probability 
	
	
	/**
	 * Constructs a new GP
	 * 
	 * @param phrase Target String
	 * @param pop Initial population size
	 * @param cross_over Rate of mating between two elements in the population
	 * @param mutation Rate of mutating an elements values
	 * @modifies Nothing
	 * @effects Constructs a new GP with the variable phrase as a target, pop as the initial population size,
	 * 			cross_over as the chance of mating two elements in the population and mutation as the rate
	 * 			and element's values get mutated
	 * @returns Nothing
	 */
	public GP(String phrase, int pop, double cross_over, double mutation){
		this.phrase = phrase;
		this.best_fitness = 0.0;
		this.best_fit = "";
		
		this.rand = new Random();
		
		this.pop = pop;
		this.cross_over = cross_over;
		this.mutation = mutation;
	}
	
	
	/**
	 * Runs the GP
	 * 
	 * @modifies Nothing
	 * @effects Nothing
	 * @return Nothing
	 */
	public void InitializeGP(){
		System.out.println("Initializing GP...");
		System.out.println("Phrase: " + phrase);
		System.out.println("Population: " + pop + "    Cross Over: " + cross_over + "    Mutation: " + mutation);
		System.out.println("\nStarting GP...");
		StartGP();
	}
	
	/**
	 * Runs the GP
	 * 
	 * @param
	 * @modifies Nothing
	 * @effects Runs the GP
	 * @return Nothing
	 */
	private void StartGP(){
		ArrayList<Element> population;
		
		// Runs for the number of generations we need to iterate over
		int gen = 0;
		
		// Create the population
		population = createPopulation();
					
		while(!this.best_fit.equals(phrase)){	
			// Print out population
			printPopulation(population);
			
			// Calculate the fitness for each population 
			calculateFitness(population);
			
			// Create a new generation of elements
			population = newGeneration(population);
			
			gen++;
			
		}
		System.out.println("Generations: " + gen);
		System.out.println("Best phrase: " + this.best_fit);
		System.out.println("Best fitness: " + this.best_fitness);
	}
	
	/**
	 * This method prints out the current best phrase and the fitness value associated with that phrase
	 * 
	 * @param population is a list containing all the current Elements alive
	 * @modifies Nothing
	 * @effects Prints to console
	 * @return Nothing
	 */
	private void printPopulation(ArrayList<Element> population) {
		System.out.println("Best phrase: " + this.best_fit);
		System.out.println("Best fitness: " + this.best_fitness);
		System.out.println();
	}

	/**
	 * Creates a new generation of elements based on the old ones
	 * 
	 * @param population is a list containing all the current Elements alive
	 * @modifies population
	 * @effects returns a new population based off the old one
	 * @return a new population
	 */
	private ArrayList<Element> newGeneration(ArrayList<Element> population){		
		ArrayList<Element> new_population = new ArrayList<Element>();
		for(int i = 0; i < population.size(); i++){
			Element child;
			
			// Randomly choose a parent that may mate 
			Element par1 = acceptReject(population); 
			
			// Mate two elements
			if(rand.nextDouble() <= this.cross_over){
				// Randomly choose a parent to mate
				Element par2 = acceptReject(population);
				
				// Create the child out of the two parents
				child = par1.crossOver(par2, this.rand);
			}else{ // Copy par1 element, no mating
				child = par1;
			}
			
			// Mutate the child (possibility of mutation) 
			child.mutation(this.rand, this.mutation);
			
			// Set new child into the population
			new_population.add(child);
		}
		return new_population;
	}
	
	
	/**
	 * Returns an Element from the population that fits certain requirments 
	 * 
	 * @param population is a list containing all the current Elements alive
	 * @modifies Nothing
	 * @effects Nothing
	 * @return Element from the population
	 */
	private Element acceptReject(ArrayList<Element> population){
		int stop_infinite_loop = 0;
		// Repeat until an Element is returned
		while(true){
			// Get a random index to select an element from the population at that index
			int index = rand.nextInt(population.size());
			// Get a random fitness that is <= this.best_fitness
			double random_fitness = rand.nextDouble() * this.best_fitness;
			// Select the element from the population at index, index
			Element parent = population.get(index);
			
			// If the fitness of a random Element is greater than the random fitness return it
			if(random_fitness < parent.getFitness()){
				return parent;
			}
			
			// Safe guard to stop infinite loop
			if(stop_infinite_loop >= 10000){
				return parent;
			}
			stop_infinite_loop++;
		}
	}
	


	/**
	 * Creates the initial population for the GP
	 * 
	 * @modifies Nothing
	 * @effects Creates a new population with size pop
	 * @return The newly created populaiton
	 */
	private ArrayList<Element> createPopulation(){
		
		ArrayList<Element> returnPop = new ArrayList<Element>();
		
		for(int cur_pop = 0; cur_pop < pop; cur_pop++){
			// Creates a char array the size of the phrase passed in
			char[] phrases = new char[phrase.length()];
			
			// Fills in the char array to create a random string of characters
			for(int i = 0; i < phrases.length; i++){
				phrases[i] = (char)(rand.nextInt(MAX - MIN) + MIN);//characters[rand.nextInt(characters.length)];
			}
			
			// Adds a new phrases to the total population
			returnPop.add(new Element(phrases));
		}
		return returnPop;
	}
	
	/**
	 * Calculates the fitness of individual elements for the whole population
	 * 
	 * @param population is a list containing all the current Elements alive
	 * @modifies The fitness value of each Element in the population
	 * @effects population's element's fitness value
	 * @return Nothing
	 */
	private void calculateFitness(ArrayList<Element> population){
		for(Element elm : population){
			elm.calcFitness(phrase);
			if(elm.getFitness() >= this.best_fitness){
				this.best_fitness = elm.getFitness();
				this.best_fit = elm.getPhrase();
			}
		}
	}
	
}