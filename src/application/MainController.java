package application;

import java.io.File;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import client.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
import utils.PlaceHolderUtil;
import utils.PopOverAddMenu;

public class MainController implements Initializable {
	@FXML
	private ListView<Group> lvGroups;
	@FXML
	private ListView<Message> lvMessage;
	@FXML
	private ListView<User> lvComfirmRequest;
	@FXML
	private Circle mAvatarCircle, groupAvatarCircle;
	@FXML
	private TextField tfSearch, tfTypeMessage;
	@FXML
	private Text fullnameText, avatarText, friendChatNameText, friendChatStatusText, groupNameText;
	@FXML
	private ImageView sendMessage, sendFileImageView, addFriendsBtn;
	@FXML
	private GridPane controlChatGridPane, infoSelectedFriendOrGroupChatGridPane;
	@FXML
	private MenuItem logoutMenuItem;
	@FXML
	private HBox homeHBox, confirmFriendsHBox;
	@FXML
	private ImageView homeChatImageView, confirmFriendsImageView;
	@FXML
	private StackPane listViewStackPane;

	private Stage primaryStage = Main.getPrimaryStage();
	private Client client = Client.getInstance();
	private CurrentUser me = CurrentUser.getInstance();

	private ObservableList<Group> listGroups = FXCollections.observableArrayList();
	private ObservableList<Message> listMessages = FXCollections.observableArrayList();
	private ObservableList<User> users = FXCollections.observableArrayList();

	private List<User> listFriends;
	public boolean isSelectedHomeGroup = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initInfoUser();
		initClient();
		initListGroups();
		initMessage();
		initStage();
		initSearchGroup();
		initAddMenu();
	}

	private void previousLogin() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/LoginRegister.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initStage() {
		primaryStage.setResizable(true);
		primaryStage.setOnCloseRequest(e -> exit());

		logoutMenuItem.setOnAction(e -> {
			String requestLogout = FlagConnection.LOGOUT + "|";
			client.send(requestLogout);

			Platform.runLater(() -> previousLogin());
		});

		sendFileImageView.setOnMouseClicked(e -> {
			Group selectedGroup = getSelectedGroup();
			if (selectedGroup != null) {
				FileChooser fc = new FileChooser();
				File file = fc.showOpenDialog(primaryStage);

				if (file != null) {
					String fileName = file.getName();
					System.out.println("sendFileImageView: " + fileName);
					long size = file.length();
					String desId = getDesFriendId(selectedGroup);

					String requestSendFile = FlagConnection.SEND_FILE + "|" + me.getUser_id() + "|" + me.getFullName()
							+ "|" + selectedGroup.getId() + "|" + desId + "|" + fileName + "," + size;
					client.sendFile(requestSendFile, selectedGroup.getId(), file);

					Message newMsg = new Message();
					newMsg.setMessage(file.getName());
					newMsg.setSender(me.getFullName());
					newMsg.setMe(true);
					newMsg.setFile(true);
					newMsg.setGroupID(selectedGroup.getId());

					listMessages.add(newMsg);
					tfTypeMessage.clear();
					scrollToNewMsg();
				}
			}
		});
	}

	private void initInfoUser() {
		fullnameText.setText(me.getFullName());
		avatarText.setText("" + me.getFullName().charAt(0));
	}

	private void initClient() {
		client.getClientConnection().setMainController(this);
	}

	public void initAddMenu() {
		addFriendsBtn.setOnMouseClicked(event -> {
			System.out.println("initAddMenu: ");
			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem itemAddFriend = new MenuItem("Add new friend");
			final MenuItem itemNewGroup = new MenuItem("Add new group");
			contextMenu.getItems().addAll(itemAddFriend, itemNewGroup);

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

	private void initListGroups() {
		setDataForListFriends(me.getRelationship());

		lvGroups.setItems(listGroups);
		lvGroups.setCellFactory(lv -> new CustomListCellGroup(listFriends, me.getUser_id()));
		lvGroups.setPlaceholder(PlaceHolderUtil.createPlaceHolderGroup());

		lvGroups.setOnMouseClicked(e -> {
			showControlAndInfoChatMessage();
			loadMsgs();
		});

		homeHBox.setOnMouseClicked(e -> {
			if (!isSelectedHomeGroup) {
				isSelectedHomeGroup = true;
				changeTop();
			}
			client.send(FlagConnection.GET_RELATIONSHIP + "|");
			selectedHomeChat();
		});

		confirmFriendsHBox.setOnMouseClicked(e -> {
			if (isSelectedHomeGroup) {
				isSelectedHomeGroup = false;
				changeTop();
			}

			client.send(FlagConnection.GET_ALL_REQUESTS + "|");
			selectedConfirmFriends();
		});

	}

	private void selectedConfirmFriends() {
		homeChatImageView.setImage(new Image("/images/home_black.png"));
		confirmFriendsImageView.setImage(new Image("/images/invite_selected.png"));

		lvComfirmRequest.setItems(users);
		lvComfirmRequest.setCellFactory(lv -> new CustomListCellConfirmRequest(this));
		lvComfirmRequest.getSelectionModel().select(0);
		lvComfirmRequest.setPlaceholder(PlaceHolderUtil.createPlaceHolderConfirm());
	}

	private void selectedHomeChat() {
		homeChatImageView.setImage(new Image("/images/home_selected.png"));
		confirmFriendsImageView.setImage(new Image("/images/invite_black.png"));
	}

	private void loadMsgs() {
		Group selectedGroup = lvGroups.getSelectionModel().getSelectedItem();
		if (selectedGroup != null) {
			String requestGetMsg = FlagConnection.GET_MESSAGE + "|" + selectedGroup.getId();
			client.send(requestGetMsg);
		}
	}

	private void initMessage() {
		hideControlAndInfoChatMessage();
		lvMessage.setPlaceholder(PlaceHolderUtil.createPlaceHolderMessage());
		lvMessage.setCellFactory(lv -> new CustomListCellMessage(this));
		lvMessage.setItems(listMessages);

		tfTypeMessage.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER && !tfTypeMessage.getText().isEmpty()) {
				sendMessageTo(tfTypeMessage.getText());

				Message newMsg = new Message();
				newMsg.setMessage(tfTypeMessage.getText());
				newMsg.setMe(true);
				newMsg.setSender(me.getFullName());

				listMessages.add(newMsg);
				tfTypeMessage.clear();
				scrollToNewMsg();
			}
		});

		sendMessage.setOnMouseClicked(e -> {
			if (!tfTypeMessage.getText().isEmpty()) {
				Message newMsg = new Message();
				newMsg.setMessage(tfTypeMessage.getText());
				newMsg.setMe(true);
				newMsg.setSender(me.getFullName());

				sendMessageTo(tfTypeMessage.getText());

				listMessages.add(newMsg);
				tfTypeMessage.clear();
				scrollToNewMsg();
			}
		});
	}

	private void scrollToNewMsg() {
		if (listMessages.size() > 0) {
			int lastPos = listMessages.size() - 1;
			Platform.runLater(() -> {
				lvMessage.scrollTo(lastPos);
				lvMessage.getSelectionModel().select(lastPos);
			});
		}
	}

	private void sendMessageTo(String msg) {
		Group selectedGroup = getSelectedGroup();
		String desId = getDesFriendId(selectedGroup);
		String requestMsg = FlagConnection.SEND_MESSAGE + "|" + selectedGroup.getId() + "|" + desId + "|" + msg;
		System.out.println("sendMessageTo: " + requestMsg);
		client.send(requestMsg);
	}

	private String getDesFriendId(Group selectedGroup) {
		String result = selectedGroup.getListUserIDStr();
		return result;
	}

	public Group getSelectedGroup() {
		return lvGroups.getSelectionModel().getSelectedItem();
	}

	public void notifyLogoutFriend(int logoutUserId) {
		handleNotification(logoutUserId, false);
	}

	public void notifyOnlineFriend(int onlineUserId) {
		handleNotification(onlineUserId, true);
	}

	private void handleNotification(int userId, boolean offline) {
		for (int i = 0; i < listFriends.size(); i++) {
			User user = listFriends.get(i);
			if (user.getId() == userId) {
				System.out.println("handleNotification: " + user.getId() + " - " + offline);

				user.setOnline(offline);
				listFriends.set(i, user);

				lvGroups.setCellFactory(lv -> new CustomListCellGroup(listFriends, me.getUser_id()));
				lvGroups.refresh();
				newUserNotification(user.getFullname(), user.isOnline());
				break;
			}
		}
	}

	public void newUserNotification(String nameOnlineUser, boolean online) {
		String message = nameOnlineUser + (online ? " đang online" : " đã offline");

		NotificationType notification = NotificationType.USERONLINE;
		TrayNotification tray = new TrayNotification();

		tray.setTitle("Notification " + (online ? "Online" : "Offline"));
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

	public void notifyNewMsg(Message msg) {
		NotificationType notification = NotificationType.NEWMESSAGE;
		TrayNotification tray = new TrayNotification();

		if (msg.isFile()) {
			tray.setTitle("Bạn nhận được một tập tin từ ");
			tray.setMessage(getFullNameOfFriend(msg.getUserID()));
		} else {
			tray.setTitle("Có một tin nhắn mới từ " + getFullNameOfFriend(msg.getUserID()));
			tray.setMessage(msg.getMessage());
		}
		tray.setNotificationType(notification);
		tray.setAnimationType(AnimationType.FADE);
		tray.showAndDismiss(Duration.seconds(3));

		try {
			Media media = new Media(getClass().getResource("/sounds/message.mp3").toURI().toString());
			MediaPlayer player = new MediaPlayer(media);
			player.setAutoPlay(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMessagesToSelectedGroup(int groupId, List<Message> getMsgs) {
		Group selectedGroup = getSelectedGroup();
		if (selectedGroup != null && selectedGroup.getId() == groupId) {
			listMessages.clear();
			listMessages.addAll(getMsgs);
			scrollToNewMsg();
		}
	}

	public void setDataForListFriends(String listOfMyFriends) {
		listGroups.clear();

		JSONObject jsonData = new JSONObject(listOfMyFriends);

		JSONArray jsonArrayGroups = jsonData.getJSONArray("groups");
		this.listGroups.addAll(JSONUtils.parseGroups(jsonArrayGroups));

		JSONArray jsonArrayListFriends = jsonData.getJSONArray("friends");
		this.listFriends = JSONUtils.parseFriends(jsonArrayListFriends);
	}

	public void setMessageToGroupById(int groupId, int senderId, String sender, String msg, boolean isFile) {
		Group selectedGroup = getSelectedGroup();
		Message newMsg = new Message(groupId, senderId, sender, msg, isFile);

		if (selectedGroup != null && selectedGroup.getId() == groupId) {
			listMessages.add(newMsg);
			scrollToNewMsg();
		} else {
			notifyNewMsg(newMsg);
		}
	}

	public String getLastMessageFromGroup(int groupId) {
		for (int i = listMessages.size() - 1; i >= 0; i--) {
			Message msg = listMessages.get(i);
			if (msg.getGroupID() == groupId) {
				return msg.getMessage();
			}
		}
		return "";
	}

	private void hideControlAndInfoChatMessage() {
		infoSelectedFriendOrGroupChatGridPane.setManaged(false);
		infoSelectedFriendOrGroupChatGridPane.setVisible(false);
		controlChatGridPane.setManaged(false);
		controlChatGridPane.setVisible(false);
	}

	private void showControlAndInfoChatMessage() {
		Group currentGroup = lvGroups.getSelectionModel().getSelectedItem();
		if (currentGroup != null) {

			if (!infoSelectedFriendOrGroupChatGridPane.isManaged() && !infoSelectedFriendOrGroupChatGridPane.isVisible()
					&& !controlChatGridPane.isManaged() && !controlChatGridPane.isVisible()) {
				infoSelectedFriendOrGroupChatGridPane.setManaged(true);
				infoSelectedFriendOrGroupChatGridPane.setVisible(true);
				controlChatGridPane.setManaged(true);
				controlChatGridPane.setVisible(true);
			}

			String name = getNameGroupOrFriend(currentGroup);
			friendChatNameText.setText(name);
			groupNameText.setText(name.charAt(0) + "");

			friendChatStatusText.setText("> " + getStatusGroup(currentGroup));
		}
	}

	public String getStatusGroup(Group currentGroup) {
		String result = me.getFullName() + ", ";
		if (currentGroup.isChatGroup()) {
			String userGroup = currentGroup.getListUserIDStr();
			for (User user : listFriends) {
				if (userGroup.contains(user.getId() + "")) {
					result += user.getFullname() + ", ";
				}
			}
			if (!result.equals("")) {
				result = result.substring(0, result.length() - 2);
			}
		} else {
			if (checkOlineFriend())
				result = "Online";
			else
				result = "Offline";
		}
		return result;
	}

	private boolean checkOlineFriend() {
		Group selectedGroup = getSelectedGroup();
		if (selectedGroup != null && !selectedGroup.isChatGroup()) {
			for (String userId : selectedGroup.getListUserIDStr().split("[,]")) {
				int id = Integer.parseInt(userId);
				if (id != me.getUser_id()) {
					for (User user : listFriends) {
						if (user.getId() == id && user.isOnline()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isChatGroup(int id) {
		if (listGroups.size() > 0) {
			for (Group group : listGroups)
				if (group.getId() == id && group.isChatGroup())
					return true;
		}
		return false;
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

	private String checkExistListUsers(Group item) {
		String name = "";
		if (item.getListUserID().size() > 0)
			for (Integer userId : item.getListUserID()) {
				if (userId != me.getUser_id()) {
					name = getFullNameOfFriend(userId);
					break;
				}
			}
		return name;
	}

	public String getFullNameOfFriend(int userId) {
		String result = "";
		for (User user : this.listFriends) {
			if (user.getId() == userId) {
				result = user.getFullname();
				break;
			}
		}
		return result;
	}

	public void exit() {
		String requestLogout = FlagConnection.LOGOUT + "|";
		client.send(requestLogout);
		System.exit(0);
	}

	public void downloadFileFromServer(int groupId, String imgName) {
		String requestDownLoadFile = FlagConnection.DOWN_LOAD_FILE + "|" + groupId + "|" + imgName;
		System.out.println("downloadFileFromServer: " + requestDownLoadFile);
		DirectoryChooser dc = new DirectoryChooser();
		File file = dc.showDialog(primaryStage);
		if (file != null && file.isDirectory()) {
			client.downloadFile(requestDownLoadFile, file, imgName);
		}
	}

	private void initSearchGroup() {
		List<User> filteredUser = new ArrayList<>();
		ObservableList<Group> groups = FXCollections.observableArrayList();

		tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredUser.clear();
			groups.clear();
			String lowerCaseFilter = deAccent(newValue.toLowerCase());

			if (newValue != null || !newValue.isEmpty()) {

				for (User user : listFriends) {
					if (deAccent(user.getFullname()).toLowerCase().trim().contains(lowerCaseFilter.trim())) {
						filteredUser.add(user);
					}
				}

				for (Group group : listGroups) {
					if (deAccent(group.getName()).toLowerCase().trim().contains(lowerCaseFilter.trim())) {
						groups.add(group);
					}
				}

				if (filteredUser.size() > 0) {
					for (User user : filteredUser) {
						Group group = getGroupByName(user);
						if (group != null) {
							boolean isExistGroup = isExistGroup(group, groups);
							if (!isExistGroup) {
								groups.add(group);
							}
						}
					}
				}
			}

			lvGroups.setItems(groups);
			lvGroups.setCellFactory(lv -> new CustomListCellGroup(filteredUser, me.getUser_id()));
		});
	}

	private boolean isExistGroup(Group group, ObservableList<Group> groups) {
		for (Group gr : groups) {
			if (gr.getId() == group.getId()) {
				return true;
			}
		}
		return false;
	}

	private Group getGroupByName(User user) {
		Group result = null;
		for (Group group : listGroups) {
			if (!group.isChatGroup()) {
				if (group.getListUserID().size() > 0) {
					for (int userId : group.getListUserID()) {
						if (userId != me.getUser_id()) {
							String name = getFullNameOfFriend(userId);
							if (name.equals(user.getFullname())) {
								return group;
							}
						}
					}
				}
			}
		}
		return result;
	}

	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	private String getFullNameOfFriend(Integer userId) {
		String result = "";
		for (User user : listFriends)
			if (user.getId() == userId) {
				result = user.getFullname();
				break;
			}
		return result;
	}

	public void receiveRequestFriends(int userId, String fullName) {
		System.out.println("receiveRequestFriends: " + fullName);
		notifyNewInviteFriend(fullName);
		User user = new User(userId, fullName);
		this.users.add(user);
		lvComfirmRequest.setCellFactory(lv -> new CustomListCellConfirmRequest(this));
		if (!isSelectedHomeGroup) {
			lvComfirmRequest.setItems(users);
		}
	}

	public void notifyNewInviteFriend(String fullName) {
		NotificationType notification = NotificationType.NEWMESSAGE;
		TrayNotification tray = new TrayNotification();

		tray.setTitle("Có một lời mời kết bạn từ");
		tray.setMessage(fullName);
		tray.setNotificationType(notification);
		tray.setAnimationType(AnimationType.FADE);
		tray.showAndDismiss(Duration.seconds(3));

		try {
			Media media = new Media(getClass().getResource("/sounds/message.mp3").toURI().toString());
			MediaPlayer player = new MediaPlayer(media);
			player.setAutoPlay(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeTop() {
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
		listGroups.clear();
		listFriends.clear();

		setDataForListFriends(relationship);
		lvGroups.setCellFactory(lv -> new CustomListCellGroup(listFriends, me.getUser_id()));
		lvGroups.setItems(listGroups);
	}

	public void updateListOfRequests(List<User> allRequestUsers) {
		users.clear();
		if (allRequestUsers != null) {
			users.addAll(allRequestUsers);
			lvComfirmRequest.setItems(users);
			lvComfirmRequest.setCellFactory(lv -> new CustomListCellConfirmRequest(this));
		} else {
			lvComfirmRequest.setItems(users);
		}
	}

	public void nofityAddGroup(boolean isSuccess, int groupId, String name, String userIds) {
		if (isSuccess) {
			List<Integer> listUserIds = new ArrayList<>();
			for (String id : userIds.split("[,]")) {
				listUserIds.add(Integer.parseInt(id));
			}
			Group group = new Group(groupId, name, true, listUserIds);
			listGroups.add(group);
			lvGroups.setItems(listGroups);
		}
	}
}
