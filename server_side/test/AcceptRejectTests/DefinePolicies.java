package AcceptRejectTests;

import Domain.Controllers.RefereeController;
import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

public class DefinePolicies {

    RfaController rfaController=new RfaController();
    RefereeController refereeController = new RefereeController();
    SystemOperationsController operationsController=new SystemOperationsController();

    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma = MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa = operationsController.getAllRFA().get(0);
        SystemManager sm = operationsController.showAllSystemManagers().get(0);


        /**all scheduling policies options**/
        List<SchedulingPolicy> schedulingPolicies = rfaController.watchSchedulingPolicies();
        SchedulingPolicy schedualeOption1= schedulingPolicies.get(0);

        /**all calculating policies options**/
        List<CalculationPolicy> calculationPolicies = rfaController.watchCalculationPolicies();
        CalculationPolicy calculateOption1 = calculationPolicies.get(0);

        LinkedList<Season> allSeasons =operationsController.getAllSeasons();
        Season firstSeason=allSeasons.get(0);

        List<Referee> allRefsList= operationsController.showAllReferee();
        HashSet< Referee > allRefs=new HashSet<>(allRefsList);
        Referee mainReferee= allRefsList.get(0);

        /** define Scheduling Policy num 1  to season 1 (first season )**/
        try {
            rfaController.DefinePoliciesToSeason(String.valueOf(firstSeason.getYear()),calculateOption1.getNameOfCalculationPolicy(),
                    schedualeOption1.getNameOfSchedulingPolicy(),rfa.getUserName());
        }
        catch (Exception e){
            fail();
        }
    }

}
