package pojo;

public class User {

	private int id;
	private String username;
	private String password;
	private int online;
	private String fullname;

	public User() {
	}

	public User(int id, String username, String password, String fullname, int online) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.setFullname(fullname);
		this.online = online;
	}

	public User(int id, String fullName, int online) {
		this.id = id;
		this.fullname = fullName;
		this.online = online;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int isOnline() {
		return online;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

}
