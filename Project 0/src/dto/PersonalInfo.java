package dto;

public class PersonalInfo {
	private String firstName, lastName, address, phoneNumber;
	
	public PersonalInfo(String fName, String lName, String address, String pNum) {
		firstName = fName;
		lastName = lName;
		this.address = address;
		phoneNumber = pNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
}
