package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {
	private Socket socket = ConfigureClientSocket.getInstance().getSocket();
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private boolean running = true;
	private LoginRegisterController loginRegisterController;
	private MainController mainController;

	public ClientConnection(Object controller) {
		if (controller instanceof LoginRegisterController) {
			this.loginRegisterController = (LoginRegisterController) controller;
		} else if (controller instanceof MainController) {
			this.mainController = (MainController) controller;
		}
		try {
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public void setRunning(boolean running) {
		this.running = running;
	}

	public void sendMessage(String msg) {
		try {
			dataOut.writeUTF(msg);
			dataOut.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			try {
				dataIn.close();
				dataOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("null")
	@Override
	public void run() {
		try {
			while (running) {
//				while (dataIn.available() == 0) {
//				}
				String msg = dataIn.readUTF();
				if (msg != null || !msg.isEmpty()) {
					if (msg.contains("alertNotExistUsername-")) {
						String msgError = msg.substring(msg.indexOf("-") + 1);
						if (!msgError.isEmpty() || msg != null) {
							//loginRegisterController.showAlertError(msgError);
						}
					}
					if (msg.contains("lgResult-true")) {
						String user_id = msg.substring(0, msg.indexOf("-"));
						String username = msg.substring(msg.indexOf("-") + 1, msg.indexOf(":"));
						if (loginRegisterController.isLogged(true, username, Integer.parseInt(user_id))) {
							this.mainController = new MainController();
							continue;
						} else {
							// error transfer maincontroller.
						}
						continue;
					}
					if (msg.equalsIgnoreCase("lgResult-false")) {
						if (loginRegisterController.isLogged(false, null, 0)) {
							loginRegisterController.showAlertError("Dang nhap khong thanh cong");
						}
					}

				} else {
					// msg is empty.
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		sendMessage("#quit");
		this.running = false;
	}

	public boolean getRunning() {
		return this.running;
	}
}
