package pages;

import dto.PersonalInfo;

public class ApplyAccountPage {
	private PersonalInfo personInfo;
	
	void retrievePersonalInfo() {
		String firstName = null, lastName = null, address = null, phoneNumber = null;
		
		boolean isValidFirst = false, isValidLast = false, isValidAddress = false, isValidPhoneNum = false;
		
		while(!isValidFirst) {
			System.out.print("Please enter your first name: ");
			firstName = Page.sc.next();
			
			if(firstName.length() != 0)
				isValidFirst = true;
			else
				System.out.println("Invalid first name");
		}
		
		while(!isValidLast) {
			System.out.print("Please enter your last name: ");
			lastName = Page.sc.next();
			
			if(lastName.length() != 0)
				isValidLast = true;
			else
				System.out.println("Invalid last name");
		}
		
		while(!isValidAddress) {
			System.out.print("Please enter your Address: ");
			address = Page.sc.nextLine();
			
			if(address.length() != 0)
				isValidAddress = true;
			else
				System.out.println("Invalid address");
		}
		
		while(!isValidPhoneNum) {
			System.out.print("Please enter your phone number: ");
			phoneNumber = Page.sc.next();
			
			if(phoneNumber.length() == 10)
				isValidPhoneNum = true;
			else
				System.out.println("Invalid phone number");
		}
		
		personInfo = new PersonalInfo(firstName, lastName, address, phoneNumber);
	}

	public PersonalInfo getPersonInfo() {
		return personInfo;
	}
}
