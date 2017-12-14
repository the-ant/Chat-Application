package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;

public class ClientConnection extends Thread {
	private Socket socket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private boolean running = true;
	private LoginRegisterController loginRegisterController;
	private MainController mainController;

	public ClientConnection(LoginRegisterController controller, Socket socket) {
		this.loginRegisterController = controller;
		this.socket = socket;
		try {
			dataIn = new DataInputStream(this.socket.getInputStream());
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
				while (dataIn.available() == 0) {
				}
				String msg = dataIn.readUTF();
				if (msg != null || !msg.isEmpty()) {
					int options = handleOptions(msg);
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
						case 5:
							showListOfMyFriends(msg);
							break;
						default:
							break;
					}
					continue;
				} else {
					// msg is empty.
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void showListOfMyFriends(String msg) {
		String listOfMyFriends = msg.substring(msg.indexOf(":") + 1);
		System.out.println("list of my friends : "  + listOfMyFriends);
		if (!listOfMyFriends.isEmpty()) {
			System.out.println("main controller : " + mainController.toString());
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					mainController.setDataForListFriends(listOfMyFriends);
				}
			});
		}
	}

	private void alertNotExistUsername(String msg) {
		String msgError = msg.substring(msg.indexOf("-") + 1);
		if (!msgError.isEmpty() || msg != null) {
			setTextAlertErrorLoginFom("That username don't exist. Try again?");
		}
	}
	private void handleLoginSuccessful(String msg) {
		String [] inforUser = msg.substring(msg.indexOf(":") + 1).split("-");
		if (loginRegisterController.isLogged(true, inforUser[1], Integer.parseInt(inforUser[0]), inforUser[2])) {
			this.mainController = new MainController();
			//this.mainController.setDataForListFriends(inforUser[2]);
		} else {
			// error transfer maincontroller.
		}
	}
	private void handleLoginUnsuccessful(String msg) {
		loginRegisterController.isLogged(false, null, 0, null);
		setTextAlertErrorLoginFom("Login unsuccessful. Try again!");
	}
	private void alertExistUsernameRegister(String msg) {
		String msgErrorExistUsername = msg.substring(msg.indexOf("-") + 1);
		if (!msgErrorExistUsername.isEmpty() || msg != null) {
			setTextAlertErrorRegister("That username is taken. Try again!");
			//continue;
		}
	}
	public int handleOptions(String msg) {
		if (msg != null) {
			if (msg.contains("alertNotExistUsername-")) {
				return 1;
			}
			if (msg.contains("lgResult-true")) {
				return 2;
			}
			if (msg.contains("lgResult-false")) {
				return 3;
			}
			if (msg.contains("alertExistUsernameReg-")) {
				return 4;
			}
			if (msg.contains("listOFYourFriends")) {
				return 5;
			}
		}
		return 0;
	}

	public void setTextAlertErrorLoginFom(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loginRegisterController.showAlertErrorLoginForm(msg);
			}
		});

	}

	public void setTextAlertErrorRegister(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loginRegisterController.showAlertErrorRegisterForm(msg);
			}
		});

	}

	public void close() {
		sendMessage("#quit");
		this.running = false;
	}

	public boolean getRunning() {
		return this.running;
	}
}
