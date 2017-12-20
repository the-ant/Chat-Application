package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pojo.CurrentUser;
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
	ListView<User> lvAllUser;

	private Client client = Client.getInstance();
	@SuppressWarnings("unused")
	private CurrentUser me = CurrentUser.getInstance();
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
				String formatTxtSearching = tfNewFriend.getText().toLowerCase();
				for (User user : this.listAllUser) {
					if (user.getFullname().toLowerCase().contains(formatTxtSearching)) {
						obListFriendName.add(user);
					}
				}
			}
			lvAllUser.setItems(obListFriendName);
		});
		
	}

	public void setAllUser(List<User> allUser) {
		this.listAllUser.addAll(allUser);
		lvAllUser.setItems(listAllUser);
		lvAllUser.setCellFactory(lv -> new CustomAllUserListCell());
	}
	
	public void handleAddNewFriend(ActionEvent event) {
		User userReceived = lvAllUser.getSelectionModel().getSelectedItem();
		client.send(FlagConnection.ADD_FRIEND+ "|" + userReceived.getId());
	}
}
