package utils;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import pojo.Message;

public class CustomListCellMessage extends ListCell<Message> {

	@Override
	protected void updateItem(Message item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			HBox root = new HBox();
			root.setAlignment(Pos.BOTTOM_LEFT);

			Circle circle = new Circle(10, Paint.valueOf("#d87b7b"));
			Text name = new Text("HL");
			name.setFont(new Font("Sysmtem", 10));
			name.setFill(Color.WHITE);
			name.setBoundsType(TextBoundsType.VISUAL);
			
			StackPane stack = new StackPane();
			stack.getChildren().addAll(circle, name);
			HBox avatar = new HBox();
			avatar.setAlignment(Pos.BOTTOM_LEFT);
			avatar.getChildren().add(stack);
			

			Text message = new Text(item.getMessage());
			message.setFont(Font.font("System", 15));
			if (item.isMe()) {

				if (message.getText().length() < 35) {
					message.setTextAlignment(TextAlignment.RIGHT);
				} else {
					message.setWrappingWidth(350);
				}
				message.setFill(Paint.valueOf("#ffffff"));

				StackPane shapeMessage = new StackPane(message);
				shapeMessage.setStyle("-fx-background-color: #1c1c49; -fx-background-radius: 20; -fx-padding: 10.0;");

				root.getChildren().addAll(shapeMessage);
				root.setAlignment(Pos.CENTER_RIGHT);
			} else {
				if (message.getText().length() >= 35) {
					message.setWrappingWidth(350);
				}
				StackPane shapeMessage = new StackPane(message);
				shapeMessage.setStyle("-fx-background-color: #ededed; -fx-background-radius: 20; -fx-padding: 10.0;");

				root.getChildren().addAll(avatar, shapeMessage);
				root.setSpacing(5);
			}
			setGraphic(root);
		}
	}
}
