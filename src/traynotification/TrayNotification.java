package traynotification;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public final class TrayNotification {

    @FXML
    private Label lblTitle, lblMessage, lblClose;
    @FXML
    private ImageView imageIcon;
    @FXML
    private Rectangle rectangleColor;
    @FXML
    private AnchorPane rootNode;

    private CustomStage stage;
    private NotificationType notificationType;
    private AnimationType animationType;
    private EventHandler<ActionEvent> onDismissedCallBack, onShownCallback;
    private TrayAnimation animator;
    private AnimationProvider animationProvider;

    public TrayNotification(String title, String body, Image img, Paint rectangleFill) {
        initTrayNotification(title, body, NotificationType.CUSTOM);

        setImage(img);
        setRectangleFill(rectangleFill);
    }

    public TrayNotification(String title, String body, NotificationType notificationType ) {
        initTrayNotification(title, body, notificationType);
    }
    
    public TrayNotification() {
        initTrayNotification("", "", NotificationType.CUSTOM);
    }

    private void initTrayNotification(String title, String message, NotificationType type) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TrayNotification.fxml"));

            fxmlLoader.setController(this);
            fxmlLoader.load();

            initStage();
            initAnimations();

            setTray(title, message, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAnimations() {

        animationProvider =
            new AnimationProvider(new FadeAnimation(stage), new SlideAnimation(stage), new PopupAnimation(stage));

        setAnimationType(AnimationType.SLIDE);
    }

    private void initStage() {

        stage = new CustomStage(rootNode, StageStyle.UNDECORATED);
        stage.setScene(new Scene(rootNode));
        stage.setAlwaysOnTop(true);
        stage.setLocation(stage.getBottomRight());

        lblClose.setOnMouseClicked(e -> dismiss());
    }

    public void setNotificationType(NotificationType nType) {

        notificationType = nType;

        URL imageLocation = null;
        String paintHex = null;

        switch (nType) {

            case USERONLINE:
                imageLocation = getClass().getResource("/images/notification.png");
                paintHex = "#2C54AB";
                break;

            case NEWMESSAGE:
                imageLocation = getClass().getResource("/images/new_msg.png");
                paintHex = "#8D9695";
                break;
            case CUSTOM:
                return;
        }

        setRectangleFill(Paint.valueOf(paintHex));
        setImage(new Image(imageLocation.toString()));
        setTrayIcon(imageIcon.getImage());
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setTray(String title, String message, NotificationType type) {
        setTitle(title);
        setMessage(message);
        setNotificationType(type);
    }

    public void setTray(String title, String message, Image img, Paint rectangleFill, AnimationType animType) {
        setTitle(title);
        setMessage(message);
        setImage(img);
        setRectangleFill(rectangleFill);
        setAnimationType(animType);
    }

    public boolean isTrayShowing() {
        return animator.isShowing();
    }

    public void showAndDismiss(Duration dismissDelay) {

        if (isTrayShowing()) {
            dismiss();
        } else {
            stage.show();

            onShown();
            animator.playSequential(dismissDelay);
        }

        onDismissed();
    }

    public void showAndWait() {

        if (! isTrayShowing()) {
            stage.show();

            animator.playShowAnimation();

            onShown();
        }
    }

    public void dismiss() {

        if (isTrayShowing()) {
            animator.playDismissAnimation();
            onDismissed();
        }
    }

    private void onShown() {
        if (onShownCallback != null)
            onShownCallback.handle(new ActionEvent());
    }

    private void onDismissed() {
        if (onDismissedCallBack != null)
            onDismissedCallBack.handle(new ActionEvent());
    }

    public void setOnDismiss(EventHandler<ActionEvent> event) {
        onDismissedCallBack  = event;
    }

    public void setOnShown(EventHandler<ActionEvent> event) {
        onShownCallback  = event;
    }

    public void setTrayIcon(Image img) {
        stage.getIcons().clear();
        stage.getIcons().add(img);
    }

    public Image getTrayIcon() {
        return stage.getIcons().get(0);
    }

    public void setTitle(String txt) {
        lblTitle.setText(txt);
    }

    public String getTitle() {
        return lblTitle.getText();
    }

    public void setMessage(String txt) {
        lblMessage.setText(txt);
    }

    public String getMessage() {
        return lblMessage.getText();
    }

    public void setImage (Image img) {
        imageIcon.setImage(img);

        setTrayIcon(img);
    }

    public Image getImage() {
        return imageIcon.getImage();
    }

    public void setRectangleFill(Paint value) {
        rectangleColor.setFill(value);
    }

    public Paint getRectangleFill() {
        return rectangleColor.getFill();
    }

    public void setAnimationType(AnimationType type) {
        animator = animationProvider.findFirstWhere(a -> a.getAnimationType() == type);

        animationType = type;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }
}