package AcceptRejectTests;

import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.League;
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

import java.util.*;

import static org.junit.Assert.fail;


/**  define a season for league by year. UC: 29   **/

public class DefineSeasonForLeague {


    RfaController rfaController=new RfaController();
    SystemOperationsController operationsController=new SystemOperationsController();

    @Test
    public void accept() throws Exception{
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma= MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa=operationsController.getAllRFA().get(0);
        SystemManager sm=operationsController.showAllSystemManagers().get(0);

        
        List<League> allLeagus= operationsController.showLeagus();
        LinkedHashSet<Referee> allRefs = operationsController.getAllREfereeInHasSet();
        List<SchedulingPolicy>SchedulingPolicies=rfaController.watchSchedulingPolicies();

        List<CalculationPolicy> CalculationPolicies=rfaController.watchCalculationPolicies();
        HashSet<Team> allTeams=operationsController.showAllTeams();

        /** success define a season for league by year. **/
        int currSeasonYear=2021;
        int counter=0;
        try {
            rfaController.defineSeasonToLeagues(rfa,allLeagus,currSeasonYear,SchedulingPolicies.get(0),CalculationPolicies.get(0),
                    allTeams,allRefs,true);
        }
        catch (Exception e){
            fail();
        }

        try{
            HashMap<Season, HashSet<Team>> allSeasonInLeague=null;
            HashSet<Team> teammsInSeason=null;
            League myLeague=null;
            Season myLSeason=null;
            /** A new season added to system **/
            LinkedList<Season> allSeasons=operationsController.getAllSeasons();
            for(Season s: allSeasons){
                if(s.getYear()==currSeasonYear){
                    counter++;
                    myLSeason=s;
                }
            }
            Assert.assertTrue(counter==1);

            /**league will be contain the teams that will play it **/
            //HashMap<Season, HashSet<Team>> teamsInSeason; in league
            for(League l: allLeagus){
                allSeasonInLeague =l.getTeamsInSeason();
                teammsInSeason=allSeasonInLeague.get(myLSeason);
                Assert.assertTrue(teammsInSeason.containsAll(allTeams) && allTeams.containsAll(teammsInSeason));
            }
            /**Teams belong to the league**/
            for(Team t: allTeams){
                HashMap<Season, League> leaguesPerSeason= t.getLeaguePerSeason();
                Assert.assertTrue(leaguesPerSeason.containsKey(myLSeason));
            }
            /** League belong to selected season **/
            for(League l: allLeagus){
                allSeasonInLeague =l.getTeamsInSeason();
                Assert.assertTrue(allSeasonInLeague.containsKey(myLSeason));
            }

            /** league and season are associated with the judges who will judge it **/
            for(League l: allLeagus){
                HashMap<Season, LinkedHashSet<Referee>> refereesInLeague=l.getRefereesInLeague();
                LinkedHashSet<Referee> allRefInLeague=refereesInLeague.get(myLSeason);
                Assert.assertTrue(allRefInLeague.containsAll(allRefs) && allRefs.containsAll(allRefInLeague));
            }

        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }


    }

    @Test
    public void reject() throws Exception{
        /*****system init*****/
        operationsController.deleteSystem();
        operationsController.initSystemObjectsAvital();
        MainSystem ma= MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa=operationsController.getAllRFA().get(0);
        SystemManager sm=operationsController.showAllSystemManagers().get(0);

        List<League> allLeagus= operationsController.showLeagus();
        LinkedHashSet<Referee> allRefs = operationsController.getAllREfereeInHasSet();
        List<SchedulingPolicy>SchedulingPolicies=rfaController.watchSchedulingPolicies();
        List<CalculationPolicy> CalculationPolicies=rfaController.watchCalculationPolicies();
        HashSet<Team> allTeams=operationsController.showAllTeams();
        int currSeasonYear=2020;

        /** RFA Try add new season that already exists (year already exists) **/
        try {
            rfaController.defineSeasonToLeagues(rfa,allLeagus,currSeasonYear,SchedulingPolicies.get(0),CalculationPolicies.get(0),
                    allTeams,allRefs,true);
            fail("test fail");
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("There is team that already play in this season, check the team's list again",e.getMessage());
        }

        /** try add season for league that teams already associated with the same season to another league **/
        rfaController.createLeague("ligaB",rfa);
        List <League> leagueBList=new ArrayList<>();
        leagueBList.add(allLeagus.get(1));
        try {
            rfaController.defineSeasonToLeagues(rfa,leagueBList,currSeasonYear,SchedulingPolicies.get(0),CalculationPolicies.get(0),
                    allTeams,allRefs,true);
            fail("test fail");
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("There is team that already play in this season, check the team's list again",e.getMessage());
        }

    }
}
