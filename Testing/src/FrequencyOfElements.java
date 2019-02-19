public class FrequencyOfElements {
	public static void getFrequency(int a[]) {
		int fr[] = new int[a.length];
		int visited = -1;
		for (int i = 0; i <= a.length - 1; i++) {
			int count = 1;
			for (int j = i+1 ; j < a.length - 1; j++) {
				if (a[i] == a[j]) {
					count++;
					fr[j] = visited;
				}
			}
			if (fr[i] != visited)
				fr[i] = count;
		}
		for (int i = 0; i < fr.length; i++) {
			if (fr[i] != visited)
				System.out.println("    " + a[i] + "    |    " + fr[i]);
		}
	}

	public static void main(String[] args) {
		int arr[] = {  10, 10, 10, 10,20, 30, 30, 30, 20  };
		getFrequency(arr);
	}

}
