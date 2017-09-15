package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CouponDbDao implements CouponDao {

	public CouponDbDao() {

	}

	// Here starts the implements of the INTERFACE

	// changed to void 7.7.2017

	public Coupon createCoupon(Coupon coupon, long companyId) throws DataBaseException {
		Connection connection = null;
		ResultSet results = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			String title = coupon.getTitle();
			Date startDate = coupon.getStartDate();
			Date endDate = coupon.getEndDate();
			int amount = coupon.getAmount();
			CouponType type = coupon.getType();
			String message = coupon.getMessage();
			double price = coupon.getPrice();
			String image = coupon.getImage();
			String sql = "INSERT INTO coupon (`TITLE` , `Start_Date` , `End_Date` , `Amount` , `Type` , `Message` , `Price` , `Image` ) VALUES ('"
					+ title + "','" + startDate + "' , '" + endDate + "','" + amount + "','" + type + "','" + message
					+ "','" + price + "','" + image + "')";
			System.out.println(sql);
			connection.createStatement().execute(sql);

			sql = "INSERT INTO `project`.`company-coupon` (`COMP_ID`, `COUPON_ID`) VALUES (" + companyId
					+ ", (SELECT ID FROM project.`coupon` WHERE `TITLE`= '" + title + "'))";
			System.out.println(sql);
			connection.createStatement().execute(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to create company ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

		return coupon;
	}

	@Override
	public void removeCompanyCoupon(Coupon coupon) throws DataBaseException {

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			long couponId = coupon.getId();

			String sql = "DELETE FROM `project`.`company-coupon` WHERE `COUPON_ID`=" + couponId;

			connection.createStatement().execute(sql);

			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	public void removeCouponByCompanyId(Coupon coupon) throws DataBaseException {
		Company company = null;
		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			@SuppressWarnings("null")
			long companyId = company.getId();

			String sql = "DELETE FROM `project`.`company-coupon` WHERE `COMP_ID`=" + companyId;

			connection.createStatement().execute(sql);

			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	@Override
	public void removeCustomerCoupon(Coupon coupon) throws DataBaseException {

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			long couponId = coupon.getId();

			String sql = "DELETE FROM `project`.`customer_coupon` WHERE `COUPON_ID`=" + couponId;

			connection.createStatement().execute(sql);

			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	public void removeCouponByCustomerId(Coupon coupon) throws DataBaseException {
		Customer customer = null;
		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			@SuppressWarnings("null")
			long customerId = customer.getId();

			String sql = "DELETE FROM `project`.`customer_coupon` WHERE `CUST_ID`=" + customerId;

			connection.createStatement().execute(sql);

			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	@Override
	public Coupon removeCoupon(Coupon coupon) throws DataBaseException {

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			long couponId = coupon.getId();

			String sql = "DELETE FROM `project`.`customer_coupon` WHERE `COUPON_ID`=" + couponId;

			connection.createStatement().execute(sql);
			sql = "DELETE FROM `project`.`company-coupon` WHERE `COUPON_ID`=" + couponId;

			connection.createStatement().execute(sql);

			sql = "DELETE FROM `project`.`coupon` WHERE `ID`=" + couponId;

			connection.createStatement().execute(sql);

			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return coupon;
	}

	@Override
	public Coupon updateCoupon(Coupon coupon) throws DataBaseException {
		Connection connection = null;
		long id = coupon.getId();
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			Date endDate = coupon.getEndDate();
			double price = coupon.getPrice();
			String sql = "UPDATE `project`.`coupon` SET" + " `End_Date`='" + endDate + "' ," + " `Price`='" + price
					+ "' WHERE `ID`=" + id;

			connection.createStatement().execute(sql);

			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to update coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return coupon;
	}

	@Override
	public Coupon getCouponById(long id) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()
					.executeQuery("select * from project.coupon where ID=" + id);

			Coupon coupon = null;

			if (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));

			}
			return coupon;
		} catch (SQLException e) {

			throw new DataBaseException("incapable to get coupon by id " + id, e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllCoupons(long companyId) throws DataBaseException {

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			String sql = "SELECT * FROM `project`.`coupon` where `ID` = any(SELECT COUPON_ID FROM project.`company-coupon` WHERE `COMP_ID`='"
					+ companyId + "')";

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
			throw new DataBaseException("incapable to get all coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	public Collection<Coupon> getCouponByType(CouponType couponType, long companyId) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()

					.executeQuery("SELECT * FROM project.coupon where Type = '" + couponType
							+ "' and ID = any(SELECT COUPON_ID FROM project.`company-coupon` WHERE `COMP_ID`= '"
							+ companyId + "')");
			Collection<Coupon> CouponByType = new ArrayList<Coupon>();

			Coupon coupon = null;

			while (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));
				CouponByType.add(coupon);
			}
			return CouponByType;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get all companies", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

	@Override
	public Coupon updateCouponAmount(Coupon couponId) throws DataBaseException {

		Connection connection = null;
		long id = couponId.getId();
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			int amount = couponId.getAmount();
			int finalAmount = amount - 1;

			String sql = "UPDATE `coupon` SET `Amount`='" + finalAmount + "' WHERE `ID`='" + id;

			connection.createStatement().executeQuery(sql);
			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("ERROR - please try again", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);

		}
		return couponId;

	}

	@Override
	public boolean couponIsAlreadyExist(String couponTitle) throws DataBaseException {

		String sql = "SELECT TITLE FROM coupon where TITLE = '" + couponTitle + "'";

		Connection connection = null;
		ResultSet results;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			results = connection.createStatement().executeQuery(sql);
			if (results.next())
				return true;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to create coupon ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		System.out.println(sql);
		return false;
	}

	
	public Collection<Coupon> getCouponByMaximalPrice(double maximalPrice, long companyId) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()
					.executeQuery("SELECT * FROM coupon where Price <= " + maximalPrice
							+ " and ID = any(SELECT COUPON_ID FROM project.`company-coupon` WHERE `COMP_ID`= '"
							+ companyId + "')");

			Collection<Coupon> couponByMaximalPrice = new ArrayList<Coupon>();

			Coupon coupon = null;

			while (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));
				couponByMaximalPrice.add(coupon);
			}
			return couponByMaximalPrice;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

	public Collection<Coupon> getCouponByEndDate(Date endDate, long companyId) throws DataBaseException {
		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()
					.executeQuery("select * from coupon where End_Date <=  '" + endDate
							+ "' and ID = any(SELECT COUPON_ID FROM project.`company-coupon` WHERE `COMP_ID`= '"
							+ companyId + "')");
			
			Collection<Coupon> CouponByEndDate = new ArrayList<Coupon>();

			Coupon coupon = null;

			while (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));
				CouponByEndDate.add(coupon);
			}
			return CouponByEndDate;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

	public Collection<Coupon> getCouponCombined(CouponType couponType, double maximalPrice, Date endDate,
			long companyId) throws DataBaseException {
		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery("SELECT * FROM coupon where Type ="
					+ couponType + " and Price <= " + maximalPrice + " and  End_Date <=  " + endDate
					+ "=any(SELECT COUPON_ID FROM `project`.`company-coupon` WHERE `COMP_ID`=')" + companyId + "'");
			Collection<Coupon> CouponCompbined = new ArrayList<Coupon>();

			Coupon coupon = null;

			if (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));

			}
			return CouponCompbined;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

	// get All Purchased Coupons for SPECIEFIC Customer!

	public Collection<Coupon> getAllPurchasedCoupons(long customerId) throws DataBaseException {
		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			String sql = "SELECT  * from `coupon` where ID=any(SELECT COUPON_ID FROM `project`.`customer_coupon` where `CUST_ID`='"
					+ customerId + "')";
			ResultSet results = connection.createStatement().executeQuery(sql);
			Collection<Coupon> AllpurchasedCoupons = new ArrayList<Coupon>();

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
				AllpurchasedCoupons.add(coup);
			}
			return AllpurchasedCoupons;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get purchased coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	// getAllPurchasedCouponsByType
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType couponType, long customerId)
			throws DataBaseException {
		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			String sql = "SELECT * FROM project.coupon where Type = '" + couponType
					+ "' and ID =any(SELECT COUPON_ID FROM project.customer_coupon WHERE `CUST_ID`='" + customerId
					+ "')";

			ResultSet results = connection.createStatement().executeQuery(sql);
			
			Collection<Coupon> allPurchasedCouponsByType = new ArrayList<Coupon>();
			Coupon coupon = null;

			while (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));
				allPurchasedCouponsByType.add(coupon);

			}
			return allPurchasedCouponsByType;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get purchased coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

	public Collection<Coupon> getAllPurchasedCouponsByPrice(double coupPrice, long customerId)
			throws DataBaseException {
		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()
					.executeQuery("SELECT * from coupon where Price <= " + coupPrice
							+ "and ID = any(SELECT COUPON_ID FROM project.customer_coupon WHERE `CUST_ID`='"
							+ customerId + "')");

			Collection<Coupon> purchasedCouponsByPrice = new ArrayList<Coupon>();

			Coupon coupon = null;

			while (results.next()) {
				coupon = new Coupon();
				coupon.setId(results.getLong("ID"));
				coupon.setTitle(results.getString("TITLE"));
				coupon.setStartDate(results.getDate("Start_Date"));
				coupon.setEndDate(results.getDate("End_Date"));
				coupon.setAmount(results.getInt("Amount"));
				coupon.setMessage(results.getString("Message"));
				coupon.setPrice(results.getDouble("Price"));
				coupon.setImage(results.getString("Image"));
				purchasedCouponsByPrice.add(coupon);
			}

			return purchasedCouponsByPrice;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get coupons", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

}
