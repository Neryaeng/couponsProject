package dao;

import dao.DataBaseException;;

public class CouponSystemSingleton {

	private static CouponSystemSingleton instance;
	private DailyCouponExpirationTask expirationTask;

	private CouponSystemSingleton() {
		expirationTask = new DailyCouponExpirationTask();
		Thread thread = new Thread(expirationTask);
		thread.start();
	}

	public static CouponSystemSingleton getInstance() {
		if (instance == null) {
			instance = new CouponSystemSingleton();
		}
		return instance;
	}

	public CouponClientFacade login(String name, String password, String clientType) throws DataBaseException {

		if (clientType.equals("company")) {
			CompanyFacade companyFacade = new CompanyFacade();
			CompanyFacade ret = (CompanyFacade) companyFacade.login(name, password);
			if (ret != null) {
				return ret;
			} else {
				return null;
			}
		} else if (clientType.equals("customer")) {
			CustomerFacade customerFacade = new CustomerFacade();
			CustomerFacade ret =  (CustomerFacade) customerFacade.login(name, password);
			if (ret != null) {
				return ret;
			} else {
				return null;
			}
		} else if (clientType.equals("admin")) {
			AdminFacade adminFacade = new AdminFacade();
			AdminFacade ret = (AdminFacade) adminFacade.login(name, password);
			if (ret != null) {
				return ret;
			} else {
				return null;
			}
		}
		return null;
	}

	public void shutdown() {
		expirationTask.stopTask();
	}
}
