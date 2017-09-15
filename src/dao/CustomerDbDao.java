package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CustomerDbDao implements CustomerDao {

	// CONSTRUCTOR!!!
	public CustomerDbDao() {

	}

	// Here starts the implements of the INTERFACE "CustomerDao":
	@Override
	public Customer createCustomer(Customer customer) throws DataBaseException {
		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			String name = customer.getCustName();
			String password = customer.getPassword();

			String sql = "INSERT INTO customer (`Cust_name`,`Password` ) VALUES ('" + name + "','" + password + "')";

			connection.createStatement().execute(sql);
			System.out.println(sql);
		} catch (SQLException e) {
			throw new DataBaseException("incapable to create customer ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return customer;
	}

		public Customer removeCustomer(Customer customer) throws DataBaseException {

	

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			long custId = customer.getId();
			String  sql = "DELETE FROM `project`.`customer_coupon` WHERE `CUST_ID`=" + custId ;
			System.out.println(sql);
			connection.createStatement().execute(sql);
			 sql = "DELETE FROM project.customer WHERE `ID`=" + custId;
			
			connection.createStatement().execute(sql);
			System.out.println(sql);
		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove  the customer " + customer.getCustName(), e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return customer;
	}
	 
	@Override
	public Customer updateCustomer(Customer customer) throws DataBaseException {

		Connection connection = null;
		long id = customer.getId();
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			
			String password = customer.getPassword();
			String sql = "UPDATE   `customer` SET " + "`Password`='" + password + "' WHERE `ID`='" + id + "'";

			connection.createStatement().execute(sql);
			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to update customer " + id, e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);

		}
		return customer;

	}

	@Override
	public Customer getCustomerById(long id) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()
					.executeQuery("select * from project.customer where ID=" + id);
			

			Customer customer = null;

			if (results.next()) {
				customer = new Customer();
				customer.setId(results.getLong("ID"));
				customer.setCustName(results.getString("Cust_Name"));
				customer.setPassword(results.getString("Password"));

			}
			return customer;

		} catch (SQLException e) {

			throw new DataBaseException("incapable to get customer by id " + id, e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	@Override
	public Collection<Customer> getAllCustomers() throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery("select * from customer");

			Collection<Customer> allCustomers = new ArrayList<Customer>();

			while (results.next()) {
				Customer cust = new Customer();
				cust.setId(results.getLong("ID"));
				cust.setCustName(results.getString("Cust_Name"));
				cust.setPassword(results.getString("Password"));
				allCustomers.add(cust);
			}
			return allCustomers;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get all customers", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}


	
	public Collection<Coupon> generalCouponsByType(CouponType couponType) throws DataBaseException {

		Connection connection = null;

		try {

			connection = ConnectionPoolSingleton.getInstance().getConnection();

		

			String sql = "SELECT * FROM coupon where Type='" + couponType+ "'";

			ResultSet results = connection.createStatement().executeQuery(sql);

			Collection<Coupon> allCoupon = new ArrayList<Coupon>();

			while (results.next()) {
				Coupon coup = new Coupon();
				coup.setId(results.getLong("ID"));
				coup.setTitle(results.getString("TITLE"));
				coup.setStartDate(results.getDate("Start_Date"));
				coup.setEndDate(results.getDate("End_Date"));
				coup.setAmount(results.getInt("Amount"));
				coup.setMessage(results.getString("Message"));
				coup.setPrice(results.getDouble("Price"));
				coup.setImage(results.getString("Image"));
				allCoupon.add(coup);
			}
			return allCoupon;

		} catch (SQLException e) {
			throw new DataBaseException("incapable to filter coupons by type ", e);
		}

		finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}
	
	// *** GENERAL GET COUPON BY PRICE ***	
	public Collection<Coupon> generalCouponsByPrice(double coupPrice) throws DataBaseException {

		Connection connection = null;

		try {

			connection = ConnectionPoolSingleton.getInstance().getConnection();

		

			String sql = "SELECT * FROM coupon where Price <= " + coupPrice ;

			ResultSet results = connection.createStatement().executeQuery(sql);

			Collection<Coupon> allCoupon = new ArrayList<Coupon>();

			while (results.next()) {
				Coupon coup = new Coupon();
				coup.setId(results.getLong("ID"));
				coup.setTitle(results.getString("TITLE"));
				coup.setStartDate(results.getDate("Start_Date"));
				coup.setEndDate(results.getDate("End_Date"));
				coup.setAmount(results.getInt("Amount"));
				coup.setMessage(results.getString("Message"));
				coup.setPrice(results.getDouble("Price"));
				coup.setImage(results.getString("Image"));
				allCoupon.add(coup);
			}
			return allCoupon;

		} catch (SQLException e) {
			throw new DataBaseException("incapable to filter coupons by price ", e);
		}

		finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}
	
	@Override
	public Collection<Coupon> getAllCoupons() throws DataBaseException {

		Connection connection = null;

		try {

			connection = ConnectionPoolSingleton.getInstance().getConnection();

		

			String sql = "SELECT * FROM coupon ";

			ResultSet results = connection.createStatement().executeQuery(sql);

			Collection<Coupon> allCoupon = new ArrayList<Coupon>();

			while (results.next()) {
				Coupon coup = new Coupon();
				coup.setId(results.getLong("ID"));
				coup.setTitle(results.getString("TITLE"));
				coup.setStartDate(results.getDate("Start_Date"));
				coup.setEndDate(results.getDate("End_Date"));
				coup.setAmount(results.getInt("Amount"));
				coup.setMessage(results.getString("Message"));
				coup.setPrice(results.getDouble("Price"));
				coup.setImage(results.getString("Image"));
				allCoupon.add(coup);
			}
			return allCoupon;

		} catch (SQLException e) {
			throw new DataBaseException("incapable to get coupons from customer ", e);
		}

		finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}
	
	@Override
	public Customer login(String custName, String password) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery(
					"SELECT * FROM customer where Cust_Name = '" + custName + "' and Password = '" + password + "'");

			if (results.next()) {
				return getCustomerFromDB(results);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DataBaseException("incorrect name or password ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	
	@Override
	public Coupon updateCustomerCoupon(long customerId, long couponId) throws DataBaseException {

		Date dateNow = new Date(System.currentTimeMillis());
		
		String sql = "SELECT * FROM coupon c  inner join customer_coupon cc where c.ID = '" + couponId
				+ "' and c.Amount >0 and'" + dateNow + "'<c.End_Date and cc.CUST_ID='" + customerId
				+ "' and  cc.COUPON_ID=" + couponId;

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results;
			results = connection.createStatement().executeQuery(sql);
		
			if (results.next()) {

				return null; // Changed - 15.6.17 Time - 17:27

			} else {
				sql = " INSERT INTO `project`.`customer_coupon` (`CUST_ID`, `COUPON_ID`) VALUES ('" + customerId + "','"
						+ couponId + "')";
				connection.createStatement().execute(sql);
				sql = " UPDATE `project`.`coupon` SET Amount=Amount-1 WHERE ID ='" + couponId + "'";

				connection.createStatement().execute(sql);// Changed 18.6.17 ,
															// Time - 12:18
															// executeQuery(sql);

				sql = "SELECT * FROM `project`.`coupon` WHERE ID ='" + couponId + "'";

				connection.createStatement().execute(sql);

				return getCouponFromDB(results, couponId);

			}
		} catch (SQLException e) {
			throw new DataBaseException("incapable to update this coupon , sorry", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	

	@Override
	public boolean customerIsAlreadyExist(String custName) throws DataBaseException {

		String sql = "SELECT Cust_Name FROM customer where Cust_Name = '" + custName + "'";

		Connection connection = null;
		boolean results;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			results = connection.createStatement().execute(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to create customer ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		System.out.println(sql);
		return results;
	}

	private Customer getCustomerFromDB(ResultSet rs) throws DataBaseException {
		Customer customer = new Customer();
		try {
			customer.setId(rs.getLong(1));
			customer.setCustName(rs.getString(2));
			customer.setPassword(rs.getString(3));

		} catch (Exception e) {
			throw new DataBaseException("Do you have issue with your DB APP.CUSTOMER", e);

		}
		return customer;
	}

	private Coupon getCouponFromDB(ResultSet rs, long id) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			rs = connection.createStatement().executeQuery("select * from project.coupon where ID=" + id);

			Coupon coupon = new Coupon();

			while (rs.next()) {
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6))); 
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			
			}
			return coupon;
		} catch (Exception e) {
			throw new DataBaseException("Do you have issue with your DB APP.COUPON", e);

		}

	}

	
	
}
