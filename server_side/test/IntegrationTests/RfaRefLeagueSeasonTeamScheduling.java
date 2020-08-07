package IntegrationTests;

import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Scheduling.SchedualeOption2;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Notifications.Notification;
import Domain.Users.Player;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.TeamRole;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Integration between - Rfa, Referee, League, Team, Season, SchedulingPolicy
 *
 * Rfa create leagues, add referees, add teams to leagues, add leagues to season,
 * add referees to league in season and start scheduling police
 * @CodeBy yarden
 */
public class RfaRefLeagueSeasonTeamScheduling {

    MainSystem ms = MainSystem.getInstance();
    Rfa nadav = new Rfa(ms,"nadav","052","nadav@","nadavS", "nadav123",MainSystem.birthDateFormat.parse("06-07-1992"));
    SchedulingPolicy schedulingPolicy = new SchedualeOption2();
    CalculationPolicy calculationPolicy = new CalculateOption1();

    public RfaRefLeagueSeasonTeamScheduling() throws ParseException {
    }

    @Test
    public void test1(){

        try {
            //add referees to the system
            nadav.addReferee("ref1", "0546145795", "ref1@gmail.com", "ref111", "ref111", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            nadav.addReferee("ref2", "0546145795", "ref2@gmail.com", "ref222", "ref222", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            nadav.addReferee("ref3", "0546145795", "ref3@gmail.com", "ref333", "ref333", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            nadav.addReferee("ref4", "0546145795", "ref4@gmail.com", "ref444", "ref444", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

            Assert.assertTrue(ms.getAllReferees().size()==4);
            List<Referee> referees = ms.getAllReferees();
            LinkedHashSet<Referee> hashReferees = new LinkedHashSet<>();
            for (Referee ref: referees) {
                hashReferees.add(ref);
            }

            TeamRole coach= new TeamRole(ms,"coachM","0522150912","coachM@gmail.com","coachM222","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
            coach.becomeCoach();
            TeamRole teamOwner = new TeamRole(ms,"michael","0522150912","teamO@gmail.com","owner123","owner123",MainSystem.birthDateFormat.parse("09-12-1995"));
            teamOwner.becomeTeamOwner();
            TeamRole teamOwner1 = new TeamRole(ms,"michael","0522150912","teamO1@gmail.com","owner222","owner123",MainSystem.birthDateFormat.parse("09-12-1995"));
            teamOwner1.becomeTeamOwner();
            TeamRole teamOwner2 = new TeamRole(ms,"michael","0522150912","teamO2@gmail.com","owner333","owner123",MainSystem.birthDateFormat.parse("09-12-1995"));
            teamOwner2.becomeTeamOwner();
            TeamRole teamOwner3 = new TeamRole(ms,"michael","0522150912","teamO3@gmail.com","owner444","owner123",MainSystem.birthDateFormat.parse("09-12-1995"));
            teamOwner3.becomeTeamOwner();

            Team team = new Team("beer sheva", teamOwner.getTeamOwner());
            nadav.answerRequest(team,true);

            Assert.assertTrue(teamOwner.getTeamOwner().getApprovedTeams().contains(team));
            Assert.assertTrue(!nadav.getTeamRequests().contains(team));

            Team tApproved = teamOwner.getTeamOwner().getApprovedTeams().get(0);
             if(tApproved.equals(team)) {
                 HashSet<Notification> notifications = teamOwner.getTeamOwner().getNotificationsList();
                 for (Notification n : notifications) {
                     if (n.getSender() instanceof Team) {
                         if (((Team) n.getSender()).getName().equals(tApproved.getName())) {
                             teamOwner.getTeamOwner().MarkAsReadNotification(n);
                             teamOwner.getTeamOwner().getNotificationsList().remove(n);
                             break;
                         }
                     }
                 }
             }

            TeamRole player1= new TeamRole(ms,"player1","0522150912","player1@gmail.com","player111","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player2= new TeamRole(ms,"player2","0522150912","player2@gmail.com","player222","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player3= new TeamRole(ms,"player3","0522150912","player3@gmail.com","player333","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player4= new TeamRole(ms,"player4","0522150912","player4@gmail.com","player444","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player5= new TeamRole(ms,"player5","0522150912","player5@gmail.com","player555","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player6= new TeamRole(ms,"player6","0522150912","player6@gmail.com","player666","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player7= new TeamRole(ms,"player7","0522150912","player7@gmail.com","player777","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player8= new TeamRole(ms,"player8","0522150912","player8@gmail.com","player888","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player9= new TeamRole(ms,"player9","0522150912","player9@gmail.com","player999","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player10= new TeamRole(ms,"player10","0522150912","player10@gmail.com","player100","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            TeamRole player11= new TeamRole(ms,"player11","0522150912","player11@gmail.com","player11","player4432",MainSystem.birthDateFormat.parse("09-12-1995"));
            player1.becomePlayer();
            player2.becomePlayer();
            player3.becomePlayer();
            player4.becomePlayer();
            player5.becomePlayer();
            player6.becomePlayer();
            player7.becomePlayer();
            player8.becomePlayer();
            player9.becomePlayer();
            player10.becomePlayer();
            player11.becomePlayer();

            HashSet<TeamRole> players=new HashSet<>();
            players.add(player1);
            players.add(player2);
            players.add(player3);
            players.add(player4);
            players.add(player5);
            players.add(player6);
            players.add(player7);
            players.add(player8);
            players.add(player9);
            players.add(player10);
            players.add(player11);

            teamOwner.getTeamOwner().makeTeamActive(team,players,coach,new Field("fieldA"));
            Assert.assertTrue(teamOwner.getTeamOwner().getNotificationsList().size()==0);

            Team team2 = new Team("team2", teamOwner.getTeamOwner());
            team2.setActive(true);
            Team team3 = new Team("team3", teamOwner.getTeamOwner());
            team3.setActive(true);
            Team team4 = new Team("team4", teamOwner.getTeamOwner());
            team4.setActive(true);

            team2.setField(new Field("homeField2"));
            team3.setField(new Field("homeField3"));
            team4.setField(new Field("homeField4"));

            HashSet<Team> group1 = new HashSet<>();
            HashSet<Team> group2 = new HashSet<>();
            //teams in league A
            group1.add(team);
            group1.add(team2);
            //teams in league B
            group2.add(team3);
            group2.add(team4);

            nadav.createNewLeague("A",ms);
            nadav.createNewLeague("B",ms);

            nadav.defineSeasonToLeague(schedulingPolicy,calculationPolicy,2020, ms.getLeagues().get(0),group1,hashReferees,true);
            nadav.defineSeasonToLeague(schedulingPolicy,calculationPolicy,2020, ms.getLeagues().get(1),group2,hashReferees, true);
            nadav.startSchedulingPolicy(ms.getSeasons().get(0));

            Assert.assertTrue(team.getHome().size()==1 && team.getAway().size()==1);

            HashMap<Season, League> test = team.getLeaguePerSeason();
            for (Season s: test.keySet()) {
                Assert.assertTrue(test.get(s).getName().equals("A"));
            }

        }
        catch (Exception e){
            Assert.fail();
        }
    }

}
