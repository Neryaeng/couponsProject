package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ReturnConnection extends Thread {

	private ConnectionPoolSingleton pool;
	private Connection conn;

	public ReturnConnection() {
		pool = ConnectionPoolSingleton.getInstance();

	}

	@Override
	public void run() {
		while (true) {

			try {
				System.out.println("     Return Connection is now ----->>  RUN");
				// We put the "create connection" code - inside the WHILE LOOP
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "root");
				} catch (SQLException e) {

					e.printStackTrace();
				}
				pool.returnConnection(conn);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
