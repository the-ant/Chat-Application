package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pojo.Group;
import pojo.Message;
import traynotification.AnimationType;
import traynotification.NotificationType;
import traynotification.TrayNotification;
import utils.CustomListCellGroup;
import utils.CustomListCellMessage;
import utils.PopOverAddMenu;

public class MainController implements Initializable {
	@FXML
	private ListView<Group> lvGroups;
	@FXML
	private ListView<Message> lvMessage;
	@FXML
	private Circle mAvatarCircle;
	@FXML
	private TextField tfSearch, tfTypeMessage;
	@FXML
	private Text friendChatNameText, friendChatStatusText, statusLb, notificationAddFriendText,
			notificationNewGroupText;
	@FXML
	private ImageView addFriendsBtn;

	private int countCLick = 0;

	private ObservableList<Group> data = FXCollections.observableArrayList();
	private ObservableList<Message> message = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initListFriend();
		initMessage();
		initAddMenu();
		newUserNotification();
	}

	private void initListFriend() {
		data.add(new Group("Inu Shiba"));
		data.add(new Group("SesshoumaruSama"));
		data.add(new Group("DehyDration"));
		data.add(new Group("HangNguyen"));

		lvGroups.setItems(data);
		lvGroups.setCellFactory(lv -> new CustomListCellGroup());
	}

	private void initMessage() {
		message.add(new Message(1, "Alo moi nguoi", true));
		message.add(new Message(2, "Hi Hang", false));
		message.add(new Message(1, "Alo moi nguoi", true));
		message.add(new Message(2, "cả bầu Trời nắng", false));
		message.add(
				new Message(2,
						"Mưa trôi cả bầu Trời nắng, trượt theo những nỗi buồn Thấm ướt lệ sầu môi đắng "
								+ "vì đánh mất hy vọng Lần đầu gặp nhau dưới mưa, trái tim rộn ràng bởi ánh nhìn ",
						false));
		message.add(new Message(1, "Alo moi nguoi Alo moi nguoi Alo moi nguoi Alo moi nguoi"
				+ " Alo moi nguoi Alo moi nguoi Alo moi nguoi", true));
		message.add(new Message(2, "cả bầu Trời nắng", false));

		message.add(new Message(1, "Alo moi nguoi", true));
		message.add(new Message(2, "Hi Hang", false));
		message.add(new Message(1, "Alo moi nguoi", true));
		message.add(new Message(2, "cả bầu Trời nắng", false));

		lvMessage.setCellFactory(lv -> new CustomListCellMessage());
		lvMessage.setItems(message);
	}

	public void newUserNotification() {
		System.out.println("hello");
		String title = "Has a yourfriend online";
		String message = "ThanhHang online";
		NotificationType notification = NotificationType.NEWMESSAGE;
		TrayNotification tray = new TrayNotification();
		tray.setTitle(title);
		tray.setMessage(message);
		tray.setNotificationType(notification);
		tray.setAnimationType(AnimationType.FADE);
		tray.showAndDismiss(Duration.seconds(3));

		try {
			Media media = new Media(getClass().getResource("/sounds/notification.mp3").toURI().toString());
			MediaPlayer player = new MediaPlayer(media);
			player.setAutoPlay(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	MouseEvent mouseType ;
	public void initAddMenu() {
		addFriendsBtn.setOnMouseClicked(event -> {

			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem itemAddFriend = new MenuItem("Add new friend");
			final MenuItem itemNewGroup = new MenuItem("Add new group");
			contextMenu.getItems().addAll(itemAddFriend, itemNewGroup);
			
			contextMenu.addEventFilter(MouseEvent.ANY, e->{
				mouseType = e;
			});

			addFriendsBtn.setOnContextMenuRequested(e -> {
					contextMenu.show(addFriendsBtn, e.getScreenX(), e.getScreenY());
			});
			
			PopOverAddMenu popOverAddMenu = new PopOverAddMenu();
			itemNewGroup.setOnAction(e -> {
				popOverAddMenu.showPopOverNewGroup(addFriendsBtn);
			});
			itemAddFriend.setOnAction(e -> {
				popOverAddMenu.showPopOverAddFriend(addFriendsBtn);
			});
		});

	}
}
