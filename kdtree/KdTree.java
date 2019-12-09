/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private static final boolean ODD = true;
    private static final boolean EVEN = false;
    private Node root;
    private ArrayList<Point2D> rangeset;
    private Point2D nearest;

    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private boolean level;
        private int count;

        public Node(Point2D point) {
            this.point = point;
        }
    }

    private boolean isOdd(Node x) {
        if (x == null) return false;
        return x.level == ODD;
    }


    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid Point!");
        if (contains(p)) return;
        root = insert(root, p);
    }

    private Node insert(Node node, Point2D p) {
        if (node == null) {
            Node a = new Node(p);
            a.level = ODD;
            a.count = 1;
            return a;
        }
        if (isOdd(node)) {
            if (p.x() < node.point.x()) node.left = insert(node.left, p);
            else if (p.x() > node.point.x()) node.right = insert(node.right, p);
            else if (p.y() < node.point.y()) node.left = insert(node.left, p);
            else if (p.y() > node.point.y()) node.right = insert(node.right, p);
            else node.point = p;
        }
        else {
            if (p.y() < node.point.y()) node.left = insert(node.left, p);
            else if (p.y() > node.point.y()) node.right = insert(node.right, p);
            else if (p.x() < node.point.x()) node.left = insert(node.left, p);
            else if (p.x() > node.point.x()) node.right = insert(node.right, p);
            else node.point = p;
        }
        if (isOdd(node) && isOdd(node.left)) node.left.level = EVEN;
        if (isOdd(node) && isOdd(node.right)) node.right.level = EVEN;
        node.count = 1 + size(node.left) + size(node.right);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid Point!");
        Node node = root;
        while (node != null) {
            if (isOdd(node)) {
                if (p.x() < node.point.x()) node = node.left;
                else if (p.x() > node.point.x()) node = node.right;
                else if (p.y() < node.point.y()) node = node.left;
                else if (p.y() > node.point.y()) node = node.right;
                else return true;
            }
            else {
                if (p.y() < node.point.y()) node = node.left;
                else if (p.y() > node.point.y()) node = node.right;
                else if (p.x() < node.point.x()) node = node.left;
                else if (p.x() > node.point.x()) node = node.right;
                else return true;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
        StdDraw.show();
    }

    private void draw(Node node) {
        if (node == null) return;
        if (node.level == ODD) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.point.draw();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(node.point.x(), 0.0, node.point.x(), 1.0);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.point.draw();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(0.0, node.point.y(), 1.0, node.point.y());
        }
        draw(node.left);
        draw(node.right);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Invalid Rectangular!");
        if (isEmpty()) return null;
        rangeset = new ArrayList<Point2D>();
        range(rect, root);
        return rangeset;
    }

    private void range(RectHV rect, Node node) {
        if (node == null) return;
        if (rect.contains(node.point)) {
            rangeset.add(node.point);
        }
        if (node.level == ODD) {
            if (node.point.x() < rect.xmin()) range(rect, node.right);
            else if (node.point.x() > rect.xmax()) range(rect, node.left);
            else {
                range(rect, node.left);
                range(rect, node.right);
            }
        }
        else {
            if (node.point.y() < rect.ymin()) range(rect, node.right);
            else if (node.point.y() > rect.ymax()) range(rect, node.left);
            else {
                range(rect, node.left);
                range(rect, node.right);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Invalid Point!");
        if (isEmpty()) return null;
        nearest = root.point;
        nearest(p, root);
        return nearest;
    }

    private void nearest(Point2D p, Node node) {
        if (node == null) return;
        if (p.distanceTo(node.point) < p.distanceTo(nearest)) nearest = node.point;
        if (node.level == ODD) {
            if (p.x() <= node.point.x()) {
                nearest(p, node.left);
                if (Math.abs(p.x() - node.point.x()) < p.distanceTo(nearest))
                    nearest(p, node.right);
            }
            else {
                nearest(p, node.right);
                if (Math.abs(p.x() - node.point.x()) < p.distanceTo(nearest))
                    nearest(p, node.left);
            }
        }
        else {
            if (p.y() <= node.point.y()) {
                nearest(p, node.left);
                if (Math.abs(p.y() - node.point.y()) < p.distanceTo(nearest))
                    nearest(p, node.right);
            }
            else {
                nearest(p, node.right);
                if (Math.abs(p.y() - node.point.y()) < p.distanceTo(nearest))
                    nearest(p, node.left);
            }
        }
    }


    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        StdOut.println(kdtree.size());
        StdOut.println(kdtree.contains(new Point2D(0.75, 0.5)));

        // add a virtual rect
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        Iterable<Point2D> rangeset = kdtree.range(rect);
        for (Point2D point : rangeset) {
            StdOut.println(point.toString());
        }
    }

}

