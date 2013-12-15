/*
 * Princeton Algorithms 1 on Coursera (2013 Aug offering)
 * 
 * Programming assignment 3 - Collinear Points
 * 
 * Fan Xiao
 * 
 * 2013 Oct
 * 
 */


import java.util.Arrays;
import java.util.Comparator;

public class Fast {
	private Point[] points;
	private Point[] auxPoints;
	private Point[] results;
	private double[] slopes;
	private int numOfPoints;
		
	public static void main(String[] args) {
		Fast f = new Fast();
		
		f.initialise();
//		f.readInInput(args[0]);
		f.readInInput(null);
		f.findCollinearPoints();
        f.finalDraw();
	}

	// rescale coordinates and turn on animation mode
	private void initialise(){
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
	}
	
	// read in the input
	private void readInInput(String fileName){
		String dir = "testCases\\";
		fileName = dir + "input8.txt";    
        
        In in = new In(fileName);
        int N = in.readInt();
        
        points = new Point[N];
        numOfPoints = N;

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            p.draw();
            points[i] = p;
        }
    }

	// display to screen all at once
	private void finalDraw(){
        StdDraw.show(0);
	}
	
	//draw every (maximal) line segment that connects a subset of 4 or more of the points
	private void findCollinearPoints(){
		if(numOfPoints >= 4){
	        slopes = new double[numOfPoints];
			Arrays.sort(points);
			
			for(int i = 0; i < numOfPoints; i++){
				//Arrays.sort() uses Merge Sort to sort objects, hence the results
				//are stable.(Check slides 2.2 Mergesort p52 for the explanation on stability)
				//The points sorted in lexicographical order are copied to auxPoints 
				//at the beginning of each iteration.
				auxPoints = Arrays.copyOf(points, numOfPoints);

				Point currentPoint = points[i];
				Comparator<Point> cmp = currentPoint.SLOPE_ORDER;
				//Sort the auxPoints again with SLOPE_ORDER comparator
				//The result is now ordered by SLOPE_ORDER, then lexicographical order
				Arrays.sort(auxPoints, cmp);
				
				//Calculate the slopes from currentPoint to each point in auxPoints
				for(int j = 0; j < numOfPoints; j++){
					slopes[j] = currentPoint.slopeTo(auxPoints[j]);	
				}
				
				int counter = 1;
				//Find adjacent points which have equal slope values
				for(int j = 0; j < numOfPoints; j++){
					if(j != (numOfPoints - 1) && slopes[j] == slopes[j + 1]){
						counter++;						
					} else {
						//A line contains 4 or more points has been found
						if(counter >= 3){
							//Copy the points of the line into the results array.
							//Note that currentPoint has a value of NEGATIVE_INFINITY in auxPoints,
							//hence it's not in the range of [from] and [to].
							//currentPoint should be manually added to the results array.
							int from = j - counter + 1;
							int to = j;
							results = new Point[counter + 1];
							results[0] = currentPoint;
							for(int k = from, l = 1; k <= to; k++, l++){
								results[l] = auxPoints[k]; 
							}
							
							//Sort the results array in lexicographical order
							Arrays.sort(results);
							
							//The results array should only be printed out if the currentPoint
							//is at the beginning of the line. (i.e. is the 1st element of the sorted
							//results array)
							//The purpose of this check is to eliminate the duplicated lines which have
							//points in different order
							if(currentPoint == results[0]){
								printResult();
							}
						}
						
						//if (slopes[j] != slopes[j + 1]), reset the counter.
						counter = 1;
					}
				}
			}
		}
	}
	
	private void printResult(){
		int i;
		for(i = 0; i < results.length; i++){
			if(i == (results.length - 1)){
				StdOut.print(results[i]);
			} else {
				StdOut.print(results[i] + " -> ");
			}
		}
		StdOut.println();

		//The auto-grader allows only 1 drawing operation to cover
		//all the points on the line.
		//Since the points are sorted in lexicographical order,
		//first element in the array is the head of the line and last element is the tail.
		results[0].drawTo(results[results.length - 1]);
	}
}