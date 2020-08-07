package AcceptRejectTests;


import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Service.RfaApplication;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.fail;

/** RFA Turn On Scheduling Policy he selected . Requirement number: 9.7  **/


public class TurnOnSchedulingPolicy {

    RfaController rfaController = new RfaController();
    SystemOperationsController operationsController = new SystemOperationsController();


    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma = MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa = operationsController.getAllRFA().get(0);
        SystemManager sm = operationsController.showAllSystemManagers().get(0);


        Season currSeason= operationsController.getCurrSeason();
        LinkedHashSet<Referee> allRefsHash = operationsController.getAllREfereeInHasSet();
        ArrayList<Referee> allRefs = new ArrayList<>(allRefsHash);
        Referee mainReferee = allRefs.get(0);
        try {
            rfaController.startSchedulingPolicy(rfa,currSeason,allRefsHash,mainReferee);

        }
        catch (Exception e){
            fail();
        }

        /**  check if Scheduling Policy started   **/

        try{
            /** maches define in all teams in season  **/
            List<Referee> allReferee=operationsController.showAllReferee();
            List<Team> allTeamsInSeason= rfaController.getAllTeamsInSeason(currSeason);
            for(Team t: allTeamsInSeason){
                HashSet<Match> allHomeMatches= t.getHome();
                HashSet<Match> allAwayMatches= t.getAway();
                /** schedualeOption1 is define in season 1 **/
                Assert.assertTrue((allAwayMatches.size() ==1) || (allHomeMatches.size() ==1));
            }
            /** schedualeOption1 is define in refarees **/
            for(Referee r: allRefs){
                Assert.assertTrue(r.getMatches().size()>0);
            }

        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }






        //startSchedulingPolicy
    }


}
