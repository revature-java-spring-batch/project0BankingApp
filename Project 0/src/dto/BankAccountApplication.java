package dto;

import users.Customer;
import users.User.USER;

public class BankAccountApplication {
	private long applicationId;
	private boolean approved;
	private Customer customer;
	
	public BankAccountApplication(long appId, String user, boolean approved, 
			String fName, String lName, String address, String phoneNum){
		
		applicationId = appId;
		this.approved = approved;
		customer = new Customer(user, USER.CUSTOMER, fName, lName, address, phoneNum);
	}
	
	public long getApplicationId() {
		return applicationId;
	}

	public boolean isApproved() {
		return approved;
	}
	
	public Customer getCustomer() {
		return customer;
	}
}
