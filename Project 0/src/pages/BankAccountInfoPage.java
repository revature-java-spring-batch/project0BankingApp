package pages;

import java.util.List;
import dto.Account;
import repository.BankAppRepository;

public class BankAccountInfoPage {
	static String headerSpace = "%-16s %-12s %-18s %-18s %-11s %-13s\n\n";
	static String dataSpace = "%-16d %-12.2f %-18s %-18s %-11s %-13s\n\n";

	public static void displayAccountsInfo() {
		List<Account> accounts = BankAppRepository.getAccountInfo();
		if (accounts == null || accounts.size() == 0)
			System.out.println("No records available");
		else {
			printHeader();
			for (Account account : accounts) {
				String joint = null;
				String active = null;

				if (account.isJointAccount())
					joint = "Yes";
				else
					joint = "No";

				if (account.isActive())
					active = "Yes";
				else
					active = "No";

				System.out.format(dataSpace, account.getAccountNumber(), account.getBalance(),
						account.getAccountType().toString(), joint, active, account.getUsername());
			}
		}
	}

	static void printHeader() {
		System.out.format(headerSpace, "Acccount ID", "Balance", "Acccount Type", "Joint Account", "Active",
				"Username");
	}
}
