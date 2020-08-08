package Presentation;

//import Service.FanApplication;
//import Service.RefereeApplication;
//import Service.UserApplication;
import javafx.application.Platform;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RefereePageController extends HomePageController {


    @FXML
    Button viewSettings;
    @FXML
    Button registrationForGamesAlerts;
    @FXML
    Button myAlerts;

    @FXML
    public javafx.scene.control.Button addEvent;

    @FXML
    public javafx.scene.control.Button idReportbtn;

    @FXML
    //private RefereeApplication refereeApplication = new RefereeApplication();
    //private FanApplication fanApplication= new FanApplication();
    //private UserApplication userApplication= new UserApplication();

    private String userName;
    private String role;

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
            scheduler.setOnFailed(e -> System.out.println("failed to run"));
            scheduler.start();
        }
        else if(connectionOK && scheduler !=null){
            scheduler.restart();
        }
    }

    @FXML
    public void initUser (String userName,String role) {
        this.role=role;
        this.userName=userName;
    }

    @FXML
    public void addEventToMatch(ActionEvent actionEvent) throws IOException {

        //String match = refereeApplication.displayAllMatches(userName);
        String match = ClientController.connectToServer("RefereeApplication", "displayAllMatches", userName);

        if(! match.equals("")) {
            //display matches that still not take place
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("eventsPage.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            eventsPageController eventsPageController = loader.getController();
            eventsPageController.initUser(userName, match,role);

            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stageTheEventSourceNodeBelongs.setScene(scene);
            stageTheEventSourceNodeBelongs.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You do not have a match that takes place right now so you can not add"
                    + " any events.");
            alert.show();
        }
    }

    public void createReport(ActionEvent actionEvent) throws IOException {
        //String matchesStr = refereeApplication.getAllMatches(userName);

        String matchesStr = ClientController.connectToServer("RefereeApplication", "getAllMatches", userName);

        List<String> matches = Arrays.asList(matchesStr.split(";"));

        if(matches.size() > 1){

            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("ReportOfMatch.fxml"));
            Parent root=loader.load();

            Scene scene = new Scene(root);

            ReportOfMatchController reportOfMatchController = loader.getController();
            reportOfMatchController.initPage(userName, matches);

            Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            stageTheEventSourceNodeBelongs.setScene(scene);
            stageTheEventSourceNodeBelongs.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You do not have a match that ended and in which you were a main referee," +
                    " so you can not create a report");
            alert.show();
        }
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

    @Override
    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent calcRoot = loader.load();
        HomePageController controller = loader.getController();
        controller.initHomePage(userName,role);
        controller.closeHandling(mouseEvent);
    }

    public void RefereeDetailsEventClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePage.fxml"));
        Parent root=loader.load();
        HomePageController hpc=loader.getController();
        hpc.initHomePage(userName,"Referee");
        hpc.fanDetailsEventClick(actionEvent);
    }

    //<editor-fold desc="Notifications">
    public void FanNotificationEventClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("MyAlerts.fxml"));
        Parent root=loader.load();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        MyAlertsControllr myRolesController=loader.getController();
        myRolesController.initAllertsUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    @FXML
    public void MyAlertsFunctionAsRef(ActionEvent event) throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("RefereeNotification.fxml"));
        Parent root=loader.load();

        Scene scene = new Scene(root);

        RefereeNotificationController controller=loader.getController();
        controller.initAlertsUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();

    }
    //</editor-fold>
}
