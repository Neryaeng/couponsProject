package dao;

public class GetConnection extends Thread {

	private ConnectionPoolSingleton pool;

	public GetConnection() {
		pool = ConnectionPoolSingleton.getInstance();
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("Get Connection is now ----->>  RUN");
			pool.getConnection();
			try {

				Thread.sleep(3000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
}