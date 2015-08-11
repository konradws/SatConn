package SatConn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static java.lang.System.*;
import static SatConn.Relations.*;
import static SatConn.Math.*;

/**
 * Provides functions to calculate the clone and co-clone of relations.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Clones {

	public static void main(String[] args) {
		boolean[] r = rel("x-y-z -xz");
		out.println(Clone(r) + " " + coClone(r));
	}

	/**
	 * Calculates the clone of a Boolean relation.
	 */
	public static String Clone(boolean[] f) {
		HashSet<String> h = new HashSet<>();
		LinkedList<String> l = new LinkedList<>();
		l.add("BF");
		String s = "";
		while (!l.isEmpty()) {
			s = l.poll();
			for (String t : down(s)) {
				if (t.equals("S0_3") && check("S0", f, h)) {
					l.add("S0");
					h.add("S0");
				}
				else if (t.equals("S1_3") && check("S1", f, h)) {
					l.add("S1");
					h.add("S1");
				}
				else if (check(t, f, h) && !l.contains(t)) {
					l.add(t);
					h.add(t);
				}
			}
		}
		return s;
	}

	/**
	 * Calculates the co-clone of a Boolean relation.
	 */
	public static String coClone(boolean[] f) {
		HashSet<String> h = new HashSet<>();
		LinkedList<String> l = new LinkedList<>();
		l.add("I2");
		String s = "I2";
		while (!l.isEmpty()) {
			s = l.poll();
			for (String t : up(s)) {
				if (closed(t, f, h) && !l.contains(t)) {
					l.add(t);
					h.add(t);
				}
			}
			if (s.equals("S00")) {
				int i = 2;
				for (; !closed("S00_" + i, f, h); i++);
				s = "S00_" + i;
				l.add(s);
				h.add(s);
			}
			else if (s.equals("S10")) {
				int i = 2;
				for (; !closed("S10_" + i, f, h); i++);
				s = "S10_" + i;
				l.add(s);
				h.add(s);
			}
		}
		return s;
	}

	// check whether f is in the clone c, given the set h of all larger clones containing f
	private static boolean check(String c, boolean[] f, HashSet<String> h) {
		String[] l = up(c);
		int nvecs = f.length;
		int nc = l2(nvecs);
		if (l != null && l.length > 1) {
			// all clones with at least two next-larger clones are the intersections of those
			return h.contains(l[0]) && h.contains(l[1]);
		}
		else if (c.equals("BF")) {
			return true;
		}
		else if (c.equals("R0")) {
			return !f[0];
		}
		else if (c.equals("R1")) {
			return f[nvecs - 1];
		}
		else if (c.equals("S0")) {
			s:
			for (int i = 0; i < nc; i++) {
				for (int j = 0; j < nvecs; j++) {
					if (!f[j] && bit(j, i)) {
						continue s;
					}
				}
				return true;
			}
			return false;
		}
		else if (c.equals("S02")) {
			return h.contains("S02_2") && h.contains("S0");
		}
		else if (c.equals("S01")) {
			return h.contains("S01_2") && h.contains("S0");
		}
		else if (c.equals("S12")) {
			return h.contains("S12_2") && h.contains("S1");
		}
		else if (c.equals("S11")) {
			return h.contains("S11_2") && h.contains("S1");
		}
		else if (c.startsWith("S0_")) {
			int i = Integer.parseInt(c.substring(3));
			return nsep(f, 0, i);
		}
		else if (c.equals("S1")) {
			s:
			for (int i = 0; i < nc; i++) {
				for (int j = 0; j < nvecs; j++) {
					if (f[j] && !bit(j, i)) {
						continue s;
					}
				}
				return true;
			}
			return false;
		}
		else if (c.startsWith("S1_")) {
			int i = Integer.parseInt(c.substring(3));
			return nsep(f, 1, i);
		}
		else if (c.equals("E")) {
			boolean[] g = remFic(f);
			int d = g.length;
			if (!g[d - 1]) {
				return false;
			}
			for (int i = 0; i < d - 1; i++) {
				if (g[i]) {
					return false;
				}
			}
			return true;
		}
		else if (c.equals("V")) {
			boolean[] g = remFic(f);
			int d = g.length;
			if (g[0]) {
				return false;
			}
			for (int i = 1; i < d; i++) {
				if (!g[i]) {
					return false;
				}
			}
			return true;
		}
		else if (c.equals("N")) {
			boolean[] g = remFic(f);
			return g.length == 2 && g[0] && !g[1];
		}
		else if (c.equals("L")) {
			boolean[] g = remFic(f);
			int p = -1;
			for (int i = 0; i < g.length; i++) {
				int n = 0;
				for (int j = 0; j < l2(g.length); j++) {
					if (bit(i, j)) {
						n++;
					}
				}
				if (g[i]) {
					if (p == -1) {
						p = n % 2;
					}
					else if (n % 2 != p) {
						return false;
					}
				}
				else if (p == -1) {
					p = (1 + n) % 2;
				}
				else if ((n + 1) % 2 != p) {
					return false;
				}
			}
			return true;
		}
		else if (c.equals("M")) {
			for (int i = 0; i < nvecs; i++) {
				if (f[i]) {
					for (int j = 0; j < nc; j++) {
						if (!bit(i, j)) {
							if (!f[i + e2i(j)]) {
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		else if (c.equals("D")) {
			for (int i = 0; i < nvecs / 2; i++) {
				if (!(f[i] && !f[nvecs - 1 - i] || !f[i] && f[nvecs - 1 - i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	// check whether f is closed under c, i.e. whether f is in the co-clone of c
	static boolean closed(String c, boolean[] f, HashSet<String> h) {
		String[] l = down(c);
		if (c.startsWith("S01_")) {
			int n = Integer.parseInt(c.substring(4));
			return h.contains("S00_" + n) && h.contains("S01");
		}
		else if (c.startsWith("S02_")) {
			int n = Integer.parseInt(c.substring(4));
			return h.contains("S00_" + n) && h.contains("S02");
		}
		else if (c.startsWith("S0_")) {
			int n = Integer.parseInt(c.substring(3));
			return h.contains("S02_" + n) && h.contains("S0");
		}
		else if (c.startsWith("S11_")) {
			int n = Integer.parseInt(c.substring(4));
			return h.contains("S10_" + n) && h.contains("S11");
		}
		else if (c.startsWith("S12_")) {
			int n = Integer.parseInt(c.substring(4));
			return h.contains("S10_" + n) && h.contains("S12");
		}
		else if (c.startsWith("S1_")) {
			int n = Integer.parseInt(c.substring(3));
			return h.contains("S12_" + n) && h.contains("S1");
		}
		else if (l != null && l.length > 1 && !(c.equals("S00_2") || c.equals("S10_2"))) {
			return h.contains(l[0]) && h.contains(l[1]);
		}
		else {
			for (String b : base(c)) {
				if (!Check.closed(f, rel(b))) {
					return false;
				}
			}
			return true;
		}
	}

	// get next-smaller clones
	private static String[] down(String c) {
		if (c.equals("BF")) {
			return new String[]{"R1", "R0", "M", "D", "L"};
		}
		else if (c.equals("R1")) {
			return new String[]{"S0_2", "L1", "M1", "R2"};
		}
		else if (c.equals("R0")) {
			return new String[]{"S1_2", "L0", "M0", "R2"};
		}
		else if (c.equals("M")) {
			return new String[]{"V", "E", "M1", "M0"};
		}
		else if (c.equals("D")) {
			return new String[]{"D1", "L3"};
		}
		else if (c.equals("L")) {
			return new String[]{"L1", "L3", "L0", "N"};
		}
		else if (c.equals("M1")) {
			return new String[]{"S01_2", "E1", "M2"};
		}
		else if (c.equals("M0")) {
			return new String[]{"M2", "S11_2", "V0"};
		}
		else if (c.equals("M2")) {
			return new String[]{"S00_2", "S10_2"};
		}
		else if (c.equals("R2")) {
			return new String[]{"S02_2", "S12_2", "D1", "M2"};
		}
		else if (c.equals("S0")) {
			return new String[]{"S02", "S01"};
		}
		else if (c.equals("S02")) {
			return new String[]{"S00"};
		}
		else if (c.equals("S01")) {
			return new String[]{"S00", "V1"};
		}
		else if (c.equals("S00")) {
			return new String[]{"V2"};
		}
		else if (c.startsWith("S0_")) {
			int i = Integer.parseInt(c.substring(3));
			return new String[]{"S0_" + (i + 1), "S02_" + i, "S01_" + i};
		}
		else if (c.startsWith("S02_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S02_" + (i + 1), "S00_" + i};
		}
		else if (c.startsWith("S01_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S01_" + (i + 1), "S00_" + i};
		}
		else if (c.equals("S00_2")) {
			return new String[]{"S00_3", "D2"};
		}
		else if (c.startsWith("S00_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S00_" + (i + 1)};
		}
		else if (c.equals("S1")) {
			return new String[]{"S12", "S11"};
		}
		else if (c.equals("S12")) {
			return new String[]{"S10"};
		}
		else if (c.equals("S11")) {
			return new String[]{"S10", "E0"};
		}
		else if (c.equals("S10")) {
			return new String[]{"E2"};
		}
		else if (c.startsWith("S1_")) {
			int i = Integer.parseInt(c.substring(3));
			return new String[]{"S1_" + (i + 1), "S12_" + i, "S11_" + i};
		}
		else if (c.startsWith("S12_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S12_" + (i + 1), "S10_" + i};
		}
		else if (c.startsWith("S11_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S11_" + (i + 1), "S10_" + i};
		}
		else if (c.equals("S10_2")) {
			return new String[]{"S10_3", "D2"};
		}
		else if (c.startsWith("S10_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S10_" + (i + 1)};
		}
		else if (c.equals("V")) {
			return new String[]{"V1", "V0", "I"};
		}
		else if (c.equals("V1")) {
			return new String[]{"V2", "I1"};
		}
		else if (c.equals("V0")) {
			return new String[]{"V2", "I0"};
		}
		else if (c.equals("V2")) {
			return new String[]{"I2"};
		}
		else if (c.equals("E")) {
			return new String[]{"E1", "E0", "I"};
		}
		else if (c.equals("E1")) {
			return new String[]{"E2", "I1"};
		}
		else if (c.equals("E0")) {
			return new String[]{"E2", "I0"};
		}
		else if (c.equals("E2")) {
			return new String[]{"I2"};
		}
		else if (c.equals("D1")) {
			return new String[]{"D2", "L2"};
		}
		else if (c.equals("D2")) {
			return new String[]{"I2"};
		}
		else if (c.equals("L1")) {
			return new String[]{"L2", "I1"};
		}
		else if (c.equals("L3")) {
			return new String[]{"L2", "N2"};
		}
		else if (c.equals("L0")) {
			return new String[]{"L2", "I0"};
		}
		else if (c.equals("L2")) {
			return new String[]{"I2"};
		}
		else if (c.equals("N")) {
			return new String[]{"I", "N2"};
		}
		else if (c.equals("N2")) {
			return new String[]{"I2"};
		}
		else if (c.equals("I")) {
			return new String[]{"I1", "I0"};
		}
		else if (c.equals("I1")) {
			return new String[]{"I2"};
		}
		else if (c.equals("I0")) {
			return new String[]{"I2"};
		}
		else if (c.equals("I2")) {
			return new String[]{};
		}
		return null;
	}

	// get next-larger clones
	private static String[] up(String c) {
		ArrayList<String> a = up.get(c);
		if (a != null && !c.startsWith("S10_3") && !c.startsWith("S00_3")
						&& !c.startsWith("S11_3") && !c.startsWith("S01_3")
						&& !c.startsWith("S12_3") && !c.startsWith("S02_3")) {
			return arr(a);
		}
		else if (c.equals("BF")) {
			return new String[]{};
		}
		else if (c.equals("S0") || c.equals("S1")) {
			return new String[]{};
		}
		else if (c.startsWith("S0_")) {
			int i = Integer.parseInt(c.substring(3));
			return new String[]{"S0_" + (i - 1)};
		}
		else if (c.startsWith("S02_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S02_" + (i - 1), "S0_" + i};
		}
		else if (c.startsWith("S01_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S01_" + (i - 1), "S0_" + i};
		}
		else if (c.startsWith("S00_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S00_" + (i - 1), "S02_" + i, "S01_" + i};
		}
		else if (c.startsWith("S1_")) {
			int i = Integer.parseInt(c.substring(3));
			return new String[]{"S1_" + (i - 1)};
		}
		else if (c.startsWith("S12_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S12_" + (i - 1), "S1_" + i};
		}
		else if (c.startsWith("S11_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S11_" + (i - 1), "S1_" + i};
		}
		else if (c.startsWith("S10_")) {
			int i = Integer.parseInt(c.substring(4));
			return new String[]{"S10_" + (i - 1), "S12_" + i, "S11_" + i};
		}
		return null;
	}

	static String[] base(String c) {
		if (c.equals("I2")) {
			return new String[]{"x"};
		}
		else if (c.equals("I1")) {
			return new String[]{"x", "T"};
		}
		else if (c.equals("I0")) {
			return new String[]{"x", "F"};
		}
		else if (c.equals("E2")) {
			return new String[]{"x&y"};
		}
		else if (c.equals("V2")) {
			return new String[]{"x|y"};
		}
		else if (c.equals("N2")) {
			return new String[]{"!x"};
		}
		else if (c.equals("L2")) {
			return new String[]{"x+y+z"};
		}
		else if (c.equals("D2")) {
			return new String[]{"x&y|y&z|z&x"};
		}
		else if (c.equals("S10")) {
			return new String[]{"x&(y|z)"};
		}
		else if (c.equals("S00")) {
			return new String[]{"x|y&z"};
		}
		else if (c.equals("S02")) {
			return new String[]{"x|y&-z"};
		}
		else if (c.equals("S12")) {
			return new String[]{"x&(y|-z)"};
		}
		else if (c.startsWith("S00_")) {
			int n = Integer.parseInt(c.substring(4));
			return new String[]{"x|y&z", dhn(n)};
		}
		else if (c.startsWith("S10_")) {
			int n = Integer.parseInt(c.substring(4));
			return new String[]{"x&(y|z)", hn(n)};
		}
		return null;
	}

	static String hn(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= n; j++) {
				if (i != j) {
					s.append(j + "&");
				}
			}
			s.replace(s.length() - 1, s.length(), "|");
		}
		s.delete(s.length() - 1, s.length());
		return s.toString();
	}

	static String dhn(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i <= n; i++) {
			s.append("(");
			for (int j = 0; j <= n; j++) {
				if (i != j) {
					s.append(j + "|");
				}
			}
			s.replace(s.length() - 1, s.length(), ")&");
		}
		s.delete(s.length() - 1, s.length());
		return s.toString();
	}

	// check if f is a-separating of degree n
	static boolean nsep(boolean[] f, int a, int n) {
		int[] sep = new int[l2(f.length) + 1];
		for (int dd = 1; dd <= n; dd++) {
			sep[0] = -1;
			if (!nsep(f, sep, a, dd, 0)) {
				return false;
			}
		}
		return true;
	}

	private static boolean nsep(boolean[] f, int[] s, int a, int n, int l) {
		int nvecs = f.length;
		int nc = l2(nvecs);
		if (n == l) {
			s:
			for (int i = 0; i < nc; i++) {
				for (int j = 0; j < n; j++) {
					if (a == 0 ? bit(s[j], i) : !bit(s[j], i)) {
						continue s;
					}
				}
				return nsep(f, s, a, n, l - 1);
			}
			return false;
		}
		else {
			for (int k = s[l] + 1; k < nvecs; k++) {
				if (a == 0 ? !f[k] : f[k]) {
					s[l] = k;
					s[l + 1] = k;
					return nsep(f, s, a, n, l + 1);
				}
			}
			if (l == 0) {
				return true;
			}
			else {
				return nsep(f, s, a, n, l - 1);
			}
		}
	}

	static String[] arr(ArrayList<String> a) {
		String[] r = new String[a.size()];
		for (int i = 0; i < a.size(); i++) {
			r[i] = a.get(i);
		}
		return r;
	}

	static final HashMap<String, ArrayList<String>> up = new HashMap<>();

	// calculate a directory of next-larger clones
	static {
		LinkedList<String> f = new LinkedList<>();
		f.add("BF");
		while (!f.isEmpty()) {
			String e = f.poll();
			if (e.equals("S0_3")) {
				f.add("S0");
			}
			else if (e.equals("S1_3")) {
				f.add("S1");
			}
			else if (e.equals("S12_3") || e.equals("S11_3") || e.equals("S10_3") || e.equals("S02_3") || e.equals("S01_3") || e.equals("S00_3")) {
			}
			else {
				for (String s : down(e)) {
					ArrayList<String> a = up.get(s);
					if (a == null) {
						a = new ArrayList<>();
						up.put(s, a);
					}
					if (!a.contains(e)) {
						a.add(e);
					}
					if (!f.contains(s)) {
						f.add(s);
					}
				}
			}
		}
	}
}
