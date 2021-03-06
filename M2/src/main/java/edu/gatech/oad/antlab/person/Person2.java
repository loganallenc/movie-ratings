package edu.gatech.oad.antlab.person;
import java.util.Random;

/**
 *  A simple class for person 2
 *  returns their name and a
 *  modified string 
 *
 * @author Bob
 * @author Will
 * @version 1.1
 */
public class Person2 {
    /** Holds the persons real name */
    private String name;
	 	/**
	 * The constructor, takes in the persons
	 * name
	 * @param pname the person's real name
	 */
	 public Person2(String pname) {
	   name = pname;
	 }
	/**
	 * This method should take the string
	 * input and return its characters in
	 * random order.
	 * given "gtg123b" it should return
	 * something like "g3tb1g2".
	 *
	 * @param input the string to be modified
	 * @return the modified string
	 */
	private String calc(String input) {
	  //Person 2 put your implementation here
	  Random randy = new Random();
		String clone = input;
		String toReturn = "";
		for (int i = 0; i < input.length() - 1; i++) {
			int temp = randy.nextInt(input.length() - i - 1);
			toReturn += clone.charAt(temp);
			if (temp == 0) {
				clone = input.substring(1, input.length() - 1);
			} else if (temp == input.length() - 1) {
				clone = input.substring(0, input.length() - 2);
			} else {
				clone = input.substring(0, temp -1) + input.substring(temp + 1, input.length() - 1);
			}
		}
	  return toReturn;
	}

	/**
	 * Return a string rep of this object
	 * that varies with an input string
	 *
	 * @param input the varying string
	 * @return the string representing the 
	 *         object
	 */
	public String toString(String input) {
	  return name + calc(input);
	}
}
