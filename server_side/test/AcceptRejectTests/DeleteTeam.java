package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeleteTeam {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void acceptDeleteTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        teamManagementController.deleteTeam(ilanTeamOwner.getTeamOwner(), t1Macabi);

        Assert.assertFalse(t1Macabi.isActive());
        Assert.assertFalse(MainSystem.getInstance().getActiveTeams().contains(t1Macabi));
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getDeletedTeams().contains(t1Macabi));
    }
    @Test
    public void rejectDeleteTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);
        teamManagementController.deleteTeam(ilanTeamOwner.getTeamOwner(), t1Macabi);

        try {
            //cant delete team that already deleted
            teamManagementController.deleteTeam(ilanTeamOwner.getTeamOwner(), t1Macabi);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
}
