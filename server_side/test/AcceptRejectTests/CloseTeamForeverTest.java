package AcceptRejectTests;
import Domain.Controllers.SystemManagerController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.TeamSubscription;
import Domain.Users.*;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;


public class CloseTeamForeverTest {
    SystemManagerController managerController=new SystemManagerController();
    SystemOperationsController operationsController=new SystemOperationsController();

    /**
     * accept test for - close team forever by system manager .
     * @throws Exception
     * @codeBy Eden
     */
    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsEden();
        MainSystem ma= MainSystem.getInstance();
        HashSet<Team> teams=operationsController.showAllTeams();
        int size=teams.size();
        SystemManager sm=operationsController.showAllSystemManagers().get(0);

        Iterator<Team > iter =teams.iterator();
        Team first=iter.next();
        Team second=iter.next();
        Team t1=first.getName().equals("macabi")?first:second;
        HashSet<TeamOwner> teamOwners=t1.getTeamOwners();
        TeamRole teamManager=t1.getTeamManager().getTeamRole();

        for (TeamOwner tO:teamOwners){
            assertTrue(tO.getTeams().contains(t1));
        }
        Coach coach=t1.getCoach();
        HashSet<Player> players=t1.getPlayers();

        try {
            sm.removeTeamFromSystem(t1);
        }
        catch (Exception e){
            fail();
        }
        /***delete from system**/
        assertTrue(operationsController.showAllTeams().size()==size-1);
        assertTrue(!ma.getTeamNames().contains(t1.getName()));
        /***delete from all owners**/
        for (TeamOwner tO:teamOwners){
            assertTrue(!tO.getTeams().contains(t1));
        }
        /**all subscriptions deleted*/
        for (TeamOwner tO:teamOwners){
            HashSet<TeamSubscription> teamSubscriptions=tO.getMySubscriptions();
            for(TeamSubscription ts: teamSubscriptions){
                assertTrue(ts.team.getName()!=t1.getName());
            }
        }
        /**delete from teamManager*/
        assertTrue(!teamManager.isTeamManager());
        /**check for players*/
        for(Player p: players){
            assertTrue(p.getTeam()==null);
        }
        /**check for coach*/
        assertTrue(coach.getCoachTeam()==null);


    }

    /**
     * reject test for - close team forever by system manager .
     * @throws Exception
     * @codeBy Eden
     */
    @Test
    public  void reject() throws Exception {
        operationsController.deleteSystem();
        operationsController.initSystemObjectsEden();
        MainSystem ma= MainSystem.getInstance();
        Season season=new Season(ma,null,null,2019);
        ma.setCurrSeason(season);
        HashSet<Team> teams=operationsController.showAllTeams();
        Team t1=teams.iterator().next();
        League league=new League("ligi",ma,season);
        HashMap<Season,League> seasonLeagueHashMap=new HashMap<>();
        seasonLeagueHashMap.put(season,league);
        t1.setLeaguePerSeason(seasonLeagueHashMap);
        SystemManager sm=operationsController.showAllSystemManagers().get(0);

        try {
            sm.removeTeamFromSystem(t1);
            fail();
        }
        catch (Exception e){//
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("cannot delete team in current season",e.getMessage());
        }


    }
}
