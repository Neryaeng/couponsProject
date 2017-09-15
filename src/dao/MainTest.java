// package dao;
//
//
// import java.text.ParseException;
// import java.util.ArrayList;
// import java.util.Collection;
//
// import dao.Customer;
// import dao.CustomerDbDao;
// import dao.DataBaseException;
//
// public class MainTest {
//
// public static void main(String[] args) throws ParseException {
//
// try {
//
// testCustomer();
//
// } catch (DataBaseException e) {
// System.err.println(e.getMessage() + ": " + e.getCause());
// }
// }
//
//
// private static void testCustomer() throws DataBaseException{
// long setCustomerId;
// Coupon coupon = new Coupon();
// Collection<Coupon> purchasedCoupons = new ArrayList<Coupon>();
// purchasedCoupons.customerId("1");
// purchasedCoupons.Price(100);
// CustomerFacade.getAllPurchasedCoupons(coupon);
// }
//
// }