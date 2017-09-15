package dao;

import java.sql.Connection;
import java.sql.ResultSet;

import dao.ConnectionPoolSingleton;
import dao.Coupon;
import dao.CouponDbDao;


public class DailyCouponExpirationTask implements Runnable {

	private static final int DAY = 24 * 60 * 60 * 1000;
	private boolean running = true;
	private final ConnectionPoolSingleton instance = ConnectionPoolSingleton.getInstance();

	public DailyCouponExpirationTask() {

	}

	public void run() {

		while (running) {
			try {
				Coupon coupon = new Coupon();
				CouponDbDao couponDbDao = new CouponDbDao();

				Connection connection = instance.getConnection();
				ResultSet results = connection.createStatement()
						.executeQuery("SELECT ID from coupon c WHERE"
								+ " datediff(c.End_Date,CURDATE()) <= 0");

				Thread.sleep(DAY);
				
					Thread.currentThread().getState();

				while (results.next()) {
					coupon.setId(results.getLong("ID"));
					couponDbDao.removeCoupon(coupon);
				}
			} catch (Exception e) {
			}
		}
	}

	public void stopTask() {
		this.running = false;
	}
}
