public class TestSeq{
	static long startTime = 0;

	//start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime); 
	}
	
	public static void main(String[] args){

		if(args.length != 2)
		{
		        System.out.println("Incorrect number of command line arguments. Should have form: java TestSeq.java intputfilename outputfilename");
			System.exit(0);
		}

		CloudData cloud = new CloudData();
      		cloud.readData(args[0]);
		
		System.gc();
		tick();
		cloud.setClass();
		System.out.println("Time taken = " + tock() + "ms");
		for(int t = 0; t < cloud.dimt; t++)
			for(int x = 0; x < cloud.dimx; x++)
				for(int y = 0; y < cloud.dimy; y++){
					cloud.writeData(args[1],cloud.advection[t][x][y]);
				}

	}
}
