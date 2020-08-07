package AcceptRejectTests;


import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**  define Scheduling Policy . UC: 34   **/

public class DefineSchedulingPolicy {


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


        /**all scheduling policies options**/
        List<SchedulingPolicy> schedulingPolicies = rfaController.watchSchedulingPolicies();
        SchedulingPolicy schedualeOption1= schedulingPolicies.get(0);

        LinkedList<Season> allSeasons =operationsController.getAllSeasons();
        Season firstSeason=allSeasons.get(0);
        List< Referee > allRefsList= operationsController.showAllReferee();
        HashSet< Referee > allRefs=new HashSet<>(allRefsList);
        Referee mainReferee= allRefsList.get(0);
        /** define Scheduling Policy num 1  to season 1 (first season )**/
        try {
            rfaController.startSchedulingPolicy(rfa,firstSeason,allRefs,mainReferee);
            //(Rfa user, Season season, HashSet< Referee > refs, Referee mainReferee)
            int x=0;
        }
        catch (Exception e){
            fail();
        }

        /** maches define in all teams in season  **/
        try{
            //for all lig in season - the mac
            List<Referee> allReferee=operationsController.showAllReferee();
            List<Team> allTeamsInSeason= rfaController.getAllTeamsInSeason(firstSeason);
            for(Team t: allTeamsInSeason){
                HashSet<Match> allHomeMatches= t.getHome();
                HashSet<Match> allAwayMatches= t.getAway();
                /** schedualeOption1 is define in season 1 **/
                Assert.assertTrue((allAwayMatches.size() ==1) || (allHomeMatches.size() ==1));
            }
            /** schedualeOption1 is define in refarees **/
            for(Referee r: allRefsList){
                Assert.assertTrue(r.getMatches().size()>0);
            }

        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }



    }







    }
