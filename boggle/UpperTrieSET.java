/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class UpperTrieSET implements Iterable<String> {

    private static final int R = 26;
    private Node root;

    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public UpperTrieSET() {
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("Null Argument!");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d + 1);
    }

    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) x.isString = true;
        }
        else {
            char c = key.charAt(d);
            x.next[c - 65] = add(x.next[c - 65], key, d + 1);
        }
        return x;
    }

    public boolean hasPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        return (x != null);
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c + 65);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    public static void main(String[] args) {
        // intentional empty main method
    }
}
