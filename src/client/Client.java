package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import application.AddFriendController;
import application.LoginRegisterController;
import interfaces.ReceiveResponseListener;

public class Client {

	private static class ClientHelper {
		private static final Client INSTANCE = new Client();
	}

	public static Client getInstance() {
		return ClientHelper.INSTANCE;
	}

	private static final String IP_ADDRESS = "127.0.0.1";
	private static final int PORT = 5151;

	private ClientConnection clientConnection;
	private Socket socket;
	private boolean connected = false;
	private LoginRegisterController controller;

	private Client() {
	}

	public void init(LoginRegisterController controler) {
		try {
			this.controller = controler;
			socket = new Socket(IP_ADDRESS, PORT);
			if (socket != null && socket.isConnected()) {
				clientConnection = new ClientConnection(controller, socket);
				startClient();
				connected = true;
			}
		} catch (IOException e) {
			System.out.println("--> Khong the ket noi den server!");
		}
	}

	public void login(String username, String password, ReceiveResponseListener listener) {
		clientConnection.requestLoginToServer(username, password);
		listener.onLoginReceive();
	}

	public ClientConnection getClientConnection() {
		return clientConnection;
	}

	public void getRelationship(ReceiveResponseListener listener) {

	}

	public void startClient() {
		if (socket != null && socket.isConnected()) {
			clientConnection.start();
		}
	}

	public void closeClient() {
		if (socket != null && socket.isConnected()) {
			clientConnection.close();
			clientConnection.interrupt();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(String msg) {
		if (socket != null && socket.isConnected()) {
			System.out.println("send");
			clientConnection.sendMessage(msg);
		} else {
			System.out.println("ko send");
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void sendFile(String requestSendFile, int groupId, File file) {
		try {
			Socket sendSocket = new Socket(IP_ADDRESS, PORT);
			Thread sendFileThread = new Thread(new SendingFileRunnable(requestSendFile, sendSocket, file, groupId));
			sendFileThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFile(String requestDownLoadFile, File file, String imgName) {
		try {
			Socket dlSocket = new Socket(IP_ADDRESS, PORT);
			Thread dlFileThread = new Thread(new DownloadFileRunnable(requestDownLoadFile, dlSocket, file, imgName));
			dlFileThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setAddFriendController(AddFriendController addFriendController) {
		this.getClientConnection().setAddFriendController(addFriendController);
	}

}
