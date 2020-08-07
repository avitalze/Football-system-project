import Domain.*;
import Domain.Enums.TeamManagerPermissions;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.Users.*;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;

public class TeamManagerTest {
    MainSystem ms = MainSystem.getInstance();
    Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123", MainSystem.birthDateFormat.parse("02-11-1996") );
    Team team = new Team();
    HashSet<TeamManagerPermissions> per = new HashSet<>();
    TeamRole tMYossi = new TeamRole(yossi);
    Fan moshe = new Fan(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123", MainSystem.birthDateFormat.parse("02-11-1996"));
    Fan david = new Fan(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123", MainSystem.birthDateFormat.parse("02-11-1996"));

    public TeamManagerTest() throws ParseException {
    }

    /**or**/
    @Test
    public void subscribeTeamOwnerTest(){
        per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
        tMYossi.becomeTeamManager(team, per);

        try {
            TeamRole tOMoshe = tMYossi.getTeamManager().subscribeTeamOwner(moshe, team);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditTeamOwner);
        try {
            TeamRole tOMoshe = tMYossi.getTeamManager().subscribeTeamOwner(moshe, team);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }


    }

    /**or**/
    @Test
    public void removeTeamOwnerTest() throws Exception {
        per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
        tMYossi.becomeTeamManager(team, per);
        TeamRole owner= tMYossi.getTeamManager().subscribeTeamOwner(moshe,team);
        try {
            tMYossi.getTeamManager().removeTeamOwner(owner.getTeamOwner(), team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }


    }
    /**or**/
    @Test
    public void addRemoveCoachTest()  {
        per.add(TeamManagerPermissions.addRemoveEditCoach);
        tMYossi.becomeTeamManager(team, per);
        TeamRole teamRoleMoshe = new TeamRole(moshe);
        TeamRole teamRoleDavid = new TeamRole(david);
        Coach cDavid = new Coach(team, "mainCoach", teamRoleMoshe);
        try {
            team.addCoach(cDavid);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            tMYossi.getTeamManager().removeAndReplaceCoach(cDavid, teamRoleDavid, "main", team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    /**or**/
    @Test
    public void editCoachRole() {
        per.add(TeamManagerPermissions.addRemoveEditCoach);
        tMYossi.becomeTeamManager(team, per);
        TeamRole teamRoleMoshe = new TeamRole(moshe);
        TeamRole teamRoleDavid = new TeamRole(david);
        Coach cDavid = new Coach(team, "mainCoach", teamRoleMoshe);

        try {
            tMYossi.getTeamManager().editCoachRole(cDavid,"newRole");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditCoach);
        try {
            tMYossi.getTeamManager().editCoachRole(cDavid,"newRole2");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }

    }

    /**or**/
    @Test
    public void addPlayerTest() {
        per.add(TeamManagerPermissions.addRemoveEditPlayer);
        tMYossi.becomeTeamManager(team, per);
        Date d = new Date();
        TeamRole teamRoleDavid = new TeamRole(david);
        teamRoleDavid.becomePlayer();
        TeamRole mosheR= new TeamRole(moshe);
        mosheR.becomePlayer();
        try {
            tMYossi.getTeamManager().addPlayer(teamRoleDavid, "defense", team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditPlayer);
        try {
            tMYossi.getTeamManager().addPlayer(mosheR,"offside",team);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }



    }
    /**or**/
    @Test
    public void removePlayer() {
        tMYossi.becomeTeamManager(team, per);
        TeamRole mosheR= new TeamRole(moshe);
        mosheR.becomePlayer();
        try {
            tMYossi.getTeamManager().removePlayer(mosheR.getPlayer(), team);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }

    }
    /**or**/
    @Test
    public void editPlayerRole() {
        per.add(TeamManagerPermissions.addRemoveEditPlayer);
        tMYossi.becomeTeamManager(team, per);
        TeamRole teamRoleMoshe = new TeamRole(moshe);
        TeamRole teamRoleDavid = new TeamRole(david);
        teamRoleMoshe.becomePlayer();


        try {
            tMYossi.getTeamManager().editPlayerRole(teamRoleMoshe.getPlayer(),"newRole");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditPlayer);
        try {
            tMYossi.getTeamManager().editPlayerRole(teamRoleMoshe.getPlayer(),"newRole2");
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }
    }

    /**or**/
    @Test
    public void addRemoveFieldTest() {
        per.add(TeamManagerPermissions.addRemoveEditField);
        tMYossi.becomeTeamManager(team, per);
        Field field = new Field("Beer Sheva Domain.LeagueManagment.Field");
        team.setField(field);
        Field field2 = new Field("Beer Sheeeeeeeva Domain.LeagueManagment.Field");
        try {
            tMYossi.getTeamManager().removeAndReplaceField(field, field2, team);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditField);
        try {
            tMYossi.getTeamManager().removeAndReplaceField(field2,field,team);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }

    }

    @Test
    public void editFieldName() {
        per.add(TeamManagerPermissions.addRemoveEditField);
        tMYossi.becomeTeamManager(team, per);
        Field field = new Field("Beer Sheva Domain.LeagueManagment.Field");
        team.setField(field);
        Field field2 = new Field("Beer Sheeeeeeeva Domain.LeagueManagment.Field");
        try {
            tMYossi.getTeamManager().editFieldName(field, "name");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void addIncomeToTeam() {
        per.add(TeamManagerPermissions.addToBudgetControl);
        tMYossi.becomeTeamManager(team, per);

        try {
            tMYossi.getTeamManager().addIncomeToTeam(team,"money",200);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditPlayer.addToBudgetControl);
        try {
            tMYossi.getTeamManager().addIncomeToTeam(team,"money",200);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }
    }

    @Test
    public void addExpense() {
        per.add(TeamManagerPermissions.addToBudgetControl);
        tMYossi.becomeTeamManager(team, per);
        try {
            tMYossi.getTeamManager().addIncomeToTeam(team,"money",200);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            tMYossi.getTeamManager().addExpenseToTeam(team,"money",50);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        tMYossi.getTeamManager().getTeamManagerPermissions().remove(TeamManagerPermissions.addRemoveEditPlayer.addToBudgetControl);
        try {
            tMYossi.getTeamManager().addExpenseToTeam(team,"money",100);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("This user doesn't have the permission to do this action", e.getMessage());
        }
    }
}