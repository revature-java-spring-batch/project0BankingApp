package bank;

public class Account {
	final private long accountNumber;
	private double balance;
	final private String accountType;
	private boolean jointAccount = false;
	
	Account(long aNum, double bal, String aType, boolean jAcc){
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
	public String getAccountType() {
		return accountType;
	}

	public void setJointAccount(boolean jointAccount) {
		this.jointAccount = jointAccount;
	}

	public boolean isJointAccount() {
		return jointAccount;
	}
	
/*	public void withdraw(double amount) {
		if(amount <= 0)
			System.out.println("Error: Amount entered is zero or negative.");
		else {
			balance -= amount;
		}
	}
	
	public void deposit(double amount) {
		if(amount <= 0)
			System.out.println("Error: Amount entered is zero or negative.");
		else{
			balance += amount;
		}
	}*/
}
