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
 * 
 */

import java.util.List;
import java.util.LinkedList;

public class Solver {
	private boolean isSolvable = false;
	private int moves = -1;
	private MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
	private MinPQ<SearchNode> minPQForTwin = new MinPQ<SearchNode>();
	private SearchNode solution = null;
    
    // find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		Board twin = initial.twin();
		SearchNode minNode = null;
		boolean isTwinRound = false;
		MinPQ<SearchNode> currentPQ = null;
		
		minPQ.insert(new SearchNode(initial));
		minPQForTwin.insert(new SearchNode(twin));

		while(true){
			//Searching solution in the initial board and and its twin board
			//simultaneously(alternating in loops). 
			//It has been proven by Math theory that exactly one of the two 
			//will lead to the goal board. If a solution is found in the twin
			//board, it immediately proves that the initial board is not solvable,
			//and the search will terminate there.
			//Otherwise, the initial board will reach a solution.
			if(isTwinRound){
				currentPQ = minPQForTwin;
			} else {
				currentPQ = minPQ;				
			}
			
			minNode = currentPQ.delMin();
			if(minNode.getBoard().isGoal()){
				break;
			} else {
				for(Board neighbor : minNode.getBoard().neighbors()){
					//Insert the neighbors into the MinPQ if:
					// 1. Current node contains the initial board(has no prev node)
					// 2. The new neighbor is not the same as current node's previous search node.
					//    This is a critical optimization used to reduce unnecessary 
					//    exploration of already visited search nodes.
					if(minNode.getPrev() == null 
							|| !neighbor.equals(minNode.getPrev().getBoard())){
						currentPQ.insert(new SearchNode(neighbor, minNode));
					}
				}
				//Flip the state of the isTwinRound flag
				isTwinRound = isTwinRound ? false : true; 
			}
		}
		
		if(isTwinRound){
			isSolvable = false;
			solution = null;
		} else {
			isSolvable = true;
			solution = minNode;
			moves = solution.getMoves();
		}
	}
	
	// is the initial board solvable?
    public boolean isSolvable(){
    
    	return isSolvable;
    }
    
    // min number of moves to solve initial board; -1 if no solution
    public int moves(){
    	
    	return moves;
    }
    
    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution(){
    	if(solution == null){
    		return null;
    	}
    	
    	List<Board> resultList = new LinkedList<Board>();
    	
    	//Spec stated that Collections.reverse() is not allowed for this assignment.
    	//Using Stack to reverse the list.
    	Stack<SearchNode> stack = new Stack<SearchNode>();
    	
    	SearchNode node = solution;
    	while(node != null){
    		stack.push(node);
    		node = node.getPrev(); 
    	}
    	
    	while(!stack.isEmpty()){
    		resultList.add(stack.pop().getBoard());
    	}
    	
    	return resultList;
    }
    
    // solve a slider puzzle
    public static void main(String[] args) {
//    	args = new String[1];
//    	args[0] = "testCases/puzzle05.txt";
//    	args[0] = "testCases/puzzle4x4-unsolvable.txt";
    	    	
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

class SearchNode implements Comparable<SearchNode>{
	private int existingMoves;
	private int manhattan;
	private int priority;
	private final SearchNode prev;
	private final Board board;
	
	SearchNode(Board b){
		board = b;
		prev = null;
		existingMoves = 0;
		manhattan = b.manhattan();
		priority = existingMoves + manhattan;
	}
	
	SearchNode(Board b, SearchNode prevNode){
		board = b;
		prev = prevNode;
		existingMoves = prevNode.getMoves() + 1;
		manhattan = b.manhattan();
		priority = existingMoves + manhattan;
	}
	
    public SearchNode getPrev(){
    	return prev;
    }
	
    public int getMoves(){
    	return existingMoves;
    }
    
    public Board getBoard(){
    	return board;
    }
    
    public int compareTo(SearchNode that){
    	Integer a = new Integer(this.priority);
    	Integer b = new Integer(that.priority);
    	
    	return a.compareTo(b);
    }
}