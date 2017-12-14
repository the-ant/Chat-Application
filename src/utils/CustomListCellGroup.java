package utils;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
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
	private List<User> users;
	private int currentUserId;
	public CustomListCellGroup(List<User> users, int currentUserId) {
		this.users = users;
		this.currentUserId = currentUserId;
	}
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
			String name = "";

			System.out.println("check: " + item.isChatGroup());
			if (item.isChatGroup()) {
				name = item.getName();
			} else {
				name = checkExistListUsers(item);
			}
			System.out.println("name ==> " + name);
			Text nameCircle = new Text("" + name.charAt(0));
			nameCircle.setFont(new Font("Sysmtem", 15));
			nameCircle.setFill(Color.WHITE);
			nameCircle.setBoundsType(TextBoundsType.VISUAL);

			StackPane stack = new StackPane();
			stack.getChildren().addAll(circle, nameCircle);

			VBox title = new VBox();
			title.setAlignment(Pos.CENTER_LEFT);
			title.setSpacing(2);
			Text nameGroup = new Text(name);
			nameGroup.setFont(new Font("System", 18));
			Text content = new Text(name);
			content.setFill(Paint.valueOf("#9d9d9d"));
			content.setFont(new Font("System", 12));
//			// name of user.
			
			title.getChildren().addAll(nameGroup, content);
			root.getChildren().addAll(stack, title);
			setGraphic(root);
		}
	}
	private String checkExistListUsers(Group item) {
		for (Integer userId : item.getListUserID()) {
			if (!userId.equals(this.currentUserId)) {
				if (!this.users.isEmpty()) {
					for (User user: this.users) {
						if (user.getId() == userId) {
							return user.getFullname();							
						}
					}
				}
			}
		}
		return null;
	}
	public String getFullNameByUserId(int userId) {
		
		return null;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public int getCurrentUserId() {
		return currentUserId;
	}
	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}
	
}
