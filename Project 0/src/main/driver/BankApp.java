package main.driver;

import java.util.List;
import dto.Account;
import pages.AdminBankingPage;
import pages.ApplyAccountPage;
import pages.BankAccountInfoPage;
import pages.CustomerBankingPage;
import pages.CustomerInfoPage;
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
	public enum TRANSACTION_TYPE {
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

		System.out.println("Welcome to the Revature banking app!");
		while (!loggedIn) {
			lPage.displayChoices();

			do {
				input = getInput();
				if (input < 0 || input > 4)
					System.out.println("Invalid choice");
			} while (input < 0 || input > 4);

			switch (input) {
			case 1:
				validLogin = false;
				while (!validLogin) {
					validLogin = lPage.login();
					if (!validLogin)
						System.out.println("Invalid username or password");
				}
				loggedIn = true;
				break;
			case 2:
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

		System.out.println("Successful Login!");
		currentUser = lPage.getCurrentUser();
		accountList = BankAppRepository.getUserAccounts(currentUser.getUserName());
		if ((validLogin && accountList.size() > 0) || (validLogin
				&& (currentUser.getUserType() == USER.ADMIN || currentUser.getUserType() == USER.EMPLOYEE))) {
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

						if (!BankAppRepository.verifyFunds(currentAccount.getAccountNumber(), 1))
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

						if (!BankAppRepository.verifyFunds(currentAccount.getAccountNumber(), 1))
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

									if (BankAppRepository.verifyAccount(transferAccount))
										transferExists = true;
									else
										System.out.println("Account does not exist.");
								} catch (NumberFormatException e) {
									System.out.println("Invalid input");
								}
							}
							TransferPage.transfer(currentAccount.getAccountNumber(), transferAccount);
						}
						break;
					case 4:
						currentAccount = chooseAccount(accountList);
						Account acc = BankAppRepository.getAccount(currentAccount.getAccountNumber());
						if (acc != null)
							System.out.println("Current Balance: " + acc.getBalance());
						else
							System.out.println("Could not access account");
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
						if (input < 0 || input > 5)
							System.out.println("Invalid choice");
					} while (input < 0 || input > 5);

					switch (input) {
					case 1:
						eBPage.displayApplicationChoices();
						do {
							input = getInput();
							if (input < 0 || input > 3)
								System.out.println("Invalid choice");
						} while (input < 0 || input > 3);

						switch (input) {
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
					case 3:
						CustomerInfoPage.displayCustomerInfo();
						break;
					default:
						System.exit(0);
					}
				} else if (currentUser.getUserType() == USER.ADMIN) {
					AdminBankingPage abPage = new AdminBankingPage();
					abPage.displayChoices();

					do {
						input = getInput();
						if (input < 0 || input > 8)
							System.out.println("Invalid choice");
					} while (input < 0 || input > 8);

					switch (input) {
					case 1:
					case 2:
					case 3:
					case 6:
						boolean validAcc = false;
						long accountId = 0;
						while (!validAcc) {
							System.out.print("Please enter account number: ");
							String acc = Page.sc.nextLine();

							try {
								accountId = Long.parseLong(acc);
							} catch (NumberFormatException e) {
								System.out.println("Invalid input.");
							}

							if (BankAppRepository.verifyAccount(accountId))
								validAcc = true;
							else
								System.out.println("Account does not exist.");
						}

						if (input == 1) {
							if (!BankAppRepository.verifyFunds(accountId, 1))
								System.out.println("Your balance is zero");
							else
								WithdrawPage.withdraw(accountId);
						} else if (input == 2) {
							DepositPage.deposit(accountId);
						} else if (input == 3) {
							if (!BankAppRepository.verifyFunds(accountId, 1))
								System.out.println("Your balance is zero");
							else {
								long transferAccount = 0;
								boolean transferExists = false;
								String accNum;

								while (!transferExists) {
									System.out.print("Please enter account number to transfer money to: ");

									try {
										accNum = Page.sc.nextLine();
										transferAccount = Long.parseLong(accNum);

										if (BankAppRepository.verifyAccount(transferAccount))
											transferExists = true;
										else
											System.out.println("Account does not exist");
									} catch (NumberFormatException e) {
										System.out.println("Invalid input");
									}
								}
								TransferPage.transfer(accountId, transferAccount);
							}
						} else {
							String ans = null;
							boolean validAns = false;
							System.out.print("Are you sure you want to cancel this account? (Y/N):  ");
							while (!validAns) {
								ans = Page.sc.nextLine();
								if ("Y".equals(ans.toUpperCase())) {
									validAns = true;
									BankAppRepository.cancelAccount(accountId);
								} else if ("N".equals(ans.toUpperCase())) {
									validAns = true;
								}
							}
						}
						break;
					case 4:
						abPage.displayApplicationChoices();
						do {
							input = getInput();
							if (input < 0 || input > 3)
								System.out.println("Invalid choice");
						} while (input < 0 || input > 3);

						switch (input) {
						case 1:
							OpenApplicationPage.displayBankApplications();
							break;
						default:
							JointApplicationsPage.displayJointApplications();
						}
						break;
					case 5:
						BankAccountInfoPage.displayAccountsInfo();
						break;
					default:
						System.exit(0);
					}
				}
			}
		} else {
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