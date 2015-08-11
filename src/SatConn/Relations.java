package SatConn;

import static SatConn.Parse.*;
import static SatConn.Math.*;
import static java.lang.Integer.min;
import java.util.ArrayList;

/**
 * Provides functions directly related to Boolean vectors and relations.
 *
 * Boolean vectors are encoded in the bits of int-variables. n-ary relations are
 * represented as boolean arrays of length 2^n, signifying which vectors are in
 * the relation.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Relations {

	/**
	 * Compute the represented relation, either from a formula or a list of
	 * vectors.
	 *
	 * See resources/help.html (the help for the graphical tool) for the syntax.
	 */
	public static boolean[] rel(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '0' && c != '1' && c != ' ' && c != ',') {
				return parse(s);
			}
		}
		return relv(s);
	}

	/**
	 * String representation of a Boolean relation for printing.
	 */
	public static String string(boolean[] a) {
		StringBuilder s = new StringBuilder();
		int n = l2(a.length);
		for (int i = 0; i < a.length; i++) {
			if (a[i]) {
				for (int j = 0; j < n; j++) {
					s.append((i >> j) % 2 == 0 ? '0' : '1');
				}
				s.append(' ');
			}
		}
		if (s.length() > 0) {
			s.deleteCharAt(s.length() - 1);
		}
		return s.toString();
	}

	// get the represented vector
	public static int vector(String s) {
		int r = 0;
		s = s.trim();
		int d = s.length();
		for (int j = 0; j < d; j++) {
			if (s.charAt(j) == '1') {
				r += e2(j);
			}
		}
		return r;
	}

	// get the relation represented by a list of vectors
	public static boolean[] relv(String s) {
		try {
			String[] a = s.split("[ ,]");
			int d = a[0].length();
			boolean[] r = new boolean[e2i(d)];
			for (String e : a) {
				if (e.length() > 0) {
					int v = 0;
					for (int j = 0; j < d; j++) {
						if (e.charAt(j) == '1') {
							v += e2(j);
						}
						else if (e.charAt(j) != '0') {
							return null;
						}
					}
					r[v] = true;
				}
			}
			return r;
		} catch (Exception e) {
			return null;
		}
	}

	// the inverse of a relation
	public static boolean[] invFnc(boolean[] a) {
		if (a == null) {
			return null;
		}
		boolean[] r = new boolean[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = !a[i];
		}
		return r;
	}

	// remove fictional variables from a relation
	public static boolean[] remFic(boolean[] t) {
		int m = l2(t.length);
		t:
		while (true) {
			f:
			for (int j = 0; j < m; j++) {
				for (int p = 0; p < e2(m); p++) {
					if (t[p]) {
						if (!t[bit0(p, j)] || !t[bit1(p, j)]) {
							continue f;
						}
					}
				}
				t = remVar(t, j);
				m--;
				continue t;
			}
			break t;
		}
		return t;
	}

	// existentially quantify over variable v
	public static boolean[] remVar(boolean[] r, int v) {
		int n = l2(r.length);
		boolean[] s = new boolean[e2i(n - 1)];
		for (int p = 0; p < e2(n); p++) {
			if (r[p]) {
				s[bitR(p, v)] = true;
			}
		}
		return s;
	}

	// the Hamming distance between vectors x and y
	public static int hamm(int x, int y) {
		int d = x ^ y;
		int n = 0;
		for (int i = 0; i < 32; i++) {
			if ((d >> i) % 2 == 1) {
				n++;
			}
		}
		return n;
	}

	// bit b of vector v
	static boolean bit(int v, int b) {
		return (v >> b) % 2 == 1;
	}

	// bit b of vector v
	static int biti(int v, int b) {
		return (v >> b) % 2;
	}

	// set bit b of vector v to 0
	static int bit0(int v, int b) {
		return (v >> b) % 2 == 1 ? v - (1 << b) : v;
	}

	// set bit b of vector v to 1
	static int bit1(int v, int b) {
		return (v >> b) % 2 == 0 ? v + (1 << b) : v;
	}

	// remove bit b from vector v
	static int bitR(int v, int b) {
		return ((v >> (1 + b)) << b) + (v % e2i(b));
	}

	// decode the lowest d bits of a to a boolean array
	static boolean[] rel(long a, int d) {
		boolean[] r = new boolean[e2i(d)];
		for (int i = 0; i < e2(d); i++) {
			r[i] = (a >> i) % 2 == 1;
		}
		return r;
	}

	// get the vectors of a relation
	static int[] vectors(boolean[] a) {
		if (a == null) {
			return null;
		}
		int l = 0;
		for (boolean b : a) {
			if (b) {
				l++;
			}
		}
		int[] r = new int[l + 1];
		int k = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i]) {
				r[k++] = i;
			}
		}
		r[k] = l2(a.length); // the last element stores the dimension
		return r;
	}

	// get the relation from a list of vectors
	static boolean[] rel(int[] a) {
		if (a == null) {
			return null;
		}
		int d = a[a.length - 1];
		boolean[] r = new boolean[e2i(d)];
		for (int i = 0; i < a.length - 1; i++) {
			r[a[i]] = true;
		}
		return r;
	}

	// check whether the relation obtained by r by permutation and
	// identification according to s contains v
	static boolean chkc(int v, boolean[] r, int[] s) {
		n:
		for (int k = 0; k < r.length; k++) {
			if (r[k]) {
				for (int m = 0; m < s.length; m++) {
					if (bit(v, s[m]) != bit(k, m)) {
						continue n;
					}
				}
				return true;
			}
		}
		return false;
	}

	// check if the relation r is 1-isolating
	static boolean oneIso(boolean[] r) {
		for (int i = 1; i < r.length; i++) {
			if (r[i] && isolated(r, i)) {
				return true;
			}
		}
		return false;
	}

	// check if the vector e is isolated in the relation r
	static boolean isolated(boolean[] r, int e) {
		for (int i = 0; i < r.length; i++) {
			if (r[i] && hamm(i, e) == 1) {
				return false;
			}
		}
		return true;
	}

	// the "conjunction", i.e. intersection, of relations
	static boolean[] cj(boolean[]  
		... r) {
    boolean[] t = new boolean[min(r[0].length, r[1].length)];
		f:
		for (int v = 0; v < t.length; v++) {
			for (int k = 0; k < r.length; k++) {
				if (!r[k][v]) {
					continue f;
				}
			}
			t[v] = true;
		}
		return t;
	}

	// the relation obtained from r identification and permutation of variables according to p
	static boolean[] trf(boolean[] r, int[] p) {
		boolean[] b = new boolean[r.length];
		for (int v = 0; v < r.length; v++) {
			if (chkc(v, r, p)) {
				b[v] = true;
			}
		}
		return b;
	}

	// identify the i'th an j'th variables in the relation r
	static int[] identify(int[] r, int i, int j) {
		ArrayList<Integer> t = new ArrayList<>();
		for (int k = 0; k < r.length - 1; k++) {
			if (bit(r[k], i) == bit(r[k], j)) {
				t.add(bitR(r[k], j));
			}
		}
		t.add(r[r.length - 1] - 1);
		return array(t);
	}

	static int[] array(ArrayList<Integer> a) {
		int[] r = new int[a.size()];
		for (int i = 0; i < a.size(); i++) {
			r[i] = a.get(i);
		}
		return r;
	}
}
