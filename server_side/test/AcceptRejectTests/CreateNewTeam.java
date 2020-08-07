package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.TeamManagementController;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Player;
import Domain.Users.Rfa;
import Domain.Users.TeamRole;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class CreateNewTeam {
    SystemOperationsController systemOperationsController = new SystemOperationsController();

    @Test
    public void acceptCreateTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Rfa nadav = new Rfa(MainSystem.getInstance(),"nadav","052","nadav@","nadavS", "nadav123",MainSystem.birthDateFormat.parse("06-07-1992"));

        ilanTeamOwner.getTeamOwner().requestNewTeam("beer sheva");
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().checkIfTeamInRequestTeam("beer sheva"));
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getRequestedTeams().size()==1);
        Team t= ilanTeamOwner.getTeamOwner().getRequestedTeams().get(0);
        Assert.assertTrue(Rfa.getTeamRequests().size()==1);
        Assert.assertTrue(Rfa.getTeamRequests().contains(t));
        nadav.answerRequest(t,true);
        Assert.assertTrue(Rfa.getTeamRequests().size()==0);
        Assert.assertFalse(Rfa.getTeamRequests().contains(t));
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getRequestedTeams().size()==0);
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getApprovedTeams().contains(t));

        HashSet<TeamRole> players= new HashSet<>();
        TeamRole coach= new TeamRole(MainSystem.getInstance(),"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(MainSystem.getInstance(),"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player);
            counter++;
        }
        coach.becomeCoach();
        Field field= new Field("fielsName");
        try {
            ilanTeamOwner.getTeamOwner().makeTeamActive(t,players,coach,field);
            Assert.assertTrue(ilanTeamOwner.getTeamOwner().getTeams().contains(ilanTeamOwner));
            Assert.assertFalse(ilanTeamOwner.getTeamOwner().getApprovedTeams().contains(ilanTeamOwner));

        } catch (Exception e) {
            Assert.fail();
        }
    }
    @Test
    public void rejectCreateTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Rfa nadav = new Rfa(MainSystem.getInstance(),"nadav","052","nadav@","nadavS", "nadav123",MainSystem.birthDateFormat.parse("06-07-1992"));

        ilanTeamOwner.getTeamOwner().requestNewTeam("beer sheva");
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().checkIfTeamInRequestTeam("beer sheva"));
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getRequestedTeams().size()==1);
        Team t= ilanTeamOwner.getTeamOwner().getRequestedTeams().get(0);
        Assert.assertTrue(Rfa.getTeamRequests().size()==1);
        Assert.assertTrue(Rfa.getTeamRequests().contains(t));
        nadav.answerRequest(t,true);
        Assert.assertTrue(Rfa.getTeamRequests().size()==0);
        Assert.assertFalse(Rfa.getTeamRequests().contains(t));
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getRequestedTeams().size()==0);
        Assert.assertTrue(ilanTeamOwner.getTeamOwner().getApprovedTeams().contains(t));

        HashSet<TeamRole> players= new HashSet<>();
        TeamRole coach= new TeamRole(MainSystem.getInstance(),"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(MainSystem.getInstance(),"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player);
            counter++;
        }
        coach.becomeCoach();
        Field field= new Field("fielsName");
        Team testTeam = new Team("test");
        //put an unapproved team
        try {
            ilanTeamOwner.getTeamOwner().makeTeamActive(testTeam,players,coach,field);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("this team is not approved by RFA", e.getMessage());
        }
    }
}
