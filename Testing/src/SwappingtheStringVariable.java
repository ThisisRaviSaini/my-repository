
public class SwappingtheStringVariable {

	public static void main(String[] args) {
		String firstName = "Ravi";
		String lastName = "Saini";
		firstName = firstName + lastName;
		lastName = firstName.substring(0, firstName.indexOf(lastName));// ravi
		firstName = firstName.substring(lastName.length()); // Saini
		System.out.println(firstName + lastName);
		
		
		
		

	}

}
