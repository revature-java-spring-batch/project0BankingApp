package pages;

import repository.BankAppRepository;
import users.User;

public class LoginPage implements ChoicePage {
	private User currentUser;

	@Override
	public void displayChoices() {
		System.out.println("1. Login \n2. Register \n3. Apply For Account \n4. Exit");
	}

	public boolean login() {
		String user, pass;
		System.out.print("Username: ");
		user = Page.sc.next();

		System.out.print("Password: ");
		pass = Page.sc.next();

		// Query database here to check for valid username and password
		currentUser = BankAppRepository.verifyUser(user, pass);
		if (currentUser != null)
			return true;
		else
			return false;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
