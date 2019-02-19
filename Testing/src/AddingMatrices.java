
public class AddingMatrices {

	public static int[][] addMatrices(int a[][], int b[][]) {
		int c[][] = new int[3][2];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				c[i][j] = a[i][j] + b[i][j];

			}
		}

		return c;
	}

	public static void main(String[] args) {

		int a[][] = { { 10, 20 }, { 30, 40 }, { 50, 60 } };
		int b[][] = { { 10, 20 }, { 30, 40 }, { 50, 60 } };
		int c[][] = addMatrices(a, b);
		for (int i = 0; i < 3; i++) {
			System.out.println();
			for (int j = 0; j < 2; j++) {
				System.out.print(c[i][j]+" ");

			}
		}

	}

}
