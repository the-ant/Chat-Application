package utils;

import java.util.List;

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
import pojo.User;

public class CustomListCellGroup extends ListCell<Group> {

	private List<User> listFriends;
	private int myId;

	public CustomListCellGroup(List<User> listFriends, int myId) {
		this.listFriends = listFriends;
		this.myId = myId;
	}

	@Override
	protected void updateItem(Group item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			String name = getNameGroupOrFriend(item);

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
			
			Circle circleView;
			if (getStatusGroupOrFriend(item)) { // isStatus? 
				circleView = new Circle(4, Paint.valueOf("#58FA82"));
			} else{
				circleView = new Circle(4, Paint.valueOf("#E6E6E6"));
			}
			Region regionPaddingRightName = new Region();
			HBox.setHgrow(regionPaddingRightName, Priority.ALWAYS);

			title.getChildren().addAll(nameGroup, content);
			root.getChildren().addAll(stack, title, regionPaddingRightName, circleView);
			setGraphic(root);
		}
	}

	private String getNameGroupOrFriend(Group item) {
		String name = "";
		if (item.isChatGroup()) {
			name = item.getName();
		} else {
			name = checkExistListUsers(item);
		}
		return name;
	}
	private boolean getStatusGroupOrFriend(Group item) {
		boolean status = false;
		if (item.isChatGroup()) {
			status = true; // kiem tra co user nao online trong group do khong
		} else {
			status = checkExistListUsers2(item);
		}
		return status;
	}

	private String checkExistListUsers(Group item) {
		String name = "";
		if (item.getListUserID().size() > 0)
			for (Integer userId : item.getListUserID()) {
				if (userId != myId) {
					name = getFullNameOfFriend(userId);
					break;
				}
			}
		return name;
	}
	private boolean checkExistListUsers2(Group item) {
		boolean status = false;
		if (item.getListUserID().size() > 0)
			for (Integer userId : item.getListUserID()) {
				if (userId != myId) {
					status = getStatusOfFriend(userId);
					break;
				}
			}
		return status;
	}

	private String getFullNameOfFriend(Integer userId) {
		String result = "";
		for (User user : this.listFriends)
			if (user.getId() == userId) {
				result = user.getFullname();
				break;
			}
		return result;
	}
	
	private boolean getStatusOfFriend(Integer userId) {
		boolean result = false;
		for (User user : this.listFriends)
			if (user.getId() == userId) {
				result = user.isOnline();
				break;
			}
		return result;
	}
}
