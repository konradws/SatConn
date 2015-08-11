package SatConn;

import static java.lang.Math.*;

public class Math {

	// 2^n
	static long e2(long n) {
		long r = 1;
		for (; n > 0; n--) {
			r *= 2;
		}
		return r;
	}

	// 2^n
	static int e2i(int n) {
		int r = 1;
		for (; n > 0; n--) {
			r *= 2;
		}
		return r;
	}

	// base-2 logarithm
	static int l2(long n) {
		int r = 0;
		for (; n > 1; r++) {
			n = n / 2;
		}
		return r;
	}

	static final double p2(double x) {
		return x * x;
	}

	// next tuple of a.lenght integers from {0,...,r-1}
	// (used to iterate over all arrangements with repetition of an r-element set)
	static boolean nextT(int[] a, int r) {
		if (a.length == 0) {
			return false;
		}
		int i = a.length - 1;
		for (; a[i] == r - 1; i--) {
			if (i == 0) {
				return false;
			}
		}
		a[i]++;
		for (i++; i < a.length; i++) {
			a[i] = 0;
		}
		return true;
	}

	// next identification of variables
	static boolean nextI(int[] a) {
		if (a.length == 0) {
			return false;
		}
		int i = a.length - 1;
		f:
		for (;; i--) {
			if (i == 0) {
				return false;
			}
			for (int j = 0; j < i; j++) {
				if (a[j] >= a[i]) {
					break f;
				}
			}
		}
		a[i]++;
		for (i++; i < a.length; i++) {
			a[i] = 0;
		}
		return true;
	}

	// [0,1,...,r-1]
	static int[] iniS(int r) {
		int[] a = new int[r];
		for (int i = 0; i < r; i++) {
			a[i] = i;
		}
		return a;
	}

	// next permutation of variables
	static boolean nextP(int[] c, int[] v) {
		int first = getFirst(c);
		if (first == -1) {
			return false;
		}
		int toSwap = c.length - 1;
		while (c[ first] >= c[ toSwap]) {
			--toSwap;
		}
		swap(c, first, toSwap);
		swapBit(v, first++, toSwap);
		toSwap = c.length - 1;
		while (first < toSwap) {
			swap(c, first, toSwap);
			swapBit(v, first++, toSwap--);
		}
		return true;
	}

	private static void swapBit(int[] v, int i, int j) {
		for (int k = 0; k < v.length - 1; k++) {
			if ((v[k] >> i) % 2 == 1 && (v[k] >> j) % 2 == 0) {
				v[k] = v[k] - e2i(i) + e2i(j);
			}
			else if ((v[k] >> i) % 2 == 0 && (v[k] >> j) % 2 == 1) {
				v[k] = v[k] + e2i(i) - e2i(j);
			}
		}
	}

	private static void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	private static int getFirst(int[] c) {
		for (int i = c.length - 2; i >= 0; --i) {
			if (c[i] < c[i + 1]) {
				return i;
			}
		}
		return -1;
	}

	static int mx(int[] a) {
		int r = 0;
		for (int k : a) {
			r = max(r, k);
		}
		return r;
	}
}
