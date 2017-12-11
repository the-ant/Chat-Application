package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.event.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author sonpham
 *
 */
public class LoginRegisterController implements Initializable {
	@FXML
	private Button loginBtn, registerBtn, selectLoginBtn;
	@FXML
	private Label lbWarningLogin, lbAlertError;
	@FXML
	private Hyperlink selectRegisterLink;
	@FXML
	private StackPane loginStackPane;
	@FXML
	private TextField lgUsername, lgPassword, rgUsername, rgPassword, rgConfirmPassword;

	private Client client;
	private String username, password, confirmPassword;
	private String msgAlertError;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	private void init() {
		ConfigureClientSocket.getInstance().createSocket();
		LoginRegisterController controller = new LoginRegisterController();
		client = new Client(controller);
		client.startClient(); // run thread ClientConnection.
		handleEventFromLoginRegisterForm();
	}

	private void handleEventFromLoginRegisterForm() {

		lgUsername.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					msgAlertError = "";
					showAlertError(msgAlertError);
					username = lgUsername.getText();
					if (!isValidInformation(username)) {
						msgAlertError = "Username is empty.";
						showAlertError(msgAlertError);
						// System.out.println(msgAlertError);
					} else if (!username.matches("[A-Za-z0-9_]+")) {
						msgAlertError = "Username is invalid.";
						showAlertError(msgAlertError);
					} else {
						client.send("lgUsername-" + username);
					}
				}
			}
		});
		lgPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					username = lgUsername.getText();
					if (isValidInformation(username)) {
						password = lgPassword.getText();
						if (isValidInformation(password)) {
							client.send("lgRequest-" + username + ":" + password);
						} else {
							showAlertError("Login unsuccessful");
						}
					} else {
						showAlertError("Login unsuccessfult.");
					}
				}
			}
		});
		rgUsername.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					msgAlertError = "";
					showAlertError(msgAlertError);
					username = rgUsername.getText();
					if (!isValidInformation(username)) {
						msgAlertError = "Username is empty.";
						showAlertError(msgAlertError);
						// System.out.println(msgAlertError);
					} else if (!username.matches("[A-Za-z0-9_]+")) {
						msgAlertError = "Username is invalid.";
						showAlertError(msgAlertError);
					} else {
						client.send("rgUsername-" + username);
					}
				}
			}
		});
		rgPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.TAB) {
					password = rgPassword.getText();
					if (!isValidInformation(password)) {
						showAlertError("Password is empty.");
					}
				}
			}
		});
		rgConfirmPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				username = rgUsername.getText();
				if (isValidInformation(username)) {
					password = rgPassword.getText();
					if (isValidInformation(password)) {
						confirmPassword = rgConfirmPassword.getText();
						if (confirmPassword.equals(password)) {
							client.send("lgRequest-" + username + ":" + password);
						} else {
							showAlertError("Register unsuccessful");
						}
					}
				} else {
					showAlertError("lgError- Register unsuccessful.");
				}
			}
		});

	}

	private boolean isValidInformation(String info) {
		if (info == null || info.isEmpty()) {
			return false;
		}
		return true;
	}

	public void handleCreateAccount(ActionEvent event) {
		changeTop();
	}

	public void handleBackLoginScreen(ActionEvent event) {
		changeTop();
	}

	public void handleButtonLogin(ActionEvent event) {
		msgAlertError = null;
		username = lgUsername.getText();
		if (!isValidInformation(username)) {
			msgAlertError = "Username is empty";
			showAlertError(msgAlertError);
			System.out.println(msgAlertError);
		} else if (!username.matches("[A-Za-z0-9_]+")) {
			msgAlertError = "Username is invalid";
			showAlertError(msgAlertError);
		} else {
			client.send("lgUsername-" + username);
			password = lgPassword.getText();
			if (!isValidInformation(password)) {
				msgAlertError = "Password is empty";
				showAlertError(msgAlertError);
			} else {
				client.send("lgPassword-" + password);
			}
		}

	}

	public boolean isLogged(boolean resultLoggin, String username, int user_id) {
		if (resultLoggin) {
			if (username != null) {
				CurrentUser.getInstance().setUsername(username);
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
						Scene scene = new Scene(root);
						Main.getPrimaryStage().setScene(scene);
						Main.getPrimaryStage().show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			return true;
		}
		return false;
	}

	public void showAlertError(String msg) {
		if (msg != null) {
			lbAlertError.setText(msg);
		}
	}

	private void changeTop() {
		ObservableList<Node> childs = this.loginStackPane.getChildren();
		if (childs.size() > 1) {
			Node topNode = childs.get(childs.size() - 1);
			Node newTopNode = childs.get(childs.size() - 2);
			topNode.setVisible(false);
			topNode.toBack();
			newTopNode.setVisible(true);
		}
	}

}
