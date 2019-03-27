package pages;

import java.util.List;
import repository.BankAppRepository;
import users.Customer;

public class CustomerInfoPage {
	static String headerSpace = "%-13s %-15s %-14s %-30s %-17s\n\n";

	public static void displayCustomerInfo() {
		List<Customer> customersList = BankAppRepository.getCustomerInfo();

		if (customersList == null || customersList.size() == 0)
			System.out.println("No records available");
		else {
			printHeader();
			for (Customer c : customersList) {
				System.out.format(headerSpace, c.getUserName(), c.getFirstName(), c.getLastName(), c.getAddress(),
						c.getPhoneNumber());
			}
		}
	}

	public static void printHeader() {
		System.out.format(headerSpace, "Username", "First Name", "Last Name", "Address", "Phone Number");

	}
}
