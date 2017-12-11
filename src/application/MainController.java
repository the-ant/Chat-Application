package application;

import java.awt.CheckboxGroup;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckListView;
import org.controlsfx.control.PopOver;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
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
	private Text friendChatNameText, friendChatStatusText, statusLb;
	@FXML
	private ImageView addFriendsBtn;
	
	private BorderPane p;


	private Circle circle;

	private ObservableList<Group> data = FXCollections.observableArrayList();
	private ObservableList<Message> message = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initListFriend();
		initMessage();
		initAddMenu();
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
	
	public void initAddMenu() {
		addFriendsBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              
            	final ContextMenu contextMenu = new ContextMenu();
    		    final MenuItem itemAddFriend = new MenuItem("Add new friend");
    		    final MenuItem itemAddGroup = new MenuItem("Add new group");
    		    contextMenu.getItems().addAll(itemAddFriend, itemAddGroup);
    		    addFriendsBtn.setOnContextMenuRequested(e -> 
    		    contextMenu.show(addFriendsBtn, e.getScreenX(), e.getScreenY()));
    		    itemAddGroup.setOnAction(e -> {
    		    	addNewGroupLayout();
    		    });
    		    
    		    itemAddFriend.setOnAction(e ->{
    		    	addNewFriendLayout();
    		    });
            }
        });	
	}
	public void addNewGroupLayout() {
		
		Pane popupLayout = new Pane();
	    popupLayout.setPrefSize(300, 450);
	    
	    TextField groupNameTextField = new TextField();
	    groupNameTextField.setStyle("-fx-font-size: 14;");

	    
	    VBox vboxTextField = new VBox();
	    vboxTextField.setPadding(new Insets(20,15,20,15));
	    
	    HBox hBox = new HBox();
	    hBox.getChildren().addAll(new Label("Group Name   "),groupNameTextField);

	    //=================CheckList
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
	    
	    //=================
	    Label notificationLb = new Label();
	    
	    Button btnAddGroup = new Button();
	    btnAddGroup.setText("Add Group");
	    //================Action AddButton
	    btnAddGroup.setOnAction(e->{
	    	// processing...........
	    	// if add success
	    		notificationLb.setText("Successfully.");
	    		notificationLb.setTextFill(Paint.valueOf("#0B0B3B"));

	    });
	    //================
	    HBox hBox2 = new HBox();
	    hBox2.setSpacing(22);
		hBox2.getChildren().addAll(btnAddGroup,notificationLb);
	    
	    vboxTextField.getChildren().addAll(hBox,new Label("Add Group"),checkListView,hBox2);//them checklist
	    vboxTextField.setStyle("-fx-spacing: 8;");
	    popupLayout.getChildren().addAll(vboxTextField);
	    
		
		PopOver popOver = new PopOver();
		popOver.setTitle("New Group");
		popOver.arrowSizeProperty();
		popOver.setArrowSize(0);
		popOver.setContentNode(popupLayout);
		
		popOver.show(addFriendsBtn);
		
		 
		
	}
	
public void addNewFriendLayout() {
		
		Pane popupLayout = new Pane();
	    popupLayout.setPrefSize(300, 120);
	    
	    TextField userNameTextField = new TextField();
	    userNameTextField.setStyle("-fx-font-size: 14;");

	    
	    VBox vboxTextField = new VBox();
	    vboxTextField.setPadding(new Insets(20,15,20,15));
	    
	    HBox hBox = new HBox();
	    hBox.getChildren().addAll(new Label("Username Name   "),userNameTextField);

	   
	    
	    Button btnAddUser = new Button();
	    btnAddUser.setText("Add Friend");
	    //================Action AddButton
	    Label notificationLb = new Label();
	    btnAddUser.setOnAction(e->{
	    	// processing...........
	    	if(true) { // if add success
	    		notificationLb.setText("Successfully.");
	    		notificationLb.setTextFill(Paint.valueOf("#0B0B3B"));
	    		
		    }else {
		    	notificationLb.setText("Username not found!!!");
		    	notificationLb.setTextFill(Paint.valueOf("#FF0040"));
		    }
	    });
	    //================
	    
	    HBox hBox2 = new HBox();
	    hBox2.setSpacing(22);
		hBox2.getChildren().addAll(btnAddUser,notificationLb);
	    
	    vboxTextField.getChildren().addAll(hBox,hBox2);//them checklist
	    vboxTextField.setStyle("-fx-spacing: 10;");
	    popupLayout.getChildren().addAll(vboxTextField);
	    
		
		PopOver popOver = new PopOver();
		popOver.setTitle("New Friend");
		popOver.arrowSizeProperty();
		popOver.setArrowSize(0);
		popOver.setContentNode(popupLayout);	    
		popOver.show(addFriendsBtn);
		
	    }
		
	
	
	
}
