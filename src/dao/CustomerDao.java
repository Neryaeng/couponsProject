package dao;

import java.util.Collection;

public interface CustomerDao {
	public Customer createCustomer(Customer customer) throws DataBaseException;

	public Customer removeCustomer(Customer customer) throws DataBaseException;

	public Customer updateCustomer(Customer customer) throws DataBaseException;

	public Customer getCustomerById(long id) throws DataBaseException;

	public Collection<Customer> getAllCustomers() throws DataBaseException;

	public Collection<Coupon> getAllCoupons() throws DataBaseException;

	public Customer login(String custName, String password) throws DataBaseException;

	
	//changed 15.6  Time - 17:39
	public Coupon updateCustomerCoupon(long customerId, long couponId) throws DataBaseException;

	boolean customerIsAlreadyExist(String custName) throws DataBaseException;

}
