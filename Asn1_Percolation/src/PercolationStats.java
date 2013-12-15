/*
 * Princeton Algorithms 1 on Coursera (2013 Aug offering)
 * 
 * Programming assignment 1 - Percolation
 * 
 * Fan Xiao
 * 
 * 2013 Sep 08
 * 
 */
public class PercolationStats {
	private final int repetitions;
	private final int gridSize;
	private double[] thresholds;

	// perform T independent computational experiments on an N-by-N grid	
	public PercolationStats(int n, int t){
		if(n <= 0 || t <= 0){
			throw new IllegalArgumentException();
		}
		
		gridSize = n;
		repetitions = t;
		int totalSites = n * n;
		int openSites = 0;

		//Ranging from 0 to t-1
		thresholds = new double[t];
		
		for(int i = 0; i < t; i++){
			openSites = mcSimulation();
			thresholds[i] = ((double) openSites) / totalSites;
		}
	}
	
	// sample mean of percolation threshold
	public double mean(){
		return StdStats.mean(thresholds);
	}
	
	// sample standard deviation of percolation threshold
	public double stddev(){
		return StdStats.stddev(thresholds);
	}
	
	// returns lower bound of the 95% confidence interval
	public double confidenceLo(){
		return mean() - ((1.96 * stddev()) / Math.sqrt(repetitions));
	}
	
	// returns upper bound of the 95% confidence interval
	public double confidenceHi(){
		
		return mean() + ((1.96 * stddev()) / Math.sqrt(repetitions));
	}
	
	//Monte Carlo simulation: One time percolation experiment
	private int mcSimulation(){
		int numOfOpenSites = 0;
		int x = 0;
		int y = 0;
		Percolation pc = new Percolation(gridSize);
		
		 
		Coordinate[] randomCoords = generateRandomCoords();
		
		for(int i = 0; i < randomCoords.length; i++){
			x = randomCoords[i].getX();
			y = randomCoords[i].getY();
			pc.open(x, y);
			
			if(pc.percolates()){
				numOfOpenSites = i + 1;
				break;
			}
		}

		//Assumption: the system will percolate before all the sites are opened
		//So returning numOfOpenSites = 0 is not possible
		return numOfOpenSites;
	}
	
	//Generate a sequence of coordinates that will randomly and uniformly open up all the closed sites in 
	//the 2d matrix
	private Coordinate[] generateRandomCoords(){
		int[] randomPositions = new int[gridSize * gridSize];
		
		for(int i = 0; i < randomPositions.length; i++){
			randomPositions[i] = i;
		}
		
		StdRandom.shuffle(randomPositions);

		Coordinate[] randomCoords = new Coordinate[gridSize * gridSize];
		for(int i = 0; i < randomPositions.length; i++){
			randomCoords[i] = positionToIndex(randomPositions[i]);
		}
		
		return randomCoords;
	}
	
	//Mapping the 1d array's position (ranging from 0 to N*N-1)
	//to the 2d matrix's index (ranging from [1,1] to [N,N]) 
	private Coordinate positionToIndex(int position){
		int x = ((int) (position / gridSize)) + 1;
		int y = position % gridSize + 1;
		
		return new Coordinate(x, y);
	}
	
	// test client
	public static void main(String[] args) {
		if (args == null || args.length != 2) {
			StdOut.println("Usage: java PercolationStats [gridSize] [repetitions]");
			return;
		}
		int n = Integer.parseInt(args[0]);
		int t = Integer.parseInt(args[1]);

		PercolationStats ps = new PercolationStats(n, t);
		StdOut.println("mean			= " + ps.mean());
		StdOut.println("stddev			= " + ps.stddev());
		StdOut.println("95% confidence interval	= " + ps.confidenceLo() + ", " + ps.confidenceHi());
	}
}

class Coordinate {
	private int x;
	private int y;
	
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setCoordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}