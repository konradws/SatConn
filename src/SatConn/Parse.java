package SatConn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static SatConn.Math.*;

/**
 * Class for parsing formulas.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Parse {

	private static final String op = "-!() &/|AE+=TF";
	private static final String bo = "&|+=";

	private Parse x, y;
	private char t;
	private int n, q, v;
	private boolean f;
	private static ArrayList<Parse> l;

	static ArrayList<Character> vs;

	/**
	 * Parse a formula and calculate the represented relation.
	 *
	 * See resources/help.html (the help for the graphical tool) for the syntax.
	 */
	public static boolean[] parse(String f) {
		if (f.contains("&") || f.contains("|") || f.contains("/") || f.contains("+") || f.contains("=")) {
			f = f.replaceAll(" ", "");
			f = f.replaceAll("&&", "&");
			f = f.replaceAll("\\|\\|", "\\|");
		}
		Parse c = prs(f);
		return c == null ? null : c.brep();
	}

	private static Parse prs(String s) {
		try {
			s = s.trim().replaceAll("\\s+", " ");
			l = new ArrayList<>();
			vs = new ArrayList<>();
			ArrayList<Character> qs = new ArrayList<>();
			int q = -1;
			f:
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c == ' ' && q >= 0) {
					int m = 0;
					for (int k = q; k < i; k++) {
						char qu = s.charAt(k);
						for (k += 2; !op.contains(s.charAt(k) + ""); k++) {
							l.add(k + m++, new Parse(qu));
						}
					}
					q = -1;
					continue;
				}
				if (c == 'A' || c == 'E') {
					q = i;
				}
				if (op.contains(c + "")) {
					l.add(new Parse(c));
				}
				else if (Character.isLetterOrDigit(c)) {
					for (Parse t : l) {
						if (t.t == c) {
							l.add(t);
							continue f;
						}
					}
					vs.add(c);
					l.add(new Parse(c));
				}
				else {
					return null;
				}
			}
			ArrayList<Parse> ll = (ArrayList<Parse>) l.clone();
			w:
			while (l.size() > 1) {
				for (int i = 1; i < l.size() - 1; i++) {
					if (l.get(i - 1).t == '(' && l.get(i + 1).t == ')') {
						l.remove(i + 1);
						l.remove(i - 1);
						continue w;
					}
				}
				for (int i = 0; i < l.size() - 1; i++) {
					if (!l.get(i).f && l.get(i).t == '!' && l.get(i + 1).f) {
						l.get(i).x = l.get(i + 1);
						l.get(i).f = true;
						l.remove(i + 1);
						continue w;
					}
				}
				for (int i = 0; i < l.size() - 1; i++) {
					if (l.get(i).f && l.get(i + 1).f
									&& !(i > 0 && (l.get(i - 1).t == 'A' || l.get(i - 1).t == 'E'))) {
						l.add(i, new Parse('|'));
						l.get(i).x = l.get(i + 1);
						l.get(i).y = l.get(i + 2);
						l.get(i).f = true;
						l.remove(i + 1);
						l.remove(i + 1);
						continue w;
					}
				}
				for (char c : bo.toCharArray()) {
					for (int i = 1; i < l.size() - 1; i++) {
						if (!l.get(i).f && l.get(i).t == c && l.get(i + 1).f && l.get(i - 1).f
										&& !(i > 1 && (l.get(i - 2).t == 'A' || l.get(i - 2).t == 'E'))) {
							l.get(i).x = l.get(i - 1);
							l.get(i).y = l.get(i + 1);
							l.get(i).f = true;
							l.remove(i + 1);
							l.remove(i - 1);
							continue w;
						}
					}
				}
				if ((l.get(l.size() - 3).t == 'E' || l.get(l.size() - 3).t == 'A') && l.get(l.size() - 1).f) {
					int i = l.size() - 3;
					l.get(i).x = l.get(i + 1);
					l.get(i).y = l.get(i + 2);
					vs.remove((Object) l.get(i).x.t);
					qs.add(l.get(i).x.t);
					l.get(i).f = true;
					l.remove(i + 1);
					l.remove(i + 1);
					continue w;
				}
				return null;
			}
			Collections.sort(vs);
			for (Parse t : ll) {
				int i = vs.indexOf(t.t);
				t.v = i >= 0 ? i : qs.indexOf(t.t) + vs.size();
			}
			l.get(0).n = vs.size();
			l.get(0).q = qs.size();
			return l.get(0);
		} catch (Exception e) {
			return null;
		}
	}

	private Parse(char tt) {
		t = tt;
		t = t == ' ' ? '&' : t == '/' ? '|' : t == '-' ? '!' : t;
		if (!op.contains(t + "")) {
			f = true;
		}
	}

	private boolean[] brep() {
		boolean[] b = new boolean[e2i(n)];
		boolean[] r = new boolean[n];
		for (int i = 0; i < e2i(n); i++) {
			for (int j = 0; j < n; j++) {
				r[j] = (i >> j) % 2 == 1;
			}
			if (eval(r)) {
				b[i] = true;
			}
		}
		return b;
	}

	private boolean eval(boolean[] b) {
		if (q > 0) {
			b = Arrays.copyOf(b, n + q);
		}
		if (t == '!') {
			return !x.eval(b);
		}
		else if (t == '&') {
			return x.eval(b) && y.eval(b);
		}
		else if (t == '|') {
			return x.eval(b) || y.eval(b);
		}
		else if (t == '=') {
			return x.eval(b) == y.eval(b);
		}
		else if (t == '+') {
			return x.eval(b) != y.eval(b);
		}
		else if (t == 'T') {
			return true;
		}
		else if (t == 'F') {
			return false;
		}
		else if (t == 'A') {
			b[x.v] = true;
			if (y.eval(b)) {
				b[x.v] = false;
				return y.eval(b);
			}
			return false;
		}
		else if (t == 'E') {
			b[x.v] = true;
			if (y.eval(b)) {
				return true;
			}
			b[x.v] = false;
			return y.eval(b);
		}
		else {
			return b[v];
		}
	}
}
