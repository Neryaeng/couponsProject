package services;

	import java.net.URI;
	import java.net.URISyntaxException;
	import java.util.ArrayList;
	import java.util.List;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
	import javax.ws.rs.POST;
	import javax.ws.rs.Path;
	import javax.ws.rs.PathParam;
	import javax.ws.rs.Produces;
	import javax.ws.rs.core.Context;
	import javax.ws.rs.core.MediaType;
	import javax.ws.rs.core.Response;

	import services.*;
	import dao.*;

	@Path ("webLogin")
	public class  SystemLogin {
		AdminFacade adminFacade = new AdminFacade();
		CompanyFacade companyFacade = new CompanyFacade();
		CustomerFacade customerFacade = new CustomerFacade();
		
		
		@Context
		private HttpServletRequest req;
		
		
		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("login")
		@Produces(MediaType.TEXT_PLAIN)
		/**
		 * Login 
		 * @param adminName
		 * @param password
		 * @param cType
		 * @throws Exception
		 */
		public Response login(LoginReq loginreq) throws DataBaseException
		{
			String cTypeStr =loginreq.getRole();
			String userName= loginreq.getUsername();
			String password=loginreq.getPassword();
			//get the current session
			HttpSession session=req.getSession(false); 
			if(session!=null)
			{
				//close session
				req.getSession(false).invalidate();
			}
			//cTypeStr=null;
			try{
				CouponSystemSingleton cSys = CouponSystemSingleton.getInstance();
				//create new session
				session=req.getSession(true);

				switch (cTypeStr)
				{
				
				// HttpSession setAttribute  =  Binds an object to this session, using the name specified.
				
				case "admin":	
							AdminFacade clientAdmin= (AdminFacade) cSys.login(userName, password, cTypeStr);
							if(clientAdmin!=null)
							{
								session.setAttribute("adminFacade", clientAdmin);
								session.setAttribute("admin", userName); //set the username
							}else{
								return Response.status(400).entity("Not a valid User or Password").build();	
							}
							break;
							
				case "company":	CompanyFacade clientComp= (CompanyFacade) cSys.login(userName, password, cTypeStr);
								if(clientComp!=null){
									session.setAttribute("companyFacade", clientComp);
									session.setAttribute("company", userName);
								}else{
									return Response.status(400).entity("Not a valid User or Password").build();
								}
								break;
				case "customer":	CustomerFacade clientCust= (CustomerFacade) cSys.login(userName, password, cTypeStr);
								if(clientCust!=null)
								{
								session.setAttribute("customerFacade", clientCust);
								session.setAttribute("customer", userName);
								}else{
									return Response.status(400).entity("Not a valid User or Password").build();
								}
								break;	
								
				default: //TODO login again, clean fields 
					return Response.status(400).entity("Not a valid User or Password").build();
								}						
				
				session.setAttribute("couponSystem", cSys);
				
				}catch(Exception e)
			{
				System.out.println(e.getMessage());
				return Response.status(500).entity(e.getMessage()).build();	
			
			}
				
			return Response.status(200).entity(userName).build();	
			
		}
		
		/**
		 * Check if the session is available
		 * 
		 * @return the username if valid.
		 */
		//TODO for all type of users
		@GET
		@Path("/chkLogin/{att}")
		@Produces(MediaType.TEXT_PLAIN)
		public String chkLogin(@PathParam ("att") String attr ) {
			
			String result=null;
			
			if ((req.getSession(false) == null) || (req.getSession().getAttribute(attr) == null)) {
				result = "false"; 
			}
			else result = (String)req.getSession().getAttribute(attr);
			return result;	
		}

		@GET
		@Path("/logoff")
		public Response logout() {
			
			if (req.getSession(false) != null) {
				req.getSession(false).invalidate(); //loginpage/
			}		
			
			URI loc;
			
			try {
				loc = new URI("../#/login");
				return Response.seeOther(loc).build();
			} catch (URISyntaxException e) {
				return null;
			}		
		}	

		@GET
		@Path("clientType/{name}")
		@Produces(MediaType.APPLICATION_JSON)
		public CouponClientFacade getClientType(@PathParam("name")String name){
			switch (name)
			{
			case "ADMIN": return adminFacade;
			case "CUSTOMER": return customerFacade;
			case "COMPANY": return companyFacade;
			
			}
			
			return null;
		}
		
		
		@GET
		@Path("clientTypeNames")
		@Produces(MediaType.APPLICATION_JSON)
		public List<StringWrapper> getClientNames() throws DataBaseException{
			List<StringWrapper> names=new ArrayList<>();
			StringWrapper w1=new StringWrapper();
		//	w1.setValue(CustomerDbDao.toString());
			w1.compNames();
			names.add(w1);
			StringWrapper w2=new StringWrapper();
			w2.custNames();
			names.add(w2);
			StringWrapper w3=new StringWrapper();
			w3.adminName();
			names.add(w3);
	
			return names;
		}

		/**
		 * @param cTypeST
		 * @return
		 */

	}

