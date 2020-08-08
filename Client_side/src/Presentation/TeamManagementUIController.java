package Presentation;

//import Service.TeamManagementApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class TeamManagementUIController { //implements Initializable {
    @FXML
    private ChoiceBox teamNameCB = new ChoiceBox();
    @FXML
    private ChoiceBox coachCB = new ChoiceBox();
    @FXML
    private ListView playersListView = new ListView();
    @FXML
    private TextField fieldName;
    @FXML
    private TextField newTeamName;
    String userName;
    String role;
    //private TeamManagementApplication tMApp = new TeamManagementApplication();

//    @Override
//    public void initialize(URL location, ResourceBundle resources){
//            activateScene();
//    }

    @FXML
    public void initUser(String userName,String role) {
        this.role=role;
        this.userName = userName;
        activateScene();
    }

    private void changeScene(ActionEvent event, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        TeamManagementUIController tMUICont = loader.getController();
        tMUICont.initUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    @FXML
    public void changeToRequestScene(ActionEvent event) throws IOException {
        changeScene(event, "RequestNewTeam.fxml");
    }

    @FXML
    public void changeToActivateScene(ActionEvent event) throws IOException {
        //String approvedTeamsStr = tMApp.getMyApprovedTeams(userName);
        String approvedTeamsStr = ClientController.connectToServer("TeamManagementApplication", "getMyApprovedTeams", userName);

        //String tempPlayersStr = tMApp.getAllTeamRolesThatArentPlayerWithTeam();
        String tempPlayersStr = ClientController.connectToServer("TeamManagementApplication", "getAllTeamRolesThatArentPlayerWithTeam");

        List<String> tempPlayers = Arrays.asList(tempPlayersStr.split(";"));

        //String CoachesStr = tMApp.getAllTeamRolesThatArentCoachWithTeam();
        String CoachesStr = ClientController.connectToServer("TeamManagementApplication", "getAllTeamRolesThatArentCoachWithTeam");

        List<String> Coaches = Arrays.asList(CoachesStr.split(";"));

        if (approvedTeamsStr == null || approvedTeamsStr.equals("")) {
            alertError("You do not have any teams to activate.");
        } else if (tempPlayers.size() < 11) {
            alertError("Cannot create a new team. There are less than 11 potential players in the system.");
        } else if (Coaches.size() < 1) {
            alertError("Cannot create a new team. There aren't any potential coaches in the system.");
        } else {
            List<String> approvedTeams = Arrays.asList(approvedTeamsStr.split(";"));
            changeScene(event, "ActivateTeam.fxml");
        }
    }

    @FXML
    public void changeToTeamManagementScene(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "TeamOwner.fxml");
    }

    @FXML
    public void changeToHomePage(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();
        // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        HomePageController homeCont = loader.getController();
        homeCont.initHomePage(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    @FXML
    public void changeToMyRolesScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MyRoles.fxml"));
        Parent root = loader.load();
        // Parent root = FXMLLoader.load(getClass().getResource("FanDetails.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        MyRolesController myRolesCont = loader.getController();
        myRolesCont.initUser(userName,role);

        Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stageTheEventSourceNodeBelongs.setScene(scene);
        stageTheEventSourceNodeBelongs.show();
    }

    @FXML
    public void requestNewTeam(ActionEvent event) throws IOException {

        String teamName = newTeamName.getText();
        if (teamName == null || teamName.equals("")) {
            alertError("Please insert a team name.");
        } else {
            //String message = tMApp.requestNewTeam(userName, teamName);
            String message = ClientController.connectToServer("TeamManagementApplication", "requestNewTeam", userName, teamName);


            if (message == "team name not unique, already exist in system") {
                alertError("This team name already exists. Please choose a different team name.");
            } else {
                alertInformation("Your request has been sent to the RFA. Once approved you may activate your team.");
                changeToTeamManagementScene(event);
            }
        }
    }

    @FXML
    public void activateScene() {
        //if (tMApp.getMyApprovedTeams(userName) != null && tMApp.getMyApprovedTeams(userName).length() != 0) {
        String approvedTeamsStr = ClientController.connectToServer("TeamManagementApplication", "getMyApprovedTeams", userName);
        if(approvedTeamsStr != null && approvedTeamsStr.length() != 0){
            //String approvedTeamsStr = tMApp.getMyApprovedTeams(userName);

            List<String> approvedTeams = Arrays.asList(approvedTeamsStr.split(";"));

            teamNameCB.getItems().clear();
            for (String teamName : approvedTeams) {
                teamNameCB.getItems().add(teamName);
            }
        }

        //String tempPlayersStr = tMApp.getAllTeamRolesThatArentPlayerWithTeam();
        String tempPlayersStr = ClientController.connectToServer("TeamManagementApplication", "getAllTeamRolesThatArentPlayerWithTeam");

        List<String> tempPlayers = Arrays.asList(tempPlayersStr.split(";"));

        ObservableList<String> players = FXCollections.observableArrayList(tempPlayers);
        playersListView.getItems().clear();
        playersListView.setItems(players);
        playersListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //String CoachesStr = tMApp.getAllTeamRolesThatArentCoachWithTeam();
        String CoachesStr = ClientController.connectToServer("TeamManagementApplication", "getAllTeamRolesThatArentCoachWithTeam");

        List<String> Coaches = Arrays.asList(CoachesStr.split(";"));

        coachCB.getItems().clear();
        for (String coachUserName : Coaches) {
            coachCB.getItems().add(coachUserName);
        }
    }

    @FXML
    public void activateButton(ActionEvent actionEvent) throws IOException {
        String teamName = (String) teamNameCB.getValue();

        String coachUserName = (String) coachCB.getValue();

        ObservableList<String> selectedPlayers = playersListView.getSelectionModel().getSelectedItems();
        HashSet<String> players = new HashSet<>(selectedPlayers);
        String playersStr = new String();
        for (String p : players) {
            playersStr += p + ";";
        }
        String field = fieldName.getText();

        if (teamName == null) {
            alertError("Please choose a team name.");
        } else if (teamName.contains(";")) {
            alertError("Team Name cannot contain: ;");
        } else if (selectedPlayers == null || selectedPlayers.size() < 11) {
            alertError("Please choose at least 11 players.");
        } else if (coachUserName == null) {
            alertError("Please choose a coach.");
        } else if (field == null || field.equals("")) {
            alertError("Please insert a field name.");
        } else if (field.contains(";")) {
            alertError("Field name cannot contain: ;");
        } else if (players == null || players.size() < 11) {
            alertError("Please choose players.");
        } else {
            //String message = tMApp.makeTeamActive(userName, teamName, playersStr, coachUserName, field);
            String message = ClientController.connectToServer("TeamManagementApplication", "makeTeamActive", userName, teamName, playersStr, coachUserName, field);

            if (message.equals("this team is not approved by RFA")) {
                alertError("This team was not approved by the RFA.");
            } else if (message.equals("Field name already exists")){
                alertError("This field name already exists, please choose a different team name.");
            }
            else {
                changeToTeamManagementScene(actionEvent);
                alertInformation("Congratulations! Your new team has been activated.");
            }
        }
    }


    private void alertError(String message) {
        Alert chooseFile = new Alert(Alert.AlertType.ERROR);
        chooseFile.setHeaderText("Error");
        chooseFile.setContentText(message);
        chooseFile.show();
    }

    private void alertInformation(String message) {
        Alert chooseFile = new Alert(Alert.AlertType.INFORMATION);
        chooseFile.setContentText(message);
        chooseFile.show();
    }

    public void closeHandling(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent calcRoot = loader.load();
        HomePageController controller = loader.getController();
        controller.initHomePage(userName,role);
        controller.closeHandling(mouseEvent);
    }
}
