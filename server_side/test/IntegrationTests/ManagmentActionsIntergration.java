package IntegrationTests;

import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.Player;
import Domain.Users.TeamRole;
import Stubs.ManagmentActionsStubOr;
import Stubs.TeamStubOr;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ManagmentActionsIntergration {
    MainSystem ms = MainSystem.getInstance();
    Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOYossi = new TeamRole(yossi);
    Fan moshe = new Fan(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan david = new Fan(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan simchon = new Fan(ms, "Simchon Hamelech", "0541235678","Simchon@gmail.com", "SimchonHamelech", "Simchon123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole tOsimchon = new TeamRole(simchon);

    Team team= new Team();
    ManagmentActionsStubOr managmentActions = new ManagmentActionsStubOr();

    public ManagmentActionsIntergration() throws ParseException {
    }

    /**or*
     * Integration - ManagmentActions-Team-TeamOwner
     */
    @Test
    public void subscribeTeamOwnerTest() {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());
        try {
            managmentActions.subscribeTeamOwner(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            managmentActions.subscribeTeamOwner(tOYossi,team);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("Already team Owner of this team", e.getMessage());
            Assert.assertEquals(1, tOYossi.getTeamOwner().getTeams().size());
            Assert.assertEquals(1, team.getTeamOwners().size());
        }

        TeamRole tOMoshe = null;
        try {
            tOMoshe = managmentActions.subscribeTeamOwner(moshe, team);
            Assert.assertEquals(2, team.getTeamOwners().size());
            Assert.assertTrue(tOMoshe.getTeamOwner().getTeams().contains(team));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    /**or*
     * Integration - ManagmentActions-Team-TeamOwner
     */
    @Test
    public void removeTeamOwnerTest() {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            managmentActions.removeTeamOwner(null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }
        TeamRole tOMoshe = null;
        try {
            tOMoshe = managmentActions.subscribeTeamOwner(moshe, team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(2, team.getTeamOwners().size());
        try {
            managmentActions.removeTeamOwner(tOMoshe.getTeamOwner(), team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(1, team.getTeamOwners().size());
        Assert.assertEquals(0, tOMoshe.getTeamOwner().getTeams().size());
    }

    /**or*
     * Integration - ManagmentActions-Team-Coach
     */
    @Test
    public void removeAndReplaceCoach() throws Exception {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        try {
            managmentActions.removeAndReplaceCoach(null,null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        TeamRole coachNow = new TeamRole(moshe);
        coachNow.becomeCoach();
        team.addCoach(coachNow.getCoach());
        coachNow.getCoach().setCoachTeam(team);

        TeamRole coachNew = new TeamRole(david);
        coachNew.becomeCoach();


        try {
            managmentActions.removeAndReplaceCoach(coachNow.getCoach(), coachNew, "main", team);
            Assert.assertEquals(null, coachNow.getCoach().getCoachTeam());
            Assert.assertEquals(team, coachNew.getCoach().getCoachTeam());
            Assert.assertEquals(coachNew.getCoach(), team.getCoach());
        } catch (Exception e) {
            Assert.fail();
        }

    }

    /**or*
     * Integration - ManagmentActions-Team-Coach
     */
    @Test
    public void editCoachRole() throws Exception {
        tOYossi.becomeTeamOwner();

        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());
        TeamRole coachNow = new TeamRole(moshe);
        coachNow.becomeCoach();
        team.addCoach(coachNow.getCoach());
        coachNow.getCoach().setCoachTeam(team);

        managmentActions.editCoachRole(coachNow.getCoach(),"newString");
        Assert.assertEquals("newString",coachNow.getCoach().getRoleAtTeam());
    }

    /**or*
     * Integration - ManagmentActions-Team-Player
     */
    @Test
    public void addPlayer() {
        tOYossi.becomeTeamOwner();
        tOsimchon.becomeTeamOwner();

        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        TeamRole teamRoleDavid = new TeamRole(david);
        teamRoleDavid.becomePlayer();
        try {
            managmentActions.addPlayer(teamRoleDavid, "defense", team);
            Assert.assertEquals(team, teamRoleDavid.getPlayer().getTeam());
            Assert.assertTrue(team.getPlayers().contains(teamRoleDavid.getPlayer()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }


        try {
            managmentActions.addPlayer(null,null,null);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }


    }

    /**or*
     * Integration - ManagmentActions-Team-Player
     */
    @Test
    public void removePlayer() {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        TeamRole teamRoleDavid = new TeamRole(david);
        teamRoleDavid.becomePlayer();

        TeamRole mosheTR= new TeamRole(moshe);
        mosheTR.becomePlayer();
        try {
            //12!!!!!!!!!!!
            //TeamRole coach= new TeamRole(ms,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
            int counter=0;
            while(counter<11){
                TeamRole player= new TeamRole(ms,"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
                player.becomePlayer();
               managmentActions.addPlayer(player,"roles",team);
                counter++;
            }
           // coach.becomeCoach();
          //  Field field= new Field("fielsName");

            managmentActions.addPlayer(teamRoleDavid,"role",team);
            managmentActions.addPlayer(mosheTR,"different role",team);
            Assert.assertTrue(team.getPlayers().contains(teamRoleDavid.getPlayer()));
            managmentActions.removePlayer(teamRoleDavid.getPlayer(),team);
            Assert.assertFalse(team.getPlayers().contains(teamRoleDavid.getPlayer()));
            Assert.assertNull(teamRoleDavid.getPlayer().getTeam());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    /**or*
     * Integration - ManagmentActions-Team-Player
     */
    @Test
    public void editPlayerRole() {
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());
        TeamRole player = new TeamRole(moshe);
        player.becomePlayer();
        try {
            managmentActions.addPlayer(player,"role",team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            managmentActions.editPlayerRole(player.getPlayer(),"newString");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        Assert.assertEquals("newString",player.getPlayer().getRoleAtField());
    }

    /**or*
     * Integration - ManagmentActions-Team-Field
     */
    @Test
    public void removeAndReplaceField(){
        tOYossi.becomeTeamOwner();
        tOYossi.getTeamOwner().addNewTeam(team);
        team.getTeamOwners().add(tOYossi.getTeamOwner());

        Field field = new Field("Beer Sheva Field");
        team.setField(field);
        field.addTeam(team);
        Field field2 = new Field("Beer Sheeeeeeeva Field");

        try {
            managmentActions.removeAndReplaceField(field, field2, team);
            Assert.assertTrue(field2.getTeam().equals(team));
            Assert.assertFalse(field.getTeam().equals(team));
            Assert.assertEquals(field2,team.getField());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**or*
     * Integration - ManagmentActions-Field
     */
    @Test
    public void editFieldName() {
        try {
            Field f= new Field("name");
            managmentActions.editFieldName(f, "changedName");
            Assert.assertEquals("changedName",f.getNameOfField());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**or*
     * Integration - ManagmentActions-Team-BudgetControl
     */
    @Test
    public void addIncome() {

        try {
            managmentActions.addIncomeToTeam(team,"something",230);
            Assert.assertEquals(230,team.getBudgetControl().getBalance());
            Assert.assertEquals(1,team.getBudgetControl().getIncomeAndExpenses().size());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**or*
     * Integration - ManagmentActions-Team-BudgetControl
     */
    @Test
    public void addExpense() {
        try {
            managmentActions.addIncomeToTeam(team,"income",500);
            managmentActions.addExpenseToTeam(team,"something",230);
            Assert.assertEquals(270,team.getBudgetControl().getBalance());
            Assert.assertEquals(2,team.getBudgetControl().getIncomeAndExpenses().size());
        } catch (Exception e) {
            Assert.fail();
        }

    }
}
