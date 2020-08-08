package Presentation;

//import Service.FanApplication;
//import Service.UserApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public class RfaPageController extends HomePageController {


//    @FXML
//    Button viewSettings;
//    @FXML
//    Button registrationForGamesAlerts;
//    @FXML
//    Button myAlerts;

    @FXML
    public javafx.scene.control.Button definePolicyBtn;

    //delete later!!!!!!!!
    //private FanApplication fanApplication=new FanApplication();
    //private UserApplication userApplication= new UserApplication();



    private String userName;
    private String role="Rfa";

    @FXML
    public void initUser (String userName,String role) {
        this.role=role;
        this.userName=userName;
    }

    @FXML
    public void definePolicies(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("DefinePolicy.fxml"));
        Parent root=loader.load();

        Scene scene = new Scene(root);

        DefinePolicyController definePolicyController = loader.getController();
        definePolicyController.initUser(userName);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    @FXML
    public void MyAlertsFunction(MouseEvent mouseEvent) throws IOException {

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("RfaNotification.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("SignUp.css").toExternalForm());
        stageTheEventSourceNodeBelongs.setScene(scene);

    }

    @FXML
    public void registrationForGamesAlertsMouseClickHandling(ActionEvent event) throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("RegistrationGamesAlerts.fxml"));
        Parent root=loader.load();

        Scene scene = new Scene(root);

        RegistrationGamesAlertsController registrationGamesAlertsController=loader.getController();
        registrationGamesAlertsController.initUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    public void onLogOut(ActionEvent actionEvent) throws IOException {
        scheduler.cancel();
        //String ans= userApplication.logout(userName);
        String ans = ClientController.connectToServer("UserApplication", "logout", userName);
        if(ans.equals("success")){
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("Login.fxml"));
            Parent root=loader.load();

            Scene scene = new Scene(root);

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

    @FXML
    @Override
    public void initialize() {
        if(userName==null){
            return;
        }
        if (connectionOK && scheduler == null) {
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
            scheduler.setOnFailed(e -> {
                System.out.println("failed to run");
                scheduler.cancel();
                connectionOK=false;}
            );
            scheduler.start();
        }
        else if(connectionOK && scheduler !=null){
            scheduler.restart();
        }
    }

    public void RFADetailsEventClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePage.fxml"));
        Parent root=loader.load();
        HomePageController hpc=loader.getController();
        hpc.initHomePage(userName,"Rfa");
        hpc.fanDetailsEventClick(actionEvent);
    }

    //<editor-fold desc="create events">
    public void FanNotificationEventClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("MyAlerts.fxml"));
        Parent root=loader.load();
        //Scene scene = new Scene(root, 900, 600);
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        MyAlertsControllr myRolesController=loader.getController();
        myRolesController.initAllertsUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();

    }

    @FXML
    public void MyAlertsFunctionAsRFA(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("RfaNotification.fxml"));
        Parent root=loader.load();

        Scene scene = new Scene(root);

        RfaNotificationController controller=loader.getController();
        controller.initUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }
    //</editor-fold>

    @Override
    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent calcRoot = loader.load();
        HomePageController controller = loader.getController();
        controller.initHomePage(userName,role);
        controller.closeHandling(mouseEvent);
    }
}
