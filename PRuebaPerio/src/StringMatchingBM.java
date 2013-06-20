/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Carlos Basso
 */
import java.util.*;
public class StringMatchingBM {
    

/**
 * String matching Boyer-Moore.
 */

	public static final int ALPHABET_SIZE = 2;
	private int[] bmBC;
	private int[] bmGs;
	public int comparisons;
	
	public void preBmBc(char[] x) {
		
		int m = x.length;
		bmBC = new int[ALPHABET_SIZE];

		Arrays.fill(bmBC, m);

		for (int i = 0; i < m - 1; i++) {
			bmBC[x[i]] = m - i - 1;
		}
	}

	public int[] suffixes(char[] x) {
		
		int m = x.length;
		int suff[] = new int[m];
		int g = m - 1;
		int f = m - 1;

		suff[m - 1] = m;

		for (int i = m - 2; i >= 0; --i) {
			if (i > g && (i + m - 1 - f) < m && suff[i + m - 1 - f] < i - g) {
				suff[i] = suff[i + m - 1 - f];
			} else {
				//if (i < g) {
				g = i;
				//}
				f = g;

				while (g >= 0 && x[g] == x[g + m - 1 - f]) {
					--g;
				}

				suff[i] = f - g;

			}
		}

		return suff;
	}

	public void preBmGs(char[] x) {
		
		int m = x.length;
		bmGs = new int[m];

		int suff[] = suffixes(x);

		Arrays.fill(bmGs, m);

		int j = 0;

		for (int i = m - 1; i >= -1; --i) {
			if (i == -1 || suff[i] == i + 1) {
				for (; j < m - 1 - i; ++j) {
					if (bmGs[j] == m) {
						bmGs[j] = m - 1 - i;
					}
				}
			}
		}

		for (int i = 0; i < m - 1; i++) {
			bmGs[m - 1 - suff[i]] = m - 1 - i;
		}
		

	}

	
	public List<Integer> search(String text, String pattern) {

		char[] y = text.toCharArray();
		char[] x = pattern.toCharArray();
		int n = y.length; // string length
		int m = x.length; // pattern length
		List<Integer> resultado = new ArrayList<Integer>();

		int j = 0;
		int i = 0;
		comparisons = 0;

		/* Precompute */
		preBmBc(x);
		preBmGs(x);	

		/* Searching */
		while (j <= n - m) {
			for (i = m - 1; i >= 0 && x[i] == y[i + j]; i--) {
				comparisons++;
			}

			if (i < 0) {
				resultado.add(j);
				j += bmGs[0];
			} else {
				j += Math.max(bmGs[i], bmBC[y[i + j]] - m + 1 + i);				
			}

		}

		return resultado;
	}

	public static void main(String args[]) {

		String text = "1010101010010101010010101001010101001010100101010101010100101010100101010010101010000101001111111111111101010100101011101010010100101001010010010100101001010010100101010010100101001010100101010010111000100100100100101010010101010010101001010101010100101010101001010110010101010101010100101010100101100101010";
		String pattern = "00011";

		StringMatchingBM bm = new StringMatchingBM();
		
		// Expected: [20, 67, 114, 161, 208, 255, 302, 349, 396, 443, 490, 537]
		System.out.printf("%s%n",bm.search(text, pattern));

	}

}

