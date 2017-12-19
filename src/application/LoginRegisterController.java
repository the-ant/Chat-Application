package application;

import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import pojo.CurrentUser;

public class LoginRegisterController implements Initializable {
	@FXML
	private Button loginBtn, registerBtn, selectLoginBtn;
	@FXML
	private Label lbWarningLogin, lbAlertErrorRegisterForm, lbAlertLoginForm;
	@FXML
	private Hyperlink selectRegisterLink;
	@FXML
	private StackPane loginStackPane;
	@FXML
	private TextField lgUsername, lgPassword, rgUsername, rgPassword, rgConfirmPassword, rgFullName;
	private Client client = Client.getInstance();
	private String username, password, confirmPassword;
	private String msgAlertError;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	private void init() {
		client.init(this);
		handleEventFromLoginRegisterForm();
	}

	private void handleEventFromLoginRegisterForm() {

		lgUsername.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.TAB) {

				msgAlertError = "";
				showAlertErrorLoginForm(msgAlertError);
				username = lgUsername.getText();

				if (!isValidInformation(username)) {
					msgAlertError = "Username is empty.";
					showAlertErrorLoginForm(msgAlertError);

				} else if (!username.matches("[A-Za-z0-9_]+")) {
					msgAlertError = "Username is invalid.";
					showAlertErrorLoginForm(msgAlertError);

				} else {
					client.send("lgUsername-" + username);
				}
			}
		});

		lgPassword.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				msgAlertError = "";
				username = lgUsername.getText();

				if (isValidInformation(username)) {
					password = lgPassword.getText();
					if (isValidInformation(password)) {
						client.send("lgRequest:" + username + "-" + password);
					} else {
						showAlertErrorLoginForm("Login unsuccessful");
					}

				} else {
					showAlertErrorLoginForm("Login unsuccessfult.");
				}
			}
		});

		rgUsername.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.TAB) {
				msgAlertError = "";
				showAlertErrorRegisterForm(msgAlertError);
				username = rgUsername.getText();

				if (!isValidInformation(username)) {
					msgAlertError = "Username is empty.";
					showAlertErrorRegisterForm(msgAlertError);

				} else if (!username.matches("[A-Za-z0-9_]+")) {
					msgAlertError = "Username is invalid.";
					showAlertErrorRegisterForm(msgAlertError);

				} else {
					client.send("rgUsername-" + username);
				}
			}
		});

		rgPassword.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.TAB) {
				msgAlertError = "";
				showAlertErrorRegisterForm(msgAlertError);
				password = rgPassword.getText();

				if (!isValidInformation(password)) {
					showAlertErrorRegisterForm("Password is empty.");
				}
			}
		});

		rgConfirmPassword.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.TAB) {
				msgAlertError = "";
				showAlertErrorRegisterForm(msgAlertError);

				if (!isValidInformation(password))
					password = rgPassword.getText();

				confirmPassword = rgConfirmPassword.getText();

				if (!isValidInformation(confirmPassword)) {
					showAlertErrorRegisterForm("Password is empty.");
				} else if (!confirmPassword.equals(password)) {
					showAlertErrorRegisterForm("These passwords don't match. Try again?");
				}
			}
		});

	}

	public void showAlertErrorLoginForm(String msg) {
		if (msg != null) {
			lbAlertLoginForm.setText(msg);
		}
	}

	public void showAlertErrorRegisterForm(String msg) {
		if (msg != null) {
			lbAlertErrorRegisterForm.setText(msg);
		}
	}

	private boolean isValidInformation(String info) {
		if (info == null || info.isEmpty()) {
			return false;
		}
		return true;
	}

	public void handleRegisterAccount(ActionEvent event) {
		msgAlertError = "";
		showAlertErrorLoginForm(msgAlertError);
		username = rgUsername.getText();

		if (!isValidInformation(username)) {
			msgAlertError = "Username is empty.";
			showAlertErrorRegisterForm(msgAlertError);
			System.out.println(msgAlertError);

		} else if (!username.matches("[A-Za-z0-9_]+")) {
			msgAlertError = "Username is invalid.";
			showAlertErrorRegisterForm(msgAlertError);

		} else {
			client.send("lgUsername-" + username);
			password = rgPassword.getText();

			if (!isValidInformation(password)) {
				msgAlertError = "Password is empty.";
				showAlertErrorRegisterForm(msgAlertError);

			} else {
				confirmPassword = rgConfirmPassword.getText();
				if (!isValidInformation(confirmPassword)) {
					msgAlertError = "Confirm password is empty.";
					showAlertErrorRegisterForm(msgAlertError);

				} else {
					if (!confirmPassword.equals(password)) {
						msgAlertError = "These passwords don't match. Try again?";
						showAlertErrorRegisterForm(msgAlertError);

					} else {
						if (!isValidInformation(rgFullName.getText())) {
							msgAlertError = "Full name is empty.";
							showAlertErrorRegisterForm(msgAlertError);

						} else {
							client.send("register:" + username + "-" + password + "-" + rgFullName.getText());
						}
					}
				}
			}
		}
	}

	public void handleCreateAccount(ActionEvent event) {
		changeTop();
	}

	public void handleBackLoginScreen(ActionEvent event) {
		changeTop();
	}

	public void handleButtonLogin(ActionEvent event) {
		msgAlertError = "";
		showAlertErrorLoginForm(msgAlertError);

		username = lgUsername.getText();
		if (!isValidInformation(username)) {
			msgAlertError = "Username is empty";
			showAlertErrorLoginForm(msgAlertError);
			System.out.println(msgAlertError);

		} else if (!username.matches("[A-Za-z0-9_]+")) {
			msgAlertError = "Username is invalid";
			showAlertErrorLoginForm(msgAlertError);

		} else {
			client.send("lgUsername-" + username);
			password = lgPassword.getText();

			if (!isValidInformation(password)) {
				msgAlertError = "Password is empty";
				showAlertErrorLoginForm(msgAlertError);

			} else {
				client.send("lgPassword-" + password);
			}
		}
	}

	public void getMyInfo(boolean resultLoggin, String fullName, int user_id, String relationship) {
		if (resultLoggin) {
			CurrentUser.getInstance().setFullName(fullName);
			CurrentUser.getInstance().setUser_id(user_id);
			CurrentUser.getInstance().setRelationship(relationship);
			Platform.runLater(() -> startMain());
		}
	}

	private void startMain() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
			Scene scene = new Scene(root);
			Main.getPrimaryStage().setScene(scene);
			Main.getPrimaryStage().show();
		} catch (Exception e) {
			e.printStackTrace();
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
