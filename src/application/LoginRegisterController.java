package application;

import java.net.URL;
import java.util.ResourceBundle;

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

public class LoginRegisterController implements Initializable {
	@FXML
	private Button loginBtn, registerBtn, selectLoginBtn;
	@FXML
	private Label lbWarningLogin;
	@FXML
	private Hyperlink selectRegisterLink;
	@FXML
	private StackPane loginStackPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

	private void init() {
	}

	public void handleCreateAccount(ActionEvent event) {
		changeTop();
	}
	
	public void handleBackLoginScreen(ActionEvent event) {
		changeTop();
	}

	public void handleButtonLogin(ActionEvent event) {
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

	private void showWarningRegister() {
		lbWarningLogin.setText("sai ");
	}
}
