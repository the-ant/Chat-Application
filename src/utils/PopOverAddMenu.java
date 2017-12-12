package utils;

import java.io.IOException;

import org.controlsfx.control.CheckListView;
import org.controlsfx.control.PopOver;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PopOverAddMenu  {
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
		 // TODO Auto-generated catch block
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
		 // TODO Auto-generated catch block
		 e1.printStackTrace();
		 }
	}
}
