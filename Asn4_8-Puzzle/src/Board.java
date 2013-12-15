/*
 * Princeton Algorithms 1 on Coursera (2013 Aug offering)
 * 
 * Programming assignment 4 - 8 Puzzle
 * 
 * 		This assignment solves solve the 8-puzzle problem 
 * 		(and its natural generalizations) using the A* search algorithm.
 *  
 * Fan Xiao
 * 
 * 2013 Oct
 */

import java.util.List;
import java.util.LinkedList;

public class Board{
	//Making the variables immutable as required by the spec
	private final int dimension;
	private final int[][] grid;
	private int blankSqaureI;
	private int blankSqaureJ;
	
	
	// construct a board from an N-by-N array of blocks
	//(where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks){
    	//It is given that the blocks is not null
    	dimension = blocks.length;
		grid = new int[dimension][dimension];

		for(int i = 0; i < dimension; i++){
			for(int j = 0; j < dimension; j++){
				grid[i][j] = blocks[i][j];
				if(grid[i][j] == 0){
					blankSqaureI = i;
					blankSqaureJ = j;
				}
        	}
    	}
    }
    
    // board dimension N    
    public int dimension(){

    	return dimension;
    }
    
	// number of blocks out of place
    public int hamming(){
    	int hammingCounter = 0;
    	int index = 1;
    	for(int i = 0; i < dimension; i++){
    		for(int j = 0; j < dimension; j++){
    			//Spec:
    			//we do not count the blank square when computing the Hamming priorities.
    			if(grid[i][j] == 0){
    				index++;
    				continue;
    			} else if(grid[i][j] != index){
    				hammingCounter++;
    			}
				index++;
    		}
    	}

    	return hammingCounter;
    }
    
    //sum of Manhattan distances between blocks and goal
    public int manhattan(){
    	int mDistances = 0;
    	for(int i = 0; i < dimension; i++){
    		for(int j = 0; j < dimension; j++){
				mDistances += calcManhattanDistance(grid[i][j], i, j);
    		}
    	}

    	return mDistances;
    }
    
    //Calculate the Manhattan distance for a single block
    //between its current position to its goal position
    private int calcManhattanDistance(int val, int i, int j){
    	int mDistance = 0;
    	int x = 0;
    	int y = 0;
    	
    	if(val == 0){
    		//Spec:
			//we do not count the blank square when computing the Manhattan priorities.
    		return 0;
    	} else {
    		val = val - 1;
    		x = val / dimension;
    		y = val % dimension;
    	}
    	
    	mDistance += Math.abs(i - x);
    	mDistance += Math.abs(j - y);
    	    	
    	return mDistance;
    }
    
	// is this board the goal board?
    public boolean isGoal(){
    	boolean result = true;
    	int index = 1;

    	for(int i = 0; i < dimension; i++){
    		for(int j = 0; j < dimension; j++){
    			if(i == (dimension - 1) && j == (dimension - 1)){
        			//Check whether '0' is in the last block.    				
    				if(grid[i][j] != 0){
    					result = false;
    				}
    			} else if(grid[i][j] != index){
    				//Check whether each block is in its goal position.
    				result = false;
    			}
    			index++;
    		}
    	}
    
    	return result;
    }
    
	// a board obtained by exchanging two adjacent blocks in the same row    
    public Board twin(){
    	Board twinBoard = null;
    	
    	TwinOutter:
    	for(int i = 0 ; i < dimension; i++){
    		for(int j = 1; j < dimension; j++){
    			if(grid[i][j] != 0 && grid[i][j - 1] != 0){
    				//Swap the adjacent blocks
    				twinBoard = new Board(grid);
    				twinBoard.swapBlocks(i, j, i, j - 1);
    				break TwinOutter;
    			}
    		}
    	}

    	return twinBoard;
    }
    
    private void swapBlocks(int blockOnei, int blockOnej, int blockTwoi, int blockTwoj){
    	int temp = grid[blockOnei][blockOnej];
    	grid[blockOnei][blockOnej] = grid[blockTwoi][blockTwoj];
    	grid[blockTwoi][blockTwoj] = temp;
    }

	// does this board equal y?
    public boolean equals(Object y){
    	if(y == null){
    		return false;    		
    	}
    	
    	//Check if is same object
    	if(this == y){
    		return true;
    	}
    	
    	if(this.getClass() != y.getClass()){
    		return false;
    	}
    	
    	Board that = (Board) y;
    	boolean result = true;
    	
    	if(this.dimension != that.dimension){
    		result = false;
    	} else {
    		for(int i = 0; i < dimension; i++){
    			for(int j = 0; j < dimension; j++){
    				if(this.grid[i][j] != that.grid[i][j]){
    					result = false;
    				}
            	}
        	}
    	}
    	
    	return result;
    }
    
    //all neighboring boards    
    public Iterable<Board> neighbors(){
    	int i = blankSqaureI;
    	int j = blankSqaureJ;
    	List<Board> list = new LinkedList<Board>();
    	Board neighbors[] = new Board[4];
    
    	//Move the blank square towards 4 different directions.
    	//moveTo() may return null if the destination position is invalid.
    	neighbors[0] = moveTo(i + 1, j);	//UP
    	neighbors[1] = moveTo(i - 1, j);   	//Down
    	neighbors[2] = moveTo(i, j + 1);	//Right
    	neighbors[3] = moveTo(i, j - 1);	//Left
    	
    	//Move the non-null neighbors into a List
    	for(int k = 0; k < 4; k++){
    		if(neighbors[k] != null){
    			list.add(neighbors[k]);
    		}
    	}
    	
    	return list;
    }
    
    //Move the blank square
    private Board moveTo(int i, int j){
    	Board nextStep = null;
    	
    	if(isPositionValid(i, j)){
    		nextStep = new Board(grid);
    		nextStep.swapBlocks(blankSqaureI, blankSqaureJ, i, j);
    		nextStep.setBlankSqaurePos(i, j);
    	}
    	return nextStep;
    }
    
    //Check whether the position is out of bound
    private boolean isPositionValid(int i, int j){
    	boolean result = true;
    	
    	if(i < 0 || i >= dimension || j < 0 || j >= dimension){
    		result = false;
    	}
    	
    	return result;
    }
    
    private void setBlankSqaurePos(int i, int j){
    	blankSqaureI = i;
		blankSqaureJ = j;
    }
   
	// string representation of the board (in the output format specified below)
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(dimension + "\n");
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++){
            	sb.append(String.format("%2d ",  grid[i][j]));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
