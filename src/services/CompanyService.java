package services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import org.apache.catalina.User;

import dao.CompanyFacade;
import dao.Coupon;
import dao.AdminFacade;
import dao.Company;
import dao.CouponSystemSingleton;
import dao.CouponType;
import dao.DataBaseException;

@Path("companyService")
public class CompanyService {

	private boolean checkFlag = false;
	@Context
	private HttpServletRequest req;

	public CompanyService() {

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addCoup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCoupon(Coupon coupon) throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Samsung", "Samsung1234", "company");
				Coupon newCoup = client.createCoupon(coupon);
				return Response.status(200).entity(newCoup).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
					Coupon newCoup = client.createCoupon(coupon);
					return Response.status(200).entity(newCoup).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}

	@POST
	@Path("removeCoup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCoupon(Coupon coupon) throws DataBaseException {
		HttpSession session = req.getSession();

		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Samsung", "Samsung1234", "company");
				Coupon removeCoup = client.removeCoupon(coupon);
				if (removeCoup != null)
					return Response.status(200).entity(removeCoup).build();
				else
					return Response.status(204).entity("the company not exist " + coupon.getTitle()).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
					Coupon removeCoup = client.removeCoupon(coupon);
					if (removeCoup != null)
						return Response.status(200).entity(removeCoup).build();
					else
						return Response.status(204).entity("the coupon not exist " + coupon.getTitle()).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateCoup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCoupon(Coupon coupon) throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Samsung", "Samsung1234", "company");
				client.updateCoupon(coupon);
				return Response.status(200).entity(coupon).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else
			try {
				if (session != null) {
					CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
					Coupon newCoup = client.updateCoupon(coupon);
					return Response.status(200).entity(newCoup).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCouponById")
	public Response getCouponById(@QueryParam("id") long id) throws DataBaseException {
		HttpSession session = req.getSession(); // init of REQUEST FROM THE
												// SERVER.

		if (checkFlag) {

			try {

				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Samsung", "Samsung1234", "company");
				Coupon coup = client.getCouponById(id);
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else
			try {
				if (session == null) {
					return null;
				}
				CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
				Coupon coup = client.getCouponById(id);
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCoup")
	public Response getAllCoupons() throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Samsung", "Samsung1234", "company");
				Collection<Coupon> coup = client.getAllCoupons();
				return Response.status(200).entity(coup).build();

			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();

			}
		} else
			try {
				if (session == null) {
					return null;
				}
				CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
				Collection<Coupon> coup = client.getAllCoupons();
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCoupByType")
	public Response getCouponByType(@QueryParam("coupType") String coupTypeStr) throws DataBaseException, SQLException {
		if (coupTypeStr != "") {
			HttpSession session = req.getSession();
			CouponType coupType = CouponType.valueOf(coupTypeStr);

			if (checkFlag) {
				try {
					CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
					CompanyFacade client = (CompanyFacade) sys.login("Samsung", "Samsung1234", "company");
					Collection<Coupon> coup = client.getCouponByType(coupType);
					return Response.status(200).entity(coup).build();

				} catch (DataBaseException e) {
					e.printStackTrace();
					return Response.serverError().entity(e.getMessage()).build();

				}
			} else
				try {
					if (session == null) {
						return null;
					}
					CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
					Collection<Coupon> coup = client.getCouponByType(coupType);
					return Response.status(200).entity(coup).build();
				} catch (DataBaseException e) {
					return null;
				}

		} else {
			return Response.status(400).entity("bad request coupType is empty").build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCoupByPrice")
	public Response getCouponByMaximalPrice(@QueryParam("coupPrice") double coupPrice)
			throws DataBaseException, SQLException {

		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
				Collection<Coupon> coup = client.getCouponByMaximalPrice(coupPrice);
				return Response.status(200).entity(coup).build();

			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();

			}
		} else
			try {
				if (session == null) {
					return null;
				}
				CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
				Collection<Coupon> coup = client.getCouponByMaximalPrice(coupPrice);
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCoupByEndDate")
	public Response getCouponByEndDate(@QueryParam("coupEndDate") Date endDate) throws DataBaseException, SQLException {

		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CompanyFacade client = (CompanyFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
				Collection<Coupon> coup = client.getCouponByEndDate(endDate);
				return Response.status(200).entity(coup).build();

			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();

			}
		} else
			try {
				if (session == null) {
					return null;
				}
				CompanyFacade client = (CompanyFacade) session.getAttribute("companyFacade");
				Collection<Coupon> coup = client.getCouponByEndDate(endDate);
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

}