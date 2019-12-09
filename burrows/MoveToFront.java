/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class MoveToFront {
    private static final int R = 256;
    private static final int lgR = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        ArrayList<Character> chars = new ArrayList<Character>();
        for (int i = 0; i < R; i++) {
            chars.add(i, (char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(lgR);
            int i = chars.indexOf(c);
            BinaryStdOut.write(i, lgR);
            chars.remove(i);
            chars.add(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        ArrayList<Character> chars = new ArrayList<Character>();
        for (int i = 0; i < R; i++) {
            chars.add(i, (char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt(lgR);
            char c = chars.get(i);
            BinaryStdOut.write(c, lgR);
            chars.remove(i);
            chars.add(0, c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal Argument!");
    }
}
