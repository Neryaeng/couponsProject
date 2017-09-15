package dao;


import java.util.Collection;

import dao.DataBaseException;

public class CustomerFacade implements CouponClientFacade {

	CustomerDbDao customerDbDao = new CustomerDbDao();
	CouponDbDao couponDbDao = new CouponDbDao();
	Customer customer;

	
	public CustomerFacade() {

	}

	public Coupon purchaseCoupon(Coupon coupon) throws DataBaseException {

		return customerDbDao.updateCustomerCoupon(this.customer.getId(), coupon.getId());

	}

	
	public Collection<Coupon> getAllPurchasedCoupons() throws DataBaseException {
		return couponDbDao.getAllPurchasedCoupons(customer.getId());
	}

	public Collection<Coupon> getAllCoupons() throws DataBaseException {
		return customerDbDao.getAllCoupons();
	}

	
	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType coupType) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			return coupDbDao.getAllPurchasedCouponsByType(coupType, this.customer.getId());

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}


	public Collection<Coupon> getAllPurchasedCouponsByPrice(double coupPrice) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			return coupDbDao.getAllPurchasedCouponsByPrice(coupPrice, this.customer.getId());

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CouponClientFacade login(String user, String password) {

		try {
			customer = customerDbDao.login(user, password);
			if (customer != null) {
				return this;
			}
		} catch (DataBaseException e) {

			e.printStackTrace();
		}
		return null;

	}

	// *** GENERAL GET COUPON BY TYPE ***
	public Collection<Coupon> generalCouponsByType(CouponType coupType) throws DataBaseException {

		CustomerDbDao custDbDao = new CustomerDbDao();

		try {
			return custDbDao.generalCouponsByType(coupType);

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Collection<Coupon> generalCouponsByPrice(double coupPrice) throws DataBaseException {

		CustomerDbDao custDbDao = new CustomerDbDao();

		try {
			return custDbDao.generalCouponsByPrice(coupPrice);

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
