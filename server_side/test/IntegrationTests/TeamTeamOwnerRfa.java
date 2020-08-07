package IntegrationTests;

import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Notifications.Notification;
import Domain.Users.Rfa;
import Domain.Users.TeamRole;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;

public class TeamTeamOwnerRfa {

    MainSystem ms = MainSystem.getInstance();
    Rfa nadav = new Rfa(ms,"nadav","052","nadav@","nadavS", "nadav123",MainSystem.birthDateFormat.parse("06-07-1992"));
    Rfa rfa2 = new Rfa(ms,"nadav","052","nadav@","rfa2222", "rfa2222",MainSystem.birthDateFormat.parse("06-07-1992"));
    TeamRole teamOwner3 = new TeamRole(ms,"michael","0522150912","teamO3@gmail.com","owner444","owner123",MainSystem.birthDateFormat.parse("09-12-1995"));

    public TeamTeamOwnerRfa() throws ParseException {
    }

    @Test
    public void requestAndAnswerForOpenTeamTest(){

            teamOwner3.becomeTeamOwner();
            try {
                teamOwner3.getTeamOwner().requestNewTeam("beer sheva");
                Assert.assertTrue(teamOwner3.getTeamOwner().checkIfTeamInRequestTeam("beer sheva"));
                Assert.assertTrue(teamOwner3.getTeamOwner().getRequestedTeams().size()==1);
                Team t= teamOwner3.getTeamOwner().getRequestedTeams().get(0);
                Assert.assertTrue(Rfa.getTeamRequests().size()==1);
                Assert.assertTrue(Rfa.getTeamRequests().contains(t));
                nadav.answerRequest(t,true);
                Assert.assertTrue(Rfa.getTeamRequests().size()==0);
                Assert.assertFalse(Rfa.getTeamRequests().contains(t));
                Assert.assertTrue(teamOwner3.getTeamOwner().getRequestedTeams().size()==0);
                Assert.assertTrue(teamOwner3.getTeamOwner().getApprovedTeams().contains(t));

            }
            catch (Exception e){
                Assert.fail();
            }


    }
}
