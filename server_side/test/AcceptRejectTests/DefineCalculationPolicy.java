package AcceptRejectTests;

import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.Events.Event;
import Domain.LeagueManagment.*;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Service.RfaApplication;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

/**  define Calculation Policy . UC: 33 ,32  **/

public class DefineCalculationPolicy {

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


        LinkedList<Season> allSeasons =operationsController.getAllSeasons();
        Season firstSeason=allSeasons.get(0);
        List<Referee> allReferee=operationsController.showAllReferee();

        HashMap<League, HashSet<Team>> teamPerLeague = new HashMap<League, HashSet<Team>>();
        HashSet<Team> allTeams=operationsController.showAllTeams();
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");


        Match m1 = new Match(6,4,team1,team2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , allReferee.get(0),"17-04-2020 20:00:00");


        team1.addMatchToAwayMatches(m1);
        team2.addMatchToHomeMatches(m1);
        HashSet<Team> group1 = new HashSet<>();
        group1.add(team1);
        group1.add(team2);
        League firstLeague=operationsController.showLeagus().get(0);
        teamPerLeague.put(firstLeague,group1);
        firstSeason.setTeamsInCurrentSeesonleagus(teamPerLeague);

        //
        /** define Calculation Policy num 1  to season 1 (first season )**/
        try {
            rfaController.startCalculationPolicy(rfa,firstSeason);
            //startCalculationPolicy(Rfa user, Season season)
        }
        catch (Exception e){
            fail();
        }

        try{
            /** alculationPolicy define in season 1  **/
            Assert.assertTrue(firstSeason.getCalculationPolicy()!=null);

            /** calculate the score of each team in the leagues table**/
            int sumPoints=0;
            List<Team> allTeamsInSeason= rfaController.getAllTeamsInSeason(firstSeason);
            for(Team t: allTeamsInSeason){
                /** schedualeOption1 is define in all teams in legue **/
                Assert.assertTrue((t.getScore()>=0));
                sumPoints+=t.getScore();
            }
            /** the sum of points the policy gave bigger then zero **/
            Assert.assertTrue(sumPoints>0);


        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }

    }





    }
