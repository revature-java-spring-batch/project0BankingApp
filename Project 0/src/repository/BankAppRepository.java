package repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Account;
import dto.Account.ACCOUNTTYPE;
import dto.BankAccountApplication;
import dto.JointAccountApplication;
import dto.PersonalInfo;
import main.driver.BankApp.TRANSACTION_TYPE;
import users.Customer;
import users.User;
import users.User.USER;
import util.DBUtil;

public class BankAppRepository {
	private static Connection conn = null;
	private static PreparedStatement st = null;
	private static ResultSet rs = null;
	
	static {
		conn = null;
		st = null;
		rs = null;
	}
	
	
	//Returns a user with matching username and password entered by the user
	public static User verifyUser(String user, String pass) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from users where username=? and password=?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, pass);
			rs = st.executeQuery();

			if (rs.next()) {
				USER type;
				String userType = rs.getString(3);
				
				if(userType.equals("Customer"))
					type = USER.CUSTOMER;
				else if(userType.equals("Employee"))
					type = USER.EMPLOYEE;
				else {
					type = USER.ADMIN;
				}
				User tempUser = new User(rs.getString(1), rs.getString(2), type);
				return tempUser;
			}
			else
				return null;
		} catch (SQLException e) {
			System.out.println("Error. Could not verify login. Try again later.");
		} finally {
			closeConnections();
		}
		return null;
	}
	
	//Verifies whether or not a username exists in the database
	public static boolean verifyUserName(String user) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from users where username=?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;

			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Could not verify username. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}
	
	//Checks if a customer exists with matching username
	public static boolean customerExists(String user) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from customers where username=?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;

			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Could not verify customer. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}
	
	//Checks whether or not an account has enough funds to withdraw or transfer money
	public static boolean verifyFunds(long account_id, double amount) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_accounts where account_id=? and balance >= ?";
			st = conn.prepareStatement(sql);
			st.setLong(1, account_id);
			st.setDouble(2, amount);

			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}

	//Verifies whether or not an account exists
	public static boolean verifyAccount(long account_id) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_accounts where account_id=?";
			st = conn.prepareStatement(sql);
			st.setLong(1, account_id);

			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}

	//Retrieves the bank account details with matching account id
	public static Account getAccount(long accId) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts where account_id=?";
			st = conn.prepareStatement(sql);
			st.setLong(1, accId);

			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				String type = rs.getString(3);

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				return new Account(rs.getLong(1), rs.getDouble(2), temp, rs.getBoolean("joint_account"), rs.getBoolean(5), rs.getString(6));
			}
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later." + e.getMessage());
		} finally {
			closeConnections();
		}
		
		return null;
	}
	
	//Returns all accounts tied to a user including joint accounts
	public static List<Account> getUserAccounts(String user) {
		List<Account> accList = new ArrayList<>();
		
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts where username=? and active=1 "
					+ "union select b.account_id, b.balance, b.account_type, "
					+ "b.joint_account, b.active, j.username from bank_accounts "
					+ "b,joint_accounts j where b.account_id = j.account_id and j.username = ?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, user);

			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				String type = rs.getString(3);

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				accList.add(new Account(rs.getLong(1), rs.getDouble(2), temp, rs.getBoolean(4), rs.getBoolean(5), rs.getString(6)));
			}
			
			return accList;
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find accounts. Try again later." + e.getMessage());
		} finally {
			closeConnections();
		}
		
		return null;
	}
	
	//Returns all customer data in the database in the form of an arraylist
	public static List<Customer> getCustomerInfo() {
		List<Customer> custList = new ArrayList<>();
		
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from customers";
			st = conn.prepareStatement(sql);

			rs = st.executeQuery();
			
			while(rs.next()) {
				custList.add(new Customer(rs.getString(1), USER.CUSTOMER, rs.getString(2), 
							rs.getString(3), rs.getString(4), rs.getString(5)));
			}
			return custList;
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find customer. Try again later." + e.getMessage());
		} finally {
			closeConnections();
		}
		
		return null;
	}
	
	//Returns all bank account data in the database in the form of an arraylist
	public static List<Account> getAccountInfo() {
		List<Account> accounts = new ArrayList<>();
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				String type = rs.getString(3);

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				
				accounts.add(new Account(rs.getLong(1), rs.getDouble(2), temp, rs.getBoolean("joint_account"), rs.getBoolean(5), rs.getString(6)));
			}

			return accounts;
		} catch (SQLException e) {
			System.out.println("Error. Could not get account info. Try again later");
		} finally {
			closeConnections();
		}
		return null;
	}

	//Withdraws money from a bank account
	public static boolean withdraw(Long accountId, double amount) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_accounts set balance = balance - ? where account_id = ? and balance >= ?";
			st = conn.prepareStatement(sql);
			st.setDouble(1, amount);
			st.setLong(2, accountId);
			st.setDouble(3, amount);

			int result = st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
			if(result == 0)
				return false;
			else
				return true;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not perform withdrawal. Try again later");
		} finally {
			closeStatementConnection();
		}
		return false;
	}

	//Deposits money into a bank account
	public static boolean deposit(Long accountId, double amount) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_accounts set balance = balance + ? where account_id = ?";
			st = conn.prepareStatement(sql);
			st.setDouble(1, amount);
			st.setLong(2, accountId);

			int result = st.executeUpdate();
			try { conn.commit(); } catch (Exception e) { }
			if(result == 0)
				return false;
			else
				return true;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not perform deposit. Try again later");
		} finally {
			closeStatementConnection();
		}
		return false;
	}

	//Transfers money from one bank account to another
	public static boolean transfer(long fromAccountId, long toAccountId, double amount) {
		CallableStatement cstmt = null;
		String sql = "{call transfer (?, ?, ?)}";

		try {
			conn = DBUtil.getInstance();
			cstmt = conn.prepareCall(sql);
			cstmt.setDouble(1, amount);
			cstmt.setLong(2, fromAccountId);
			cstmt.setLong(3, toAccountId);
			int result = cstmt.executeUpdate();
			System.out.println("Result of transfer: " + result);
			if(result == 1) {
				try { conn.commit(); } catch (Exception e) { }
				return true;
			}
			else {
				conn.rollback();
				return false;
			}
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not perform transfer. Try again later.");
		} finally {
			try { cstmt.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
		}
		return false;
	}

	//Creates a new user and adds it to the Users table
	public static void addNewUser(String user, String pass) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into users values(?, ?, 'Customer')";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, pass);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not add user. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	//Creates a new bank account application and adds it to the bank_applications table
	public static void createBankApplication(PersonalInfo pi, String user, ACCOUNTTYPE type) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into bank_applications (username, first_name, last_name, address, phone_number, account_type) values(?,?,?,?,?,?)";
			st = conn.prepareStatement(sql);
			String accType;
			if(type == ACCOUNTTYPE.CHECKING)
				accType = "Checking";
			else
				accType = "Savings";
			
			st.setString(1, user);
			st.setString(2, pi.getFirstName());
			st.setString(3, pi.getLastName());
			st.setString(4, pi.getAddress());
			st.setString(5, pi.getPhoneNumber());
			st.setString(6, accType);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	//Approves or denies a bank account application
	public static boolean approveOrDenyBankApplication(long appId, int i) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_applications set approved = ? where application_id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, i);
			st.setLong(2, appId);
			int result = st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
			if(result == 0)
				return false;
			else 
				return true;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not find application. Try again later.");
		}finally {
			closeStatementConnection();
		}
		return false;
	}
	
	//Approves or denies a joint account application
	public static boolean approveOrDenyJointApplication(long appId, int i) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update joint_account_applications set approved = ? where application_id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, i);
			st.setLong(2, appId);
			int result = st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
			if(result == 0)
				return false;
			else 
				return true;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not find application. Try again later.");
		}finally {
			closeStatementConnection();
		}
		return false;
	}
	
	//Cancels an account by setting it to inactive
	public static void cancelAccount(long accId) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_accounts set active = 0 where account_id = ?";
			st = conn.prepareStatement(sql);
			st.setLong(1, accId);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not cancel account. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	//Creates a new joint account application and adds it to the joint_account_applications table
	public static void createJointApplication(String user1, String user2, String type) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into joint_account_applications (joint_user1, joint_user2, account_type) values(?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, user1);
			st.setString(2, user2);
			st.setString(3, type);

			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	//Creates a new bank account and adds it to the bank_accounts table
	public static void createBankAccount(ACCOUNTTYPE type, boolean joint, String user) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into bank_accounts (balance, account_type, joint_account, username) values(0,?,?,?)";
			st = conn.prepareStatement(sql);
			if(type == ACCOUNTTYPE.CHECKING)
				st.setString(1, "Checking");
			else
				st.setString(1, "Saving");
			
			if(joint)
				st.setInt(2, 1);
			else
				st.setInt(2, 0);
			
			st.setString(3, user);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create account. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	//Creates a joint account for both the primary and joint user of the joint account
	public static void createJointAccount(String primaryUser, String jointUser) {
		try {
			long accNum = 0;
			conn = DBUtil.getInstance();
			String getAccount = "select * from bank_accounts where username=? and joint_account=1";
			st = conn.prepareStatement(getAccount);
			st.setString(1, primaryUser);
			ResultSet rs = st.executeQuery();
			
			//Get most recent joint account id
			while(rs.next()) {
				long temp = rs.getLong("account_id");
				if(temp > accNum)
					accNum = temp;
			}
			
			if(accNum != 0){
				String sql = "insert into joint_accounts values(?,?)";
				st = conn.prepareStatement(sql);
				st.setString(1, primaryUser);
				st.setLong(2, accNum);
				st.executeUpdate();
				
				st.setString(1, jointUser);
				st.setLong(2, accNum);
				st.executeUpdate();
			}
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create joint account. Try again later.");
		}finally {
			closeConnections();
		}
	}
	
	//Creates a transaction based on what type of transaction occurred (withdraw, deposit, transfer)
	public static long createTransaction(TRANSACTION_TYPE type, double amount, long from, long to) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into transactions (transaction_type, amount, account_id_from, account_id_to) values(?,?,?,?)";
			st = conn.prepareStatement(sql);
			if(type == TRANSACTION_TYPE.WITHDRAW)
				st.setString(1, "Withdraw");
			else if(type == TRANSACTION_TYPE.DEPOSIT)
				st.setString(1, "Deposit");
			else
				st.setString(1, "Transfer");
			
			st.setDouble(2, amount);
			st.setLong(3, from);
			st.setLong(4, to);
			
			st.executeUpdate();
			
			sql = "select * from transactions where account_id_from=? and account_id_to=?";
			st = conn.prepareStatement(sql);
			st.setLong(1, from);
			st.setLong(2, to);
			rs = st.executeQuery();
			
			long recentTransactionId = 0;
			while(rs.next()) {
				long tempId = rs.getLong(1);
				if(tempId > recentTransactionId)
					recentTransactionId = tempId;
			}
			
			try { conn.commit(); } catch (Exception e) { }
			return recentTransactionId;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create transaction. Try again later.");
		}finally {
			closeConnections();
		}
		return 0;
	}
	
	//Creates a new customer and adds it to the customers table
	public static void createCustomer(String user, String fName, String lName, String address, String phoneNum) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into customers values(?,?,?,?,?)";
			st = conn.prepareStatement(sql);
			
			st.setString(1, user);
			st.setString(2, fName);
			st.setString(3, lName);
			st.setString(4, address);
			st.setString(5, phoneNum);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create customer. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}

	//Closes the prepared statement and connection
	private static void closeStatementConnection() {
		try { st.close(); } catch (Exception e) { }
		try { conn.close(); } catch (Exception e) { }
	}
	
	//Returns an arraylist of all pending bank applications
	public static List<BankAccountApplication> getBankApplications(){	
		try {
			List<BankAccountApplication> applications = new ArrayList<>();
			conn = DBUtil.getInstance();
			String sql = "select * from bank_applications where approved is null";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE type;
				String aType = rs.getString(8);
				if(aType.equals("Checking"))
					type = ACCOUNTTYPE.CHECKING;
				else
					type = ACCOUNTTYPE.SAVINGS;
				applications.add(new BankAccountApplication(rs.getLong(1), rs.getString(2), rs.getBoolean(3),
								rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), type));
			}
			return applications;
		} catch (SQLException e) {
			System.out.println("Error. Could not get applications. Try again later");
		} finally {
			closeConnections();
		}
		return null;
	}
	
	//Returns an arraylist of all pending joint account applications
	public static List<JointAccountApplication> getJointApplications(){	
		try {
			List<JointAccountApplication> applications = new ArrayList<>();
			conn = DBUtil.getInstance();
			String sql = "select * from joint_account_applications where approved is null";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				if(rs.getString(5) == "Checking")
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				applications.add(new JointAccountApplication(rs.getLong(1), rs.getBoolean(2), rs.getString(3), rs.getString(4), temp));
			}
			return applications;
		} catch (SQLException e) {
			System.out.println("Error. Could not get joint account applications. Try again later");
		} finally {
			closeConnections();
		}
		return null;
	}
	
	//Counts the number of bank accounts
	public static int countBankAccounts() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_accounts";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}
	
	//Counts the number of customers
	public static int countCustomers() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from customers";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}

	//Counts the number of bank applications
	public static int countBankApplications() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_applications";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}

	//Count the number of joint account applications
	public static int countJointApplications() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from joint_account_applications";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}

	//Closes the result set, prepared statement and connection
	private static void closeConnections() {
		try { rs.close(); } catch (Exception e) { }
		closeStatementConnection();
	}
}
