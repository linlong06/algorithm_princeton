/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);
        int first = 0;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);

        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(input.charAt(csa.length() - 1));
            }
            else BinaryStdOut.write(input.charAt(csa.index(i) - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int N = t.length();
        char[] b = new char[N];
        for (int i = 0; i < N; i++) b[i] = t.charAt(i);
        Arrays.sort(b);

        int[] next = new int[N];
        int[] count = new int[R + 1];

        for (int i = 0; i < N; i++) {
            count[t.charAt(i) + 1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }
        for (int i = 0; i < N; i++) {
            next[count[t.charAt(i)]++] = i;
        }

        int number = 0;
        for (int i = first; number < N; i = next[i]) {
            BinaryStdOut.write(b[i]);
            number++;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal Argument!");
    }
}
