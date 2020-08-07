import Domain.*;
import Domain.Enums.TeamManagerPermissions;
import Domain.Events.Event;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.Users.*;
import Stubs.TeamStubOr;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TeamOwnerTest {
    //Team t= new Team("name",null);
    MainSystem ms = MainSystem.getInstance();
    Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOYossi = new TeamRole(yossi);
    Fan moshe = new Fan(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan david = new Fan(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan simchon = new Fan(ms, "Simchon Hamelech", "0541235678","Simchon@gmail.com", "SimchonHamelech", "Simchon123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOsimchon = new TeamRole(simchon);

    public TeamOwnerTest() throws ParseException {
    }



    /**adi+or**/
    @Test
    public void subscribeTeamManagerTest() throws Exception {
        HashSet<TeamManagerPermissions> per = new HashSet<>();
        per.add(TeamManagerPermissions.addRemoveEditPlayer);

        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            tOYossi.getTeamOwner().subscribeTeamManager(null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        try {
            TeamRole tMDavid = tOYossi.getTeamOwner().subscribeTeamManager(david, teamForTest, per);
            Assert.assertEquals(tMDavid.getTeamManager(), teamForTest.getTeamManager());
            tOYossi.getTeamOwner().subscribeTeamManager(tMDavid, teamForTest, per);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("Already Team Manager of this team", e.getMessage());
        }


    }

    /**adi+or**/
    @Test
    public void removeTeamManagerTest() {
        HashSet<TeamManagerPermissions> per = new HashSet<>();
        per.add(TeamManagerPermissions.addRemoveEditPlayer);

        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            tOYossi.getTeamOwner().removeTeamManager(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        TeamRole tOMoshe = null;
        try {
            tOMoshe = tOYossi.getTeamOwner().subscribeTeamManager(moshe, teamForTest,per);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(tOMoshe.getTeamManager(), teamForTest.getTeamManager());
        try {
            tOYossi.getTeamOwner().removeTeamManager(tOMoshe.getTeamManager(), teamForTest);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertNull(tOMoshe.getTeamManager());
    }



    /**or**/
    @Test
    public void requestNewTeam() throws ParseException {
        Rfa rfa= new Rfa(ms,"RFA","0543150912","rfa@gmail.com","rfa123","rfa123",MainSystem.birthDateFormat.parse("01-12-1995"));
        TeamRole teamOwner = new TeamRole(ms,"michael","0522150912","teamO@gmail.com","owner123","owner123",MainSystem.birthDateFormat.parse("09-12-1995"));
        teamOwner.becomeTeamOwner();
        try {
            teamOwner.getTeamOwner().requestNewTeam("hapoel Beer Sheva");
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertTrue(rfa.getTeamRequests().size()==1);
        Assert.assertTrue(teamOwner.getTeamOwner().getRequestedTeams().size()==1);
        try {
            teamOwner.getTeamOwner().requestNewTeam("hapoel Beer Sheva");
        } catch (Exception e) {
            Assert.assertEquals("team name not unique, already exist in system",e.getMessage());
        }

    }

    /**or**/
    @Test
    public void makeTeamActive() throws ParseException {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        try {
            tOYossi.getTeamOwner().makeTeamActive(teamForTest,null,null,null);
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
            tOYossi.getTeamOwner().makeTeamActive(teamForTest,players,coach,field);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("this team is not approved by RFA", e.getMessage());
        }

        tOYossi.getTeamOwner().getApprovedTeams().add(teamForTest);
        try {
            tOYossi.getTeamOwner().makeTeamActive(teamForTest,players,coach,field);
            Assert.assertTrue(tOYossi.getTeamOwner().getTeams().contains(teamForTest));
            Assert.assertFalse(tOYossi.getTeamOwner().getApprovedTeams().contains(teamForTest));

        } catch (Exception e) {
            Assert.fail();
        }


    }

    /**or**/
    @Test
    public void deleteTeamByTeamOwner() throws Exception {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        TeamStubOr teamForTest2= new TeamStubOr("macabi haifa",false);
        try {
            tOYossi.getTeamOwner().deleteTeam(null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        try {
            tOYossi.getTeamOwner().deleteTeam(teamForTest);
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        Referee ref= new Referee(ms,"ref1","0542150912","ref1@gmail.com","ref101","ref101","qualification",MainSystem.birthDateFormat.parse("01-03-1970"));
        Match m1 = new Match(0,0,teamForTest,teamForTest2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , ref,"17-04-2020 20:00:00");
        teamForTest.getHome().add(m1);
        try {
            tOYossi.getTeamOwner().deleteTeam(teamForTest);
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("cannot delete team with future matches", e.getMessage());
        }
    }

    /**or**/
    @Test
    public void reopenTeam() throws ParseException {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);

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
            tOYossi.getTeamOwner().reopenTeam(teamForTest,players,coach.getCoach(),field);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("the team was not deleted", e.getMessage());
        }

        tOYossi.getTeamOwner().getDeletedTeams().add(teamForTest);
        try {
            tOYossi.getTeamOwner().reopenTeam(teamForTest,players,coach.getCoach(),field);
            Assert.assertTrue(tOYossi.getTeamOwner().getTeams().contains(teamForTest));
            Assert.assertFalse(tOYossi.getTeamOwner().getDeletedTeams().contains(teamForTest));

        } catch (Exception e) {
            Assert.fail();
        }
    }

}
