package application;

import java.net.URL;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class MainController implements Initializable {
	@FXML
	private MenuItem menuItemAddFriend, menuItemAddGroup;
	@FXML
	private ContextMenu contextAddMenu;
	@FXML
	private Label createNew;
	@FXML
	private FontAwesomeIconView attachedFile, sendBtn, imageFile;
	@FXML
	private ListView<Data> listFriend;
	@FXML
	private Circle friendImage, yourImage;
	@FXML
	private Label firstTextYourName, firstTextFriendName;
	private final ObservableList<Data> data = FXCollections.observableArrayList();
	private final ObservableList<Message> message = FXCollections.observableArrayList();

	private Circle circleView;

	@FXML
	private ListView<Message> lvMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initListFriend();
		initMessage();
		initMenu();
	}

	private void initListFriend() {
		yourImage.setFill(Paint.valueOf("#CFD8DC"));
		yourImage.setStroke(Paint.valueOf("#CFD8DC"));
		firstTextYourName.setText("M");

		friendImage.setFill(Paint.valueOf("#CDDC39"));
		friendImage.setStroke(Paint.valueOf("#CDDC39"));
		firstTextFriendName.setText("K");

		data.clear();
		data.add(new Data("Inu Shiba", "images", true));
		data.add(new Data("SesshoumaruSama", "sama", false));
		data.add(new Data("DehyDration", "dogmini", false));
		data.add(new Data("HangNguyen", "dogmini", false));

		listFriend.setCellFactory(new Callback<ListView<Data>, ListCell<Data>>() {

			@Override
			public ListCell<Data> call(ListView<Data> arg0) {
				// TODO Auto-generated method stub
				ListCell<Data> cell = new ListCell<Data>() {
					@Override
					protected void updateItem(Data dataItem, boolean btl) {
						super.updateItem(dataItem, btl);
						if (dataItem != null) {
							HBox hBox = new HBox();
							Text name = new Text(dataItem.getUsername());
							name.setFill(Paint.valueOf("#FFFFFF"));
							name.setFont(Font.font("System", 15));
							if (dataItem.isStatus_user() == true) {
								circleView = new Circle(4, Paint.valueOf("#9FF781"));
							} else {
								circleView = new Circle(4, Paint.valueOf("#E6E6E6"));
							}
							Image image = new Image(getClass().getClassLoader()
									.getResource("images/" + dataItem.getImagefile().toLowerCase() + ".jpg").toString(),
									40, 40, true, true);

							Circle cirPictureImageView = new Circle(30, 30, 20);
							cirPictureImageView.setFill(new ImagePattern(image));

							Region regionPaddingRightName = new Region();
							HBox.setHgrow(regionPaddingRightName, Priority.ALWAYS);

							hBox.getChildren().addAll(cirPictureImageView, name, regionPaddingRightName, circleView);
							hBox.setAlignment(Pos.CENTER);
							hBox.setSpacing(15.0); // In your case

							setGraphic(hBox);

						}
					}
				};
				return cell;
			}
		});

		listFriend.setItems(data);
	}

	private void initMessage() {
		message.clear();
		message.add(new Message(1, "hang", "images", "Alo moi nguoi", true));
		message.add(new Message(2, "kien", "images", "Hi Hang", false));
		message.add(new Message(1, "hang", "images", "Alo moi nguoi", true));
		message.add(new Message(2, "kien", "images", "cả bầu Trời nắng", false));
		message.add(new Message(2, "kien", "images",
				"Mưa trôi cả bầu Trời nắng, trượt theo những nỗi buồn Thấm ướt lệ sầu môi đắng vì đánh mất hy vọng Lần đầu gặp nhau dưới mưa, trái tim rộn ràng bởi ánh nhìn ",
				false));
		message.add(new Message(1, "hang", "images",
				"Alo moi nguoi Alo moi nguoi Alo moi nguoi Alo moi nguoi Alo moi nguoi Alo moi nguoi Alo moi nguoi",
				true));
		message.add(new Message(2, "kien", "images", "cả bầu Trời nắng", false));

		lvMessage.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {

			@Override
			public ListCell<Message> call(ListView<Message> arg0) {
				// TODO Auto-generated method stub
				ListCell<Message> cell = new ListCell<Message>() {
					@Override
					protected void updateItem(Message messageItem, boolean btl) {
						super.updateItem(messageItem, btl);
						if (messageItem != null) {
							HBox hBox = new HBox();
							Image image = new Image(getClass().getClassLoader()
									.getResource("images/" + messageItem.getImagePath().toLowerCase() + ".jpg")
									.toString(), 40, 40, true, true);

							Circle cirPictureImageView = new Circle(30, 30, 20);
							cirPictureImageView.setFill(new ImagePattern(image));
							Text message = new Text(messageItem.getMessage());
							message.setFont(Font.font("System", 15));

							if (messageItem.isMe()) {

								if (message.getText().length() < 35) {
									message.setTextAlignment(TextAlignment.RIGHT);
								} else {
									message.setWrappingWidth(250);
								}

								StackPane shapeMessage = new StackPane(message);
								shapeMessage.setStyle(
										"-fx-background-color: #A9F5D0; -fx-background-radius: 20; -fx-padding: 10.0;");

								hBox.getChildren().addAll(shapeMessage);
								hBox.setAlignment(Pos.CENTER_RIGHT);
							} else {
								if (message.getText().length() >= 35) {
									message.setWrappingWidth(250);
								}
								StackPane shapeMessage = new StackPane(message);
								shapeMessage.setStyle(
										"-fx-background-color: #F2F2F2; -fx-background-radius: 20; -fx-padding: 10.0;");

								hBox.getChildren().addAll(cirPictureImageView, shapeMessage);
								hBox.setSpacing(15.0); // In your case
								hBox.setAlignment(Pos.CENTER_LEFT);
							}

							setGraphic(hBox);

						}
					}
				};
				return cell;
			}
		});

		lvMessage.setItems(message);
	}

	private void initMenu() {
		createNew.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				contextAddMenu.show(createNew, event.getScreenX(), event.getScreenY());
			}
		});
	}

	@FXML
	private AnchorPane pn_sign;
	@FXML
	private AnchorPane loginscene, registerscene;

	@FXML
	private Button loginBtn, registerBtn, selectLoginBtn;

	@FXML
	private Hyperlink selectRegisterLink;

	@FXML
	private void handleButtonAction(ActionEvent event) {

		if (event.getSource() == selectLoginBtn) {
			loginscene.toFront();
		} else {
			if (event.getSource() == selectRegisterLink) {
				registerscene.toFront();
			}
		}
	}
}
