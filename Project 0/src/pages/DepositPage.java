package pages;

import org.apache.log4j.Logger;

import main.driver.BankApp.TRANSACTION_TYPE;
import repository.BankAppRepository;

public class DepositPage {
	static Logger logger = Logger.getLogger(DepositPage.class);
	private static TRANSACTION_TYPE type = TRANSACTION_TYPE.DEPOSIT;

	public static void deposit(long accountId) {
		String depositAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter deposit amount: ");
			depositAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(depositAmt);

				if(amount < 0)
					amount *= -1;
				
				if (BankAppRepository.deposit(accountId, amount)) {
					validAmt = true;
					long transactionId = BankAppRepository.createTransaction(type, amount, accountId, accountId);

					String transaction = String.format(
							"Transaction Id = %d  Transaction Type= %s  Transaction Amount=$ %f  Account Id From= %d  Account Id To= %d",
							transactionId, type.toString(), amount, accountId, accountId);
					logger.info(transaction);

					System.out.println("Deposit Successful");
				} else
					System.out.println("Unable to deposit amount");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
	}
}
