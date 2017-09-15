package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringWrapper {
	List<String> names = new ArrayList<>();

	// private CouponDbDao couponDbDao = new CouponDbDao();
	// private CompanyDbDao companyDbDao = new CompanyDbDao();
	// private CustomerDbDao customerDbDao = new CustomerDbDao();
	//
	// Company company;
	// Customer customer;
	// Coupon coupon;
	//
	public StringWrapper() {

	}

	//
	// public Collection<Coupon> getCouponByType(CouponType coupType) throws
	// DataBaseException {
	// private CompanyDbDao compDbDao = new CompanyDbDao();
	//
	// try {
	// return compDbDao.getCompNames(args);
	// //getCouponByType(coupType,this.company.getId());
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public Collection<String[]> compNames() throws DataBaseException {
		// public static void main(String[] args) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery("SELECT Comp_Name from company");

			Collection<String[]> objNames = new ArrayList<String[]>();
			String[] arr = null;
			while (results.next()) {
				String em = results.getString("Comp_Name");
				arr = em.split("\n");
				for (int i = 0; i < arr.length; i++) {
					// System.out.println(arr[i]);
					objNames.add(arr);
				}
			}

		} catch (SQLException e) {
			throw new DataBaseException("incapable to get all companies's names", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return  null;
	}
	
//**********
//**********
	
	public Collection<String[]> custNames() throws DataBaseException {
		// public static void main(String[] args) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery("SELECT Cust_Name from customer");

			Collection<String[]> objNames = new ArrayList<String[]>();
			String[] arr = null;
			while (results.next()) {
				String em = results.getString("Cust_Name");
				arr = em.split("\n");
				for (int i = 0; i < arr.length; i++) {
					// System.out.println(arr[i]);
					objNames.add(arr);
				}
			}

		} catch (SQLException e) {
			throw new DataBaseException("incapable to get all customers's names", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return null ;
	}
	
	public String adminName() throws DataBaseException {
		String admin = "admin";
		return null;
	}
	
	
}
