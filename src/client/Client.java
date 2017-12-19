package client;

import java.io.IOException;
import java.net.Socket;

import javax.security.auth.login.LoginContext;

import application.LoginRegisterController;
import application.MainController;
import interfaces.ReceiveResponseListener;
import pojo.Relationship;

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
			clientConnection.sendMessage(msg);
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public Relationship requestGetRelationship(ReceiveResponseListener listener) {
		
		return null;
	}

}
