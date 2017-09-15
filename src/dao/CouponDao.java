package dao;

//import java.sql.Date;
import java.util.Collection;

public interface CouponDao {
//Changed to void 7.7.2017
	public Coupon createCoupon(Coupon coupon , long companyId) throws DataBaseException;

	public Coupon removeCoupon(Coupon coupon) throws DataBaseException;

	public void removeCompanyCoupon(Coupon coupon) throws DataBaseException;

	public void removeCustomerCoupon(Coupon coupon) throws DataBaseException;

	public Coupon updateCoupon(Coupon coupon) throws DataBaseException;

	public Coupon getCouponById(long id) throws DataBaseException;

	public Collection<Coupon> getAllCoupons(long companyId) throws DataBaseException;

	public Coupon updateCouponAmount(Coupon couponId) throws DataBaseException;

	boolean couponIsAlreadyExist(String couponTitle) throws DataBaseException;

}
