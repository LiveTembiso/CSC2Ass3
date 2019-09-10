import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.lang.Math;

public class CloudDataP extends RecursiveAction {

	Vector [][][] advection; // in-plane regular grid of wind vectors, that evolve over time
	float [][][] convection; // vertical air movement strength, that evolves over time
	int [][][] classification; // cloud type per grid point, evolving over time
	int maxt, maxx, maxy; // data max
	int mint, minx, miny; // data min
	float xAverage = 0;
	float yAverage = 0;
	static final int SEQUENTIAL_CUTOFF = 2130;

	public CloudDataP(Vector [][][] adv, float [][][] conv, int [][][] clas, int mat, int may, int max, int mit, int miy, int mix, int dm){
		advection = adv;
		classification = clas;
		convection = conv;
		maxt = mat;
		mint = mit;
		maxx = max;
		minx = mix;
		maxy = may;
		miny = miy;
		dim = dm;
	}

	// overall number of elements in the timeline grids

	public int dim(){
		return maxt*maxx*maxy;
	}

	int dim = dim();
	
	int a = 0;
	@Override
	public void compute(){
		if(dim <= SEQUENTIAL_CUTOFF){
			for(int t = mint; t < maxt; t++)
				for(int x = minx; x < maxx; x++)
					for(int y = miny; y < maxy; y++){
						float wind = (float)getMag(t,x,y);
						
						xAverage += advection[t][x][y].getX();
		          			yAverage += advection[t][x][y].getY();

						if(Math.abs(convection[t][x][y]) > wind) classification[t][x][y] = 0;
						else if(wind > 0.2 &&  Math.abs(convection[t][x][y]) <= wind ) classification[t][x][y] = 1;
						else classification[t][x][y] = 2;

					}
		xAverage /= dim();
		yAverage /= dim();
		}

		else{
			dim = dim/2;
			int midx = maxx/2;
			int midy = maxy/2;

			CloudDataP left = new CloudDataP(advection, convection, classification, maxt, midy, midx, mint, miny, minx, dim);
			CloudDataP right = new CloudDataP(advection, convection, classification, maxt, maxy, maxx, mint, midy, midx, dim);
			invokeAll(left,right);
		}
	}

	public double getMag(int t, int x, int y)
       	{
            float localX = 0;
            float localY = 0;
            int i = y;
	    int j = x;
	    int count = 0;

                if (i >= 0 && i < maxy && j >= 0 && j < maxx){
		     localX += advection[t][i][j].getX();
                     localY += advection[t][i][j].getY();
			count++;
		}

                if ((j-1) >= 0 && (j-1) < maxy && i >= 0 && i < maxx){
                     localX += advection[t][i][j-1].getX();
                     localY += advection[t][i][j-1].getY();
			count++;
                 }
		
		if (j >= 0 && j < maxy && (i-1) >= 0 && (i-1) < maxx){
                     localX += advection[t][i-1][j].getX();
                     localY += advection[t][i-1][j].getY();
			count++;
                 }

		if ((j-1) >= 0 && (j-1) < maxy && (i-1) >= 0 && (i-1) < maxx){
                     localX += advection[t][i-1][j-1].getX();
                     localY += advection[t][i-1][j-1].getY();
			count++;
                 }

		if ((j+1) >= 0 && (j+1) < maxy && i >= 0 && i < maxx){
                     localX += advection[t][i][j+1].getX();
                     localY += advection[t][i][j+1].getY();
			count++;
                 }

		if (j >= 0 && j < maxy && (i+1) >= 0 && (i+1) < maxx){
                     localX += advection[t][i+1][j].getX();
                     localY += advection[t][i+1][j].getY();
			count++;
                 }

		if ((j+1) >= 0 && (j+1) < maxy && (i+1) >= 0 && (i+1) < maxx){
                     localX += advection[t][i+1][j+1].getX();
                     localY += advection[t][i+1][j+1].getY();
			count++;
                 }

		if ((j+1) >= 0 && (j+1) < maxy && (i-1) >= 0 && (i-1) < maxx){
                     localX += advection[t][i-1][j+1].getX();
                     localY += advection[t][i-1][j+1].getY();
			count++;
                 }

		if ((j-1) >= 0 && (j-1) < maxy && (i+1) >= 0 && (i+1) < maxx){
                     localX += advection[t][i+1][j-1].getX();
                     localY += advection[t][i+1][j-1].getY();
			count++;
                 }

            double w = Math.pow(localX, 2) + Math.pow(localY, 2);
            w = Math.sqrt(w);
            return w;
         }
}
