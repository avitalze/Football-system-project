import Domain.Events.Event;
import Domain.Events.Goal;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.MainSystem;
import Domain.Notifications.Notification;
import Domain.Users.Referee;
import Domain.Users.TeamRole;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

public class RefereeTest {

    MainSystem ms = MainSystem.getInstance();
    SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:MM:ss");

    @Test
    public void addEventsDuringMatchTest() {

        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();

            Event goal = new Goal(ref1,match,teamRole1.getPlayer());
            fail();
            ref2.addEventsDuringMatch(match,goal);
        }
        catch (Exception e){
            Assert.assertEquals("This player isn't in one of the participating teams",e.getMessage());
        }

        try {
            TeamStub t3 = new TeamStub("team3");
            TeamStub t4 = new TeamStub("team4");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Date date = new Date(System.currentTimeMillis());

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Match match = new Match(0,0,t3,t4,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t3.addPlayer(teamRole1.getPlayer());
            ref2.addMatchToList(match);
            Event goal = new Goal(ref1,match,teamRole1.getPlayer());
            ref2.addEventsDuringMatch(match,goal);
        }
        catch (Exception e){
           fail();
        }
    }

    @Test
    public void createReportTest() {

        try {
            TeamStub t5 = new TeamStub("team5");
            TeamStub t6 = new TeamStub("team6");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Date date = new Date(System.currentTimeMillis());

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Match match = new Match(0,0,t5,t6,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t5.addPlayer(teamRole1.getPlayer());
            ref2.addMatchToList(match);

            HashSet<Event> events = ref2.createReport(match);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals("You do not have a permission to edit events right now",e.getMessage());
        }

    }


    @Test
    public void addMatchToListTest() {
        try {
            TeamStub t5 = new TeamStub("team5");
            TeamStub t6 = new TeamStub("team6");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Date date = new Date(System.currentTimeMillis());

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Match match = new Match(0,0,t5,t6,new Field("a"),new HashSet<>(),
                    referees,ref1,dt.format(date));

//            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));
//
//            teamRole1.becomePlayer();
//            t5.addPlayer(teamRole1.getPlayer());
            ref2.addMatchToList(match);
            Assert.assertTrue(ref1.getMatches().size()==1);

        }
        catch (Exception e){
            fail();
        }

    }

    @Test
    public void notificationsTest() {
        TeamStub t1 = new TeamStub("team1");
        TeamStub t2 = new TeamStub("team2");

        try {
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,"04-06-2020 20:00:00");

            Field field = new Field("b");
            match.setField(field);
            Assert.assertTrue(ref1.getUnReadNotifications().size()==1);
            Assert.assertTrue(ref1.getNotificationsList().size()==1);

            for (Notification n : ref1.getUnReadNotifications()) {
                n.setRead(true);
            }

            Assert.assertTrue(ref1.getUnReadNotifications().size()==0);

            match.setStartDate(new Date());
            Assert.assertTrue(ref1.getUnReadNotifications().size()==1);
            Assert.assertTrue(ref1.getNotificationsList().size()==2);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void createGoalEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event goal = ref1.createGoalEvent(teamRole1.getPlayer(), match);
            match.addEventToList(goal);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
           fail();
        }

    }

    @Test
    public void createYellowCardEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event yellowCard = ref1.createYellowCardEvent(teamRole1.getPlayer(), match);
            match.addEventToList(yellowCard);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }

    }

    @Test
    public void createRedCardEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event redCard = ref1.createRedCardEvent(teamRole1.getPlayer(), match);
            match.addEventToList(redCard);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void createOffSideCardEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event offSide = ref1.createOffSideCardEvent(teamRole1.getPlayer(), match);
            match.addEventToList(offSide);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void createOffenseEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event offense = ref1.createOffenseEvent(teamRole1.getPlayer(), match);
            match.addEventToList(offense);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void createInjuryEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event injury = ref1.createInjuryEvent(teamRole1.getPlayer(), match);
            match.addEventToList(injury);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void createReplacementEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));
            TeamRole teamRole2 = new TeamRole(ms,"yarden2","0546260171","yarden@gmail.com","yarden212", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());
            teamRole2.becomePlayer();
            t1.addPlayer(teamRole2.getPlayer());

            Event replacement = ref1.createReplacementEvent(teamRole1.getPlayer(),teamRole2.getPlayer(),match);
            match.addEventToList(replacement);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void createExtraTimeEventTest() {
        try {
            TeamStub t1 = new TeamStub("team1");
            TeamStub t2 = new TeamStub("team2");
            Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Date date = new Date(System.currentTimeMillis());

            Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
                    new HashSet<>(),ref1,dt.format(date));

            TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t1.addPlayer(teamRole1.getPlayer());

            Event extraTime = ref1.createExtraTimeEvent(match, 5);
            match.addEventToList(extraTime);
            Assert.assertTrue(match.getEvents().size()==1);
        }
        catch (Exception e){
            fail();
        }
    }

}