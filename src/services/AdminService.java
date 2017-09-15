package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.AdminFacade;
import dao.Company;
import dao.CouponSystemSingleton;
import dao.Customer;
import dao.DataBaseException;

/**
 * @author user
 *
 */
/**
 * @author USER
 *
 */
@Path("adminService")
public class AdminService {

	private boolean checkFlag = false;
	@Context
	private HttpServletRequest req;

	public AdminService() {

	}

	/**
	 * Get company by its ID
	 * 
	 * @param id
	 * @return
	 * @throws DataBaseException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany")
	public Response getCompany(@QueryParam("id") long id) throws DataBaseException {
		HttpSession session = req.getSession();
		// init of REQUEST FROM THE SERVER.

		if (checkFlag) {

		
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Company comp = client.getCompanyById(id);
				return Response.status(200).entity(comp).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
				
			}
		} else
			try {
				if (session == null) {
					return null;
				}
				AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
				Company comp = client.getCompanyById(id);
				return Response.status(200).entity(comp).build();
			} catch (DataBaseException e) {
				return null;
			}
	}


// *************************************************************************
// COMPANY
// *************************************************************************

	
	/**
	 * Create NEW company
	 * 
	 * @param company
	 * @return
	 * @throws DataBaseException
	 * @throws SQLException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addComp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCompany(Company company) throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Company newComp = client.createCompany(company);
				return Response.status(200).entity(newComp).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
					Company newComp = client.createCompany(company);
					return Response.status(200).entity(newComp).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}


	/**
	 * Remove company
	 * 
	 * @param company
	 * @return
	 * @throws DataBaseException
	 */

	@POST
	@Path("removeComp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCompany(Company company) throws DataBaseException {
		HttpSession session = req.getSession();

		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Company newComp = client.removeCompany(company);
				if (newComp != null)
					return Response.status(200).entity(client.removeCompany(company)).build();
				else
					return Response.status(204).entity("the company not exist " + company.getCompName()).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
					Company newComp = client.removeCompany(company);
					if (newComp != null)
						return Response.status(200).entity(client.removeCompany(company)).build();
					else
						return Response.status(204).entity("the company not exist " + company.getCompName()).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}

	/**
	 * Update company details
	 * 
	 * @param company
	 * @return
	 * @throws DataBaseException
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateComp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCompany(Company company) throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				client.updateCompany(company);
				return Response.status(200).entity(company).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else
			try {
				if (session != null) {
					AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
					Company newComp = client.updateCompany(company);
					return Response.status(200).entity(newComp).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
	}

	/**
	 * Get all Companies
	 * 
	 * @return
	 * @throws DataBaseException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllComp")
	public Response getAllCompanies() throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Collection<Company> comp = client.getAllCompany();
				return Response.status(200).entity(comp).build();

			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();

			}
		} else
			try {
				if (session == null) {
					return null;
				}
				AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
				Collection<Company> comp = client.getAllCompany();
				return Response.status(200).entity(comp).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	// *************************************************************************
	// CUSTOMER
	// *************************************************************************

	/**
	 * create NEW customer
	 * 
	 * @param customer
	 * @return
	 * @throws DataBaseException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addCust")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Customer newCust = client.createCustomer(customer);
				return Response.status(200).entity(newCust).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
					Customer newCust = client.createCustomer(customer);
					return Response.status(200).entity(newCust).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}

	/**
	 * @param customer
	 * @return
	 * @throws DataBaseException
	 */
	@POST
	@Path("removeCust")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCustomer(Customer customer) throws DataBaseException {
		HttpSession session = req.getSession();

		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Customer newCust = client.removeCustomer(customer);
				if (newCust != null)
					return Response.status(200).entity(client.removeCustomer(customer)).build();
				else
					return Response.status(204).entity("the customer not exist " + customer.getCustName()).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
					Customer newCust = client.removeCustomer(customer);
					if (newCust != null)
						return Response.status(200).entity(client.removeCustomer(customer)).build();
					else
						return Response.status(204).entity("the customer not exist " + customer.getCustName()).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}

	/**
	 * Update customer details
	 * 
	 * @param customer
	 * @return
	 * @throws DataBaseException
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateCust")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomer(Customer customer) throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				client.updateCustomer(customer);
				return Response.status(200).entity(customer).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else
			try {
				if (session != null) {
					AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
					Customer newCust = client.updateCustomer(customer);
					return Response.status(200).entity(newCust).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
	}

	/**
	 * Get customer by use his ID
	 * @param idSt
	 * @return
	 * @throws DataBaseException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCust")
	public Response getCust(@QueryParam("id") long id) throws DataBaseException {

		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Customer cust = client.getCustomerById(id);
				return Response.status(200).entity(cust).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else
			try {
				if (session == null) {
					return null;
				}
				AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
				Customer cust = client.getCustomerById(id);
				return Response.status(200).entity(cust).build();
			} catch (DataBaseException e) {
				return null;
			}
	}

	/**
	 * @return Get All Customers
	 * @throws DataBaseException
	 * @throws SQLException
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCust")
	public Response getAllCustomers() throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Collection<Customer> cust = client.getAllCustomers();
				return Response.status(200).entity(cust).build();

			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();

			}
		} else
			try {
				if (session == null) {
					return null;
				}
				AdminFacade client = (AdminFacade) session.getAttribute("adminFacade");
				Collection<Customer> cust = client.getAllCustomers();
				return Response.status(200).entity(cust).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

}