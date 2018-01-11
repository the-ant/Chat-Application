package application;

import java.net.URL;
import java.text.Normalizer;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pojo.FlagConnection;
import pojo.User;
import utils.CustomAllUserListCell;

public class AddFriendController implements Initializable {

	@FXML
	private Button addFriendBtn;
	@FXML
	private ListView<String> allUserListView;
	@FXML
	private TextField tfNewFriend;
	@FXML
	private ListView<User> lvAllUser;
	@FXML
	private Label notificationLbl;

	private Client client = Client.getInstance();
	private ObservableList<User> listAllUser = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initListAllUsers();
		searchUserName();
	}

	private void initListAllUsers() {
		client.setAddFriendController(this);
		String requestGetAllUser = FlagConnection.GET_ALL_USER + "|";
		client.send(requestGetAllUser);
	}

	@SuppressWarnings("null")
	private void searchUserName() {
		final ObservableList<User> obListFriendName = FXCollections.observableArrayList();
		
		tfNewFriend.textProperty().addListener((observable, oldValue, newValue) -> {
			obListFriendName.clear();
			if (newValue != null || !newValue.isEmpty()) {
				String formatTxtSearching = deAccent(tfNewFriend.getText().toLowerCase());
				for (User user : this.listAllUser) {
					if (deAccent(user.getFullname().toLowerCase()).contains(formatTxtSearching)) {
						obListFriendName.add(user);
					}
				}
			}
			lvAllUser.setItems(obListFriendName);
		});

	}

	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public void setAllUser(List<User> allUser) {
		this.listAllUser.addAll(allUser);
		lvAllUser.setItems(listAllUser);
		lvAllUser.setCellFactory(lv -> new CustomAllUserListCell());
	}

	public void handleAddNewFriend(ActionEvent event) {
		User userReceived = lvAllUser.getSelectionModel().getSelectedItem();
		client.send(FlagConnection.ADD_FRIEND + "|" + userReceived.getId());
		notificationLbl.setText("Đã gửi lời mời kết bạn đến " + userReceived.getFullname());
	}
}