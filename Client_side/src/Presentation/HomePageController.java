package Presentation;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HomePageController {


    @FXML
    private Button viewSettings;
    @FXML
    private Button registrationForGamesAlerts;
    @FXML
    private Button myAlerts;
    @FXML
    private Button buttonMyRoles;
    @FXML
    private  Button tempButtonTeamRole;


    //private UserApplication userApplication= new UserApplication();
   // private FanApplication fanApplication = new FanApplication();
    private String userName; // is teamRole
    private String role;

    //for notifications
    static CheckNotificationsTask scheduler=null;
    static boolean connectionOK=true;


//    @FXML
//    public void fanDetailsMouseClickHandling(MouseEvent mouseEvent) throws IOException {
//
//        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
//        Scene scene = new Scene(root, 900, 600);
//        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
//        stageTheEventSourceNodeBelongs.setScene(scene);
//
//    }

    /**
     * or- check if notification function works
     */
    @FXML
    public void initialize() {
        if(userName==null){
            return;
        }
        if(connectionOK && scheduler==null) {
            scheduler = new CheckNotificationsTask(userName);
            scheduler.setPeriod(Duration.seconds(10));
            scheduler.setOnSucceeded(
                    e -> {
                        System.out.println(scheduler.getValue());
                        if (scheduler.getValue().equals("ERROR")) {
                            scheduler.cancel();
                            connectionOK = false;
                        }
                        else if(scheduler.getValue().equals("gotFanNotification")){//fan
                            Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
                            chooseFile.setContentText("You have a new Notification about a game you are following !");
                            chooseFile.show();
                        }else if(scheduler.getValue().equals("gotRefereeNotification")){//referee
                            Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
                            chooseFile.setContentText("You have a new notification about your match !");
                            chooseFile.show();
                        }
                        else if(scheduler.getValue().equals("gotRFAnotification")){//rfa
                            Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
                            chooseFile.setContentText("You have a new team to approve !");
                            chooseFile.show();
                        }
                        else if(scheduler.getValue().equals("multipleNotifications")){//referee
                            Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
                            chooseFile.setContentText("You have multiple new notifications!");
                            chooseFile.show();
                        }

                    });
            scheduler.setOnFailed(e -> System.out.println("failed to run"));
            scheduler.start();
        }
        else if(connectionOK && scheduler !=null){
            scheduler.restart();
        }

    }


    @FXML
    public void fanDetailsEventClick(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("FanDetails.fxml"));
        Parent root=loader.load();
        // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("SignUp.css").toExternalForm());
        FanDetailsController fanDetailsController=loader.getController();
        fanDetailsController.initUserDetails(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }


    @FXML
    public void registrationForGamesAlertsOnlickHandling(ActionEvent event) throws IOException {

        /*
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("RegistrationGamesAlerts.fxml"));
        Scene scene = new Scene(root, 900, 600);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        stageTheEventSourceNodeBelongs.setScene(scene);
        */

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("RegistrationGamesAlerts.fxml"));
        Parent root=loader.load();
        // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        RegistrationGamesAlertsController registrationGamesAlertsController=loader.getController();
        registrationGamesAlertsController.initUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();

    }


    @FXML
    public void fanAllertsEventClick(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("MyAlerts.fxml"));
        Parent root=loader.load();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        MyAlertsControllr myRolesController=loader.getController();
        myRolesController.initAllertsUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }


//    @FXML
//    //!!!!!!!!!!!!!!!!!!!!!!
//    public void MyAlertsMouseClickHandling(MouseEvent mouseEvent) throws IOException {
///*
//        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("MyAlerts.fxml"));
//        Scene scene = new Scene(root, 900, 600);
//        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
//        stageTheEventSourceNodeBelongs.setScene(scene);
//*/
//    }

    /**
     * show my roles button if fan is teamRule
     * @param // userName
     */
    @FXML
    public void initHomePage(String userName,String role){ // String userName
        this.userName=userName;
        this.role=role;
        //String isTeamRole= fanApplication.fanIsTeamRole(userName);
        String isTeamRole = ClientController.connectToServer("FanApplication", "fanIsTeamRole", userName);
        if(isTeamRole.equals("true")){
            buttonMyRoles.setVisible(true);
        }
    }

    @FXML
    public void myRolesEventClick(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("MyRoles.fxml"));
        Parent root=loader.load();
        // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        MyRolesController myRolesController=loader.getController();
        myRolesController.initUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    @FXML
    public void onLogOut(ActionEvent actionEvent) throws IOException {
        scheduler.cancel();
        //String ans= userApplication.logout(userName);
        String ans = ClientController.connectToServer("UserApplication", "logout", userName);
        if(ans.equals("success")){
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("Login.fxml"));
            Parent root=loader.load();

            Scene scene = new Scene(root, 700, 400);


            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stageTheEventSourceNodeBelongs.setScene(scene);
            stageTheEventSourceNodeBelongs.show();
        }

        else{

            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setContentText("Logout was unsuccessful");
            chooseFile.show();
        }

    }

    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        HomePageController.scheduler.cancel();
        String ans = ClientController.connectToServer("UserApplication", "logout", userName);
        if(ans.equals("success")){
            Platform.exit();
            System.exit(0);
        }
        else{
            /*
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setContentText("Logout was unsuccessful");
            chooseFile.show();
            */
            Platform.exit();
            System.exit(0);
        }

    }
}
