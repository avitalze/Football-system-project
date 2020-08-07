import Domain.LeagueManagment.Field;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.TeamRole;
import Stubs.ManagmentActionsStubOr;
import Stubs.TeamStubOr;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class ManagmentActionsStubOrTest extends ManagmentActionsStubOr {
    MainSystem ms = MainSystem.getInstance();
    Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOYossi = new TeamRole(yossi);
    Fan moshe = new Fan(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan david = new Fan(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan simchon = new Fan(ms, "Simchon Hamelech", "0541235678","Simchon@gmail.com", "SimchonHamelech", "Simchon123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOsimchon = new TeamRole(simchon);


    ManagmentActionsStubOr stub= new ManagmentActionsStubOr();

    public ManagmentActionsStubOrTest() throws ParseException {
    }

    /**adi+or**/
    @Test
    public void subscribeTeamOwnerTest() {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());
        try {
            stub.subscribeTeamOwner(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            stub.subscribeTeamOwner(tOYossi,teamForTest);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("Already team Owner of this team", e.getMessage());
        }

        TeamRole tOMoshe = null;
        try {
            tOMoshe = stub.subscribeTeamOwner(moshe, teamForTest);
            Assert.assertEquals(2, teamForTest.getTeamOwners().size());
            Assert.assertTrue(tOMoshe.getTeamOwner().getTeams().contains(teamForTest));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    /**adi+or**/
    @Test
    public void removeTeamOwnerTest() {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            stub.removeTeamOwner(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        TeamRole tOMoshe = null;
        try {
            tOMoshe = stub.subscribeTeamOwner(moshe, teamForTest);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(2, teamForTest.getTeamOwners().size());
        try {
           stub.removeTeamOwner(tOMoshe.getTeamOwner(), teamForTest);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(2, teamForTest.getTeamOwners().size());
    }

    /**adi+or**/
    @Test
    public void removeAndReplaceCoach() throws Exception {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            stub.removeAndReplaceCoach(null,null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        TeamRole coachNow = new TeamRole(moshe);
        coachNow.becomeCoach();
        teamForTest.addCoach(coachNow.getCoach());
        coachNow.getCoach().setCoachTeam(teamForTest);

        TeamRole coachNew = new TeamRole(david);
        coachNew.becomeCoach();


        try {
            stub.removeAndReplaceCoach(coachNow.getCoach(), coachNew, "main", teamForTest);
            Assert.assertEquals(null, coachNow.getCoach().getCoachTeam());
            Assert.assertEquals(teamForTest, coachNew.getCoach().getCoachTeam());
        } catch (Exception e) {
            Assert.fail();
        }

    }

    /**adi+or**/
    @Test
    public void editCoachRole() throws Exception {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());
        TeamRole coachNow = new TeamRole(moshe);
        coachNow.becomeCoach();
        teamForTest.addCoach(coachNow.getCoach());
        coachNow.getCoach().setCoachTeam(teamForTest);

        stub.editCoachRole(coachNow.getCoach(),"newString");
        Assert.assertEquals("newString",coachNow.getCoach().getRoleAtTeam());
    }

    /**adi+or**/
    @Test
    public void addPlayer() {
        tOYossi.becomeTeamOwner();
        tOsimchon.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        TeamStubOr teamForTest1= new TeamStubOr("hapoel beer-sheva",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        tOsimchon.getTeamOwner().addNewTeam(teamForTest1);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());
        teamForTest1.getTeamOwners().add(tOsimchon.getTeamOwner());

        TeamRole teamRoleDavid = new TeamRole(david);
        teamRoleDavid.becomePlayer();
        try {
            stub.addPlayer(teamRoleDavid, "defense", teamForTest);
            Assert.assertEquals(teamForTest, teamRoleDavid.getPlayer().getTeam());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            stub.addPlayer(teamRoleDavid, "defense", teamForTest);
            fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("This player is already in another team",e.getMessage());
        }

        try {
            stub.addPlayer(null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }


    }

    /**adi+or**/
    @Test
    public void removePlayer() {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
           stub.removePlayer(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

    }

    /**adi+or**/
    @Test
    public void editPlayerRole() {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());
        TeamRole player = new TeamRole(moshe);
        player.becomePlayer();
        try {
            teamForTest.addPlayer(player.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        player.getPlayer().setPlayerTeam(teamForTest);

        try {
           stub.editPlayerRole(player.getPlayer(),"newString");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        Assert.assertEquals("newString",player.getPlayer().getRoleAtField());
    }

    /**adi+or**/
    @Test
    public void removeAndReplaceField(){

        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        Field field = new Field("Beer Sheva Field");
        teamForTest.setField(field);
        Field field2 = new Field("Beer Sheeeeeeeva Field");

        try {
            stub.removeAndReplaceField(null, field2, teamForTest);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

    /**adi+or**/
    @Test
    public void editFieldName() {
        try {
            stub.editFieldName(null, null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

    /**or**/
    @Test
    public void addIncome() {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            stub.addIncomeToTeam(teamForTest,"something",230);
        } catch (Exception e) {
            Assert.fail();
        }

        try {
            stub.addIncomeToTeam(null,"comething",230);
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }
    /**or**/
    @Test
    public void addExpense() {
        tOYossi.becomeTeamOwner();
        TeamStubOr teamForTest= new TeamStubOr("hapoel raanana",false);
        tOYossi.getTeamOwner().addNewTeam(teamForTest);
        teamForTest.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            stub.addExpenseToTeam(teamForTest,"something",230);
        } catch (Exception e) {
            Assert.fail();
        }

        try {
            stub.addExpenseToTeam(null,"comething",230);
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
    }

}