package main.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import bank.Account;
import bank.BankSystem;
import pages.ApplyAccountPage;
import pages.CustomerBankingPage;
import pages.DepositPage;
import pages.EmployeeBankingPage;
import pages.LoginPage;
import pages.Page;
import pages.RegisterPage;
import pages.TransferPage;
import pages.WithdrawPage;
import users.Customer;
import users.User;
import users.User.USER;

public class Test {
	public static void main(String[] args) {
		/*
		 * testBankSystem.userPassList.put("jtran", "pass");
		 * testBankSystem.userPassList.put("jbtran64", "password");
		 */
		boolean validLogin = false;
		boolean loggedIn = false;
		boolean usingSystem = true;
		byte input = 0;

		User currentUser;
		Account currentAccount;
		List<Account> accountList = new ArrayList<>();

		LoginPage lPage = new LoginPage();

		while (!loggedIn) {
			Page.newPage();
			lPage.displayChoices();

			do {
				input = getInput();
				if (input < 0 && input > 4)
					System.out.println("Invalid choice");
			} while (input < 0 && input > 4);

			switch (input) {
			case 1:
				validLogin = false;
				Page.newPage();
				while (!validLogin) {
					validLogin = lPage.login(testBankSystem.userPassList);
					if (!validLogin)
						System.out.println("Invalid username or password");
				}
				loggedIn = true;
				break;
			case 2:
				Page.newPage();
				RegisterPage rPage = new RegisterPage();
				rPage.getUserInfo(testBankSystem.userPassList);
				// Insert new user into users table and customer table
				break;
			case 3:
				validLogin = false;
				LoginPage loginPage = new LoginPage();
				while (!validLogin) {
					validLogin = loginPage.login(testBankSystem.userPassList);
					if (!validLogin)
						System.out.println("Invalid username or password");
				}
				ApplyAccountPage aAPage = new ApplyAccountPage();
				aAPage.getPersonInfo();
				// Create new application and add to database
				break;
			default:
				System.exit(0);
			}
		}

		currentUser = lPage.getCurrentUser();
		accountList = BankSystem.getUserAccount().get(currentUser);
		if (validLogin && accountList.size() > 0) {
			while (usingSystem) {

				Page.newPage();
				if (currentUser.getUserType() == USER.CUSTOMER) {
					CustomerBankingPage cbPage = new CustomerBankingPage();
					cbPage.displayChoices();
					do {
						input = getInput();
						if (input < 0 && input > 7)
							System.out.println("Invalid choice");
					} while (input < 0 && input > 7);

					switch (input) {
					case 1:
						currentAccount = chooseAccount(accountList);

						if (currentAccount.getBalance() == 0)
							System.out.println("Your balance is zero");
						else {
							double withdrawAmt = 0;
							WithdrawPage wPage = new WithdrawPage();
							withdrawAmt = wPage.getWithdrawalAmt(currentAccount);
							// Call withdraw from bank system
						}
						break;
					case 2:
						currentAccount = chooseAccount(accountList);
						
						double depositAmt = 0;
						DepositPage dPage = new DepositPage();
						depositAmt = dPage.getDepoistAmt(currentAccount);
						// Call deposit from bank system
						break;
					case 3:
						currentAccount = chooseAccount(accountList);
						
						if (currentAccount.getBalance() == 0)
							System.out.println("Your balance is zero");
						else {
							double transferAmt = 0;
							long transferAccount = 0;
							boolean transferExists = false;
							String accNum;

							while (!transferExists) {
								System.out.println("Please enter account number to transfer money to: ");

								try {
									accNum = Page.sc.nextLine();
									transferAccount = Long.parseLong(accNum);
								} catch (NumberFormatException e) {
									System.out.println("Invalid input");
								}
								final Long transferAcct = new Long(transferAccount);
								/*
								 * Object[] objArr = BankSystem.getUserAccount().entrySet().stream() .filter(x
								 * -> x.getValue().getAccountNumber() == transferAcct).toArray(); if
								 * (objArr.length == 1) transferExists = true;
								 */
							}
							TransferPage tPage = new TransferPage();
							tPage.getTransferAmt(currentAccount);
						}
						break;
					case 4:
						currentAccount = chooseAccount(accountList);
						
						System.out.println("Current balance: " + currentAccount.getBalance());
						break;
					case 5:

						break;
					default:
						System.exit(0);
					}
				} else if (currentUser.getUserType() == USER.EMPLOYEE) {
					EmployeeBankingPage eBPage = new EmployeeBankingPage();
					eBPage.displayChoices();
					
					do {
						input = getInput();
						if (input < 0 && input > 4)
							System.out.println("Invalid choice");
					} while (input < 0 && input > 4);
					
					switch (input) {
					case 1:
						
						break;
					case 2:
						
						break;
					default:
						System.exit(0);
					}
					
				} else if (currentUser.getUserType() == USER.ADMIN) {

				}
			}
		}
	}

	private static byte getInput() {
		String choice;
		byte input;
		do {
			try {
				choice = Page.sc.nextLine();
				input = Byte.parseByte(choice);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		} while (true);
		return input;
	}

	private static Account chooseAccount(List<Account> accList) {
		if (accList.size() == 1)
			return accList.get(0);

		byte input;

		for (int i = 1; i <= accList.size(); i++) {
			System.out.println(i + ". " + accList.get(i - 1).getAccountNumber());
		}
		System.out.println("Please choose which account:");

		do {
			input = getInput();
			if (input < 0 && input > accList.size())
				System.out.println("Invalid choice");
		} while (input < 0 && input > accList.size());

		return accList.get(input);
	}

}

class testBankSystem {
	static Map<User, String> userPassList = new HashMap<>();

}
