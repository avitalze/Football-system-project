import Domain.LeagueManagment.*;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.MainSystem;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;
import Domain.Users.*;

import java.util.Date;
import java.util.HashSet;

public class LeagueTest {

    MainSystem sys = MainSystem.getInstance();
    TeamStub t1 = new TeamStub("team1");
    TeamStub t2 = new TeamStub("team2");
    TeamStub t3 = new TeamStub("team3");
    TeamStub t4 = new TeamStub("team4");

    /**eden+yarden**/
    @Test
    public void addSeasonWithTeams() throws Exception {

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
            l.addSeasonWithTeams(null, teams1);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        try {
            l.addSeasonWithTeams(s,null);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals(NullPointerException.class, e.getClass());
        }

        l.addSeasonWithTeams(s,teams1);
        /**check if both are equals**/
        Assert.assertEquals(l.getTeamsInSeason().get(s),s.getTeamsInCurrentSeasonLeagues().get(l));

        teams2.add(t1);
        teams2.add(t4);

        l.addSeasonWithTeams(s,teams2);
        /**add more teams and check if both are equals**/
        Assert.assertEquals(l.getTeamsInSeason().get(s),s.getTeamsInCurrentSeasonLeagues().get(l));

        /**double add again- both with no changes and equals*/
        l.addSeasonWithTeams(s,teams2);
        Assert.assertEquals(l.getTeamsInSeason().get(s),s.getTeamsInCurrentSeasonLeagues().get(l));
        Assert.assertTrue(l.getTeamsInSeason().get(s).size()==4);
    }
}