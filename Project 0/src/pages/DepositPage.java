package pages;

import bank.Account;

public class DepositPage {
	public double getDepoistAmt(Account account) {
		String depositAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter deposit amount: ");
			depositAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(depositAmt);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
		return amount;
	}
}
