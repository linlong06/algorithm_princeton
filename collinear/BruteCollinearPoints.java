/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 10/12/2019
 *  Description: Brute Collinear Points
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segmentsArray;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument is null!");
        }
        for (int p = 0; p < points.length; p++) {
            if (points[p] == null) {
                throw new IllegalArgumentException("Null points in the array!");
            }
        }

        Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy);
        if (hasDuplicates(pointsCopy)) {
            throw new IllegalArgumentException("Input points have duplicated values!");
        }

        segmentsArray = new ArrayList<LineSegment>();
        if (points.length < 4) {
            return;
        }

        for (int i = 0; i < pointsCopy.length - 3; i++) {
            for (int j = i + 1; j < pointsCopy.length - 2; j++) {
                for (int k = j + 1; k < pointsCopy.length - 1; k++) {
                    for (int s = k + 1; s < pointsCopy.length; s++) {
                        if (pointsCopy[i].slopeTo(pointsCopy[j]) == pointsCopy[i]
                                .slopeTo(pointsCopy[k])
                                && pointsCopy[i].slopeTo(pointsCopy[j]) == pointsCopy[i]
                                .slopeTo(pointsCopy[s])) {
                            segmentsArray.add(new LineSegment(pointsCopy[i], pointsCopy[s]));
                        }
                    }
                }
            }
        }
    }

    private boolean hasDuplicates(Point[] sortedPoints) {
        for (int i = 0; i < sortedPoints.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsArray.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segmentsArray.toArray(new LineSegment[segmentsArray.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        for (LineSegment segment : brute.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
