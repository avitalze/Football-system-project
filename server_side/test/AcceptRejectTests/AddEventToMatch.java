package AcceptRejectTests;

import Domain.Controllers.RefereeController;
import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.Events.Event;
import Domain.Events.Goal;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Domain.Users.TeamRole;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

public class AddEventToMatch {

    RfaController rfaController=new RfaController();

    SystemOperationsController operationsController=new SystemOperationsController();

    @Test
    public void accept() {
        try {
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma = MainSystem.getInstance();

        TeamStub t3 = new TeamStub("team3");
        TeamStub t4 = new TeamStub("team4");
        Referee ref1 = new Referee(ma, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref2 = new Referee(ma, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
        Date date = new Date(System.currentTimeMillis());

        HashSet<Referee> referees = new HashSet<Referee>();
        referees.add(ref2);
        Match match = new Match(0,0,t3,t4,new Field("a"),new HashSet<>(),
                new HashSet<>(),ref1,MainSystem.simpleDateFormat.format(date));

        TeamRole teamRole1 = new TeamRole(ma,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

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


}
