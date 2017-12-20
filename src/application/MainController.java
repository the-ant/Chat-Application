package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.hwpf.model.types.LVLFAbstractType;
import org.json.JSONArray;
import org.json.JSONObject;

import client.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import pojo.CurrentUser;
import pojo.FlagConnection;
import pojo.Group;
import pojo.Message;
import pojo.User;
import traynotification.AnimationType;
import traynotification.NotificationType;
import traynotification.TrayNotification;
import utils.CustomListCellConfirmRequest;
import utils.CustomListCellGroup;
import utils.CustomListCellMessage;
import utils.JSONUtils;
import utils.PopOverAddMenu;

public class MainController implements Initializable {
	@FXML
	private ListView<Group> lvGroups;
	@FXML
	private ListView<User> lvUsers;
	@FXML
	private ListView<Message> lvMessage;
	@FXML
	private Circle mAvatarCircle;
	@FXML
	private TextField tfSearch, tfTypeMessage;
	@FXML
	private Text friendChatNameText, friendChatStatusText;
	@FXML
	private HBox homeHBox, confirmFriendsHBox;
	@FXML
	private ImageView homeChatImageView, confirmFriendsImageView;
	
	@FXML
	private ImageView addMenuBtn;
	@FXML
	private StackPane listViewStackPane;
	private Stage primaryStage = Main.getPrimaryStage();
	private Client client = Client.getInstance();
	private CurrentUser me = CurrentUser.getInstance();

	private ObservableList<Group> listGroups = FXCollections.observableArrayList();
	private ObservableList<Message> listMessages = FXCollections.observableArrayList();
	private ObservableList<User> users = FXCollections.observableArrayList();
	private List<User> listFriends;

	private String newMsg = "";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initClient();
		initListFriend();
		initMessage();
		initAddMenu();
	}
	private void initClient() {
		client.getClientConnection().setMainController(this);
		primaryStage.setOnCloseRequest(e -> client.closeClient());
		client.send(FlagConnection.GET_ALL_REQUESTS + "|");
	}

	private void initListFriend() {
		homeChatImageView.setOnMouseClicked(e -> {
			selectedHomeChat();
			changeTop();
		});
		
		confirmFriendsHBox.setOnMouseClicked(e -> {
			selectedConfirmFriends();
			changeTop();
		});
		
		setDataForListFriends(me.getRelationship());
		lvGroups.setItems(listGroups);
		lvGroups.setCellFactory(lv -> new CustomListCellGroup(listFriends, me.getUser_id()));
		lvGroups.getSelectionModel().select(0);
	}

	private void selectedConfirmFriends() {
		
		homeHBox.setStyle("-fx-background-color: rgb(249, 249, 249)");
		homeChatImageView.setImage(new Image("/images/home_black.png"));
		
		confirmFriendsHBox.setStyle("-fx-background-color:  white");
		confirmFriendsImageView.setImage(new Image("/images/invite_selected.png"));
		//User user = new User(8,"hangheo", "hang heo", true);
		//users.add(user);
		lvUsers.setItems(users);
		lvUsers.setCellFactory(lv -> new CustomListCellConfirmRequest());
		lvUsers.getSelectionModel().select(0);
	}

	private void selectedHomeChat() {
		//changeTop();
		homeHBox.setStyle("-fx-background-color: white");
		homeChatImageView.setImage(new Image("/images/home_selected.png"));
		
		confirmFriendsHBox.setStyle("-fx-background-color:  rgb(249, 249, 249)");
		confirmFriendsImageView.setImage(new Image("/images/invite_black.png"));
	}
	
	private void initMessage() {
		lvMessage.setCellFactory(lv -> new CustomListCellMessage());
		lvMessage.setItems(listMessages);

		tfTypeMessage.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER && !tfTypeMessage.getText().isEmpty()) {
				sendMessageTo(tfTypeMessage.getText());
				newMsg = tfTypeMessage.getText();
			}
		});

		tfTypeMessage.setOnMouseClicked(e -> {
			if (!tfTypeMessage.getText().isEmpty()) {
				sendMessageTo(tfTypeMessage.getText());
				newMsg = tfTypeMessage.getText();
			}
		});
	}

	private void sendMessageTo(String msg) {
		Group selectedGroup = getSelectedGroup();
		String desId = getDesFriendId(selectedGroup);
		String requestMsg = FlagConnection.SEND_MESSAGE + "|" + selectedGroup.getId() + "|" + desId + "|" + msg;
		client.send(requestMsg);
	}

	private String getDesFriendId(Group selectedGroup) {
		String result = selectedGroup.getListUserIDStr();
		return result;
	}

	public void setNewMessageToListView() {

	}

	public Group getSelectedGroup() {
		return lvGroups.getSelectionModel().getSelectedItem();
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

	public void setDataForListFriends(String listOfMyFriends) {
		JSONObject jsonData = new JSONObject(listOfMyFriends);

		JSONArray jsonArrayGroups = jsonData.getJSONArray("groups");
		this.listGroups.addAll(JSONUtils.parseGroups(jsonArrayGroups));

		JSONArray jsonArrayListFriends = jsonData.getJSONArray("friends");
		this.listFriends = JSONUtils.parseFriends(jsonArrayListFriends);
	}
	public void initAddMenu() {
		addMenuBtn.setOnMouseClicked(event -> {

			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem itemAddFriend = new MenuItem("Add new friend");
			final MenuItem itemNewGroup = new MenuItem("Add new group");
			contextMenu.getItems().addAll(itemAddFriend, itemNewGroup);
			

			addMenuBtn.setOnContextMenuRequested(e -> {
					contextMenu.show(addMenuBtn, e.getScreenX(), e.getScreenY());
			});
			
			PopOverAddMenu popOverAddMenu = new PopOverAddMenu();
			itemNewGroup.setOnAction(e -> {
				popOverAddMenu.showPopOverNewGroup(addMenuBtn);
			});
			itemAddFriend.setOnAction(e -> {
				popOverAddMenu.showPopOverAddFriend(addMenuBtn);
			});
		});

	}
	public void receiveRequestFriends(int userId, String fullname) {
		User user = new User(userId, fullname);
		this.users.add(user);
		// save database
		lvUsers.setCellFactory(lv -> new CustomListCellConfirmRequest());
	}
	public void updateListOfRequests(List<User> userRequests) {
		if (userRequests != null) {
			this.users.addAll(userRequests);
			lvUsers.setItems(this.users);
			lvUsers.setCellFactory(lv -> new CustomListCellConfirmRequest());
		} else {
			this.users.clear();
			lvUsers.setItems(this.users);
		}
	}
	private void changeTop() {
		ObservableList<Node> childs = this.listViewStackPane.getChildren();
		if (childs.size() > 1) {
			Node topNode = childs.get(childs.size() - 1);
			Node newTopNode = childs.get(childs.size() - 2);
			topNode.setVisible(false);
			topNode.toBack();
			newTopNode.setVisible(true);
		}
	}
	public void updateListView(String relationship) {
		me.setRelationship(relationship);
		this.listGroups.clear();
		this.listFriends.clear();
		setDataForListFriends(relationship);
		lvGroups.setItems(listGroups);
		lvGroups.setCellFactory(lv -> new CustomListCellGroup(listFriends, me.getUser_id()));
	}
}
