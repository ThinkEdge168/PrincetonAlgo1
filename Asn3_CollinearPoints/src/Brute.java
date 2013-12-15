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

public class Brute {
	private Point[] points;
	private int numOfPoints;
	
	public static void main(String[] args) {
		Brute b = new Brute();
		
		b.initialise();
//		b.readInInput(args[0]);
		b.readInInput(null);
		b.findCollinearPoints();
        b.finalDraw();
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
		int i, j, k, l;
		Point p1, p2, p3, p4;
		double slope1, slope2, slope3;
	
		if(numOfPoints >= 4){
			Arrays.sort(points);
			for(i = 0; i < numOfPoints - 3; i++){
				for(j = i + 1; j < numOfPoints - 2; j++){
					for(k = j + 1; k < numOfPoints - 1; k++){
						for(l = k + 1; l < numOfPoints; l++){
							p1 = points[i];
							p2 = points[j];
							p3 = points[k];
							p4 = points[l];
							slope1 = p1.slopeTo(p2);
							slope2 = p1.slopeTo(p3);
							slope3 = p1.slopeTo(p4);

							if(slope1 == slope2 && slope2 == slope3){
								//The auto-grader allows only 1 drawing operation to cover
								//all the points on the line.
								//Since the points are sorted in lexicographical order,
								//p1 is always the head of the line and p4 is always the tail.
								p1.drawTo(p4);
								StdOut.println(p1 + " -> " + p2 + " -> " + p3 + " -> " + p4);
							}
						}
					}
				}
			}
		}
	}
}