/* *****************************************************************************
 *  Name: Long Lin
 *  Date: 10/12/2019
 *  Description: Fast Collinear Points
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segmentsArray;
    private Point origin;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument is null!");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
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

        // unique part of fast collinear algorithm
        for (int p = 0; p < points.length; p++) {
            origin = points[p];
            sortbySlope(pointsCopy);
            int x = 1;
            while (x < pointsCopy.length) {
                ArrayList<Point> candidates = new ArrayList<Point>();
                double slope = origin.slopeTo(pointsCopy[x]);
                while (x < pointsCopy.length && origin.slopeTo(pointsCopy[x]) == slope) {
                    candidates.add(pointsCopy[x++]);
                }
                if (candidates.size() >= 3 && origin.compareTo(min(candidates)) <= 0) {
                    segmentsArray.add(new LineSegment(origin, max(candidates)));
                }
            }
        }
    }


    private Point min(ArrayList<Point> pointsArray) {
        Point minPoint = pointsArray.get(0);
        for (int i = 1; i < pointsArray.size(); i++) {
            if (pointsArray.get(i).compareTo(minPoint) < 0) {
                minPoint = pointsArray.get(i);
            }
        }
        return minPoint;
    }

    private Point max(ArrayList<Point> pointsArray) {
        Point maxPoint = pointsArray.get(0);
        for (int i = 1; i < pointsArray.size(); i++) {
            if (pointsArray.get(i).compareTo(maxPoint) > 0) {
                maxPoint = pointsArray.get(i);
            }
        }
        return maxPoint;
    }

    private void sortbySlope(Point[] points) {
        Point[] aux = new Point[points.length];
        sort(points, aux, 0, points.length - 1);
    }

    private void merge(Point[] points, Point[] aux, int lo, int mid, int hi) {
        assert isSorted(points, mid + 1, hi);
        for (int k = lo; k <= hi; k++) aux[k] = points[k];
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) points[k] = aux[j++];
            else if (j > hi) points[k] = aux[i++];
            else if (origin.slopeTo(aux[j]) < origin.slopeTo(aux[i])) points[k] = aux[j++];
            else points[k] = aux[i++];
        }
        assert isSorted(points, lo, hi);
    }

    private void sort(Point[] points, Point[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(points, aux, lo, mid);
        sort(points, aux, mid + 1, hi);
        merge(points, aux, lo, mid, hi);
    }

    private boolean isSorted(Point[] points, int lo, int hi) {
        for (int i = lo; i < hi; i++) {
            if (origin.slopeTo(points[i + 1]) < origin.slopeTo(points[i])) {
                return false;
            }
        }
        return true;
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
        FastCollinearPoints fast = new FastCollinearPoints(points);
        for (LineSegment segment : fast.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
