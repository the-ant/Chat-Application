package application;

import java.io.IOException;
import java.net.Socket;

public class Client {
	private ClientConnection clientConnection;
	private Socket socket = ConfigureClientSocket.getInstance().getSocket();
	private boolean connected = false;
	public Client(Object controller) {
		initClient(controller);
	}
	private void initClient(Object controller) {
		if (socket != null && socket.isConnected()) {
			clientConnection = new ClientConnection(controller);
		}
		connected = true;
	}
	public void startClient() {
		if (socket != null && socket.isConnected()) {
			clientConnection.start(); // run thread.
		}
	}
	public void closeClient() {
		if (socket != null && socket.isConnected()) {
			send("#quit");
			clientConnection.close();
			clientConnection.interrupt();
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
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
}
