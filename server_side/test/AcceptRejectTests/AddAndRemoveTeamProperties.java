package AcceptRejectTests;
import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.LeagueManagment.Team;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class AddAndRemoveTeamProperties {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void acceptRemoveAndReplaceCoach() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole haimCoachToRemove = (TeamRole)systemOperationsController.getUserByUserName("Haim");
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        LinkedList<TeamRole> teamRoles = teamManagementController.getAllTeamRolesThatArentCoachWithTeamObject();
        TeamRole coachToAdd = teamRoles.get(0);
        teamManagementController.removeAndReplaceCoach(ilanTeamOwner, haimCoachToRemove.getCoach(), coachToAdd,"", t1Macabi);

        assertEquals(t1Macabi.getCoach(), coachToAdd.getCoach());
        assertEquals(coachToAdd.getCoach().getCoachTeam(), t1Macabi);
    }
    @Test
    public void rejectRemoveAndReplaceCoach() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole mark = (TeamRole)systemOperationsController.getUserByUserName("Mark");
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        LinkedList<TeamRole> teamRoles = teamManagementController.getAllTeamRolesThatArentCoachWithTeamObject();
        TeamRole coachToAdd = teamRoles.get(0);
        try {
            //mark isn't coach of this team
            teamManagementController.removeAndReplaceCoach(ilanTeamOwner, mark.getCoach(), coachToAdd,"", t1Macabi);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
    @Test
    public void acceptAddPlayer() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        LinkedList<TeamRole> teamRoles = teamManagementController.getAllTeamRolesThatArentPlayerWithTeamObject();
        TeamRole playerToAdd = teamRoles.get(3);
        teamManagementController.addPlayer(ilanTeamOwner, playerToAdd,"", t1Macabi);

        Assert.assertTrue(t1Macabi.getPlayers().contains(playerToAdd.getPlayer()));
        assertEquals(playerToAdd.getPlayer().getTeam(), t1Macabi);
    }
    @Test
    public void rejectAddPlayer() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        TeamRole playerToAdd = (TeamRole)systemOperationsController.getUserByUserName("player:macabi0d");
        try {
            //player already part of team
            teamManagementController.addPlayer(ilanTeamOwner, playerToAdd,"", t1Macabi);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
    @Test
    public void acceptRemovePlayer() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole aviTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Avi");
        Team t2 = teamManagementController.getAllMyTeams(aviTeamOwner.getTeamOwner()).get(0);

        TeamRole playerToRemove = (TeamRole)systemOperationsController.getUserByUserName("player:hapoel0d");

        teamManagementController.removePlayer(aviTeamOwner, playerToRemove.getPlayer(), t2);
        Assert.assertNull(playerToRemove.getPlayer().getTeam());
    }
    @Test
    public void rejectRemovePlayer() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole aviTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Avi");
        Team t2 = teamManagementController.getAllMyTeams(aviTeamOwner.getTeamOwner()).get(0);

        TeamRole playerToRemove = (TeamRole)systemOperationsController.getUserByUserName("player:macabi0d");

        try {
            //only 11 players on team, cant remove
            teamManagementController.removePlayer(aviTeamOwner, playerToRemove.getPlayer(), t2);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
    @Test
    public void acceptRemoveAndReplaceField() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        teamManagementController.removeAndReplaceField(ilanTeamOwner,"new field",t1Macabi);

        assertEquals(t1Macabi.getField().getNameOfField(), "new field");
    }


}
