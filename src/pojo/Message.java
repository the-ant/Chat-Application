package pojo;

import java.util.Date;

public class Message {

	private int id;
	private String message;
	private int userID;
	private boolean isMe;
	private Date dateTime;
	
	public Message(int id, String message, int userID, boolean isMe, Date dateTime) {
		this.id = id;
		this.message = message;
		this.userID = userID;
		this.isMe = isMe;
		this.dateTime = dateTime;
	}
	
	public Message(int id, String message, boolean isMe) {
		this.id = id;
		this.message = message;
		this.isMe = isMe;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public int getUserID() {
		return userID;
	}

	public boolean isMe() {
		return isMe;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
}
