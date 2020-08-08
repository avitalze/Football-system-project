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
import java.util.regex.Pattern;


public class DefinePolicyController {

    @FXML
    public ComboBox<String> chooseCalcPolicyBtn;
    @FXML
    public ComboBox<String> chooseSchedPolicyBtn;
    @FXML
    public javafx.scene.control.TextField idOfSeason;
    @FXML
    public javafx.scene.control.Button defineCalcPolicyBtn;
    //private RfaApplication rfaApplication = new RfaApplication();
   // private SystemOperationsApplication sysOpApp = new SystemOperationsApplication();


    private String userName;

    public void initialize() {
        //for comboBox of calculation policy
        //String allCalculationPoliciesStr = rfaApplication.getAllCalculationPoliciesString();
        String allCalculationPoliciesStr = ClientController.connectToServer("RfaApplication", "getAllCalculationPoliciesString");
        List<String> allCalculationPolicies = Arrays.asList(allCalculationPoliciesStr.split(";"));
        List<String> list = new LinkedList<>();
        for (String str:allCalculationPolicies) {
            list.add(str);
        }

        ObservableList<String> elements = FXCollections.observableArrayList(list);

        chooseCalcPolicyBtn.setItems(elements);
        chooseCalcPolicyBtn.getSelectionModel().selectFirst();


        // for comboBox of scheduling policy
        //String allSchedulingPoliciesStr = rfaApplication.getAllschedulingString();
        String allSchedulingPoliciesStr = ClientController.connectToServer("RfaApplication", "getAllschedulingString");

        List<String> allSchedulingPolicies = Arrays.asList(allSchedulingPoliciesStr.split(";"));
        List<String> listOfSched = new LinkedList<>();
        for (String str:allSchedulingPolicies) {
            listOfSched.add(str);
        }

        ObservableList<String> elementsOfScheduling = FXCollections.observableArrayList(listOfSched);

        chooseSchedPolicyBtn.setItems(elementsOfScheduling);
        chooseSchedPolicyBtn.getSelectionModel().selectFirst();
    }

    @FXML
    public void initUser (String userName) {
        this.userName=userName;
    }


    //when press on define btn
    @FXML
    public void defineCalcPolicy(ActionEvent event){

        idOfSeason.setDisable(true);
        defineCalcPolicyBtn.setDisable(true);

        String seasonSelected = idOfSeason.getText();
        if (Pattern.matches("[0-9]+", seasonSelected) && seasonSelected.length() == 4) {

            String calcPolicySelected = chooseCalcPolicyBtn.getSelectionModel().getSelectedItem().toString();
            String schedPolicySelected = chooseSchedPolicyBtn.getSelectionModel().getSelectedItem().toString();
            //String answer = this.rfaApplication.DefinePoliciesToSeason(seasonSelected, calcPolicySelected, schedPolicySelected, userName);
            String answer = ClientController.connectToServer("RfaApplication", "DefinePoliciesToSeason", seasonSelected, calcPolicySelected, schedPolicySelected, userName);
            if(answer.equals("ok")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Define policies was successful");
                alert.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(answer);
                alert.show();
            }

        }
        else{
            //throw alert that the year of season incorrect syntax
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Incorrect Syntax in season field, please insert 4 numbers.");
            alert.show();
        }

        defineCalcPolicyBtn.setDisable(false);
        idOfSeason.setDisable(false);
    }


    @FXML
    public void BackToRfaPage(ActionEvent event) throws IOException {
        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RfaPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        RfaPageController controller = loader.getController();
        controller.initUser(userName,"Rfa");
        stageTheEventSourceNodeBelongs.setScene(scene);
    }


    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent calcRoot = loader.load();
        HomePageController controller = loader.getController();
        controller.initHomePage(userName,"Rfa");
        controller.closeHandling(mouseEvent);
    }
}
