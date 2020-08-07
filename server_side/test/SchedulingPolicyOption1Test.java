import Domain.Events.Event;
import Domain.LeagueManagment.*;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.MainSystem;
import Domain.Users.Referee;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class SchedulingPolicyOption1Test {

    MainSystem ms = MainSystem.getInstance();
    SchedulingPolicy schedualeOption1 = new SchedualeOption1();

    @Test
    public void assignTest() throws Exception {
        HashMap<League, HashSet<Team>> teamPerLeague = new HashMap<League, HashSet<Team>>();
        League A = new League("A", ms);
        League B = new League("B", ms);

        TeamStub team1 = new TeamStub("team1");
        TeamStub team2 = new TeamStub("team2");
        TeamStub team3 = new TeamStub("team3");
        TeamStub team4 = new TeamStub("team4");
        TeamStub team5 = new TeamStub("team5");

        team1.setField(new Field("homeField1"));
        team2.setField(new Field("homeField2"));
        team3.setField(new Field("homeField3"));
        team4.setField(new Field("homeField4"));
        team5.setField(new Field("homeField5"));

        Referee moshe = new Referee(ms, "moshe", "0546145795", "moseh@gmail.com", "moshe123", "moshe123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref1@gmail.com", "ref1123", "ref1123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

        LinkedHashSet<Referee> referees = new LinkedHashSet<>();
        referees.add(ref1);
        referees.add(ref2);

        Season season = new Season(ms, schedualeOption1, new CalculateOption1(),2018);

        HashSet<Team> group1 = new HashSet<>();
        HashSet<Team> group2 = new HashSet<>();
        //teams in league A
        group1.add(team1);
        group1.add(team2);
        group1.add(team3);
        //teams in league B
        group2.add(team4);
        group2.add(team5);

        HashMap<Season, LinkedHashSet<Referee>> refereesInLeague = new HashMap<>();
        refereesInLeague.put(season,referees);
        A.setRefereesInLeague(refereesInLeague);
        B.setRefereesInLeague(refereesInLeague);

        season.addLeagueWithTeams(A,group1);
        season.addLeagueWithTeams(B,group2);

        schedualeOption1.assign(season.getTeamsInCurrentSeasonLeagues(), season);

        Assert.assertTrue(team1.getAway().size()+team1.getHome().size()==2);
        Assert.assertTrue(team4.getAway().size()+team4.getHome().size()==1);
    }

}