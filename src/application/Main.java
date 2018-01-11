package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	private static Stage state;

	public static Stage getPrimaryStage() {
		return Main.state;
	}

	public void setPrimaryStage(Stage stage) {
		Main.state = stage;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage);
			Parent root = FXMLLoader.load(getClass().getResource("/view/LoginRegister.fxml"));
			primaryStage.setTitle("Meow Meow Chat");
			primaryStage.getIcons().add(new Image("/images/ic_app.png"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

			setPrimaryStage(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
