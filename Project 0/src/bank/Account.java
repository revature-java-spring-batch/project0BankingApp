package bank;

public class Account {
	final private long accountNumber;
	private double balance;
	final private ACCOUNTTYPE accountType;
	private boolean jointAccount = false;
	
	public enum ACCOUNTTYPE{
		CHECKING, SAVINGS
	}
	
	Account(long aNum, double bal, ACCOUNTTYPE aType, boolean jAcc){
		accountNumber = aNum;
		balance = bal;
		accountType = aType;
		jointAccount = jAcc;
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
