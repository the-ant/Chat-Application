package application;

import java.net.URL;
import java.text.Normalizer;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.controlsfx.control.CheckListView;
import org.json.JSONArray;
import org.json.JSONObject;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pojo.CurrentUser;
import pojo.FlagConnection;
import pojo.User;
import utils.JSONUtils;

public class NewGroupController implements Initializable {
	@FXML
	private Button createGroupBtn;
	@FXML
	private TextField tfGroupName;
	@FXML
	private VBox listFriendVbox;
	@FXML
	private Text notifyCreateGroup;
	
	private List<User> listUser;
	private ObservableList<Integer> indexChecked;
	private Client client = Client.getInstance();
	private CurrentUser me = CurrentUser.getInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client.setNewGroupController(this);
		initListFriend();

		createGroupBtn.setOnAction(e -> {
			notifyCreateGroup.setText("");

			if (tfGroupName.getText().toString().trim().equals("")) {
				notifyCreateGroup.setText("Please fill Group Name");

			} else if (indexChecked.size() < 2) {
				notifyCreateGroup.setText("Please check more two friends");

			} else {

				String requestCreateGroup = tfGroupName.getText() + "|" + me.getUser_id() + "|";
				System.out.println(indexChecked);

				for (int i = 0; i < indexChecked.size(); i++) {
					requestCreateGroup += listUser.get(indexChecked.get(i)).getId() + ",";
				}

				requestCreateGroup = requestCreateGroup.substring(0, requestCreateGroup.length() - 1);
				System.out.println("listuserID: " + requestCreateGroup);

				sendRequestToServer(requestCreateGroup);
				Stage stage = (Stage) createGroupBtn.getScene().getWindow();
				stage.close();
			}
		});

	}

	private void sendRequestToServer(String msg) {
		String requestMsg = FlagConnection.ADD_GROUP + "|" + msg;
		System.out.println("sendRequestToServer: " + requestMsg);
		client.send(requestMsg);
	}

	private void initListFriend() {
		JSONObject jsonData = new JSONObject(me.getRelationship());
		JSONArray jsonArrayListFriends = jsonData.getJSONArray("friends");
		this.listUser = JSONUtils.parseFriends(jsonArrayListFriends);

		final ObservableList<String> obListFriendName = FXCollections.observableArrayList();
		for (int i = 0; i < listUser.size(); i++) {
			obListFriendName.add(listUser.get(i).getFullname());
		}

		indexChecked = FXCollections.observableArrayList();
		final CheckListView<String> checkListView = new CheckListView<>(obListFriendName);
		checkListView.setPrefSize(250, 300);
		checkListView.setPadding(new Insets(10));
		
		checkListView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
			public void onChanged(ListChangeListener.Change<? extends String> c) {
				indexChecked = checkListView.getCheckModel().getCheckedIndices();
				System.out.println(indexChecked);
			}
		});
		listFriendVbox.getChildren().addAll(checkListView);
	}

	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public void nofityAddGroup(boolean isSuccess, int groupId, String nameGroup, String listUserIds) {
		if (isSuccess) {

		} else {
			notifyCreateGroup.setText("Group Name is Exist! Try again.");
		}
	}
}