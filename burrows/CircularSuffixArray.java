/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private final int N;
    private final String s;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Null String!");
        this.s = s;
        N = s.length();
        index = new int[N];
        int[] a = new int[N];
        for (int i = 0; i < N; i++) {
            a[i] = i;
        }
        suffixSort(a);
        for (int i = 0; i < N; i++) {
            index[i] = a[i];
        }
    }

    private void suffixSort(int[] a) {
        if (s.length() <= 1) return;
        int[] aux = new int[a.length];
        suffixSort(a, aux, 0, N - 1);
    }

    private void suffixSort(int[] a, int[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        suffixSort(a, aux, lo, mid);
        suffixSort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    private void merge(int[] a, int[] aux, int lo, int mid, int hi) {
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid + 1, hi);

        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (suffixCompare(aux[i], aux[j]) > 0) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    private int suffixCompare(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) throw new IllegalArgumentException();
        if (i == j) return 0;
        for (int k = 0; k < N; k++) {
            if (s.charAt((i + k) % N) < s.charAt((j + k) % N)) return -1;
            else if (s.charAt((i + k) % N) > s.charAt((j + k) % N)) return +1;
        }
        return 0;
    }

    private boolean isSorted(int[] a, int lo, int hi) {
        int n = a.length;
        if (lo < 0 || hi < 0 || hi >= n || lo > hi) throw new IllegalArgumentException();
        if (lo == hi) return true;
        for (int i = lo; i < hi; i++) {
            if (suffixCompare(a[i], a[i + 1]) > 0) return false;
        }
        return true;
    }

    // length of s
    public int length() {
        return N;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= N) throw new IllegalArgumentException("Index is out of bound!");
        else return (index[i]);
    }

    // unit testing (required)
    public static void main(String[] args) {
        // In in = new In(args[0]);
        // String text = in.readAll();
        String text = args[0];
        CircularSuffixArray csa = new CircularSuffixArray(text);
        StdOut.println(csa.length());
        for (int i = 0; i < csa.length(); i++) {
            StdOut.println(csa.index(i));
            // StdOut.println(text.charAt(csa.index(i)));
        }
    }
}
