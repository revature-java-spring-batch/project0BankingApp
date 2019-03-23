package users;

public class Customer extends User{
	private String firstName;
	private String lastName;
	private String address;
	private String phoneNumber;
	
	public Customer(String user, String pass, USER type, String firstName, String lastName, String address, String phoneNumber) {
		super(user, pass, type);
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	public String getAddress() {
		return address;
	}


	public String getPhoneNumber() {
		return phoneNumber;
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
	
	public static void main(String[] args) {
		
	}
}
