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
public class Percolation {
	private WeightedQuickUnionUF ufForPercolate;
	private WeightedQuickUnionUF ufForIsFull;
	private final int gridSize;
	private final int virtualTopSite;
	private final int virtualBottomSite;
	private boolean[][] matrix;
	
	// create N-by-N grid, with all sites blocked
	public Percolation(int n){
		gridSize = n;
		
		/*
		 *	The status(open/closed) of the each site are stored in the 2d array matrix[][]
		 *	Using index x from 1 to N and y from 1 to N for convenience
		 *	(Leaving row x = 0 and column y = 0 unused)
		 * 	
		 *	Cell value of
		 *	0: the site is closed (default)
		 * 	1: the site is open
		 */
		matrix = new boolean[gridSize + 1][gridSize + 1];
				
		/*
		 * Site connectivities are stored in a 1-d array in the WeightedQuickUnionUF object 
		 * from 0 to (gridSize * gridSize + 2)
		 * including an extra cell of virtual top site and an extra cell of virtual bottom site 
		 * 
		 * 0 to (gridSize*gridSize - 1)	: the NORMAL sites
		 * (gridSize * gridSize)		: virtual TOP site
		 * (gridSize * gridSize + 1) 	: virtual BOTTOM site
		 * 
		 * Virtual bottom site introduces a Backwash problem
		 * Backwash: http://coursera.cs.princeton.edu/algs4/checklists/percolation.html
		 * 
		 * The ways to overcome this problem:
		 *  
		 * 1.To not use a virtual bottom, but then our percolates() method needs to loop over the 
		 * 		entire last row and perform a find() on each of those to see if they are connected 
		 * 		to virtual top. That would violate the "all methods should take constant time plus 
		 * 		a constant number of calls to the union-find methods union(), find(), connected(), 
		 * 		and count()" instruction.
		 * 		(Implemented in my earlier submission which has failed the last 2 tests cases)
		 *2. To create and maintain two UFs, one with a virtual bottom one without. 
		 *		Then the former is used for percolates(), and the latter is used for isFull(). 
		 *		(Implemented in this submission)
		 */
		ufForPercolate = new WeightedQuickUnionUF(gridSize * gridSize + 2);
		virtualTopSite = gridSize * gridSize;
		virtualBottomSite = gridSize * gridSize + 1;
		
		//The second WeightedQuickUnionUF instance ufForIsFull is only used by isFull()
		//to prevent the Backwash problem. It doesn't have a virtual bottom site.
		ufForIsFull = new WeightedQuickUnionUF(gridSize * gridSize + 1);
	}
	
	// open site (row i, column j) if it is not already
	public void open(int i, int j){
		if(isValidIndexThrowsException(i, j)){
			if(matrix[i][j] == false){
				matrix[i][j] = true;
				expandConnections(i, j);
			}
		}
	}
	
	// is site (row i, column j) open?
	public boolean isOpen(int i, int j){
		boolean result = false;
		
		if(isValidIndexThrowsException(i, j)){
			if(matrix[i][j] == true){
				result = true;
			} else {
				result = false;
			}
		}
		
		return result;
	}
	
	// is site (row i, column j) full?
	public boolean isFull(int i, int j){
		if(isValidIndexThrowsException(i, j)){
			//will throw exception from isValidIndexThrowsException()
		}
		
		int p = indexToPosition(i, j);
		return ufForIsFull.connected(p, virtualTopSite);
	}
	
	// does the system percolate?
	public boolean percolates(){
		return ufForPercolate.connected(virtualTopSite, virtualBottomSite);
	}
	
	//Mapping the 2d array matrix's index (ranging from [1,1] to [N,N])
	//to the 1d array WeightedQuickUnionUF's position (ranging from 0 to N*N-1)
	private int indexToPosition(int x, int y){
		return (y - 1) * gridSize + (x - 1);
	}
	
	private boolean isValidIndex(int x, int y){
		boolean result = false;
		
		if(x <= 0 || x > gridSize || y <= 0 || y > gridSize){
			result = false;			
		} else {
			result = true; 
		}
		
		return result;
	}
	
	private boolean isValidIndexThrowsException(int x, int y){
		boolean result = false;
		
		if(x <= 0 || x > gridSize || y <= 0 || y > gridSize){
			result = false;
			throw new IndexOutOfBoundsException();
		} else {
			result = true;
		}
		
		return result;
	}

	private void expandConnections(int i, int j){
		int p = indexToPosition(i, j);
		
		//up
		connect(p, i - 1, j);
		
		//down
		connect(p, i + 1, j);
		
		//left
		connect(p, i, j - 1);
		
		//right
		connect(p, i, j + 1);
		
		/*
		 * Handle top and bottom rows:
		 * 
		 * All open sites in the top row need to connect to the same and the only virtual top site 
		 * 
		 * Each open site in the bottom row needs to connect to its own corresponding virtual bottom site, 
		 * to prevent the Backwash problem mentioned in the comments above. For a site of (i, j) in the bottom row,
		 * its corresponding virtual bottom site is (i+1, j) in the matrix
		 *
		 * ! Special case: gridSize == 1
		 * ! In this case both connectToVirtualTopSite() and connectToVirtualBottomSites() will still need to run
		 * ! So cannot use 'else if' before '(i == gridSize)'.
		 */
		if(i == 1){
			connectToVirtualTopSite(p);					
		}
				
		if (i == gridSize) {
			connectToVirtualBottomSites(p, j);
		}
	}
	
	private void connect(int p, int i, int j){
		if(isValidIndex(i, j)){
			if(isOpen(i, j)){
				int q = indexToPosition(i, j);
				ufForPercolate.union(p, q);
				ufForIsFull.union(p, q);
			}	
		}
	}
	
	private void connectToVirtualTopSite(int p){
		ufForPercolate.union(p, virtualTopSite);
		ufForIsFull.union(p, virtualTopSite);
	}
	
	private void connectToVirtualBottomSites(int p, int j){
		ufForPercolate.union(p, virtualBottomSite);
	}
}
