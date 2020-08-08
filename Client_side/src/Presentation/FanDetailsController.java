package Presentation;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import javax.xml.ws.FaultAction;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FanDetailsController { //implements Initializable


    @FXML
    private Label fanUserName; //V
    @FXML
    private Label currNameLabel; //V
    @FXML
    private Label currPasswardLable; //V
    @FXML
    private Label currPhonNumberLabel; // V
    @FXML
    private Label currEmailLabel; //V
    @FXML
    private Label currDateOfBirthLable; //V
    @FXML
    private Label currQualificationLable;
    @FXML
    private Label idQualification;
    @FXML
    private TextField updateNameFeild; // V
    @FXML
    private Button UpdateButton; //V
    @FXML
    private TextField updatePasswardField; //V
    @FXML
    private TextField updatePhonNumField; //V
    @FXML
    private TextField updateEmailField; //V
    @FXML
    private Label welcomeUserNameField; //V
    @FXML
    private Button updateMyDetailsButton;



    //private FanApplication fanApplication = new FanApplication();
    //private SystemOperationsApplication syOpApp =new SystemOperationsApplication();
    private String userName; // userName; set!!!!!!!!!!!!!!!!!!!!!!!!
    private String role;


/*
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(userName!=null){
            List<String> fanDetails= syOpApp.getPrivateDetails(userName);
            //list : name, Password, PhoneNumber, Email, DateOfBirth
            currNameField.setText(fanDetails.get(0));
            currPasswardField.setText(fanDetails.get(1));
            currPhonNumberField.setText(fanDetails.get(2));
            currEmailField.setText(fanDetails.get(3));
            currDateOfBirthField.setText(fanDetails.get(4));
            fanUserName.setText(userName);
            welcomeUserNameField.setText(userName);

        }

        //String desc = fanName.getText();
    }
    */

    @FXML
    public void initUserDetails (String userName, String role) {
        this.userName=userName;
        this.role=role;
        showDetails();

    }


    @FXML
    private void showDetails() {
        //String fanDetailsStr = syOpApp.getPrivateDetails(userName);
        String fanDetailsStr = ClientController.connectToServer("SystemOperationsApplication", "getPrivateDetails", userName);
        List<String> fanDetails = Arrays.asList(fanDetailsStr.split(";"));

        //check if Referee
        if(role.equals("Referee")){
            //show details about referee
            currQualificationLable.setVisible(true);
            idQualification.setVisible(true);
            String refereeQualification = ClientController.connectToServer("RefereeApplication", "getQualificationOfReferee", userName);
            if(! refereeQualification.contains("Error")){
                currQualificationLable.setText(refereeQualification);
            }
            else{
                currQualificationLable.setText("null");
            }
        }

        //list : name, Password, PhoneNumber, Email, DateOfBirth
        currNameLabel.setText(fanDetails.get(0));
        //currPasswardLable.setText(fanDetails.get(1));
        currPasswardLable.setText("******");
        //TODO decrypt
        // ClientController.connectToServer("SystemOperationsApplication", "decrypt", fanDetails.get(1));
        currPhonNumberLabel.setText(fanDetails.get(2));
        currEmailLabel.setText(fanDetails.get(3));
        currDateOfBirthLable.setText(fanDetails.get(4));
        fanUserName.setText(userName);
        welcomeUserNameField.setText(userName);

        if(updatePasswardField.isVisible()== true){
            updatePasswardField.setText("******");
        }
    }



    @FXML
    public void showAllhideFiels(MouseEvent mouseEvent) {

        updateNameFeild.setVisible(true);
        UpdateButton.setVisible(true);
        //fanPasswardUpdateButton.setVisible(true);
        //FanPhonNumberUpdateButton.setVisible(true);
        //fanEmailUpdateButton.setVisible(true);
        updatePasswardField.setVisible(true);
        updatePhonNumField.setVisible(true);
        updateEmailField.setVisible(true);
        //fanDateUpdateButton.setVisible(true);

        updateNameFeild.setText(currNameLabel.getText());
        //updatePasswardField.setText(currPasswardLable.getText()); !!
        updatePasswardField.setText("******");
        updatePhonNumField.setText(currPhonNumberLabel.getText());
        updateEmailField.setText(currEmailLabel.getText());

        updateMyDetailsButton.setVisible(false);

    }

    /**
     *
     * update ditails of fan . check if input is valid.
     * @throws Exception
     */
    @FXML
    public void updateDetailsByUser() {
        String newName=updateNameFeild.getText();
        String newPassward=updatePasswardField.getText();
        String newPhonNum=updatePhonNumField.getText();
        String newEmail=updateEmailField.getText();
        if(newName.equals("") || newName==null){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("The name field is empty. Please insert your name.");
            chooseFile.show();
        }//password length is 6 or more
        else if(!newPassward.equals("******")&&(newPassward.equals("")|| newPassward ==null || newPassward.length() < 6) ){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("Invalid password, please enter a valid password. Password length must be at least 6 characters.");
            chooseFile.show();
        }// phone number is 10 digits
        else if(newPhonNum == null || newPhonNum.equals("")|| !(newPhonNum.matches("^[0-9]*$")) || newPhonNum.length() != 10){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("Invalid phone number, please enter a valid phone number. Phone Number length must be 10 digits.");
            chooseFile.show();
        }//email contains @
        else if(newEmail.equals("")|| newEmail==null || ((!newEmail.contains(".com") && !newEmail.contains("il")))){
            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
            chooseFile.setHeaderText("Error");
            chooseFile.setContentText("Invalid email, please enter a valid email. Email must contain a @ and end with .com or .il");
            chooseFile.show();
        }
//        else if(newDateOfBirth.equals("")|| newDateOfBirth==null){
//            Alert chooseFile = new Alert(Alert.AlertType.ERROR);
//            chooseFile.setHeaderText("Error");
//            chooseFile.setContentText("date field is empty. Please insert date.");
//            chooseFile.show();
//        }
//            // all good!!
        else {
            if(newPassward.equals("******")){
                newPassward="null";
            }
            //String massage= fanApplication.setFanDetails(userName,newName,newPassward,newPhonNum,newEmail);
            String massage = ClientController.connectToServer("FanApplication", "setFanDetails", userName,newName,newPassward,newPhonNum,newEmail);

            if(massage.equals("ok")){
                showDetails();
                Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
                chooseFile.setHeaderText("ok");
                chooseFile.setContentText("Your details are updated");
                chooseFile.show();
            }
            else{
                Alert chooseFile = new Alert(Alert.AlertType.ERROR);
                chooseFile.setHeaderText("Error");
                chooseFile.setContentText("Update canceled.");
                chooseFile.show();
            }
        }
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
        //Parent root = FXMLLoader.load(getClass().getResource(fxmlStr));
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

    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent calcRoot = loader.load();
        HomePageController controller = loader.getController();
        controller.initHomePage(userName,role);
        controller.closeHandling(mouseEvent);
    }
}
