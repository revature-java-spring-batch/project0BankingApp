package pages;

import org.apache.log4j.Logger;
import main.driver.BankApp.TRANSACTION_TYPE;
import repository.BankAppRepository;

public class WithdrawPage {
	static Logger logger = Logger.getLogger(WithdrawPage.class);
	private static TRANSACTION_TYPE type = TRANSACTION_TYPE.WITHDRAW;

	public static void withdraw(long accountId) {
		String withdrawalAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter withdrawal amount: ");
			withdrawalAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(withdrawalAmt);
				
				if(amount < 0)
					amount *= -1;
				
				// Validate withdrawal amount
				if (BankAppRepository.verifyFunds(accountId, amount)) {
					validAmt = true;
					BankAppRepository.withdraw(accountId, amount);
					long transactionId = BankAppRepository.createTransaction(type, amount, accountId, accountId);
					String transaction = String.format(
							"Transaction Id = %d  Transaction Type= %s  Transaction Amount=$ %f  Account Id From= %d  Account Id To= %d",
							transactionId, type.toString(), amount, accountId, accountId);
					logger.info(transaction);
					System.out.println("Withdraw Successful");
				} else
					System.out.println("Insufficient funds. Enter a valid amount");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
	}
}
