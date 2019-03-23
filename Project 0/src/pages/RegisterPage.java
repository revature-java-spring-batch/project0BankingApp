package pages;

import java.util.Map;

import users.Customer;
import users.User;
import users.User.USER;

public class RegisterPage implements Page{
	private User newUser;
	private String userNameIn, passwordIn, firstName, lastName, address;
	private String phoneNumber;

	public void getUserInfo(Map<User, String> loginInfo) {
		boolean isValidUser = false, isValidPass = false;
		
		while(!isValidUser){
			System.out.print("Please enter a username: ");
			userNameIn = Page.sc.next();
			
			//Validate unique username here
			if(!loginInfo.containsKey(userNameIn))
				isValidUser = true;
			else
				System.out.println("Invalid Username");
		}
		
		while(!isValidPass) {
			System.out.print("Please enter a password: ");
			passwordIn = Page.sc.next();
			
			//Validate Password here
			if(passwordIn.length() > 8)
				isValidPass = true;
			else
				System.out.println("Invalid password");
		}
		
		newUser = new User(userNameIn, passwordIn);
	}
	
	public User getCurrentCustomer() {
		return newUser;
	}
}
