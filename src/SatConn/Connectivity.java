package SatConn;

import static SatConn.Math.*;
import static SatConn.Relations.*;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Provides connectivity-related functions.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Connectivity {

	public static void main(String[] args) {
		boolean[] r = rel("110 100 001 011");
		for (boolean[] i : comps(r)) {
			out.println(string(i));
		}
	}

	/**
	 * Checks if the vectors v and w are connected in the solution graph of r.
	 */
	public static boolean conn(boolean[] r, int v, int w) {
		boolean[] g = r.clone();
		LinkedList<Integer> front = new LinkedList<>();
		front.add(v);
		int n = l2(r.length);
		g[v] = false;
		while (!front.isEmpty()) {
			int k = front.poll();
			for (int j = 0; j < n; j++) {
				int u = k;
				u += bit(k, j) ? -e2(j) : e2(j);
				if (u == w) {
					return true;
				}
				if (g[u]) {
					front.add(u);
					g[u] = false;
				}
			}
		}
		return false;
	}

	/**
	 * Calculates the connected components of r.
	 */
	public static ArrayList<boolean[]> comps(boolean[] r) {
		boolean[] g = r.clone();
		LinkedList<Integer> front = new LinkedList<>();
		ArrayList<boolean[]> l = new ArrayList<>();
		int n = l2(r.length);
		for (int i = 0; i < r.length; i++) {
			if (g[i]) {
				boolean[] a = new boolean[r.length];
				front.clear();
				front.add(i);
				a[i] = true;
				g[i] = false;
				while (!front.isEmpty()) {
					int k = front.poll();
					for (int j = 0; j < n; j++) {
						int v = k;
						v += bit(k, j) ? -e2(j) : e2(j);
						if (g[v]) {
							front.add(v);
							a[v] = true;
							g[v] = false;
						}
					}
				}
				l.add(a);
			}
		}
		return l;
	}
}
