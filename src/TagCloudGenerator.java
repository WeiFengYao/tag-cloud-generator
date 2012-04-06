import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * 
 * This class creates a new TagCloud object upon instantiation, 
 * and when the start() method is called, the program prompts 
 * for user input via the console and gives options to find, 
 * save, load, print, print the top tags, input a new url, or 
 * quit.
 * 
 * @author Evan W. Drewry (ewd2106)
 *
 */
public class TagCloudGenerator {

	TagCloud cloud;
	
	public TagCloudGenerator(){
		cloud = new TagCloud();
		
	}
	
	/**
	 * Prompts for user input via the console and gives 
	 * options to find, save, load, print, get the top
	 * tags, input a new url, or quit.
	 */
	public void start() {
		Scanner in = new Scanner(System.in);
		while(true){
			try{
				System.out.println("Input \n 'u' to input a new url, \n 'p' to print, \n 't' to print the most frequently occurring tags, \n 'f' to find, \n 's' to save, \n 'l' to load,  or \n 'x' to quit."); //We can use this method to prompt for any condiment
				String input1 = in.next();
				if(input1.equalsIgnoreCase("x")){
					return;
				}
				else if(input1.equalsIgnoreCase("u")){ 
					System.out.println("Enter the url.");
					in.nextLine();
					String input2 = in.next();
					try {
						cloud.addURL(input2);
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}				}
				else if(input1.equalsIgnoreCase("p")){ 
					cloud.show();
				}
				else if(input1.equalsIgnoreCase("t")){ 
					System.out.println("How many tags should the tag cloud contain?");
					in.nextLine();
					int input2 = in.nextInt();
					cloud.getMostFrequentTags(input2).show();
				}
				else if(input1.equalsIgnoreCase("f")){ 
					System.out.println("Enter your query.");
					in.nextLine();
					String input2 = in.next();
					try {
						cloud.find(input2);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				else if(input1.equalsIgnoreCase("s")){
					System.out.println("Enter the file name.");
					in.nextLine();
					String input2 = in.next();
					try {
						cloud.save(input2);
					} catch (IOException e) {
						System.out.println("Tag cloud could not be saved.");
						e.printStackTrace(System.out);
					}
				}
				else if(input1.equalsIgnoreCase("l")){
					System.out.println("Enter the file name.");
					in.nextLine();
					String input2 = in.next();
					try {
						this.cloud = TagCloud.load(input2);
					} catch (Exception e) {
						System.out.println("Tag cloud could not be loaded.");
						e.printStackTrace(System.out);
					}
				}
				else{
					throw new InputMismatchException();
				}
			}
			catch(InputMismatchException e){
				System.out.println("Please enter a valid input.");
			}
		}
		
	}

	
	public static void main(String[] args){
		TagCloudGenerator myTCG = new TagCloudGenerator();
		myTCG.start();
	}
}
