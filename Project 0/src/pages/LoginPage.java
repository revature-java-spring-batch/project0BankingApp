package pages;

import java.util.HashMap;
import java.util.Map;

import bank.BankSystem;
import main.driver.Test;
import users.User;

public class LoginPage implements ChoicePage{
	private User currentUser;
	@Override
	public void displayChoices() {
		System.out.println("1. Login \n2. Register \n3. Apply For Account \n4. Exit");
	}
	
	public boolean login(Map<User, String> loginInfo) {
		String user, pass;
		System.out.print("Username: ");
		user = Page.sc.next();
		
		System.out.print("Password: ");
		pass = Page.sc.next();
		
		Object[] objArr = loginInfo.entrySet().stream().filter(x->(x.getKey().getUserName().equals(user) && x.getKey().getPassword().equals(pass))).toArray();
		if (objArr.length == 1) {
			currentUser = new User(user, pass);
			return true;
		}
		else
			return false;
		
	}
	
	public User getCurrentUser() {
		return currentUser;
	}

	public static void main(String[] args) {
		Map<User, String> map = new HashMap<>();
		User temp = new User("jtran", "pass");
		map.put(temp, "test");
		LoginPage lp = new LoginPage();
		if(lp.login(map))
			System.out.println("success");
		else
			System.out.println("fail");
	}
}


