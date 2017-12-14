package application;

import java.io.IOException;
import java.net.Socket;

public class Client {

	private static final class SingletonHelper {
		private static final Client INSTANCE = new Client();
	}

	public static Client getClient() {
		return SingletonHelper.INSTANCE;
	}

	private ClientConnection clientConnection;
	private Socket socket;
	private LoginRegisterController loginController;

	private boolean connected = false;

	public Client(LoginRegisterController loginController) {
		this.loginController = loginController;
		initClient(loginController);
	}

	private Client() {
	}

	public void setLoginController(LoginRegisterController loginController) {
		this.loginController = loginController;
	}

	public void initClient(LoginRegisterController controller) {
		try {
			socket = new Socket(Constants.IP_ADDRESS, Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (socket != null && socket.isConnected()) {
			clientConnection = new ClientConnection(controller,socket);
			setLoginController(controller);
			startClient();
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
