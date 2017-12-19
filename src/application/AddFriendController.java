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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import pojo.CurrentUser;
import pojo.User;
import utils.JSONUtils;

public class AddFriendController implements Initializable{
	@FXML
	private VBox listFriendVbox;
	@FXML
	private Button addFriendBtn;
	private List<User> listUser;
	private Client client = Client.getInstance();
	private CurrentUser me = CurrentUser.getInstance();
	@FXML 
	private ListView<String> allUserListView;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		JSONObject jsonData = new JSONObject(me.getRelationship());
		JSONArray jsonArrayListFriends = jsonData.getJSONArray("friends");
		this.listUser = JSONUtils.parseFriends(jsonArrayListFriends);

		final ObservableList<String> obListFriendName = FXCollections.observableArrayList();
		for (int i = 0; i < listUser.size(); i++) {
			obListFriendName.add(listUser.get(i).getFullname());
		}
		final ListView<String> checkListView = new ListView<>(obListFriendName);
		checkListView.setPrefSize(250, 300);
		checkListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listFriendVbox.getChildren().addAll(checkListView);
	}

}
