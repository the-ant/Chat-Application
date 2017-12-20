package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PlaceHolderUtil {
	
	public static final String LIST_GROUP = "Press Add button to add friends";
	public static final String PATH_IMG_GROUP = "/images/place_holder_group.png";
	public static final String PATH_IMG_MESSAGE = "/images/place_holder_message.png";

	public static VBox createPlaceHolderGroup() {
		VBox placeHolder = new VBox();
		placeHolder.setPadding(new Insets(20));
		
		placeHolder.getChildren().add(new ImageView(new Image(PATH_IMG_GROUP)));
		
		Label txtPlaceHolder = new Label(LIST_GROUP);
		txtPlaceHolder.setAlignment(Pos.CENTER);
		txtPlaceHolder.setFont(new Font("System", 18));
		
		placeHolder.getChildren().add(txtPlaceHolder);
		placeHolder.setAlignment(Pos.CENTER);
		return placeHolder;
	}

	public static VBox createPlaceHolderMessage() {
		VBox placeHolder = new VBox();
		placeHolder.setPadding(new Insets(20));
		placeHolder.getChildren().add(new ImageView(new Image(PATH_IMG_MESSAGE)));
		placeHolder.setAlignment(Pos.CENTER);
		return placeHolder;
	}
}
