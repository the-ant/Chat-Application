package utils;

import javafx.scene.control.ListCell;
import pojo.User;

public class CustomAllUserListCell extends ListCell<User> {

	@Override
	protected void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			setText(item.getFullname());
		}
	}
}
