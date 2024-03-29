//package CSCAssignment3;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Math;

public class CloudData {

	Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	float [][][] convection; // vertical air movement strength, that evolves over time
	int [][][] classification; // cloud type per grid point, evolving over time
	int dimx, dimy, dimt; // data dimensions
	float xAverage = 0;  // overall x average
	float yAverage = 0;  //overall y average

	// overall number of elements in the timeline grids

	int dim(){
		return dimt*dimx*dimy;
	}

	// read cloud simulation data from file
	void readData(String fileName){
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

	// Sets the classification of each air layer element and also caltulates the averages for x and for y
	public void setClass(){
		for(int t = 0; t < dimt; t++)
			for(int x = 0; x < dimx; x++)
				for(int y = 0; y < dimy; y++){
					float wind = (float)getMag(t,x,y);
					
					//System.out.println(wind);
					xAverage += advection[t][x][y].getX();
                  			yAverage += advection[t][x][y].getY();
	
					if(Math.abs(convection[t][x][y]) > wind) classification[t][x][y] = 0;
					else if(wind > 0.2) classification[t][x][y] = 1;
					else classification[t][x][y] = 2;
			
				}
	xAverage /= dim();
	yAverage /= dim();
	}

	//Calculates and returns the prevailling wind magnitude
	public double getMag(int t, int x, int y)
       	{
            float localX = 0;
            float localY = 0;
	    int i = x;
	    int j = y;
	    int count = 0;

                if (i >= 0 && i < dimx && j >= 0 && j < dimy){
		     localX += advection[t][i][j].getX();
                     localY += advection[t][i][j].getY();
			count ++;
		}

                if ((j-1) >= 0 && (j-1) < dimy && i >= 0 && i < dimx){
                     localX += advection[t][i][j-1].getX();
                     localY += advection[t][i][j-1].getY();
			count ++;
                 }
		
		if (j >= 0 && j < dimy && (i-1) >= 0 && (i-1) < dimx){
                     localX += advection[t][i-1][j].getX();
                     localY += advection[t][i-1][j].getY();
			count ++;
                 }

		if ((j-1) >= 0 && (j-1) < dimy && (i-1) >= 0 && (i-1) < dimx){
                     localX += advection[t][i-1][j-1].getX();
                     localY += advection[t][i-1][j-1].getY();
			count ++;
                 }

		if ((j+1) >= 0 && (j+1) < dimy && i >= 0 && i < dimx){
                     localX += advection[t][i][j+1].getX();
                     localY += advection[t][i][j+1].getY();
			count ++;
                 }

		if (j >= 0 && j < dimy && (i+1) >= 0 && (i+1) < dimx){
                     localX += advection[t][i+1][j].getX();
                     localY += advection[t][i+1][j].getY();
			count ++;
                 }

		if ((j+1) >= 0 && (j+1) < dimy && (i+1) >= 0 && (i+1) < dimx){
                     localX += advection[t][i+1][j+1].getX();
                     localY += advection[t][i+1][j+1].getY();
			count ++;
                 }

		if ((j+1) >= 0 && (j+1) < dimy && (i-1) >= 0 && (i-1) < dimx){
                     localX += advection[t][i-1][j+1].getX();
                     localY += advection[t][i-1][j+1].getY();
			count ++;
                 }

		if ((j-1) >= 0 && (j-1) < dimy && (i+1) >= 0 && (i+1) < dimx){
                     localX += advection[t][i+1][j-1].getX();
                     localY += advection[t][i+1][j-1].getY();
			count ++;
                 }
     
            double w = Math.sqrt(Math.pow((localX/count), 2) + Math.pow((localY/count), 2));
            return w;
         }

	// write classification output to file
	void writeData(String fileName, Vector wind){
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
}
