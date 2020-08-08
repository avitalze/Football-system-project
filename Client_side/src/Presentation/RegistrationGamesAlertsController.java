package Presentation;

//import Service.FanApplication;
//import Service.SystemOperationsApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class RegistrationGamesAlertsController {

    @FXML
    private Button backHomeButton;
    @FXML
    private ComboBox<String> FanMatchesCombo;
    @FXML
    private ComboBox<String> allMatchesInSystemCombo;
    @FXML
    private Button followMatchButton;
    //private FanApplication fanApplication = new FanApplication();
    //private SystemOperationsApplication syOpApp =new SystemOperationsApplication();
    private String userName; // is teamRole
    private String role;

    private List<String> fanMatchsList=new LinkedList<>();
    private List<String> allMatchsList=new LinkedList<>();

    @FXML
    public void initUser (String userName,String role) {
        this.userName=userName;
        this.role=role;
        //update comoboxs
        updateMachesFollsComoBox();
    }

    @FXML
    public void updateMachesFollsComoBox(){
        //for comboBox of fan matches
        fanMatchsList.clear();
        fanMatchsList.add("matches you are following and receive alerts");
        //String allFanMatchsStr = fanApplication.getUserMachesFollows(userName);
        String allFanMatchsStr = ClientController.connectToServer("FanApplication", "getUserMachesFollows", userName);

        List<String> allFanMatchs = Arrays.asList(allFanMatchsStr.split(";"));

        for (String str:allFanMatchs) {
            fanMatchsList.add(str);
        }
        ObservableList<String> elementsCombo1 = FXCollections.observableArrayList(fanMatchsList);

        FanMatchesCombo.setItems(elementsCombo1);
        FanMatchesCombo.getSelectionModel().selectFirst();

        //for comboBox of system matches
        allMatchsList.clear();
        allMatchsList.add("select a match you want to follow");
        //String allMatchesInSystemStr = syOpApp.getAllMatchsInSytem();
        String allMatchesInSystemStr = ClientController.connectToServer("SystemOperationsApplication", "getAllMatchsInSytem");

        List<String> allMatchesInSystem = Arrays.asList(allMatchesInSystemStr.split(";"));

        for (String str:allMatchesInSystem) {
            allMatchsList.add(str);
        }
        ObservableList<String> elementsCombo2 = FXCollections.observableArrayList(allMatchsList);
        allMatchesInSystemCombo.setItems(elementsCombo2);
        allMatchesInSystemCombo.getSelectionModel().selectFirst();

    }

    @FXML
    public void HomePageMouseClickHandling(MouseEvent mouseEvent) throws IOException {
        String fxmlStr="";
        if(role.equals("Fan")){
            fxmlStr="HomePage.fxml";
        }
        else if( role.equals("Rfa")){
            fxmlStr="RfaPage.fxml";
        }
        else{
            fxmlStr="RefereePage.fxml";
        }
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlStr));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        if(role.equals("Fan")){
            HomePageController controller = loader.getController();
            controller.initHomePage(userName,role);

        }
        else if( role.equals("Rfa")){
            RfaPageController controller = loader.getController();
            controller.initUser(userName,role);
        }
        else{
            RefereePageController controller = loader.getController();
            controller.initUser(userName,role);
        }


        stageTheEventSourceNodeBelongs.setScene(scene);
    }


    @FXML
    public void addFanMatchFollow (ActionEvent event){
        String matchToFollow = allMatchesInSystemCombo.getSelectionModel().getSelectedItem().toString();
        //didn't choose anything
        if(matchToFollow.equals("select a match you want to follow")){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("You didn't choose a match to follow");
            chooseFile.show();
        }
        else if(fanMatchsList.contains(matchToFollow)){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("You are already following this match. Please select another match.");
            chooseFile.show();
        }
        else { // add match to fan matches follow
            //String massage= fanApplication.addMatchToFanMatchesFollow(userName,matchToFollow);
            String massage = ClientController.connectToServer("FanApplication", "addMatchToFanMatchesFollow", userName, matchToFollow);

            if (massage.equals("ok")){
                Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
                chooseFile.setHeaderText("ok");
                chooseFile.setContentText("match added to your followed matches.");
                chooseFile.show();
                updateMachesFollsComoBox();
            }
            else{
                if(massage.contains("Error")){
                    Alert chooseFile = new Alert(Alert.AlertType.ERROR);
                    chooseFile.setHeaderText("Error");
                    chooseFile.setContentText(massage);
                    chooseFile.show();
                }
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
}
