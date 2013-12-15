/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new slopeOrderComparator(); 

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
    	double slope = 0;
    	int dy = that.y - this.y;
    	int dx = that.x - this.x;
    	
    	if (dy == 0 && dx == 0){
    		//Degenerate line
    		slope = Double.NEGATIVE_INFINITY;
    	} else if(dy == 0){
    		//Horizontal line
    		slope = 0.0;	//Positive zero
    	} else if (dx == 0){
    		//Vertical line
    		slope = Double.POSITIVE_INFINITY;    		
    	} else {
    		//Need the cast here since dy and dx are both int.
    		//If no cast the result will always be 0.0!
    		slope = ((double) dy) / dx;
    	}
    		    	
    	return slope;
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
    	if(that == null){
    		throw new java.lang.NullPointerException();
    	}
    	
    	int result = 0;
    	
        if(this.y == that.y){
        	if(this.x < that.x){
        		result = -1;
        	} else if (this.x > that.x) {
        		result = 1;
        	} else if (this.x == that.x) {
        		result = 0;
        	}
        } else if(this.y < that.y) {
        	result = -1;
        } else if(this.y > that.y) {
        	result = 1;
        }
    	
    	return result;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    //Compare points p1 and p2 by the slopes they make with the invoking point.
    //This comparator is an inner class so every point instance will have its
    //own comparator.
    private class slopeOrderComparator implements Comparator<Point> {
    	public int compare(Point p1, Point p2){
    		if(p1 == null || p2 == null){
        		throw new java.lang.NullPointerException();
        	}
    		
    		int result = 0;
    		double slopeP0P1 = Point.this.slopeTo(p1);
    		double slopeP0P2 = Point.this.slopeTo(p2);
    		
    		if(slopeP0P1 < slopeP0P2){
    			result = -1;
    		} else if(slopeP0P1 > slopeP0P2){
    			result = 1;    			
    		} else {
    			result = 0;
    		}
    		
    		return result;
    	}
    }
}
