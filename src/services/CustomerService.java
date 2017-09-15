package services;

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
import dao.CustomerFacade;
import dao.DataBaseException;

@Path("customerService")
public class CustomerService {

	private boolean checkFlag = false;
	@Context
	private HttpServletRequest req;

	// Empty Constructor
	public CustomerService() {

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("purchaseCoup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(Coupon coupon) throws DataBaseException, SQLException {

		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
				Coupon newCoup = client.purchaseCoupon(coupon);
				return Response.status(200).entity(newCoup).build();
			} catch (DataBaseException e) {
				e.printStackTrace();
				return Response.serverError().entity(e.getMessage()).build();
			}
		} else {
			try {
				if (session != null) {
					CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
					Coupon newCoup = client.purchaseCoupon(coupon);
					return Response.status(200).entity(newCoup).build();
				} else {
					return Response.status(500).entity("session not founds").build();
				}
			} catch (DataBaseException e) {
				return Response.serverError().entity(e.getMessage()).build();
			}
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllPurchasedCoupons")
	public Response getAllPurchasedCoupons() throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
				Collection<Coupon> coup = client.getAllPurchasedCoupons();
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
				CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
				Collection<Coupon> coup = client.getAllPurchasedCoupons();
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCoupons")
	public Response getAllCoupons() throws DataBaseException, SQLException {
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
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
				CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
				Collection<Coupon> coup = client.getAllCoupons();
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCoupByType")
	public Response getAllPurchasedCouponsByType(@QueryParam("purchasedCouponType") String coupTypeStr)
			throws DataBaseException, SQLException {

		if (coupTypeStr != "") {
			HttpSession session = req.getSession();
			CouponType coupType = CouponType.valueOf(coupTypeStr);
			if (checkFlag) {
				try {
					CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
					CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
					Collection<Coupon> coup = client.getAllPurchasedCouponsByType(coupType);
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
					CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
					Collection<Coupon> coup = client.getAllPurchasedCouponsByType(coupType);
					return Response.status(200).entity(coup).build();
				} catch (DataBaseException e) {
					return null;
				}

		} else {
			return Response.status(400).entity("bad request coupType is empty").build();
		}

	}

	// *** GENERAL COUPONS BY TYPE ***
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("generalCouponsByType")

	// http://localhost:8080/couponProject/rest/companyService/generalCouponsByType?filterByType=SPORTS
	public Response generalCouponsByType(@QueryParam("filterByType") String coupTypeStr)
			throws DataBaseException, SQLException {

		if (coupTypeStr != "") {
			HttpSession session = req.getSession();
			CouponType coupType = CouponType.valueOf(coupTypeStr);
			if (checkFlag) {
				try {

					CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
					CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
					Collection<Coupon> coup = client.generalCouponsByType(coupType);
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
					CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
					Collection<Coupon> coup = client.generalCouponsByType(coupType);
					return Response.status(200).entity(coup).build();
				} catch (DataBaseException e) {
					return null;
				}

		} else {
			return Response.status(400).entity("canot filter coupons").build();
		}

	}

	// *** GENERAL COUPONS BY PRICE ***
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("generalCouponsByPrice")
	public Response generalCouponsByPrice(@QueryParam("filterByPrice") double coupPrice)
			throws DataBaseException, SQLException {

		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
				Collection<Coupon> coup = client.generalCouponsByPrice(coupPrice);
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
				CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
				Collection<Coupon> coup = client.generalCouponsByPrice(coupPrice);
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getPurchasedCoupByprice")
	public Response getAllPurchasedCouponsByPrice(@QueryParam("coupPrice") double coupPrice)
			throws DataBaseException, SQLException {

		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				CustomerFacade client = (CustomerFacade) sys.login("Ovad Ovadia", "OvadOvadia1234", "customer");
				Collection<Coupon> coup = client.getAllPurchasedCouponsByPrice(coupPrice);
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
				CustomerFacade client = (CustomerFacade) session.getAttribute("customerFacade");
				Collection<Coupon> coup = client.getAllPurchasedCouponsByPrice(coupPrice);
				return Response.status(200).entity(coup).build();
			} catch (DataBaseException e) {
				return null;
			}

	}

}