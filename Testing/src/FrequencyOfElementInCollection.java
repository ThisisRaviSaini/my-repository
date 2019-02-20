import java.util.ArrayList;
import java.util.Collections;

public class FrequencyOfElementInCollection {

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		list.add("Ravi");
		list.add("Saini");
		list.add("A");

		list2.add("Deepali");
		list2.add("Rathore");
		list2.add("B");

		System.out.println(Collections.frequency(list, "Ravi"));
		System.out.println(Collections.binarySearch(list, "Saini"));
		Collections.copy(list, list2);
		Collections.rotate(list2, 3);

	}

}
