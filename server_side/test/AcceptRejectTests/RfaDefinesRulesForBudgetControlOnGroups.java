package AcceptRejectTests;


import Domain.BudgetControl.BudgetReport;
import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Service.RfaApplication;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.fail;

/** RFA defines rules for budget control over groups. Requirement number: 9.8 **/

public class RfaDefinesRulesForBudgetControlOnGroups {

    RfaController rfaController=new RfaController();
    SystemOperationsController operationsController=new SystemOperationsController();


    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma = MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa = operationsController.getAllRFA().get(0);
        SystemManager sm = operationsController.showAllSystemManagers().get(0);

        /** Split budget to groups **/

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        HashSet<Team> allTeamsHash = operationsController.showAllTeams();
        ArrayList<Team> allTeams = new ArrayList<>(allTeamsHash);
        Team firstTeam = allTeams.get(0);
        Team secTeam = allTeams.get(1);


        Date d1 = sdf.parse("05-01-2020");
        Date d2 = sdf.parse("15-02-2020");

        LinkedList<BudgetReport> reports = new LinkedList<>();
        BudgetReport r1 = new BudgetReport("a", 500);
        r1.setNow(d1);
        BudgetReport r2 = new BudgetReport("a", 166);
        r2.setNow(d2);
        reports.add(r1);
        reports.add(r2);

        firstTeam.setActive(true);
        secTeam.setActive(true);
        firstTeam.getBudgetControl().setIncomeAndExpenses(reports);

        HashSet<Team> teamsExceptions1=null;
        HashSet<Team> toCheck1 = new HashSet<>();
        try {
             rfaController.addBudgetRole(rfa, "income for each month bigger than 100");
            teamsExceptions1 = rfa.firstRoleForBudget(2020);
            toCheck1.add(firstTeam);
            toCheck1.add(secTeam);

        } catch (Exception e) {
            fail();
        }

        /** test if worked **/
        try {
            /**check teams satisfies the role**/
            Assert.assertEquals(teamsExceptions1, toCheck1);
            BudgetReport r6 = new BudgetReport("a", 160);
            HashSet<Team> teamsExceptions2 = rfa.firstRoleForBudget(2020);
            Assert.assertTrue(teamsExceptions2.size() == 2);
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }


    }

    }


