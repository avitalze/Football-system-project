package IntegrationTests;

import Domain.Events.Event;
import Domain.Events.ExtraTime;
import Domain.Events.Goal;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.TeamRole;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.fail;


/**
 * Integration between - Referee, Match and EventsAdapter
 *
 * The match created and the referee can add events during match
 *
 * @CodeBy yarden
 */
public class RefereeMatchEvent {

    MainSystem ms = MainSystem.getInstance();
    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");

    @Test
    public void matchManagement(){

        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    referees,ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event goal = ref1.createGoalEvent(teamRole1.getPlayer(), match);
            ref1.addEventsDuringMatch(match,goal);

            Assert.assertTrue(match.getEvents().size()==1);

            Event extraTime = ref1.createExtraTimeEvent(match, 10);
            ref1.addEventsDuringMatch(match, extraTime);

            Assert.assertTrue(match.getEvents().size()==2);

            match.setField(new Field("c2"));
            Assert.assertTrue(ref1.getUnReadNotifications().size()==1);
            Assert.assertTrue(ref1.getNotificationsList().size()==1);

            HashSet<Event> events = ref1.createReport(match);

        }
        catch (Exception e){
            Assert.assertEquals("You can not create report until the match is over",e.getMessage());
        }


    }

}
