package dto;

public class Account {
	final private long accountNumber;
	private double balance;
	final private ACCOUNTTYPE accountType;
	private boolean jointAccount = false;
	private boolean active;
	
	public enum ACCOUNTTYPE{
		CHECKING, SAVINGS
	}
	
	public Account(long aNum, double bal, ACCOUNTTYPE aType, boolean jAcc, boolean act){
		accountNumber = aNum;
		balance = bal;
		accountType = aType;
		jointAccount = jAcc;
		active = act;
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
