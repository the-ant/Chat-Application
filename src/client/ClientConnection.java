package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.json.JSONObject;

import application.LoginRegisterController;
import application.MainController;
import javafx.application.Platform;
import pojo.FlagConnection;
import pojo.Message;
import utils.JSONUtils;

public class ClientConnection extends Thread {

	private Socket socket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private boolean running = true;
	private LoginRegisterController loginRegisterController;
	private MainController mainController;

	public ClientConnection(LoginRegisterController controller, Socket socket) {
		this.socket = socket;
		this.loginRegisterController = controller;
		try {
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (running) {
				while (dataIn.available() == 0) {
					Thread.sleep(1);
				}

				String msg = dataIn.readUTF();
				if (msg != null) {
					int options = handleOptions(msg);
					handleFrameReceive(options, msg);
				}
			}
			close();
		} catch (IOException | InterruptedException e) {
			System.out.println(e.toString());
			close();
		}
	}

	private void handleFrameReceive(int options, String msg) {
		if (options != 0) {
			switch (options) {
				case 1:
					alertNotExistUsername(msg);
					break;
				case 2:
					handleLoginSuccessful(msg);
					break;
				case 3:
					handleLoginUnsuccessful(msg);
					break;
				case 4:
					alertExistUsernameRegister(msg);
					break;
			}
		} else {
			String[] frameMsg = msg.split("[|]");
			decodeFrame(frameMsg);
		}
	}

	public int handleOptions(String msg) {
		if (msg != null) {
			if (msg.contains("alertNotExistUsername-"))
				return 1;

			if (msg.contains("lgResult-true"))
				return 2;

			if (msg.contains("lgResult-false"))
				return 3;

			if (msg.contains("alertExistUsernameReg-"))
				return 4;

		}
		return 0;
	}

	private void alertNotExistUsername(String msg) {
		String msgError = msg.substring(msg.indexOf("-") + 1);
		
		if (!msgError.isEmpty() || msg != null) {
			setTextAlertErrorLoginFom("That username don't exist. Try again?");
		}
	}
	
	private void handleLoginSuccessful(String msg) {
		String[] inforUser = msg.substring(msg.indexOf(":") + 1).split("-");
		System.out.println("handleLoginSuccessful: " + msg);
		String fullname = inforUser[1];
		int userId = Integer.parseInt(inforUser[0]);
		String relationship = inforUser[2];
		loginRegisterController.getMyInfo(true, fullname, userId, relationship);
	}

	private void handleLoginUnsuccessful(String msg) {
		loginRegisterController.getMyInfo(false, null, 0, "");
		setTextAlertErrorLoginFom("Login unsuccessful. Try again!");
	}

	private void alertExistUsernameRegister(String msg) {
		String msgErrorExistUsername = msg.substring(msg.indexOf("-") + 1);
		if (!msgErrorExistUsername.isEmpty() || msg != null) {
			setTextAlertErrorRegister("That username is taken. Try again!");
		}
	}

	private void decodeFrame(String[] frameMsg) {
		int flag = Integer.parseInt(frameMsg[0]);
		System.out.println("==> Flag: " + flag);

		switch (flag) {
		case FlagConnection.LOGOUT:
			receiveResponseLogout(Integer.parseInt(frameMsg[1]));
			break;

		case FlagConnection.GET_MESSAGE:
			int groupId = Integer.parseInt(frameMsg[1]);
			String msg = frameMsg[2];
			receiveResponseGetMessage(groupId, msg);
			break;

		case FlagConnection.ADD_FRIEND:
			receiveResponseAddFriend(Integer.parseInt(frameMsg[1]));
			break;

		case FlagConnection.ADD_GROUP:
			receiveResponseAddGroup(Integer.parseInt(frameMsg[1]));
			break;

		case FlagConnection.RECEIVE_MESSAGE:
			int receiveGroupId = Integer.parseInt(frameMsg[1]);
			int senderId = Integer.parseInt(frameMsg[2]);
			String receiveMsg = frameMsg[3];
			boolean isFile = Boolean.parseBoolean(frameMsg[4]);
			receiveMessage(receiveGroupId, senderId, receiveMsg, isFile);
			break;
			
		case FlagConnection.NOTIFY_ONLINE:
			int onlineUserId = Integer.parseInt(frameMsg[1]);
			notifyOnlineFriend(onlineUserId);
			break;
			
		case FlagConnection.NOTIFY_LOGOUT:
			int logoutUserId = Integer.parseInt(frameMsg[1]);
			notifyLogoutFriend(logoutUserId);
			break;
			
		case FlagConnection.SEND_FILE:
			break;
		}
	}

	private void notifyLogoutFriend(int logoutUserId) {
		Platform.runLater(() -> mainController.notifyLogoutFriend(logoutUserId));
	}

	private void notifyOnlineFriend(int onlineUserId) {
		Platform.runLater(() -> mainController.notifyOnlineFriend(onlineUserId));
	}

	private void receiveMessage(int groupId, int senderId, String msg, boolean isFile) {
		Platform.runLater(() -> mainController.setMessageToGroupById(groupId, senderId, msg, isFile));
	}

	private void receiveResponseAddGroup(int response) {

	}

	private void receiveResponseAddFriend(int response) {

	}

	private void receiveResponseGetMessage(int groupId, String response) {
		JSONObject msgsObj = JSONUtils.parseJSON(response);
		List<Message> getMsgs = JSONUtils.parseMessages(msgsObj.getJSONArray("messages"));
		Platform.runLater(() -> mainController.setMessagesToSelectedGroup(groupId, getMsgs));
	}

	private void receiveResponseLogout(int responseLogout) {
		if (responseLogout == 1) {
			Platform.runLater(() -> mainController.exit());
		}
	}

	public void requestGetRelationship() {
		String requestGetRelationship = FlagConnection.GET_RELATIONSHIP + "|";
		sendMessage(requestGetRelationship);
	}

	public void requestLoginToServer(String username, String password) {
		String requestLogin = FlagConnection.LOGIN + "|" + username + "|" + password;
		sendMessage(requestLogin);
	}

	public void setTextAlertErrorLoginFom(String msg) {
		Platform.runLater(() -> loginRegisterController.showAlertErrorLoginForm(msg));
	}

	public void setTextAlertErrorRegister(String msg) {
		Platform.runLater(() -> loginRegisterController.showAlertErrorRegisterForm(msg));
	}

	public void close() {
		this.running = false;
		try {
			dataIn.close();
			dataOut.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void sendMessage(String msg) {
		try {
			dataOut = new DataOutputStream(socket.getOutputStream());
			dataOut.writeUTF(msg);
			dataOut.flush();
		} catch (IOException e) {
			try {
				dataIn.close();
				dataOut.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}

