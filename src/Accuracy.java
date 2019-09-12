import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Accuracy{
    public static void main(String[] args){
        try{ 
			Scanner original = new Scanner(new File(args[0]), "UTF-8");
			Scanner generated = new Scanner(new File(args[1]), "UTF-8");

			int counter = 0;
			
			for(int s = 0; s < 2; s++){
				if (original.nextInt() != generated.nextInt()){
			        	counter++;
			        }
			}
	
			for(int s = 0; s < 1; s++){
				if (original.nextFloat() != generated.nextFloat()){
			        	counter++;
			        }
			}
						
			while (original.hasNext()){
			    
			    if (original.nextInt() != generated.nextInt()){
				counter++;
			    }
			 
			}			
			System.out.println("The number of correct results is: " + counter);
		}
		catch (IOException e){
			System.out.println("Unable to open one of the input files ");
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){
			System.out.println("Malformed input file ");
			e.printStackTrace();
		}
    }
}
