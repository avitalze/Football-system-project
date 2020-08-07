package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Player;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class ReopenTeam {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void acceptReopenTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        teamManagementController.deleteTeam(ilanTeamOwner.getTeamOwner(), t1Macabi);

        HashSet<Player> players= new HashSet<>();
        TeamRole moshe = (TeamRole)systemOperationsController.getUserByUserName("Moshe");
        moshe.becomeCoach();
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(MainSystem.getInstance(),"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player.getPlayer());
            counter++;
        }
        teamManagementController.reopenTeam(ilanTeamOwner.getTeamOwner(), t1Macabi, players, moshe.getCoach(), "fieldd" );
        Assert.assertTrue(t1Macabi.isActive());
        Assert.assertTrue(MainSystem.getInstance().getActiveTeams().contains(t1Macabi));
        Assert.assertFalse(ilanTeamOwner.getTeamOwner().getDeletedTeams().contains(t1Macabi));
    }
    @Test
    public void rejectReopenTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);
        HashSet<Player> players= new HashSet<>();
        TeamRole moshe = (TeamRole)systemOperationsController.getUserByUserName("Moshe");
        moshe.becomeCoach();
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(MainSystem.getInstance(),"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player.getPlayer());
            counter++;
        }

        try {
            //cant open team that already open
            teamManagementController.reopenTeam(ilanTeamOwner.getTeamOwner(), t1Macabi, players, moshe.getCoach(), "fieldd" );
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
}
