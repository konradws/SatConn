package SatConn;

import java.util.ArrayList;
import java.util.Arrays;
import static SatConn.Check.*;
import static SatConn.Cnf.cnf;
import static SatConn.Math.*;
import static SatConn.Relations.*;
import static java.lang.System.*;

/**
 * Provides functions to traverse and filter relations.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Sift {

	// finds not safely tight relations that are nc-CPS (according to Lemma 3.1.11)
	public static void main(String[] args) {
		list(r -> (qcbj(r) || qcim(r) || qcip(r)) && !stight(r),
						r -> {
							out.println(string(r));
							out.println("   " + cnf(r, false));
						}, 5, true);
	}

	interface Test {
		boolean test(boolean[] r);
	}

	interface Use {
		void use(boolean[] r);
	}

	/**
	 * Iterates over all relations with up to n variables.
	 *
	 * Every 30 seconds the progress is displayed.
	 *
	 * @param t relations for which t evaluates to false are skipped
	 * @param u procedure to perform on matching relations
	 * @param n maximal dimension
	 * @param filter whether to exfiltrate duplicates (relations equivalent up to
	 * permutation of variables)
	 * @return relations satisfying t
	 */
	public static ArrayList<boolean[]> list(Test t, Use u, int n, boolean filter) {
		long tt = System.currentTimeMillis();
		ArrayList<boolean[]> l = new ArrayList<>();
		for (int d = 1; d <= n; d++) {
			out.println("n=" + d);
			o:
			for (long f = 0; f < e2(e2(d)); f++) {
				boolean[] r = rel(f, d);
				if (System.currentTimeMillis() - tt > 30000) {
					tt = System.currentTimeMillis();
					out.println("% " + f * 100 / e2(e2(d)));
				}
				if (t.test(r)) {
					if (filter) {
						for (boolean[] a : l) {
							if (eqp(a, r)) {
								continue o;
							}
						}
					}
					if (u != null) {
						u.use(r);
					}
					l.add(r);
				}
			}
		}
		return l;
	}

	/**
	 * Checks whether relation r equals s up to permutation of variables.
	 */
	public static boolean eqp(boolean[] r, boolean[] s) {
		if (r.length != s.length) {
			return false;
		}
		int[] p = iniS(l2(r.length));
		int[] v = vectors(r);
		do {
			if (Arrays.equals(rel(v), s)) {
				return true;
			}
		} while (nextP(p, v));
		return false;
	}
}
