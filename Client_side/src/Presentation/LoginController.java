package Presentation;

//import Service.UserApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController{
    @FXML
    TextField txt_userName;
    @FXML
    TextField txt_password;
    @FXML
    Label lbl_error;

    @FXML
    public void closeHandling(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void validationHandling(MouseEvent mouseEvent) {
        //UserApplication uc=new UserApplication();

        try {
            if(txt_userName.getText().contains(";")||txt_password.getText().contains(";")){
                throw new Exception();
            }
            //String userRole= uc.login(txt_userName.getText(),txt_password.getText());
            String userRole = ClientController.connectToServer("UserApplication", "login", txt_userName.getText(),txt_password.getText());

            lbl_error.setText("");
            /**notification*/
            if(userRole.equals("Referee")||userRole.equals("RFA")){
                boolean notFan=false;
                String message="";
                //if(uc.haveUnreadNotifications(txt_userName.getText())){
                if(ClientController.connectToServer("UserApplication", "haveUnreadNotifications", txt_userName.getText()).equals("true")){
                    message="You have unread notifications as fan";
                    notFan=true;
                }
//                if(userRole.equals("Referee")) {
                    //if (uc.haveUnreadNotifications(userRole, txt_userName.getText())) {
                    if(ClientController.connectToServer("UserApplication", "haveUnreadNotifications", userRole, txt_userName.getText()).equals("true")){
                            if (notFan) {
                            message = message + ", and as " + userRole;
                        } else {
                            message = "You have unread notifications as " + userRole;
                        }
                        notFan = true;
                    }
//                }
//                if(userRole.equals("Rfa")) {
//                    if (uc.haveTeamRequest(userRole, txt_userName.getText())) {
//                        if (notFan) {
//                            message = message + ", and as" + userRole;
//                        } else {
//                            message = "You have unread notifications as " + userRole;
//                        }
//                        notFan = true;
//                    }
//                }
                if(notFan){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("");
                    DialogPane dialogPane = alert.getDialogPane();
                    alert.setContentText(message);
                    //dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
                    dialogPane.getStyleClass().add("alert");
                    alert.show();
                }
            }


            else {
                //if (uc.haveUnreadNotifications(txt_userName.getText())) {
                if (ClientController.connectToServer("UserApplication", "haveUnreadNotifications", txt_userName.getText()).equals("true")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("");
                    DialogPane dialogPane = alert.getDialogPane();
                    alert.setContentText("You have unread notifications!");
                    //dialogPane.getStylesheets().add(getClass().getResource("alert.css").toExternalForm());
                    dialogPane.getStyleClass().add("alert");
                    alert.show();
                }
            }
            if(userRole.equals("RFA")){

                FXMLLoader loader=new FXMLLoader();
                loader.setLocation(getClass().getResource("RfaPage.fxml"));
                Parent root2=loader.load();
                Scene scene = new Scene(root2);
                //scene.getStylesheets().add(getClass().getResource("SignUp.css").toExternalForm());
                RfaPageController rfaC=loader.getController();
                rfaC.initUser(txt_userName.getText(),"Rfa");
                rfaC.initialize();
                Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
                stageTheEventSourceNodeBelongs.setScene(scene);
                stageTheEventSourceNodeBelongs.show();
            }
            else if(userRole.equals("Referee")){

                    FXMLLoader loader=new FXMLLoader();
                    loader.setLocation(getClass().getResource("RefereePage.fxml"));
                    Parent root=loader.load();
                    // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(getClass().getResource("SignUp.css").toExternalForm());
                    RefereePageController refereeController=loader.getController();
                    refereeController.initUser(txt_userName.getText(),"Referee");
                    refereeController.initialize();

                    Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
                    stageTheEventSourceNodeBelongs.setScene(scene);
                    stageTheEventSourceNodeBelongs.show();
            }
            else if (userRole.equals("Fan")){
                    FXMLLoader loader=new FXMLLoader();
                    loader.setLocation(getClass().getResource("HomePage.fxml"));
                    Parent root=loader.load();
                    // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(getClass().getResource("SignUp.css").toExternalForm());
                    HomePageController hpc=loader.getController();
                    hpc.initHomePage(txt_userName.getText(),"Fan");
                    hpc.initialize();
                    Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
                    stageTheEventSourceNodeBelongs.setScene(scene);
                    stageTheEventSourceNodeBelongs.show();
                }
            else{//problem!!
                lbl_error.setText("incorrect user name or password");
            }

            //call to home page
        }
        catch (Exception e){
            lbl_error.setText("incorrect user name or password");
        }
    }
    @FXML
    public void signUpMouseClickHandling(MouseEvent mouseEvent) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        stageTheEventSourceNodeBelongs.setScene(scene);
    }
}
