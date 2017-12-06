package application;

public class Data {
	String username;
	String imagefile;
	boolean status_user;

	public Data(String username, String imgefile, boolean status_user) {
		this.username = username;
		this.imagefile = imgefile;
		this.status_user = status_user;

	}

	public String getImagefile() {
		return imagefile;
	}

	public void setImagefile(String imagefile) {
		this.imagefile = imagefile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isStatus_user() {
		return status_user;
	}

	public void setStatus_user(boolean status_user) {
		this.status_user = status_user;
	}
}
