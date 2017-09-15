package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ConnectionPoolSingleton {

	public static final int MAX_CONNECTIONS = 10;
	private static ConnectionPoolSingleton instance = null;
	private static Collection<Connection> pool = new ArrayList<Connection>();
	private static Collection<Connection> NOT_USED_CONNECTIONS = new ArrayList<Connection>();

	private ConnectionPoolSingleton() {

	}

	public static final ConnectionPoolSingleton getInstance() {
		if (instance == null) {
			instance = new ConnectionPoolSingleton();
			try {
				instance.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public synchronized Connection getConnection() {
		while (pool.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Connection connection = pool.iterator().next();
		pool.remove(connection);
		NOT_USED_CONNECTIONS.add(connection);
		notifyAll();
		return connection;
	}

	public synchronized void returnConnection(Connection connection) {
		while (pool.size() >= MAX_CONNECTIONS) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pool.add(connection);
		NOT_USED_CONNECTIONS.remove(connection);
		notifyAll();
	}

	public static void closeAllConnection() throws SQLException {
		for (Connection connection : pool) {
			connection.close();

		}
		for (Connection connection : NOT_USED_CONNECTIONS) {
			connection.close();
		}

	}

	public int getSize() {
		// TODO Auto-generated method stub
		return pool.size();
	}

	private void init() throws Exception {
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			pool.add(createConnection());
		}
	}

	private Connection createConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "root");
		return connection;
	}
}