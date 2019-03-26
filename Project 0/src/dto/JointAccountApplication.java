package dto;

import dto.Account.ACCOUNTTYPE;

public class JointAccountApplication {
	private long applicationId;
	private boolean approved;
	private String primaryUser;
	private String jointUser;
	private ACCOUNTTYPE accountType;
	
	public JointAccountApplication(long appId, boolean approved, String user1, String user2, ACCOUNTTYPE type) {
		applicationId = appId;
		this.approved = approved;
		primaryUser = user1;
		jointUser = user2;
		accountType = type;
	}
	
	public long getApplicationId() {
		return applicationId;
	}
	
	public boolean isApproved() {
		return approved;
	}
	
	public String getPrimaryUser() {
		return primaryUser;
	}
	
	public String getJointUser() {
		return jointUser;
	}
	
	public ACCOUNTTYPE getAccountType() {
		return accountType;
	}
}
