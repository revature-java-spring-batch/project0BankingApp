package pages;

import repository.BankAppRepository;

public class DepositPage {
	public static void deposit(long accountId) {
		String depositAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter deposit amount: ");
			depositAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(depositAmt);

				if (BankAppRepository.deposit(accountId, amount) == 1)
					validAmt = true;
				else
					System.out.println("Unable to deposit amount");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
	}
	public static void main(String[] args) {
		deposit(1011001001l);
	}
}
