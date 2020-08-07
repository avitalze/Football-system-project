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

public class ReportIncomeAndExpense {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    TeamManagementController teamManagementController = new TeamManagementController();

    @Test
    public void acceptAddIncomeToTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        long amount = t1Macabi.getBudgetControl().getBalance();
        teamManagementController.addIncomeToTeam(ilanTeamOwner, t1Macabi,"donation", 2000);

        assertEquals(t1Macabi.getBudgetControl().getBalance(), amount + 2000);
    }
    @Test
    public void rejectAddIncomeToTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        try {
            //cant put negative number
            teamManagementController.addIncomeToTeam(ilanTeamOwner, t1Macabi,"donation", -2000);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
    @Test
    public void acceptAddExpenseToTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        teamManagementController.addIncomeToTeam(ilanTeamOwner, t1Macabi,"donation", 2000);
        long amount = t1Macabi.getBudgetControl().getBalance();

        teamManagementController.addExpenseToTeam(ilanTeamOwner, t1Macabi,"salary", 1000);

        assertEquals(t1Macabi.getBudgetControl().getBalance(), amount - 1000);
    }
    @Test
    public void rejectAddExpenseToTeam() throws Exception {
        systemOperationsController.deleteSystem();
        systemOperationsController.initSystemObjectsAdi();
        TeamRole ilanTeamOwner = (TeamRole)systemOperationsController.getUserByUserName("Ilan");
        Team t1Macabi = teamManagementController.getAllMyTeams(ilanTeamOwner.getTeamOwner()).get(0);

        teamManagementController.addIncomeToTeam(ilanTeamOwner, t1Macabi,"donation", 2000);

        try {
            //cant put negative number
            teamManagementController.addExpenseToTeam(ilanTeamOwner, t1Macabi,"salary", -1000);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
        }
    }
}
