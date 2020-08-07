package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Events.Event;
import Domain.Events.ExtraTime;
import Domain.Events.YellowCard;
import Domain.LeagueManagment.Match;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.TeamOwner;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class CheckNotifications {

    SystemOperationsController controller= new SystemOperationsController();

    @Test
    public void checkNoti() throws Exception {

        controller.initSystemObjectsAvitalForUI();
        controller.initSystemObjectsAvital();

        Rfa rfa = controller.getAllRFA().get(0);
        MainSystem.getInstance().loginUsers.add(rfa);
        TeamOwner ilanTeamOwner = controller.showAllTeamOwner().get(0);
        ilanTeamOwner.requestNewTeam("hiffa");
        //TODO return to private~!!!!!
        //MainSystem.getInstance().loginUsers.add(rfa);
        Assert.assertTrue(rfa.gotRFAnotification);
        Assert.assertEquals("gotRFAnotification", rfa.checkNotificationAlert());
        Assert.assertFalse(rfa.gotRFAnotification);

        LinkedList<Match> matches= new LinkedList<>(controller.getAllCurrMatchs());
        Match m= matches.get(0);
        rfa.addMatchFollow(m);

        //LinkedList<Referee> referees= new LinkedList<>(controller.)
        Referee ref= controller.showAllReferee().get(0);
        m.addEvent(new ExtraTime(ref,m,4));
        Assert.assertTrue(rfa.gotFanNotification);
        Assert.assertEquals("gotFanNotification", rfa.checkNotificationAlert());
        Assert.assertFalse(rfa.gotFanNotification);
    }
}
