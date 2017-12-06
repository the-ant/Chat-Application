package application;

public class Message {
	private String userName;
	private double userId;
	private String imagePath;
	private String message;
	private boolean isMe;
	public Message(double userId, String userName, String imagePath, String message, boolean isMe) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
		this.userName = userName;
		this.imagePath = imagePath;
		this.message = message;
		this.isMe = isMe;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getUserId() {
		return userId;
	}
	public void setUserId(double userId) {
		this.userId = userId;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isMe() {
		return isMe;
	}
	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}
	
}
