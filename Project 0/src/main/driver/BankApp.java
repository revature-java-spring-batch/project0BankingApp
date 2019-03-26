package main.driver;

import java.util.List;
import dto.Account;
import pages.ApplyAccountPage;
import pages.BankAccountInfoPage;
import pages.CustomerBankingPage;
import pages.DepositPage;
import pages.EmployeeBankingPage;
import pages.JointAccountPage;
import pages.JointApplicationsPage;
import pages.LoginPage;
import pages.OpenApplicationPage;
import pages.Page;
import pages.RegisterPage;
import pages.TransferPage;
import pages.WithdrawPage;
import repository.BankAppRepository;
import users.User;
import users.User.USER;

public class BankApp {
	public enum TRANSACTION_TYPE{
		WITHDRAW, DEPOSIT, TRANSFER
	}
	public static void main(String[] args) {
		boolean validLogin = false;
		boolean loggedIn = false;
		boolean usingSystem = true;
		byte input = 0;

		User currentUser;
		Account currentAccount;
		List<Account> accountList;

		LoginPage lPage = new LoginPage();

		while (!loggedIn) {
			Page.newPage();
			lPage.displayChoices();

			do {
				input = getInput();
				if (input < 0 || input > 4)
					System.out.println("Invalid choice");
			} while (input < 0 || input > 4);

			switch (input) {
			case 1:
				validLogin = false;
				Page.newPage();
				while (!validLogin) {
					validLogin = lPage.login();
					if (!validLogin)
						System.out.println("Invalid username or password");
				}
				loggedIn = true;
				break;
			case 2:
				Page.newPage();
				RegisterPage rPage = new RegisterPage();
				rPage.getUserInfo();
				break;
			case 3:
				validLogin = false;
				LoginPage loginPage = new LoginPage();
				while (!validLogin) {
					validLogin = loginPage.login();
					if (!validLogin)
						System.out.println("Invalid username or password");
				}
				ApplyAccountPage aAPage = new ApplyAccountPage();
				aAPage.retrievePersonalInfo(loginPage.getCurrentUser().getUserName());
				break;
			default:
				System.exit(0);
			}
		}
		
		currentUser = lPage.getCurrentUser();
		accountList = BankAppRepository.getUserAccounts(currentUser.getUserName());
		if ((validLogin && accountList.size() > 0) || (validLogin && (currentUser.getUserType() == USER.ADMIN || currentUser.getUserType() == USER.EMPLOYEE))) {
			while (usingSystem) {
				Page.newPage();
				if (currentUser.getUserType() == USER.CUSTOMER) {
					CustomerBankingPage cbPage = new CustomerBankingPage();
					cbPage.displayChoices();
					do {
						input = getInput();
						if (input < 0 || input > 7)
							System.out.println("Invalid choice");
					} while (input < 0 || input > 7);

					switch (input) {
					case 1:
						currentAccount = chooseAccount(accountList);

						if (currentAccount.getBalance() == 0)
							System.out.println("Your balance is zero");
						else {
							WithdrawPage.withdraw(currentAccount.getAccountNumber());
						}
						break;
					case 2:
						currentAccount = chooseAccount(accountList);
						DepositPage.deposit(currentAccount.getAccountNumber());
						break;
					case 3:
						currentAccount = chooseAccount(accountList);
						
						if (currentAccount.getBalance() == 0)
							System.out.println("Your balance is zero");
						else {
							long transferAccount = 0;
							boolean transferExists = false;
							String accNum;

							while (!transferExists) {
								System.out.println("Please enter account number to transfer money to: ");

								try {
									accNum = Page.sc.nextLine();
									transferAccount = Long.parseLong(accNum);
									
									if(BankAppRepository.verifyAccount(transferAccount))
										transferExists = true;
								} catch (NumberFormatException e) {
									System.out.println("Invalid input");
								}
							}
							TransferPage.transfer(currentAccount.getAccountNumber(), transferAccount);
						}
						break;
					case 4:
						currentAccount = chooseAccount(accountList);
						
						System.out.println(BankAppRepository.getAccount(currentAccount.getAccountNumber()).getBalance());
						break;
					case 5:
						JointAccountPage.getUserInput(currentUser);
						break;
					default:
						System.exit(0);
					}
				} else if (currentUser.getUserType() == USER.EMPLOYEE) {
					EmployeeBankingPage eBPage = new EmployeeBankingPage();
					eBPage.displayChoices();
					
					do {
						input = getInput();
						if (input < 0 || input > 4)
							System.out.println("Invalid choice");
					} while (input < 0 || input > 4);
					
					switch (input) {
					case 1:
						eBPage.displayApplicationChoices();
						do {
							input = getInput();
							if (input < 0 || input > 3)
								System.out.println("Invalid choice");
						} while (input < 0 || input > 3);
						
						switch(input) {
						case 1: 
							OpenApplicationPage.displayBankApplications();
							break;
						default:
							JointApplicationsPage.displayJointApplications();
						}
						break;
					case 2:
						BankAccountInfoPage.displayAccountsInfo();
						break;
					default:
						System.exit(0);
					}
				} else if (currentUser.getUserType() == USER.ADMIN) {

				}
			}
		}
		else {
			System.out.println("You do not have any active accounts.");
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
			if (input < 0 || input > accList.size())
				System.out.println("Invalid choice");
		} while (input < 0 || input > accList.size());

		return accList.get(input - 1);
	}

}