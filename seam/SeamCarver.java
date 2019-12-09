/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;

public class SeamCarver {
    private int width;
    private int height;
    private Picture currentPic;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Null Picture!");
        currentPic = new Picture(picture);
        width = currentPic.width();
        height = currentPic.height();
        energy = new double[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (col == 0 || col == width - 1 || row == 0 || row == height - 1)
                    energy[col][row] = 1000.0;
                else {
                    Color colorUpper = currentPic.get(col, row - 1);
                    Color colorLower = currentPic.get(col, row + 1);
                    Color colorLeft = currentPic.get(col - 1, row);
                    Color colorRight = currentPic.get(col + 1, row);
                    double contrast = Math
                            .sqrt(Math.pow((colorUpper.getRed() - colorLower.getRed()), 2) + Math
                                    .pow((colorUpper.getGreen() - colorLower.getGreen()), 2) + Math
                                    .pow((colorUpper.getBlue() - colorLower.getBlue()), 2) + Math
                                    .pow((colorLeft.getRed() - colorRight.getRed()), 2) + Math
                                    .pow((colorLeft.getGreen() - colorRight.getGreen()), 2) + Math
                                    .pow((colorLeft.getBlue() - colorRight.getBlue()), 2));
                    energy[col][row] = contrast;
                }
            }
        }
    }

    // current picture
    public Picture picture() {
        return currentPic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            throw new IllegalArgumentException("Index out of bound!");
        }
        return energy[x][y];
    }

    // sequence of indices for horizontal seam
    // need to
    public int[] findHorizontalSeam() {
        int V = width * height + 2;
        int[] seam = new int[width];
        double[] weight = new double[V];
        for (int v = 0; v < width * height; v++) {
            int col = v % width;
            int row = v / width;
            weight[v] = energy(col, row);
        }
        weight[width * height] = 0.0;
        weight[width * height + 1] = 0.0;

        VertexWeightedDAG G = new VertexWeightedDAG(V, weight);
        for (int v = 0; v < width * height; v++) {
            int col = v % width;
            int row = v / width;
            if (col == width - 1) G.addEdge(new DiEdge(v, width * height + 1));
            else if (height == 1) G.addEdge(new DiEdge(v, v + 1));
            else if (row == 0) {
                G.addEdge(new DiEdge(v, v + 1));
                G.addEdge(new DiEdge(v, v + width + 1));
            }
            else if (row == height - 1) {
                G.addEdge(new DiEdge(v, v + 1));
                G.addEdge(new DiEdge(v, v - width + 1));
            }
            else {
                G.addEdge(new DiEdge(v, v + 1));
                G.addEdge(new DiEdge(v, v + width + 1));
                G.addEdge(new DiEdge(v, v - width + 1));
            }
            if (col == 0) G.addEdge(new DiEdge(width * height, v));
        }

        SP sp = new SP(G, width * height);
        int i = 0;
        for (DirectedEdge e : sp.pathTo(width * height + 1)) {
            int v = e.to();
            seam[i] = v / width;
            if (i == width - 1) break;
            i++;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int V = width * height + 2;
        int[] seam = new int[height];
        double[] weight = new double[V];
        for (int v = 0; v < width * height; v++) {
            int col = v % width;
            int row = v / width;
            weight[v] = energy(col, row);
        }
        weight[width * height] = 0.0;
        weight[width * height + 1] = 0.0;

        VertexWeightedDAG G = new VertexWeightedDAG(V, weight);
        for (int v = 0; v < width * height; v++) {
            int col = v % width;
            int row = v / width;
            if (row == height - 1) G.addEdge(new DiEdge(v, width * height + 1));
            else if (width == 1) G.addEdge(new DiEdge(v, v + width));
            else if (col == 0) {
                G.addEdge(new DiEdge(v, v + width));
                G.addEdge(new DiEdge(v, v + width + 1));
            }
            else if (col == width - 1) {
                G.addEdge(new DiEdge(v, v + width));
                G.addEdge(new DiEdge(v, v + width - 1));
            }
            else {
                G.addEdge(new DiEdge(v, v + width));
                G.addEdge(new DiEdge(v, v + width + 1));
                G.addEdge(new DiEdge(v, v + width - 1));
            }
            if (row == 0) G.addEdge(new DiEdge(width * height, v));
        }

        SP sp = new SP(G, width * height);
        int i = 0;
        for (DirectedEdge e : sp.pathTo(width * height + 1)) {
            int v = e.to();
            seam[i] = v % width;
            if (i == height - 1) break;
            i++;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Null Arguement!");
        if (seam.length != width)
            throw new IllegalArgumentException("Length of argument array is not correct!");
        if (!isValidSeam(seam)) throw new IllegalArgumentException("Invalid Seam!");
        if (height <= 1) throw new IllegalArgumentException("Not enough height!");

        Picture newPic = new Picture(width, height - 1);
        for (int col = 0; col < width; col++) {
            int v = seam[col];
            if (v > 0) {
                for (int row = 0; row < v; row++) newPic.set(col, row, currentPic.get(col, row));
            }
            for (int row = v; row < height - 1; row++) {
                Color color = currentPic.get(col, row + 1);
                newPic.set(col, row, color);
                energy[col][row] = energy[col][row + 1];
            }
        }
        currentPic = new Picture(newPic);
        height--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Null Arguement!");
        if (seam.length != height)
            throw new IllegalArgumentException("Length of argument array is not correct!");
        if (!isValidSeam(seam)) throw new IllegalArgumentException("Invalid Seam!");
        if (width <= 1) throw new IllegalArgumentException("Not enough height!");

        Picture newPic = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            int v = seam[row];
            if (v > 0) {
                for (int col = 0; col < v; col++) newPic.set(col, row, currentPic.get(col, row));
            }
            for (int col = v; col < width - 1; col++) {
                Color color = currentPic.get(col + 1, row);
                newPic.set(col, row, color);
                energy[col][row] = energy[col + 1][row];
            }
        }
        currentPic = new Picture(newPic);
        width--;
    }

    private boolean isValidSeam(int[] seam) {
        if (seam.length == 1) return true;
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) >= 2) return false;
        }
        return true;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());

        Stopwatch sw = new Stopwatch();
        SeamCarver sc1 = new SeamCarver(picture);
        SeamCarver sc2 = new SeamCarver(picture);

        Picture p1 = sc1.picture();
        Picture p2 = sc2.picture();

        // int[] hseam = sc.findHorizontalSeam();
        // sc.removeHorizontalSeam(hseam);
        StdOut.println(p1.equals(p2));
        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
    }
}
