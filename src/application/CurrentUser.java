package application;

public class CurrentUser {
	private int user_id;
	private String username;
	private static final class SingletonHelper {
		private static final CurrentUser INSTANCE = new CurrentUser();
	}
	public static CurrentUser getInstance() {
		return SingletonHelper.INSTANCE;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
