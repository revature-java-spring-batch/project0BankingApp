package dto;

public class Account {
	final private long accountNumber;
	private double balance;
	final private ACCOUNTTYPE accountType;
	private boolean jointAccount = false;
	private boolean active;
	private String username;
	
	public enum ACCOUNTTYPE{
		CHECKING, SAVINGS
	}
	
	public Account(long aNum, double bal, ACCOUNTTYPE aType, boolean jAcc, boolean act, String user){
		accountNumber = aNum;
		balance = bal;
		accountType = aType;
		jointAccount = jAcc;
		active = act;
		username = user;
	}
	
	public String getUsername() {
		return username;
	}

	public boolean isActive() {
		return active;
	}

	public long getAccountNumber() {
		return accountNumber;
	}
	public double getBalance() {
		return balance;
	}
	public ACCOUNTTYPE getAccountType() {
		return accountType;
	}

	public void setJointAccount(boolean jointAccount) {
		this.jointAccount = jointAccount;
	}

	public boolean isJointAccount() {
		return jointAccount;
	}
}
