package pages;

import java.sql.ResultSet;
import java.sql.SQLException;
import repository.BankAppRepository;

public class BankAccountInfoPage {
	static String headerSpace = "%-16s %-12s %-18s %-18s %-11s\n\n";
	static String dataSpace = "%-16d %-12.2f %-18s %-18s %-11s\n\n";

	static void displayAccountsInfo(int low, int high) {
		// Query database to get 10 accounts at a time
		ResultSet set = BankAppRepository.getAccountInfo(low, high);
		try {
			if (set == null)
				System.out.println("No more records.");
			else {
				while (set.next()) {
					String joint = null;
					String active = null;

					if (set.getInt("joint_account") == 0)
						joint = "Yes";
					else
						joint = "No";

					if (set.getInt("active") == 0)
						active = "Yes";
					else
						active = "No";

					System.out.format(dataSpace, set.getLong("account_id"), set.getDouble("balance"),
							set.getString("account_type"), joint, active);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error. Try again later");
		}
	}

	static void printHeader() {
		System.out.format(headerSpace, "Acccount ID", "Balance", "Acccount Type", "Joint Account", "Active");
	}

	static void printSpace() {
		System.out.print("      ");
	}
}
