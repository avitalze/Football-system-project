import Domain.Enums.TeamManagerPermissions;
import Domain.Events.Event;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.Player;
import Domain.Users.Referee;
import Domain.Users.TeamRole;
import Stubs.TeamStubOr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;

public class TeamOwnerTeam {

    //Team t= new Team("name",null);
    MainSystem ms = MainSystem.getInstance();
    Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOYossi = new TeamRole(yossi);
    Fan moshe = new Fan(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan david = new Fan(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan simchon = new Fan(ms, "Simchon Hamelech", "0541235678","Simchon@gmail.com", "SimchonHamelech", "Simchon123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOsimchon = new TeamRole(simchon);
    Team team= new Team();

    public TeamOwnerTeam() throws ParseException {
    }



    /**or**/
    @Test
    public void subscribeTeamManagerTest() {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        HashSet<TeamManagerPermissions> per = new HashSet<>();
        per.add(TeamManagerPermissions.addRemoveEditPlayer);


        try {
            TeamRole tMDavid = tOYossi.getTeamOwner().subscribeTeamManager(david, team, per);
            Assert.assertEquals(tMDavid.getTeamManager(), team.getTeamManager());
            Assert.assertTrue(tMDavid.getTeamManager()!= null);
            Assert.assertTrue(tMDavid.getTeamManager().getTeam().equals(team));

            tOYossi.getTeamOwner().subscribeTeamManager(tMDavid, team, per);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("Already Team Manager of this team", e.getMessage());
        }


    }

    /**or**/
    @Test
    public void removeTeamManagerTest() {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        HashSet<TeamManagerPermissions> per = new HashSet<>();
        per.add(TeamManagerPermissions.addRemoveEditPlayer);

        try {
            tOYossi.getTeamOwner().removeTeamManager(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        TeamRole tOMoshe = null;
        try {
            tOMoshe = tOYossi.getTeamOwner().subscribeTeamManager(moshe, team,per);
            Assert.assertEquals(tOMoshe.getTeamManager(), team.getTeamManager());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            tOYossi.getTeamOwner().removeTeamManager(tOMoshe.getTeamManager(), team);
            Assert.assertNull(tOMoshe.getTeamManager());
            Assert.assertTrue(team.getTeamManager()==null);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    /**or**/
    @Test
    public void makeTeamActive() throws ParseException {
        tOYossi.becomeTeamOwner();
        try {
            tOYossi.getTeamOwner().makeTeamActive(team,null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        HashSet<TeamRole> players= new HashSet<>();
        TeamRole coach= new TeamRole(ms,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(ms,"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player);
            counter++;
        }
        coach.becomeCoach();
        Field field= new Field("fielsName");
        try {
            tOYossi.getTeamOwner().makeTeamActive(team,players,coach,field);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("this team is not approved by RFA", e.getMessage());
        }

        tOYossi.getTeamOwner().getApprovedTeams().add(team);
        try {
            tOYossi.getTeamOwner().makeTeamActive(team,players,coach,field);
            Assert.assertTrue(tOYossi.getTeamOwner().getTeams().contains(team));
            Assert.assertFalse(tOYossi.getTeamOwner().getApprovedTeams().contains(team));
            Assert.assertTrue(team.isActive());
            Assert.assertTrue(team.getCoach().equals(coach.getCoach()));
            Assert.assertTrue(ms.getActiveTeams().contains(team));

        } catch (Exception e) {
            Assert.fail();
        }


    }

    /**or**/
    @Test
    public void deleteTeamByTeamOwner() throws ParseException {
        tOYossi.becomeTeamOwner();
        try {
            tOYossi.getTeamOwner().deleteTeam(null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        HashSet<TeamRole> players= new HashSet<>();
        TeamRole coach= new TeamRole(ms,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(ms,"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player);
            counter++;
        }
        coach.becomeCoach();
        Field field= new Field("fielsName");
        tOYossi.getTeamOwner().getApprovedTeams().add(team);
        try {
            team.addTeamOwner(tOYossi.getTeamOwner());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tOYossi.getTeamOwner().makeTeamActive(team,players,coach,field);
            TeamRole TOmoshe= tOYossi.getTeamOwner().subscribeTeamOwner(moshe,team);
            Assert.assertTrue(tOYossi.getTeamOwner().getMySubscriptions().size()==1);
            tOYossi.getTeamOwner().deleteTeam(team);
            Assert.assertEquals(0,tOYossi.getTeamOwner().getMySubscriptions().size());
            Assert.assertTrue(tOYossi.getTeamOwner().getDeletedTeams().contains(team));
            Assert.assertTrue(TOmoshe.getTeamOwner().getDeletedTeams().contains(team));
            Assert.assertFalse(ms.getActiveTeams().contains(team));

            //check the notification was sent
            Assert.assertEquals(1,tOYossi.getTeamOwner().getNotificationsList().size());
            Assert.assertEquals(1,TOmoshe.getTeamOwner().getNotificationsList().size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**or**/
    @Test
    public void reopenTeam() throws ParseException {
        tOYossi.becomeTeamOwner();

        try {
            tOYossi.getTeamOwner().reopenTeam(null,null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        HashSet<Player> players= new HashSet<>();
        TeamRole coach= new TeamRole(ms,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(ms,"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            players.add(player.getPlayer());
            counter++;
        }
        coach.becomeCoach();
        Field field= new Field("fielsName");
        try {
            tOYossi.getTeamOwner().reopenTeam(team,players,coach.getCoach(),field);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("the team was not deleted", e.getMessage());
        }

        tOYossi.getTeamOwner().getDeletedTeams().add(team);
        try {
            team.addTeamOwner(tOYossi.getTeamOwner());
            tOYossi.getTeamOwner().subscribeTeamOwner(moshe,team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            Assert.assertEquals(2,team.getTeamOwners().size());

            tOYossi.getTeamOwner().reopenTeam(team,players,coach.getCoach(),field);

            Assert.assertTrue(tOYossi.getTeamOwner().getTeams().contains(team));
            Assert.assertFalse(tOYossi.getTeamOwner().getDeletedTeams().contains(team));
            Assert.assertTrue(team.isActive());
            Assert.assertTrue(team.getTeamOwners().contains(tOYossi.getTeamOwner()));
            Assert.assertEquals(1,team.getTeamOwners().size());
            Assert.assertTrue(ms.getActiveTeams().contains(team));
            //check the notification was sent
            Assert.assertEquals(1,tOYossi.getTeamOwner().getNotificationsList().size());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
