package utils;

import application.MainController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import pojo.Message;

public class CustomListCellMessage extends ListCell<Message> {

	private MainController controller;

	public CustomListCellMessage(MainController controller) {
		this.controller = controller;
	}

	@Override
	protected void updateItem(Message item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {

			String nameUserSendMsg = controller.getFullNameOfFriend(item.getUserID());

			HBox root = new HBox();
			root.setAlignment(Pos.BOTTOM_LEFT);
			root.setSpacing(5);

			Circle circle = new Circle(12, Paint.valueOf("#d87b7b"));

			Text name = new Text();
			if (!nameUserSendMsg.equals("")) {
				name.setText(nameUserSendMsg.charAt(0) + "");
			}

			name.setFont(new Font("Sysmtem", 14));
			name.setFill(Color.WHITE);
			name.setBoundsType(TextBoundsType.VISUAL);

			StackPane stack = new StackPane();
			stack.setPrefHeight(24);
			stack.setPrefWidth(24);
			stack.setMaxHeight(USE_PREF_SIZE);
			stack.getChildren().addAll(circle, name);

			HBox avatar = new HBox();
			avatar.setAlignment(Pos.BOTTOM_LEFT);
			avatar.getChildren().add(stack);

			if (item.isFile()) {
				HBox containerFile = new HBox();
				containerFile.setAlignment(Pos.CENTER_LEFT);
				containerFile.setSpacing(3);
				
				ImageView imgFile = new ImageView();
				imgFile.setFitWidth(14);
				imgFile.setFitHeight(14);
				
				Hyperlink linkFile = new Hyperlink();
				linkFile.setStyle("-fx-border-color: transparent");
				linkFile.setText(item.getMessage());
				linkFile.setTextAlignment(TextAlignment.RIGHT);
				
				linkFile.setOnMouseClicked(e -> {
					Platform.runLater(() -> controller.downloadFileFromServer(item.getGroupID(), item.getMessage()));
				});
				
				
				if (item.isMe()) {
					linkFile.setTextFill(Paint.valueOf("#ffffff"));
					imgFile.setImage(new Image("/images/file_me.png"));
					containerFile.getChildren().addAll(imgFile, linkFile);
					
					StackPane shapeMessage = new StackPane(containerFile);
					shapeMessage
							.setStyle("-fx-background-color: #7979f7; -fx-background-radius: 20; -fx-padding: 10.0;");

					root.getChildren().addAll(shapeMessage);
					root.setAlignment(Pos.CENTER_RIGHT);
				} else {
					linkFile.setTextFill(Paint.valueOf("#000000"));
					imgFile.setImage(new Image("/images/file_friend.png"));
					containerFile.getChildren().addAll(imgFile, linkFile);
					
					StackPane shapeMessage = new StackPane();
					shapeMessage.getChildren().add(containerFile);
					shapeMessage
							.setStyle("-fx-background-color: #ededed; -fx-background-radius: 20; -fx-padding: 10.0;");

					if (controller.isChatGroup(item.getGroupID())) {
						VBox containerMsg = new VBox();

						Label nameUserSendMsgLb = new Label(nameUserSendMsg);
						nameUserSendMsgLb.setFont(new Font("System", 12));
						nameUserSendMsgLb.setPadding(new Insets(0, 0, 0, 10));
						nameUserSendMsgLb.setTextFill(Paint.valueOf("#727272"));

						containerMsg.getChildren().addAll(nameUserSendMsgLb, shapeMessage);
						root.getChildren().addAll(avatar, containerMsg);
					} else {
						root.getChildren().addAll(avatar, shapeMessage);
					}
				}
			} else {

				Text message = new Text(item.getMessage());
				message.setFont(Font.font("System", 15));

				if (message.getText().length() < 35) {
					message.setTextAlignment(TextAlignment.RIGHT);
				} else {
					message.setWrappingWidth(350);
				}

				if (item.isMe()) {
					message.setFill(Paint.valueOf("#ffffff"));

					StackPane shapeMessage = new StackPane(message);
					shapeMessage
							.setStyle("-fx-background-color: #7979f7; -fx-background-radius: 20; -fx-padding: 10.0;");

					root.getChildren().addAll(shapeMessage);
					root.setAlignment(Pos.CENTER_RIGHT);
				} else {
					StackPane shapeMessage = new StackPane(message);
					shapeMessage
							.setStyle("-fx-background-color: #ededed; -fx-background-radius: 20; -fx-padding: 10.0;");

					if (controller.isChatGroup(item.getGroupID())) {
						VBox containerMsg = new VBox();

						Label nameUserSendMsgLb = new Label(nameUserSendMsg);
						nameUserSendMsgLb.setFont(new Font("System", 12));
						nameUserSendMsgLb.setPadding(new Insets(0, 0, 0, 10));
						nameUserSendMsgLb.setTextFill(Paint.valueOf("#727272"));

						containerMsg.getChildren().addAll(nameUserSendMsgLb, shapeMessage);
						root.getChildren().addAll(avatar, containerMsg);
					} else {
						root.getChildren().addAll(avatar, shapeMessage);
					}
				}
			}
			setGraphic(root);
		}
	}
}
