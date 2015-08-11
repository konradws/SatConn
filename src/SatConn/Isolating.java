package SatConn;

import static java.lang.System.out;
import static SatConn.Math.*;
import static SatConn.Relations.*;

/**
 * Checks that there is a 1-isolating CNF({R})-formula for every 3-ary relation
 * R with 110 in R and 010 not in R, needed for the proof of Lemma 3.1.3.
 *
 * Running this file produces the output printed in Figure 3.1.1.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Isolating {

	public static void main(String[] args) {
		int n = 3; // Examine 3-ary Boolean relations
		for (int t = 0; t < e2(e2(n)); t++) { // There are 2^(2^n) n-ary relations;
			boolean[] p = rel(t, n); // the t's n-ary relation
			// only check the relations containing 110 and not containing 010
			if (p[vector("110")] && !p[vector("010")]) {
				out.println("{" + string(p) + "}:" + ivs(p));
			}
		}
	}

	private static String ivs(boolean[] r) {
		int n = l2(r.length);
		if (oneIso(r)) {
			return " already 1-isolating";
		}
		// try all identification of variables
		for (int i = 0; i < n - 1; i++) {
			for (int j = 1 + i; j < n; j++) {
				boolean[] v = rel(identify(vectors(r), i, j));
				String s = null;
				if (oneIso(v) || (s = cjs(v)) != null) {
					return " identify x" + (i + 1) + ",x" + (j + 1) + " "
									+ "-> {" + string(v) + "}" + (s == null ? "" : ", then" + s);
				}
			}
		}
		return cjs(r);
	}

	// try all conjunctions of two relations with permuted variables
	private static String cjs(boolean[] r) {
		int n = l2(r.length);
		int[] p = iniS(n);
		int[] pi = iniS(n);
		do {
			if (oneIso(cj(r, trf(r, p)))) {
				return " R(" + pr(pi) + ") AND R(" + pr(p) + ") = {" + string(cj(r, trf(r, p))) + "}";
			}
		} while (nextT(p, n));
		return null;
	}


	private static String pr(int[] a) {
		StringBuilder r = new StringBuilder("x");
		for (int i : a) {
			r.append((i + 1) + ",x");
		}
		r.delete(r.length() - 2, r.length());
		return r.toString();
	}
}
