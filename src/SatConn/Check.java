package SatConn;

import java.util.ArrayList;
import java.util.Arrays;
import static java.lang.System.*;
import static SatConn.Connectivity.*;
import static SatConn.Math.*;
import static SatConn.Relations.*;
import static SatConn.Sift.*;

/**
 * Provides functions to check properties of relations.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Check {

	public static void main(String[] args) {
		out.println(check(rel("x-y-z -xz")));
	}

	// compactly prints some important properties of a relation
	public static String check(boolean[] r) {
		return ((cpss(r) ? "C" : "c") + " "
						+ (schaefer(r) ? "S" : "s") + " "
						+ (stight(r) ? "ST" : "st") + " "
						+ (tight(r) ? "T" : "t") + "   "
						+ (bij(r) ? "BIJ" : "bij") + " "
						+ (scbj(r) ? "SCBIJ" : "scbij") + " "
						+ (cbij(r) ? "CBIJ" : "cbij") + "  "
						+ (im(r) ? "I-" : "i-") + " "
						+ (scim(r) ? "SCI-" : "sci-") + " "
						+ (cim(r) ? "CI-" : "ci-") + "  "
						+ (horn(r) ? "HORN" : "horn") + " "
						+ (affine(r) ? "AFFINE" : "affine") + " "
						+ (val(r) ? "VAL" : "val") + " "
						+ (compl(r) ? "COMPL" : "compl") + "  "
						+ (sorfree(r) ? "SORFREE" : "sorfree") + " "
						+ (orfree(r) ? "ORFREE" : "orfree"));
	}

	public static boolean tight(boolean[] r) {
		return orfree(r) || nfree(r) || cbij(r);
	}

	public static boolean stight(boolean[] r) { // safely tight
		return sorfree(r) || snfree(r) || scbj(r);
	}

	public static boolean schaefer(boolean[] r) {
		return horn(r) || dhorn(r) || affine(r) || bij(r);
	}

	public static boolean cpss(boolean[] r) {
		return horn(r) && cim(r) || dhorn(r) && cip(r) || affine(r) || bij(r);
	}

	public static boolean affine(boolean[] r) {
		boolean[] b = rel("x+y+z");
		return closed(r, b);
	}

	public static boolean horn(boolean[] r) {
		boolean[] b = rel("x y");
		return closed(r, b);
	}

	public static boolean dhorn(boolean[] r) { // dual Horn
		boolean[] b = rel("xy");
		return closed(r, b);
	}

	public static boolean bij(boolean[] r) { // bijunctive
		boolean[] b = rel("xy yz zx");
		return closed(r, b);
	}

	public static boolean im(boolean[] r) { // IHSB-
		boolean[] b = rel("x yz");
		return closed(r, b);
	}

	// check for IHSB+
	public static boolean ip(boolean[] r) { // IHSB+
		boolean[] b = rel("x/(y&z)");
		return closed(r, b);
	}

	public static boolean cbij(boolean[] r) { // componentwise bijunctive
		for (boolean[] i : comps(r)) {
			if (!bij(i)) {
				return false;
			}
		}
		return true;
	}

	public static boolean cim(boolean[] r) { // componentwise IHSB-
		for (boolean[] i : comps(r)) {
			if (!im(i)) {
				return false;
			}
		}
		return true;
	}

	public static boolean cip(boolean[] r) { // componentwise IHSB+
		for (boolean[] i : comps(r)) {
			if (!ip(i)) {
				return false;
			}
		}
		return true;
	}

	public static boolean orfree(boolean[] r) {
		return scf(vectors(r), vectors(rel("xy")));
	}

	public static boolean nfree(boolean[] r) { // NAND-free
		return scf(vectors(r), vectors(rel("!(x y)")));
	}

	public static boolean scbj(boolean[] r) { // safely componentwise bijunctive
		for (boolean[] b : idv(r)) {
			if (!cbij(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean scim(boolean[] r) { // safely componentwise IHSB-
		for (boolean[] b : idv(r)) {
			if (!cim(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean scip(boolean[] r) { // safely componentwise IHSB+
		for (boolean[] b : idv(r)) {
			if (!cip(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean sscbj(boolean[] r) {
		for (boolean[] b : idv(r)) {
			if (!Arrays.equals(b, r) && !scbj(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean sscim(boolean[] r) {
		for (boolean[] b : idv(r)) {
			if (!Arrays.equals(b, r) && !scim(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean sscip(boolean[] r) {
		for (boolean[] b : idv(r)) {
			if (!Arrays.equals(b, r) && !scip(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean qcbj(boolean[] r) { // quasi componentwise bijunctive
		return vall(r) && (scbj(r) || !conn(r, 0, r.length - 1) && sscbj(r));
	}

	public static boolean qcim(boolean[] r) {	// quasi componentwise IHSB-
		return vall(r) && (scim(r) || !conn(r, 0, r.length - 1) && sscim(r));
	}

	public static boolean qcip(boolean[] r) { // quasi componentwise IHSB+
		return vall(r) && (scip(r) || !conn(r, 0, r.length - 1) && sscip(r));
	}

	public static boolean sorfree(boolean[] r) { // safely OR-free
		for (boolean[] b : idv(r)) {
			if (!orfree(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean snfree(boolean[] r) { // safely NAND-free
		for (boolean[] b : idv(r)) {
			if (!nfree(b)) {
				return false;
			}
		}
		return true;
	}

	public static boolean val(boolean[] r) { // 0-valid or 1-valid
		return r[0] || r[r.length - 1];
	}

	public static boolean vall(boolean[] r) { // 0-valid and 1-valid
		return r[0] && r[r.length - 1];
	}

	public static boolean compl(boolean[] r) { // complementive
		for (int i = 0; i < r.length / 2; i++) {
			if (r[i] != r[r.length - i - 1]) {
				return false;
			}
		}
		return true;
	}

	// all relations producible from r by identification of variables
	public static ArrayList<boolean[]> idv(boolean[] r) {
		ArrayList<boolean[]> l = new ArrayList<>();
		int m = l2(r.length);
		int[] a = new int[m];
		d:
		do {
			int n = e2i(mx(a) + 1);
			boolean[] b = new boolean[n];
			for (int v = 0; v < n; v++) {
				if (chkc(v, r, a)) {
					b[v] = true;
				}
			}
			for (boolean[] x : l) {
				if (eqp(x, b)) {
					continue d;
				}
			}
			l.add(b);
		} while (nextI(a));
		return l;
	}

	/**
	 * Checks whether relation r is closed under the coordinate-wise application
	 * of function f (represented as relation).
	 */
	public static boolean closed(boolean[] r, boolean[] f) {
		int[] rr = vectors(r);
		int d = l2(r.length);
		int m = l2(f.length); // arity of f
		int n = rr.length - 1;
		if (n == 0) { // empty relation
			return true;
		}
		int[] a = new int[m];
		do { // iterate over all tuples of m vectors from r
			int c = 0; // result vector
			for (int p = 0; p < d; p++) { // iterate over the coordinates of r
				int v = 0; // "cross-vector" of the tuple
				for (int k = 0; k < m; k++) {
					if ((rr[a[k]] >> p) % 2 == 1) {
						v += e2(k);
					}
				}
				if (f[v]) { // f(v)=1 ?
					c += e2(p);
				}
			}
			if (!r[c]) { // result not in r
				return false;
			}
		} while (nextT(a, n));
		return true;
	}

	// check whether f is not producible from r by substitution of constants
	static boolean scf(int[] r, int[] f) {
		if (Arrays.equals(r, f)) {
			return false;
		}
		int n = r[r.length - 1];
		for (int i = 0; i < n; i++) {
			int[] v = subst(r, i, false);
			int[] w = subst(r, i, true);
			if (n > 3) {
				if (!scf(v, f) || !scf(w, f)) {
					return false;
				}
			}
			else {
				if (Arrays.equals(v, f) || Arrays.equals(w, f)) {
					return false;
				}
			}
		}
		return true;
	}

	// in the relation r, substitute bit i with the constant c
	static int[] subst(int[] r, int i, boolean c) {
		ArrayList<Integer> t = new ArrayList<>();
		for (int k = 0; k < r.length - 1; k++) {
			if (bit(r[k], i) == c) {
				t.add(bitR(r[k], i));
			}
		}
		t.add(r[r.length - 1] - 1);
		return array(t);
	}
}
