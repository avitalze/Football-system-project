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

public class RemoveTeamOwnerTest {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void accept1() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
       // team owner to remove
        TeamRole arnoldTeamOwner = (TeamRole) systemOperationsController.getUserByUserName("Arnold");
        TeamRole ilan = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilan.getTeamOwner()).get(0);
        teamManagementController.removeTeamOwner(ilan, arnoldTeamOwner.getTeamOwner(), t1Macabi);
        assertFalse(t1Macabi.getTeamOwners().contains(arnoldTeamOwner.getTeamOwner()));
        assertFalse(arnoldTeamOwner.getTeamOwner().getTeams().contains(t1Macabi));
        // all his subs erased as well
        assertTrue(arnoldTeamOwner.getTeamOwner().getMySubscriptions().size() == 0);
        TeamRole mosheTeamManager = (TeamRole) systemOperationsController.getUserByUserName("Moshe");
        Assert.assertNull(mosheTeamManager.getTeamManager());
        Assert.assertNull(t1Macabi.getTeamManager());
    }
    /***************SAME TESTS EXCEPT THIS TIME WITH TEAM MANAGER****************/
    @Test
    public void accept2() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        // team owner to remove
        TeamRole arminTeamOwner = (TeamRole) systemOperationsController.getUserByUserName("Armin");
        TeamRole moshe = (TeamRole)systemOperationsController.getUserByUserName("Moshe");
        Team t1Macabi = moshe.getTeamManager().getTeam();
        teamManagementController.removeTeamOwner(moshe, arminTeamOwner.getTeamOwner(), t1Macabi);
        assertFalse(t1Macabi.getTeamOwners().contains(arminTeamOwner.getTeamOwner()));
        assertFalse(arminTeamOwner.getTeamOwner().getTeams().contains(t1Macabi));
    }
    // team manager without permission
    @Test
    public void reject() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        // team owner to remove
        TeamRole aviTeamOwner = (TeamRole) systemOperationsController.getUserByUserName("Avi");
        TeamRole david = (TeamRole)systemOperationsController.getUserByUserName("David");
        Team t1Macabi = david.getTeamManager().getTeam();

        try {
            teamManagementController.removeTeamOwner(david, aviTeamOwner.getTeamOwner(), t1Macabi);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }
    }

}
