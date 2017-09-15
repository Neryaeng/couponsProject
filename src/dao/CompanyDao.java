package dao;

import java.sql.SQLException;
import java.util.Collection;

public interface CompanyDao {

	public Company createCompany(Company company) throws DataBaseException;

	public Company removeCompany(Company company) throws DataBaseException;

	public void removeAllCompanyCoupons(Company company) throws DataBaseException;

	public Company updateCompany(Company company) throws DataBaseException;

	public Company getCompanyById(long id) throws DataBaseException;

	public Collection<Company> getAllCompany() throws DataBaseException;

	public Collection<Coupon> getCoupons(Company company) throws DataBaseException;

	public Company login(String compName, String password) throws DataBaseException;

	boolean companyIsAlreadyExist(String compName) throws DataBaseException ,SQLException;
}
