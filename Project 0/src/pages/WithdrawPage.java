package pages;

import bank.Account;

public class WithdrawPage {
	public double getWithdrawalAmt(Account account) {
		String withdrawalAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter withdrawal amount: ");
			withdrawalAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(withdrawalAmt);
				// Validate withdrawal amount
				if (account.getBalance() > amount)
					validAmt = true;
				else
					System.out.println("Insufficient funds. Enter a valid amount");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
		return amount;
	}
}
