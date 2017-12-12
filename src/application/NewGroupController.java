package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckListView;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class NewGroupController implements Initializable {
	@FXML
	private Button createGroupBtn;
	@FXML
	private VBox listFriendVbox;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		// =================CheckList
					final ObservableList<String> strings = FXCollections.observableArrayList();
					for (int i = 0; i <= 20; i++) {
						strings.add("Item " + i);
					}
					final CheckListView<String> checkListView = new CheckListView<>(strings);
					checkListView.setPrefSize(250, 300);
					checkListView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
						public void onChanged(ListChangeListener.Change<? extends String> c) {
							System.out.println(checkListView.getCheckModel().getCheckedItems());
						}
					}); 
	listFriendVbox.getChildren().addAll(checkListView);
	}

}
