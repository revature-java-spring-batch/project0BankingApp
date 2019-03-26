package dto;

import dto.Account.ACCOUNTTYPE;
import users.Customer;
import users.User.USER;

public class BankAccountApplication {
	private long applicationId;
	private boolean approved;
	private Customer customer;
	private ACCOUNTTYPE type;
	
	public BankAccountApplication(long appId, String user, boolean approved, 
			String fName, String lName, String address, String phoneNum, ACCOUNTTYPE type){
		
		applicationId = appId;
		this.approved = approved;
		this.type = type;
		customer = new Customer(user, USER.CUSTOMER, fName, lName, address, phoneNum);
	}
	
	public ACCOUNTTYPE getType() {
		return type;
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
