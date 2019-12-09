/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private final SET<Point2D> pointset;

    public PointSET() {
        pointset = new SET<Point2D>();
    }
    // construct an empty set of points

    public boolean isEmpty() {
        return pointset.isEmpty();
    }
    // is the set empty?

    public int size() {
        return pointset.size();
    }
    // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid Point!");
        pointset.add(p);
    }
    // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid Point!");
        return pointset.contains(p);
    }
    // does the set contain point p?

    public void draw() {
        for (Point2D point : pointset) {
            point.draw();
        }
    }
    // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Invalid Rectangular!");
        if (isEmpty()) return null;
        SET<Point2D> rangeset = new SET<Point2D>();
        for (Point2D point : pointset) {
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax() && point.y() >= rect.ymin()
                    && point.y() <= rect.ymax()) {
                rangeset.add(point);
            }
        }
        return rangeset;
    }
    // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid Point!");
        if (isEmpty()) return null;
        Point2D nearestPoint = pointset.min();
        for (Point2D point : pointset) {
            if (point.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

            StdDraw.show();
            StdDraw.pause(40);
        }
    }
    // unit testing of the methods (optional)
}
