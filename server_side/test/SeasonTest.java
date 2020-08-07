import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Stubs.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class SeasonTest {

    MainSystem sys = MainSystem.getInstance();
    TeamStub t1 = new TeamStub("team1");
    TeamStub t2 = new TeamStub("team2");
    TeamStub t3 = new TeamStub("team3");
    TeamStub t4 = new TeamStub("team4");

    /**Eden+yarden*/
    @Test
    void addLeagueWithTeams() throws Exception {

        SchedulingPolicy schedulingPolicy = new SchedualeOption1();
        CalculationPolicy calculationPolicy = new CalculateOption1();

        League l = new League("A",sys);
        Season s = new Season(sys, schedulingPolicy, calculationPolicy,2020);

        HashSet<Team> teams1=new HashSet<Team>();
        HashSet<Team> teams2 = new HashSet<Team>();

        teams1.add(t1);
        teams1.add(t2);
        teams1.add(t3);

        /**nul check**/
        try {
            s.addLeagueWithTeams(null, teams1);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        try {
            s.addLeagueWithTeams(l,null);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }


        s.addLeagueWithTeams(l,teams1);
        /**check if both are equals**/
        Assert.assertEquals(l.getTeamsInSeason().get(s),s.getTeamsInCurrentSeasonLeagues().get(l));

        teams2.add(t1);
        teams2.add(t4);

        s.addLeagueWithTeams(l,teams2);
        /**add more teams and check if both are equals**/
        Assert.assertEquals(l.getTeamsInSeason().get(s),s.getTeamsInCurrentSeasonLeagues().get(l));

        /**double add again- both with no changes and equals*/
        s.addLeagueWithTeams(l,teams1);
        Assert.assertEquals(l.getTeamsInSeason().get(s),s.getTeamsInCurrentSeasonLeagues().get(l));
        Assert.assertTrue(l.getTeamsInSeason().get(s).size()==4);

    }

}