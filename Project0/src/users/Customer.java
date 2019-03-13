package users;

public class Customer extends User{
	private String firstName;
	private String lastName;
	
	public Customer(String user, String pass, USER type, String first, String last) {
		super(user, pass, type);
		firstName = first;
		lastName = last;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
