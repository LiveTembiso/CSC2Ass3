import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ForkJoinPool;

public class TestPar{
	static long startTime = 0;
	static int dimt, dimx, dimy;
	static Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	static float [][][] convection; // vertical air movement strength, that evolves over time
	static int [][][] classification; // cloud type per grid point, evolving over time
	static float xAverage, yAverage;

	//start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime); 
	}
	

	static final ForkJoinPool fjPool = new ForkJoinPool();

		// read cloud simulation data from file
	static void readData(String fileName){
		try{
			Scanner sc = new Scanner(new File(fileName), "UTF-8");

			// input grid dimensions and simulation duration in timesteps
			dimt = sc.nextInt();
			dimx = sc.nextInt();
			dimy = sc.nextInt();

			// initialize and load advection (wind direction and strength) and convection
			advection = new Vector[dimt][dimx][dimy];
			convection = new float[dimt][dimx][dimy];
			for(int t = 0; t < dimt; t++)
				for(int x = 0; x < dimx; x++)
					for(int y = 0; y < dimy; y++){
						advection[t][x][y] = new Vector();
						advection[t][x][y].setX(sc.nextFloat());
						advection[t][x][y].setY(sc.nextFloat());
						convection[t][x][y] = sc.nextFloat();
					}
			classification = new int[dimt][dimx][dimy];
			sc.close();
		}
		catch (IOException e){
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}

	static int dimension = dimt*dimx*dimy;

	// write classification output to file
	static void writeData(String fileName, Vector wind){
		 try{
			 FileWriter fileWriter = new FileWriter(fileName);
			 PrintWriter printWriter = new PrintWriter(fileWriter);
			 printWriter.printf("%d %d %d\n", dimt, dimx, dimy);
			 printWriter.printf("%f %f\n", xAverage, yAverage);

			 for(int t = 0; t < dimt; t++){
				 for(int x = 0; x < dimx; x++){
					for(int y = 0; y < dimy; y++){
						printWriter.printf("%d ", classification[t][x][y]);
					}
				 }
				 printWriter.printf("\n");
		     }

			 printWriter.close();
		 }
		 catch (IOException e){
			 System.out.println("Unable to open output file "+fileName);
				e.printStackTrace();
		 }
	}

	
	public static void main(String[] args){

		if(args.length != 2)
		{
		        System.out.println("Incorrect number of command line arguments. Should have form: java TestPar.java intputfilename outputfilename");
			System.exit(0);
		}

		readData(args[0]);		
		CloudDataP cloudP = new CloudDataP(advection, convection, classification, dimt, dimy, dimx, 0, 0, 0, dimension);
		
		System.gc();	
		tick();
		fjPool.invoke(cloudP);
		System.out.println("Time taken = " + tock() + "ms");	
		xAverage = cloudP.xAverage;
		yAverage = cloudP.yAverage;

		for(int t = 0; t < cloudP.maxt; t++)
			for(int x = 0; x < cloudP.maxx; x++)
				for(int y = 0; y < cloudP.maxy; y++){
					writeData(args[1],cloudP.advection[t][x][y]);
				}

	}
}
