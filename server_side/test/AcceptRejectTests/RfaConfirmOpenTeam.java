package AcceptRejectTests;

import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Domain.Users.TeamOwner;
import Service.RfaApplication;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

/** RFA confirm Open Group . stems from group opening by group owner -Requirement: 6.1. **/


public class RfaConfirmOpenTeam {

    RfaController rfaController = new RfaController();
    SystemOperationsController operationsController = new SystemOperationsController();

//
//    @Test
//    public void accept() throws Exception {
//        /*****system init*****/
//        operationsController.initSystemObjectsAvital();
//        MainSystem ma = MainSystem.getInstance();
//
//        /*****get RFA *****/
//        Rfa rfa = operationsController.getAllRFA().get(0);
//        SystemManager sm = operationsController.showAllSystemManagers().get(0);
//        //MainSystem.getInstance().loginUsers.add(rfa);
//        List<TeamOwner> allTeamOwners = operationsController.showAllTeamOwner();
//        TeamOwner ilanTeamOwner = allTeamOwners.get(0);
//        /***  teamOner want add new team - add team to  requestsTeam list  ***/
//        ilanTeamOwner.requestNewTeam("hiffa");
//
//        Team newTeam = ilanTeamOwner.getRequestedTeams().get(0);
//        /***  rfa Confirm to open team ***/
//        try {
//            rfaController.answerToRequest(rfa, newTeam, true);
//        } catch (Exception e) {
//            fail();
//        }
//
//        try {
//
//            /***  team not fount in requestedTeams list in team owner  ***/
//            Assert.assertFalse(ilanTeamOwner.getRequestedTeams().contains(newTeam));
//            /***  team fount in approvedTeams list in team owner  ***/
//            Assert.assertTrue(ilanTeamOwner.getApprovedTeams().contains(newTeam));
//        } catch (Exception e) {
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void reject() throws Exception {
//        /*****system init*****/
//        operationsController.deleteSystem();
//        operationsController.initSystemObjectsAvital();
//        MainSystem ma = MainSystem.getInstance();
//
//        /*****get RFA *****/
//        Rfa rfa = operationsController.getAllRFA().get(0);
//        SystemManager sm = operationsController.showAllSystemManagers().get(0);
//
//        List<TeamOwner> allTeamOwners = operationsController.showAllTeamOwner();
//        TeamOwner ilanTeamOwner = allTeamOwners.get(0);
//        /***  teamOner want add new team - add team to  requestsTeam list  ***/
//        ilanTeamOwner.requestNewTeam("hiffa");
//        Team newTeam = ilanTeamOwner.getRequestedTeams().get(0);
//        /***  rfa deny to open team ***/
//        try {
//            rfaController.answerToRequest(rfa, newTeam, false);
//        } catch (Exception e) {
//            fail();
//        }
//
//        try {
//
//            /***  team not fount in requestedTeams list in team owner  ***/
//            Assert.assertFalse(ilanTeamOwner.getRequestedTeams().contains(newTeam));
//        } catch (Exception e) {
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//    }
}