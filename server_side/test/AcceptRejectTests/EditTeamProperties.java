package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.LeagueManagment.Team;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EditTeamProperties {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void acceptEditCoachRole() throws Exception {
        systemOperationsController.initSystemObjectsAdi();
        TeamRole haimCoach = (TeamRole)systemOperationsController.getUserByUserName("Haim");
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");

        teamManagementController.editCoachRole(ilanTeamOwner, haimCoach.getCoach(), "new role");
        assertEquals(haimCoach.getCoach().getRoleAtTeam(), "new role");
    }
    @Test
    public void rejectEditCoachRole() throws Exception {
        systemOperationsController.initSystemObjectsAdi();
        TeamRole moshe = (TeamRole)systemOperationsController.getUserByUserName("Moshe");
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        try {
            //moshe not a coach
            teamManagementController.editCoachRole(ilanTeamOwner, moshe.getCoach(), "");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }
    @Test
    public void acceptEditPlayerRole() throws Exception {
        systemOperationsController.initSystemObjectsAdi();
        TeamRole player = (TeamRole)systemOperationsController.getUserByUserName("player:macabi0d");
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");

        teamManagementController.editPlayerRole(ilanTeamOwner, player.getPlayer(), "new role");
        assertEquals(player.getPlayer().getRoleAtField(), "new role");
    }
    @Test
    public void rejectEditPlayerRole() throws Exception {
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ben = (TeamRole)systemOperationsController.getUserByUserName("Ben");
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        try {
            //ben not a player
            teamManagementController.editPlayerRole(ilanTeamOwner, ben.getPlayer(), "");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }
    @Test
    public void acceptEditFieldName() throws Exception {
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        teamManagementController.editFieldName(ilanTeamOwner, t1Macabi, "new field");
        assertEquals(t1Macabi.getField().getNameOfField(), "new field");
    }
}
