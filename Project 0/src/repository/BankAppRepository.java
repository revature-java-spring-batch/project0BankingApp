package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.DBUtil;

public class BankAppRepository {
	public static Boolean verifyUser(String user, String pass) {
		try (Connection conn = DBUtil.getInstance(); Statement st = conn.createStatement();) {
			String sql = String.format("select count(*) from users where username='%s' and password='%s'", user, pass);
			ResultSet set = st.executeQuery(sql);

			if (set.next() && set.getInt(1) == 1)
				return true;
			else
				return false;

		} catch (SQLException e) {
			System.out.println("Error. Try again later");
		}
		return false;
	}

	public static ResultSet getAccountInfo(int low, int high) {
		try (Connection conn = DBUtil.getInstance(); Statement st = conn.createStatement();) {
			String sql = String.format("select * from bank_accounts where rownum between %d and %d", low, high);
			ResultSet set = st.executeQuery(sql);

			return set;
		} catch (SQLException e) {
			System.out.println("Error. Try again later");
		}
		return null;
	}
	
	public static int withdraw(Long accountId, double amount) {
		try (Connection conn = DBUtil.getInstance(); Statement st = conn.createStatement();) {
			String sql = String.format("update bank_accounts set balance = balance - %f where account_id = %d and balance >= %f", amount, accountId, amount);
			int result = st.executeUpdate(sql);
			return result;
		} catch (SQLException e) {
			System.out.println("Error. Try again later");
		}
		return 0;
	}
	
	public static int deposit(Long accountId, double amount) {
		try (Connection conn = DBUtil.getInstance(); Statement st = conn.createStatement();) {
			String sql = String.format("update bank_accounts set balance = balance + %f where account_id = %d", amount, accountId, amount);
			int result = st.executeUpdate(sql);
			return result;
		} catch (SQLException e) {
			System.out.println("Error. Try again later");
		}
		return 0;
	}

	public static void main(String[] args) {
		if (verifyUser("jbtran", "pass123"))
			System.out.println("sucess");

	}
}
