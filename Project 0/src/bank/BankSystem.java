package bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dto.Account;
import users.User;

public class BankSystem {
	private static Map<User, ArrayList<Account>> userAccounts = new HashMap<>();
	
	
	void withdraw() {
		
	}
	
	public static Map<User, ArrayList<Account>> getUserAccount() {
		return userAccounts;
	}

	void deposit() {
		
	}
	
	void transfer() {
		
	}
	
	/*boolean verifyUser() {
		
	}*/
	
	
}
