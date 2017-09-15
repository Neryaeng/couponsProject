package dao;

import java.sql.SQLException;
import java.util.Collection;

import dao.Company;
import dao.CompanyDbDao;
import dao.Customer;
import dao.CustomerDbDao;
import dao.DataBaseException;

public class AdminFacade implements CouponClientFacade {

	CompanyDbDao companyDbDao = new CompanyDbDao();
	CustomerDbDao customerDbDao = new CustomerDbDao();

	public AdminFacade() {
	}

	/**
	 * Creating a new company.
	 *(!) Two companies can not have the same name 
	 * @param company
	 * @throws DataBaseException
	 * @throws SQLException 
	 */
	
	public Company createCompany(Company company) throws DataBaseException, SQLException { 

/* 
* You can not add companies with the same name as one of the companies
*that already exist in the database.
*/
		
		boolean existedComp = companyDbDao.companyIsAlreadyExist(company.getCompName());
		if (!existedComp) {
			return companyDbDao.createCompany(company);
			 
		} else {
			System.out.println("  The Company " + company.getCompName() + " is already exist");
			throw new  DataBaseException("ERROR! - The Company " + company.getCompName() + " is already exist");
		}
	}
	
	
	/**
	 * Removing a company from the Data Base.
	 * @param id
	 * @throws DataBaseException
	 */
	public Company removeCompany(Company company) throws DataBaseException {
		
		return 	companyDbDao.removeCompany(company);
	}

	/**
	 * We deleting all the coupons that belonging to this Company (We sort
	 * according to the ID of the Company).
	 * @param coupon
	 * @throws DataBaseException
	 */
	public void removeCompanyByCompanyId(Coupon coupon) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			coupDbDao.removeCouponByCompanyId(coupon);
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * update ONLY Password and E-mail.
	 * @param company
	 * @return 
	 * @throws DataBaseException
	 */
	public Company updateCompany(Company company) throws DataBaseException {
	
		// update ONLY Password and E-Mail
		return 	companyDbDao.updateCompany(company);
		
	}

	/**
	 * Locating and filtering a specific company, according to its ID.
	 * @param id
	 * @return
	 * @throws DataBaseException
	 */
	public Company getCompanyById(long id) throws DataBaseException {
		return companyDbDao.getCompanyById(id);
	}

	/**
	 * Collects and displays all the companies in the database.
	 * @param companies 
	 * @return
	 * @throws DataBaseException
	 */
	public Collection<Company> getAllCompany() throws DataBaseException {
		return companyDbDao.getAllCompany();
	}

	/**
	 * Create a new Customer
	 * (!)Two customers can not have the same name.
	 * @param customer
	 * @return 
	 * @throws DataBaseException
	 */
	public Customer createCustomer(Customer customer) throws DataBaseException {

		boolean customerExist = customerDbDao.customerIsAlreadyExist(customer.getCustName());
		if (!customerExist) {
			customerDbDao.createCustomer(customer);
		} else {
			System.out.println("Customer " + customer.getCustName() + " is already exist");
		}
		return customerDbDao.createCustomer(customer);
	}

	/**
	 * Removing a customer from the Data Base.
	 * @param customer
	 * @return 
	 * @throws DataBaseException
	 */
	public Customer removeCustomer(Customer customer) throws DataBaseException {
		customerDbDao.removeCustomer(customer);
		// and remove all purchased coupons by this customer.
		return customerDbDao.removeCustomer(customer);
	}

	/**
	 * This method remove Coupons By Customer Id (We sort according to the ID of
	 * the Customer). We must clean the purchase history of the customer.
	 * @param coupon
	 * @throws DataBaseException
	 */
	public void removeCouponByCustomerId(Coupon coupon) throws DataBaseException {
		CouponDbDao coupDbDao = new CouponDbDao();

		try {
			coupDbDao.removeCouponByCustomerId(coupon);
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * update ONLY Password.
	 * @param customer
	 * @return 
	 * @throws DataBaseException
	 */
	public Customer updateCustomer(Customer customer) throws DataBaseException {
	
		return 	customerDbDao.updateCustomer(customer);

	}

	/**
	 * @param id
	 * @return
	 * @throws DataBaseException
	 */
	public Customer getCustomerById(long id) throws DataBaseException {
		return customerDbDao.getCustomerById(id);
	}

	/**
	 * Collects and displays all the customers in the database.
	 * @return
	 * @throws DataBaseException
	 */
	public Collection<Customer> getAllCustomers() throws DataBaseException {
		return customerDbDao.getAllCustomers();
	}

	/**
	 * Admin's Login
	 * 
	 */
	@Override
	public CouponClientFacade login(String name, String password) throws DataBaseException {

		if ("admin".equals(name) && "1234".equals(password)) {
			return this;
		} else {
			throw new DataBaseException("One of the folowing is wrong: name or password", null);
		}
	}
	
	
}
