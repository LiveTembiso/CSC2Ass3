import java.util.*;
import java.io.*;
import java.lang.Math;

public class Generate {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String fileName = "inputdata.txt";
		int dimt = 25;
		int dimx = 500;
		int dimy = 500;
		int dim = dimt*dimy*dimx;

		try{
			FileWriter fileWriter = new FileWriter(fileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.printf("%d %d %d\n", dimt, dimx, dimy);

			for(int t = 0; t < dimt; t++){
				for (int x = 0; x < dimx; x++) {
					for (int y = 0; y < dimy; y++) {
						printWriter.printf("%f ", Math.random());
					}
				}			
			}

            		printWriter.close();
        	}
        	catch (IOException e){
            		System.out.println("Unable to open output file "+fileName);
            		e.printStackTrace();
        	}						
	}
}
