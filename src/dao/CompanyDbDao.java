
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dao.Company;
import dao.Coupon;
import dao.DataBaseException;

public class CompanyDbDao implements CompanyDao {

	// CONSTRUCTOR!!!
	public CompanyDbDao() {

	}
	// Here starts the implements of the INTERFACE "CompanyDao":

	@Override
	public Company createCompany(Company company) throws DataBaseException {
		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			String name = company.getCompName();
			String password = company.getPassword();
			String email = company.getEmail();
			String sql = "INSERT INTO company (`Comp_Name`,`Password` , `Email`) VALUES ('" + name + "','" + password
					+ "' , '" + email + "')";

			connection.createStatement().execute(sql);
			System.out.println(sql);
		} catch (SQLException e) {
			throw new DataBaseException("incapable to create company ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return company;
	}

	// REMOVE coupons from Company_Coupon Table - BY COMPANY ID!!!!!!

	@Override
	public void removeAllCompanyCoupons(Company company) throws DataBaseException {

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

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

	public Company removeCompany(Company company) throws DataBaseException {

		Connection connection = null;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			long compId = company.getId();
			String sql = "DELETE FROM `project`.`company-coupon` WHERE `COMP_ID`=" + compId;
			System.out.println(sql);
			connection.createStatement().execute(sql);
			sql = "DELETE FROM project.company WHERE `ID`=" + compId;

			connection.createStatement().execute(sql);
			System.out.println(sql);
		} catch (SQLException e) {
			throw new DataBaseException("incapable to remove the company " + company.getCompName(), e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		return company;
	}

	
	@Override
	public Company updateCompany(Company company) throws DataBaseException {

		Connection connection = null;
		long id = company.getId();
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

		//You can not change the company name!
			String password = company.getPassword();
			String email = company.getEmail();
			String sql = "UPDATE `company` SET " + " `Password`='" + password + "' ," + " `Email`='" + email
					+ "' WHERE `ID`=" + id;

			connection.createStatement().execute(sql);
			System.out.println(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to update company " + id, e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);

		}
		return company;

	}

	@Override
	public Company getCompanyById(long id) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement()
					.executeQuery("select * from project.company where ID=" + id);

			Company company = null;

			if (results.next()) {
				company = new Company();
				company.setId(results.getLong("ID"));
				company.setCompName(results.getString("Comp_Name"));
				company.setPassword(results.getString("Password"));
				company.setEmail(results.getString("Email"));
			}
			return company;

		} catch (SQLException e) {

			throw new DataBaseException("incapable to get company by id " + id, e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	@Override
	public Collection<Company> getAllCompany() throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery("select * from company");

			Collection<Company> allCompanies = new ArrayList<Company>();

			while (results.next()) {
				Company com = new Company();
				com.setId(results.getLong("ID"));
				com.setCompName(results.getString("Comp_Name"));
				com.setPassword(results.getString("Password"));
				com.setEmail(results.getString("Email"));
				allCompanies.add(com);
			}
			return allCompanies;
		} catch (SQLException e) {
			throw new DataBaseException("incapable to get all companies", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCoupons(Company company) throws DataBaseException {

		Connection connection = null;

		try {

			connection = ConnectionPoolSingleton.getInstance().getConnection();

			long id = company.getId();

			String sql = "SELECT c.* FROM project.coupon c inner join project.`company-coupon` cc where cc.COMP_id = '"
					+ id + "' and c.ID = cc.COUPON_ID";

			ResultSet results = connection.createStatement().executeQuery(sql);

			Collection<Coupon> coupons = new ArrayList<Coupon>();

			while (results.next()) {
				Coupon c = new Coupon();
				c.setId(results.getLong("ID"));
				c.setTitle(results.getString("Title"));
				coupons.add(c);
			}
			return coupons;

		} catch (SQLException e) {
			throw new DataBaseException("incapable to get coupons from company ", e);
		}

		finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}

	}

	@Override
	public boolean companyIsAlreadyExist(String compName) throws DataBaseException, SQLException {

		String sql = "SELECT * FROM project.company where Comp_Name ='" + compName + "'";

		Connection connection = null;
		ResultSet results;
		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();

			results = connection.createStatement().executeQuery(sql);

		} catch (SQLException e) {
			throw new DataBaseException("incapable to create company ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
		System.out.println(sql);
		System.out.println(results);
		if (results.next())
			return true;
		else
			return false;
	}

	@Override
	public Company login(String compName, String password) throws DataBaseException {

		Connection connection = null;

		try {
			connection = ConnectionPoolSingleton.getInstance().getConnection();
			ResultSet results = connection.createStatement().executeQuery(
					"SELECT * FROM company where Comp_Name = '" + compName + "' and Password = '" + password + "'");

			if (results.next()) {
				return getCompanyrFromDB(results);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DataBaseException("incorrect name or password ", e);
		} finally {
			ConnectionPoolSingleton.getInstance().returnConnection(connection);
		}
	}

	/**
	 * 
	 * @param rs
	 *            ResultSet
	 * @return Company Get from the DB 1 company
	 * @throws CouponsException
	 */
	private Company getCompanyrFromDB(ResultSet rs) throws DataBaseException {
		Company company = new Company();
		try {
			company.setId(rs.getLong(1));
			company.setCompName(rs.getString(2));
			company.setPassword(rs.getString(3));
			company.setEmail(rs.getString(4));
		} catch (Exception e) {
			throw new DataBaseException("Do you have issue with your DB APP.COMPANY", e);

		}
		return company;
	}

}