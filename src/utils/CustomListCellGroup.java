package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import pojo.Group;

public class CustomListCellGroup extends ListCell<Group> {
	@Override
	protected void updateItem(Group item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			HBox root = new HBox();
			root.setAlignment(Pos.CENTER_LEFT);
			root.setSpacing(10);
			root.setPadding(new Insets(0, 0, 0, 5));

			Circle circle = new Circle(18, Paint.valueOf("#d87b7b"));
			Text name = new Text("" + item.getName().charAt(0));
			name.setFont(new Font("Sysmtem", 15));
			name.setFill(Color.WHITE);
			name.setBoundsType(TextBoundsType.VISUAL);
//			Circle circleView;
//			if (true) { // isStatus? 
//				circleView = new Circle(4, Paint.valueOf("#58FA82"));
//			} else{
//				circleView = new Circle(4, Paint.valueOf("#E6E6E6"));
//			}
			Region regionPaddingRightName = new Region();
			HBox.setHgrow(regionPaddingRightName, Priority.ALWAYS);
			
			StackPane stack = new StackPane();
			stack.getChildren().addAll(circle, name);

			VBox title = new VBox();
			title.setAlignment(Pos.CENTER_LEFT);
			title.setSpacing(2);
			
			Text nameGroup = new Text(item.getName());
			nameGroup.setFont(new Font("System", 18));
			Text content = new Text(item.getName());
			content.setFill(Paint.valueOf("#9d9d9d"));
			content.setFont(new Font("System", 12));
			
			title.getChildren().addAll(nameGroup, content);
			root.getChildren().addAll(stack, title, regionPaddingRightName);
			setGraphic(root);
		}
	}
}
