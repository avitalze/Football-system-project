package AcceptRejectTests;

import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.Events.Event;
import Domain.Events.Goal;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.TeamRole;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.fail;

public class CreateReportToMatch {

    RfaController rfaController=new RfaController();
    SystemOperationsController operationsController=new SystemOperationsController();

    @Test
    public void ApproveAndDecline() {

        try {
            /*****system init*****/
            operationsController.initSystemObjectsAvital();
            MainSystem ma = MainSystem.getInstance();

            TeamStub t5 = new TeamStub("team5");
            TeamStub t6 = new TeamStub("team6");
            Referee ref1 = new Referee(ma, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ma, "ref2", "0546145795", "ref2@gmail.com", "ref4123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Date date = new Date(System.currentTimeMillis());

            HashSet<Referee> referees = new HashSet<Referee>();
            referees.add(ref2);
            Match match = new Match(0, 0, t5, t6, new Field("a"), new HashSet<>(),
                    new HashSet<>(), ref1, MainSystem.simpleDateFormat.format(date));

            TeamRole teamRole1 = new TeamRole(ma, "yarden", "0546260171", "yarden@gmail.com", "yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

            teamRole1.becomePlayer();
            t5.addPlayer(teamRole1.getPlayer());
            ref2.addMatchToList(match);
            HashSet<Event> events = ref2.createReport(match);
            fail();
        } catch (Exception e) {
            Assert.assertEquals("You do not have a permission to edit events right now", e.getMessage());
        }
    }



}
