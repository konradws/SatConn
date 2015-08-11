package SatConn;

import static SatConn.Math.*;
import static SatConn.Relations.*;
import static java.lang.System.*;
import java.util.ArrayList;

/**
 * Class to generate simplified CNF-formulas from relations.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Cnf {

	public static void main(String[] args) {
		out.println(cnf(rel("000 111"), true));
	}

	/**
	 * Computes a simplified (though not necessarily minimal-length) CNF-formula
	 * representing relation r.
	 *
	 * @param sf whether to output a "standard" format, readable by e.g.
	 * Mathematica
	 */
	public static String cnf(boolean[] r, boolean sf) {
		ArrayList<int[]> l = new ArrayList<>();
		int n = l2(r.length);
		// 
		for (int i = 0; i < r.length; i++) {
			if (!r[i]) {
				int[] a = new int[n];
				l.add(a);
				for (int j = 0; j < n; j++) {
					a[j] = bit(i, j) ? 0 : 1;
				}
			}
		}
		boolean h = false;
		for (int i = 0; i < l.size(); i++) {
			int[] x = l.get(i);
			// remove superfluous literals from clause x
			for (int k = 0; k < n; k++) {
				if (x[k] >= 0) {
					int z = x[k];
					x[k] = -1;
					if (tst(x, r)) {
						h = true;
					}
					else {
						x[k] = z;
					}
				}
			}
			if (h) {
				o:
				// remove clauses implied by the (shortened) clause x
				for (int j = i + 1; j < l.size(); j++) {
					int[] y = l.get(j);
					for (int k = 0; k < x.length; k++) {
						if (!(x[k] < 0 || x[k] == y[k])) {
							continue o;
						}
					}
					l.remove(j);
					j--;
				}
				h = false;
			}
		}
		// remove remaining unneccessary clauses
		o:
		for (int i = 0; i < l.size(); i++) {
			f:
			for (int v = 0; v < r.length; v++) {
				if (!r[v]) {
					for (int j = 0; j < l.size(); j++) {
						if (i != j && !tst(l.get(j), v)) {
							continue f;
						}
					}
					continue o;
				}
			}
			l.remove(i);
			i--;
		}
		return string(l, sf);
	}

	// check whether clause x is true for vector v
	private static boolean tst(int[] x, int v) {
		for (int j = 0; j < x.length; j++) {
			if (x[j] == biti(v, j)) {
				return true;
			}
		}
		return false;
	}

	// check whether clause x is true for relation r
	private static boolean tst(int[] x, boolean[] r) {
		f:
		for (int i = 0; i < r.length; i++) {
			if (r[i]) {
				for (int j = 0; j < x.length; j++) {
					if (x[j] == biti(i, j)) {
						continue f;
					}
				}
				return false;
			}
		}
		return true;
	}

	private static String string(ArrayList<int[]> a, boolean sf) {
		if (a.isEmpty()) {
			return "";
		}
		StringBuilder s = new StringBuilder(sf ? "(" : "");
		char b = (char) (a.get(0).length > 3 ? 'z' - a.get(0).length + 1 : 'x');
		for (int[] r : a) {
			for (int i = 0; i < r.length; i++) {
				if (r[i] >= 0) {
					if (sf) {
						s.append((r[i] == 1 ? "" : "!") + (char) (i + b) + "||");
					}
					else {
						s.append((r[i] == 1 ? "" : "-") + (char) (i + b));
					}
				}
			}
			if (sf) {
				s.delete(s.length() - 2, s.length());
			}
			s.append(sf ? ") && (" : " ");
		}
		return s.delete(s.length() - (sf ? 5 : 1), s.length()).toString();
	}
}
