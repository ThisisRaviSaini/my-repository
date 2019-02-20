import java.util.ArrayList;
import java.util.Collections;

public class FrequencyOfElementInCollection {

	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("Ravi");
		list.add("Ravi");
		System.out.println(Collections.frequency(list, "Ravi"));

	}

}
