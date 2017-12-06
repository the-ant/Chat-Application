package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginRegisterController implements Initializable {
	@FXML
	private AnchorPane loginscene, registerscene, loginRegisterScene;

	@FXML
	private Button loginBtn, registerBtn, selectLoginBtn;

	@FXML
	private Hyperlink selectRegisterLink;

	@FXML
	private void handleButtonAction(ActionEvent event) {

		if (event.getSource() == selectLoginBtn) {
			loginscene.toFront();
		} else if (event.getSource() == selectRegisterLink) {
			registerscene.toFront();

		}
	}

	@FXML
	private void handleButtonLogin(ActionEvent event) {
		// registerscene.toFront();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));
			Scene scene = new Scene(root);
			Main.getPrimaryStage().setScene(scene);
			Main.getPrimaryStage().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
