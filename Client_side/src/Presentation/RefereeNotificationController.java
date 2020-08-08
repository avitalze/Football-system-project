package Presentation;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RefereeNotificationController {

    //private RefereeApplication refereeApplication = new RefereeApplication();
    private List<String> RefereeNotificationsList=new LinkedList<>();
    public String userName = "";
    private String role;

    @FXML
    private ComboBox<String > myNotificationsCombo;


    public void initAlertsUser(String userName,String role) {
        this.role=role;
        this.userName = userName;
    }

    @FXML
    private void initComboBox() {
        RefereeNotificationsList.clear();
        RefereeNotificationsList.add("All my unread alerts about matches");
        //String RefereeAlertsAsReferee = refereeApplication.getRefereeUnreadNotifications(userName);
        String RefereeAlertsAsReferee = ClientController.connectToServer("RefereeApplication", "getRefereeUnreadNotifications", userName);

        List<String> allRefereeAlerts = Arrays.asList(RefereeAlertsAsReferee.split(";"));
        for (String str:allRefereeAlerts) {
            RefereeNotificationsList.add(str);
        }
        ObservableList<String> elementsCombo = FXCollections.observableArrayList(RefereeNotificationsList);

        myNotificationsCombo.setItems(elementsCombo);
        myNotificationsCombo.getSelectionModel().selectFirst();
    }

    public void ClickOnMarkAsRead(ActionEvent actionEvent) {
        String notification = myNotificationsCombo.getSelectionModel().getSelectedItem();
        if(notification.equals("All my unread alerts about matches")){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("error-choose valid notification");
            chooseFile.show();
        }
        else{
            //String answer = this.refereeApplication.markNotificationAsRead(notification, this.userName);
            String answer = ClientController.connectToServer("RefereeApplication", "markNotificationAsRead", notification, userName);

            if(answer.equals("ok")) {
                initComboBox();
                Alert chooseFile = new Alert(Alert.AlertType.CONFIRMATION);
                chooseFile.setHeaderText("Confirmation");
                chooseFile.setContentText("notification marked as read");
                chooseFile.show();
            }
            else{
                Alert chooseFile = new Alert(Alert.AlertType.ERROR);
                chooseFile.setHeaderText("Error");
                chooseFile.setContentText(answer);
                chooseFile.show();
            }
        }

    }

    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent calcRoot = loader.load();
        HomePageController controller = loader.getController();
        controller.initHomePage(userName,role);
        controller.closeHandling(mouseEvent);
    }

    public void BackToReferee(ActionEvent actionEvent) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RefereePage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        RefereePageController controller = loader.getController();
        controller.initUser(userName,role);
        stageTheEventSourceNodeBelongs.setScene(scene);
    }
}
