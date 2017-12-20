package utils;

import java.io.IOException;
import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopOverAddMenu {
	public PopOverAddMenu() {
	}

	public void showPopOverAddFriend(ImageView imgBtn) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddFriend.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Add Friend");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(Main.getPrimaryStage());
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void showPopOverNewGroup(ImageView imgBtn) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/NewGroup.fxml"));
			Stage stage = new Stage();
			stage.setTitle("New Group");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(Main.getPrimaryStage());
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}