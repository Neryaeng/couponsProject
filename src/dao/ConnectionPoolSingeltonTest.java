package dao;

public class ConnectionPoolSingeltonTest {

	public static void main(String[] args) {
		GetConnection getConnection = new GetConnection();
		ReturnConnection returnConnection = new ReturnConnection();

		getConnection.start();
		returnConnection.start();
	}

}
