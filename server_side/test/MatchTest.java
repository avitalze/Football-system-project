import Domain.Events.Event;
import Domain.Events.RedCard;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.TeamRole;
import Stubs.TeamStub;
import org.jooq.meta.derby.sys.Sys;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class MatchTest {

    MainSystem ms = MainSystem.getInstance();
    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");

    public MatchTest() throws ParseException {
    }

    @Test
    public void setStartDateTest(){
        TeamStub t1 = new TeamStub("team1");
        TeamStub t2 = new TeamStub("team2");

        try {
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,"04-06-2020 20:00:00");

//            String s = match.toString();
            Date date = new Date();
            match.setStartDate(date);
        }
        catch (Exception e){
            fail();
        }

    }


    @Test
    public void setFieldTest(){
        TeamStub t1 = new TeamStub("team1");
        TeamStub t2 = new TeamStub("team2");

        try {
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,"04-06-2020 20:00:00");

           Field field = new Field("b");
           match.setField(field);


        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void addEventToListTest(){

        TeamStub t1 = new TeamStub("team1");
        TeamStub t2 = new TeamStub("team2");

        try {
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));
            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();

            Event RedCard = new RedCard(ref1,match,teamRole1.getPlayer());
            match.addEvent(RedCard);
        }
        catch (Exception e){
            fail();
        }

        try {
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref3123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref2,"08-05-2300 20:00:00");
            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();

            Event RedCard = new RedCard(ref2,match,teamRole1.getPlayer());
            fail();
            match.addEvent(RedCard);
        }
        catch (Exception e){
            Assert.assertEquals("invalid event creation",e.getMessage());
        }

    }


}