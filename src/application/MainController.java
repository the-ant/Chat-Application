package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import pojo.Group;
import pojo.Message;
import pojo.User;
import traynotification.AnimationType;
import traynotification.NotificationType;
import traynotification.TrayNotification;
import utils.CustomListCellGroup;
import utils.CustomListCellMessage;
import utils.JSONUtils;

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
	private Text friendChatNameText, friendChatStatusText, textName;
	@FXML
	public Label name;

	private Stage primaryStage = Main.getPrimaryStage();
	
	private ObservableList<Group> data = FXCollections.observableArrayList();
	private ObservableList<Message> message = FXCollections.observableArrayList();

	private Client client = Client.getInstance();
	private List<User> users = new ArrayList<>();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initClient();
		initListFriend();
		initMessage();
	}

	private void initClient() {
		textName.setText(CurrentUser.getInstance().getFullName());
		setDataForListFriends(CurrentUser.getInstance().getRelationship());
		primaryStage.setOnCloseRequest(e -> client.closeClient());
	}
	public void requestListOfFriends() {
		client.send("listOfMyFriends:");
	}

	private void initListFriend() {
		lvGroups.setItems(data);
		lvGroups.setCellFactory(lv -> new CustomListCellGroup(this.users, CurrentUser.getInstance().getUser_id()));
	}
	private void handleJsonForGroups(JSONArray jsonArrayGroups) {
		for (int i = 0; i < jsonArrayGroups.length(); i++) {
			JSONObject jsonGroup = jsonArrayGroups.getJSONObject(i);
			String[] userIds = jsonGroup.getString("list_users").split(",");
			List<Integer> list_users = new ArrayList<>(); 
			for (String userId : userIds) {
				list_users.add(Integer.parseInt(userId));
				
			}
			Group group = new Group(jsonGroup.getInt("group_id"), jsonGroup.getString("group_name")
									, jsonGroup.getInt("user_id_created"), jsonGroup.getBoolean("is_chat_group")
									, list_users);
			this.data.add(group);
		}
	}
	private void handleJsonForListFriends(JSONArray jsonArrayFriends) {
		for (int i = 0; i < jsonArrayFriends.length(); i++) {
			JSONObject jsonFriends = jsonArrayFriends.getJSONObject(i);
			this.users.add(new User(jsonFriends.getInt("user_id"), jsonFriends.getString("fullname")
							, jsonFriends.getInt("online")));
		}
	}
	public void setDataForListFriends(String listOfMyFriends) {
		System.out.println("List of my friends: " + listOfMyFriends);
		JSONObject jsonData = new JSONObject(listOfMyFriends);
		JSONArray jsonArrayGroups = jsonData.getJSONArray("groups");
		//handleJsonForGroups(jsonArrayGroups);
		this.data.addAll(JSONUtils.parseGroups(jsonArrayGroups));
		JSONArray jsonArrayListFriends = jsonData.getJSONArray("friends");
		this.users = JSONUtils.parseFriends(jsonArrayListFriends);
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
}
