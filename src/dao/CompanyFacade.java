package dao;

import java.sql.Date;
import java.util.Collection;

public class CompanyFacade implements CouponClientFacade {

	private CouponDbDao couponDbDao = new CouponDbDao();
	private CompanyDbDao companyDbDao = new CompanyDbDao();
	Company company;
	
	public CompanyFacade() {
		
	}

	public Coupon createCoupon(Coupon coupon) throws DataBaseException {

		// You can not add companies with the same name as one of the companies
		// that already exist in the database.
		boolean existedCoupon = couponDbDao.couponIsAlreadyExist(coupon.getTitle());
		if (!existedCoupon) {
		return	 couponDbDao.createCoupon(coupon,this.company.getId());

		} else {
			System.out.println("ERROR! - The Coupon " + coupon.getTitle() + " is already exist");
		}
		return null;
	}

	// romoveCustomer's Coupon
	public void removeCustomerCoupon(Coupon coupon) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			coupDbDao.removeCustomerCoupon(coupon);
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
	}


	public Coupon removeCoupon(Coupon coupon) throws DataBaseException {
	
		try {
			return couponDbDao.removeCoupon(coupon);
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// updateCoupon

	public Coupon updateCoupon(Coupon coupon) throws DataBaseException {
		try {
			return 	couponDbDao.updateCoupon(coupon);
			} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}
		// update ONLY "End Date" and "Price"
				

	/**
	 * @param couponId
	 * @return
	 * @throws DataBaseException
	 */
	public Coupon getCouponById(long id) throws DataBaseException {
		return couponDbDao.getCouponById(id);
	}

	// getAllCoupon
	/**
	 * * @param coupons 
	 * @return
	 * @throws DataBaseException
	 */
	public Collection<Coupon> getAllCoupons() throws DataBaseException {
		return couponDbDao.getAllCoupons(company.getId());
		}

	// getCouponByType
	public Collection<Coupon> getCouponByType(CouponType coupType) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			return coupDbDao.getCouponByType(coupType,this.company.getId());

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// getCouponBy Maximal Price
	public Collection<Coupon> getCouponByMaximalPrice(double maximalPrice, long companyId) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			coupDbDao.getCouponByMaximalPrice(maximalPrice, companyId);

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return getCouponByMaximalPrice(maximalPrice, companyId);
	}

	// getCouponByEndDate
	public Collection<Coupon> getCouponByEndDate(Date endDate) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			return	coupDbDao.getCouponByEndDate(endDate, this.company.getId());

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	// getCouponBy Type , Maximal Price AND EndDate

	public Collection<Coupon> getCouponCombined(CouponType coupType, double maximalPrice, Date endDate, long companyId)
			throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			coupDbDao.getCouponCombined(coupType, maximalPrice, endDate, companyId);

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return getCouponCombined(coupType, maximalPrice, endDate, companyId);
	}

	// LOGIN
	@Override
	public CouponClientFacade login(String user, String password) {
		
		

		try {
			company = companyDbDao.login(user, password);
			if(company!=null)
			{
				return this;
			}
		} catch (DataBaseException e) {

			e.printStackTrace();
		}
		return null;

	}

	public Collection<Coupon> getCouponByMaximalPrice(double coupPrice ) throws DataBaseException {

		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			return coupDbDao.getCouponByMaximalPrice(coupPrice,this.company.getId());

		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
