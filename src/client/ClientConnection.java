package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import application.MainController;
import pojo.FlagConnection;

public class ClientConnection extends Thread {

	private Socket socket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private boolean running = true;
	private MainController controller;

	public ClientConnection(MainController controller, Socket socket) {
		this.socket = socket;
		this.controller = controller;
		try {
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void sendMessage(String msg) {
		try {
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

	@Override
	public void run() {
		try {
			while (running) {
				while (dataIn.available() == 0) {
				}

				String msg = dataIn.readUTF();
				System.out.println("--> Receive from server: " + msg);
				String[] frameMsg = msg.split("[|]");
				decodeFrame(frameMsg);
			}

			close();
		} catch (IOException e) {
			System.out.println(e.toString());
			close();
		}
	}

	private void decodeFrame(String[] frameMsg) {
		int flag = Integer.parseInt(frameMsg[0]);
		System.out.println("==> Flag: " + flag);

		switch (flag) {
		case FlagConnection.LOGIN:
			receiveResponseLogin(Integer.parseInt(frameMsg[1]));
			break;
			
		case FlagConnection.LOGOUT:
			receiveResponseLogout(Integer.parseInt(frameMsg[1]));
			break;
			
		case FlagConnection.REGISTER:
			receiveResponseRegister(Integer.parseInt(frameMsg[1]));
			break;
			
		case FlagConnection.SEND_MESSAGE:
			receiveResponseSendMessage(Integer.parseInt(frameMsg[1]));
			break;
			
		case FlagConnection.GET_RELATIONSHIP:
			receiveResponseGetRelationship(frameMsg[1]);
			break;
			
		case FlagConnection.GET_MESSAGE:
			receiveResponseGetMessage(frameMsg[1]);
			break;
			
		case FlagConnection.GET_GROUP:
			receiveResponseGetGroup(frameMsg[1]);
			break;
			
		case FlagConnection.GET_USER:
			receiveResponseGetUser(frameMsg[1]);
			break;
			
		case FlagConnection.ADD_FRIEND:
			receiveResponseAddFriend(Integer.parseInt(frameMsg[1]));
			break;
			
		case FlagConnection.ADD_GROUP:
			receiveResponseAddGroup(Integer.parseInt(frameMsg[1]));
			break;
		}
	}

	private void receiveResponseAddGroup(int response) {

	}

	private void receiveResponseAddFriend(int response) {

	}

	private void receiveResponseGetUser(String response) {

	}

	private void receiveResponseGetGroup(String response) {

	}

	private void receiveResponseGetMessage(String response) {

	}

	private void receiveResponseGetRelationship(String response) {
		System.out.println("Relationship: " + response);
	}

	private void receiveResponseSendMessage(int response) {

	}

	private void receiveResponseRegister(int response) {

	}

	private void receiveResponseLogout(int response) {

	}

	private void receiveResponseLogin(int response) {
		System.out.println("--> Response login from server: " + response);
		if (response == 1) {
			System.out.println("--> Da login thanh cong.");
			
			String requestGetRelationship = FlagConnection.GET_RELATIONSHIP + "|";
			sendMessage(requestGetRelationship);
		} else {
			System.out.println("--> Login khong thanh cong.");
		}
	}

	public void requestLoginToServer(String username, String password) {
		String requestLogin = FlagConnection.LOGIN + "|" + username + "|" + password;
		sendMessage(requestLogin);
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
}
