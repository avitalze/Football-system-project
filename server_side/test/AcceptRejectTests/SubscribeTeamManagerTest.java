package AcceptRejectTests;
import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.Enums.TeamManagerPermissions;
import Domain.LeagueManagment.Team;
import Domain.Users.Fan;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubscribeTeamManagerTest {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();
    // subscribe a fan to team manager
    @Test
    public void accept1() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        // just a fan
        Fan tamar = (Fan)systemOperationsController.getUserByUserName("Tamar");
        HashSet<TeamManagerPermissions> per = new HashSet<>();

        TeamRole ilan = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilan.getTeamOwner()).get(0);
        TeamRole newTeamManager = teamManagementController.subscribeTeamManager(ilan.getTeamOwner(), tamar, t1Macabi, per);
        assertEquals(t1Macabi.getTeamManager(), newTeamManager.getTeamManager());
        assertEquals(newTeamManager.getTeamManager().getTeam(), t1Macabi);
    }

    // same except someone who is already TeamRole
    @Test
    public void alternative1() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ben = (TeamRole)systemOperationsController.getUserByUserName("Ben");
        HashSet<TeamManagerPermissions> per = new HashSet<>();

        TeamRole ilan = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilan.getTeamOwner()).get(0);
        TeamRole newTeamManager = teamManagementController.subscribeTeamManager(ilan.getTeamOwner(), ben, t1Macabi, per);
        assertEquals(t1Macabi.getTeamManager(), newTeamManager.getTeamManager());
        assertEquals(newTeamManager.getTeamManager().getTeam(), t1Macabi);
    }

    @Test
    public void reject1() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole moshe = (TeamRole)systemOperationsController.getUserByUserName("Moshe");
        HashSet<TeamManagerPermissions> per = new HashSet<>();

        TeamRole ilan = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilan.getTeamOwner()).get(0);
        try {
            //already team manager
            TeamRole newTeamManager = teamManagementController.subscribeTeamManager(ilan.getTeamOwner(), moshe, t1Macabi, per);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
}
