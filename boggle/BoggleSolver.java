/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final UpperTrieSET dictionary;
    private SET<String> validWords;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new UpperTrieSET();
        for (int i = 0; i < dictionary.length; i++) {
            this.dictionary.add(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new SET<String>();
        int m = board.rows();
        int n = board.cols();
        if (m == 1 && n == 1) return null;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                String word = "";
                char letter = board.getLetter(i, j);
                if (letter == 'Q') word += "QU";
                else word += letter;
                Node root = new Node(i, j);
                boolean[][] marked = new boolean[m][n];
                marked[i][j] = true;
                checkNext(board, root, word, marked);
            }
        }
        return validWords;
    }

    private void checkNext(BoggleBoard board, Node node, String word, boolean[][] marked) {
        for (Node neighbor : neighbors(board, node)) {
            if (marked[neighbor.getRow()][neighbor.getCol()]) continue;
            String wordCopy = word;
            char letter = board.getLetter(neighbor.getRow(), neighbor.getCol());
            if (letter == 'Q') wordCopy += "QU";
            else wordCopy += letter;
            boolean[][] markedCopy = new boolean[board.rows()][board.cols()];
            for (int i = 0; i < board.rows(); i++) {
                for (int j = 0; j < board.cols(); j++) {
                    markedCopy[i][j] = marked[i][j];
                }
            }
            if (wordCopy.length() >= 3 && dictionary.contains(wordCopy) && !validWords
                    .contains(wordCopy))
                validWords.add(wordCopy);
            if (hasPrefix(wordCopy)) {
                markedCopy[neighbor.getRow()][neighbor.getCol()] = true;
                checkNext(board, neighbor, wordCopy, markedCopy);
            }
        }
    }

    private boolean hasPrefix(String word) {
        return (dictionary.hasPrefix(word));
    }

    private Iterable<Node> neighbors(BoggleBoard board, Node node) {
        int m = board.rows();
        int n = board.cols();
        int row = node.getRow();
        int col = node.getCol();
        Bag<Node> neighbors = new Bag<Node>();
        if (m == 1 && n == 1) throw new IllegalArgumentException("Board with only one dice!");
        else if (m == 1) {
            if (col == 0) neighbors.add(new Node(row, col + 1));
            else if (col == n - 1) neighbors.add(new Node(row, col - 1));
            else {
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row, col - 1));
            }
        }
        else if (n == 1) {
            if (row == 0) neighbors.add(new Node(row + 1, col));
            else if (row == m - 1) neighbors.add(new Node(row - 1, col));
            else {
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row - 1, col));
            }
        }
        else {
            if (row == 0 && col == 0) {
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row + 1, col + 1));
            }
            else if (row == m - 1 && col == 0) {
                neighbors.add(new Node(row - 1, col));
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row - 1, col + 1));
            }
            else if (row == 0 && col == n - 1) {
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row, col - 1));
                neighbors.add(new Node(row + 1, col - 1));
            }
            else if (row == m - 1 && col == n - 1) {
                neighbors.add(new Node(row - 1, col));
                neighbors.add(new Node(row, col - 1));
                neighbors.add(new Node(row - 1, col - 1));
            }
            else if (row == 0) {
                neighbors.add(new Node(row, col - 1));
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row + 1, col - 1));
                neighbors.add(new Node(row + 1, col + 1));
            }
            else if (row == m - 1) {
                neighbors.add(new Node(row, col - 1));
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row - 1, col));
                neighbors.add(new Node(row - 1, col - 1));
                neighbors.add(new Node(row - 1, col + 1));
            }
            else if (col == 0) {
                neighbors.add(new Node(row - 1, col));
                neighbors.add(new Node(row - 1, col + 1));
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row + 1, col + 1));
            }
            else if (col == n - 1) {
                neighbors.add(new Node(row - 1, col));
                neighbors.add(new Node(row - 1, col - 1));
                neighbors.add(new Node(row, col - 1));
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row + 1, col - 1));
            }
            else {
                neighbors.add(new Node(row - 1, col - 1));
                neighbors.add(new Node(row - 1, col));
                neighbors.add(new Node(row - 1, col + 1));
                neighbors.add(new Node(row, col - 1));
                neighbors.add(new Node(row, col + 1));
                neighbors.add(new Node(row + 1, col - 1));
                neighbors.add(new Node(row + 1, col));
                neighbors.add(new Node(row + 1, col + 1));
            }
        }
        return neighbors;
    }

    private class Node {
        private final int row;
        private final int col;

        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word) || word.length() < 3) return 0;
        else if (word.length() <= 4) return 1;
        else if (word.length() == 5) return 2;
        else if (word.length() == 6) return 3;
        else if (word.length() == 7) return 5;
        else return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        // BoggleBoard board = new BoggleBoard(args[1]);
        for (int i = 0; i < 1000; i++) {
            BoggleBoard board = new BoggleBoard();
            int score = 0;
            for (String word : solver.getAllValidWords(board)) {
                StdOut.println(word);
                score += solver.scoreOf(word);
            }
            StdOut.println("Test: " + i + " Score = " + score);
        }
    }
}
