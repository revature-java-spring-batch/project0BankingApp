package pages;

import repository.BankAppRepository;
import users.User;

public class RegisterPage implements Page {
	private User newUser;
	private String userNameIn, passwordIn;

	public void getUserInfo() {
		boolean isValidUser = false, isValidPass = false;

		while (!isValidUser) {
			System.out.print("Please enter a username: ");
			userNameIn = Page.sc.next();

			// Validate unique username here
			if (BankAppRepository.verifyUserName(userNameIn))
				isValidUser = true;
			else
				System.out.println("Invalid Username");
		}

		while (!isValidPass) {
			System.out.print("Please enter a password: ");
			passwordIn = Page.sc.next();

			// Validate Password here
			if (passwordIn.length() > 8)
				isValidPass = true;
			else
				System.out.println("Invalid password");
		}

		// insert new user into users table
		BankAppRepository.addNewUser(userNameIn, passwordIn);
	}

	public User getCurrentCustomer() {
		return newUser;
	}
}
