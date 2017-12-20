package utils;


import client.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import pojo.FlagConnection;
import pojo.User;

public class CustomListCellConfirmRequest extends ListCell<User> {
	private Client client = Client.getInstance();
	private Button btnConfirm;
	@Override
	protected void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			String name = item.getFullname();

			HBox root = new HBox();
			root.setAlignment(Pos.CENTER_LEFT);
			root.setSpacing(10);
			root.setPadding(new Insets(0, 0, 0, 5));

			Circle circleAvatar = new Circle(18, Paint.valueOf("#d87b7b"));
			Text textCircle = new Text((!name.equals("") ? name.charAt(0) : "") + "");
			textCircle.setFont(new Font("Sysmtem", 15));
			textCircle.setFill(Color.WHITE);
			textCircle.setBoundsType(TextBoundsType.VISUAL);

			StackPane stack = new StackPane();
			stack.getChildren().addAll(circleAvatar, textCircle);

			VBox title = new VBox();
			title.setAlignment(Pos.CENTER_LEFT);
			title.setSpacing(2);
			Text nameGroup = new Text(name);
			nameGroup.setFont(new Font("System", 18));
			Text content = new Text(name);
			content.setFill(Paint.valueOf("#9d9d9d"));
			content.setFont(new Font("System", 12));
			
			btnConfirm = new Button();
			btnConfirm.setText("Confirm");
			btnConfirm.setOnAction(e->{
				handleConfirmRequest(item.getId());
			});
			Region regionPaddingRightName = new Region();
			HBox.setHgrow(regionPaddingRightName, Priority.ALWAYS);

			title.getChildren().addAll(nameGroup, content);
			root.getChildren().addAll(stack, title, regionPaddingRightName,btnConfirm);
			setGraphic(root);
		}
	}
	private void handleConfirmRequest(int userIdRequested) {
		client.send(FlagConnection.DELETE_REQUEST_RECORD + "|" + userIdRequested);
	}
}
