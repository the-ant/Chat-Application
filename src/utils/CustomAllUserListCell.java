package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import pojo.User;

public class CustomAllUserListCell extends ListCell<User> {

	@Override
	protected void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			
			HBox root = new HBox();
			root.setAlignment(Pos.CENTER_LEFT);
			root.setSpacing(10);
			root.setPadding(new Insets(5, 5, 5, 5));
			
			Circle circleAvatar = new Circle(12, Paint.valueOf("#d87b7b"));
			Text textCircle = new Text(item.getFullname().charAt(0) + "");
			textCircle.setFont(new Font("Sysmtem", 10));
			textCircle.setFill(Color.WHITE);
			textCircle.setBoundsType(TextBoundsType.VISUAL);

			StackPane stack = new StackPane();
			stack.getChildren().addAll(circleAvatar, textCircle);

			Text nameGroup = new Text(item.getFullname());
			nameGroup.setFont(new Font("System", 18));

			root.getChildren().addAll(stack, nameGroup);
			setGraphic(root);
		}
	}
}
