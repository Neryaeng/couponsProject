/*
@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllComp")
public Response getAllCompanies (Collection <Company> companies) throws DataBaseException , SQLException{
		HttpSession session = req.getSession();
		if (checkFlag) {
			try {
				CouponSystemSingleton sys = CouponSystemSingleton.getInstance();
				AdminFacade client = (AdminFacade) sys.login("admin", "1234", "admin");
				Collection<Customer> comp = client.getAllCompany(companies);
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
				Collection<Customer> AllC = client.getAllCustomers(customers);
				return Response.status(200).entity(AllC).build();
			} catch (DataBaseException e) {
				return null;
			}

	}
 */
