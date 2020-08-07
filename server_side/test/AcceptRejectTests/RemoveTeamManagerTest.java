package AcceptRejectTests;
import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.LeagueManagment.Team;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RemoveTeamManagerTest {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void accept1() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        // team manager to remove
        TeamRole davidTeamManager = (TeamRole) systemOperationsController.getUserByUserName("David");
        TeamRole avi = (TeamRole)systemOperationsController.getUserByUserName("Avi");
        Team t2 = teamManagementController.getAllMyTeams(avi.getTeamOwner()).get(0);
        teamManagementController.removeTeamManager(avi.getTeamOwner(), davidTeamManager.getTeamManager(), t2);
        Assert.assertNull(t2.getTeamManager());
        Assert.assertNull(davidTeamManager.getTeamManager());
    }

    @Test
    public void reject1() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        // team manager to remove
        TeamRole mosheTeamManager = (TeamRole) systemOperationsController.getUserByUserName("Moshe");
        TeamRole avi = (TeamRole)systemOperationsController.getUserByUserName("Avi");
        Team t2 = teamManagementController.getAllMyTeams(avi.getTeamOwner()).get(0);
        try {
            //moshe not team manager of this team
            teamManagementController.removeTeamManager(avi.getTeamOwner(), mosheTeamManager.getTeamManager(), t2);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }

}
