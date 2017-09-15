package dao;

public class DataBaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataBaseException(String message, Throwable arg1) {
		super(message, arg1);
	}
	public DataBaseException(String message) {
		super(message);
	}
}
