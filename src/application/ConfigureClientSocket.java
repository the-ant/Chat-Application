package application;

import java.io.IOException;
import java.net.Socket;

public class ConfigureClientSocket {
	private Socket socket;
	private ConfigureClientSocket() {}
	private static final class SingletonHelper {
		private static final ConfigureClientSocket INSTANCE = new ConfigureClientSocket();
	}
	public static ConfigureClientSocket getInstance() {
		return SingletonHelper.INSTANCE;
	}
	public void createSocket() {
		try {
			socket = new Socket(Constants.IP_ADDRESS, Constants.PORT);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	public Socket getSocket() {
		return socket;
	}
}
