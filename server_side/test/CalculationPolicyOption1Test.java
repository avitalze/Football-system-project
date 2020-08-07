import Domain.Events.Event;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class CalculationPolicyOption1Test {

    MainSystem ms = MainSystem.getInstance();
    CalculationPolicy calculationPolicy = new CalculateOption1();

    @Test
    public void calculateTest() throws Exception {

        HashMap<League, HashSet<Team>> teamPerLeague = new HashMap<League, HashSet<Team>>();
        League A = new League("A",ms);
        League B = new League("B",ms);

        TeamStub team1 = new TeamStub("team1");
        TeamStub team2 = new TeamStub("team2");
        TeamStub team3 = new TeamStub("team3");
        TeamStub team4 = new TeamStub("team4");
        TeamStub team5 = new TeamStub("team5");
        TeamStub team6 = new TeamStub("team6");

        Referee moshe = new Referee(ms,"moshe","0546145795","moseh@gmail.com","moshe123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));

        Match m1 = new Match(6,4,team1,team2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"17-04-2020 20:00:00");

        Match m2 = new Match(4,2,team3,team1, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"14-04-2020 20:00:00");

        Match m3 = new Match(3,3,team5,team4, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"14-03-2020 20:00:00");

        team1.addMatchToAwayMatches(m1);
        team1.addMatchToHomeMatches(m2);

        team2.addMatchToHomeMatches(m1);
        team3.addMatchToAwayMatches(m2);

        team5.addMatchToAwayMatches(m3);
        team4.addMatchToHomeMatches(m3);

        HashSet<Team> group1 = new HashSet<>();
        HashSet<Team> group2 = new HashSet<>();
        group1.add(team1);
        group1.add(team2);
        group1.add(team3);
        group2.add(team4);
        group2.add(team5);

        Match m4 = new Match(3,3,team5,team6, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"14-03-2020 20:00:00");

        group2.add(team6);
        team5.addMatchToAwayMatches(m4);
        team6.addMatchToHomeMatches(m4);

        teamPerLeague.put(A,group1);
        teamPerLeague.put(B,group2);

        calculationPolicy.calculate(teamPerLeague);
        Assert.assertTrue(team1.getScore()==2 && team4.getScore()==1 && team5.getScore()==2 && team6.getScore()==1);

    }


}