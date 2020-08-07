import Domain.MainSystem;
import Domain.Users.User;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UserTest {
    MainSystem ms = MainSystem.getInstance();
    User user = new User(ms);
    SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
    Date date;

    {
        try {
            date = sdf.parse("09-12-1995");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    //    Domain.Users.Player player1=new Domain.Users.Player(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123",date);
//    Domain.Users.Player player2=new Domain.Users.Player(ms, "Or Hamalcha", "0542150912","oralf@gmail.com", "OrHamalcha", "Or1234",date);
//    Domain.PrivatePage pp1=new Domain.PrivatePage();
//    Domain.PrivatePage pp2= new Domain.PrivatePage();
//    Domain.PrivatePage pp3= new Domain.PrivatePage();
//    Domain.LeagueManagment.League league1 = new Domain.LeagueManagment.League("LIGAT HAALUFUT", ms);
//    Domain.LeagueManagment.League league2 = new Domain.LeagueManagment.League("LIGAT HAAL",ms);
//    Domain.LeagueManagment.Season season2020 = new Domain.LeagueManagment.Season(ms,null,2020);
//    Domain.LeagueManagment.Season season2019 = new Domain.LeagueManagment.Season(ms,null,2019);
//    Subscription yossi = new Subscription(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123" );
//    //or added because the change in Domain.LeagueManagment.Team constructor
//    Domain.Users.TeamOwner teamOwner = new Domain.Users.TeamOwner(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123" );
//    //
//    Domain.LeagueManagment.Team hapoelBeerSheva= new Domain.LeagueManagment.Team("Hapoel Beer Sheva",teamOwner);
//    Domain.LeagueManagment.Team hapoelKfarSaba= new Domain.LeagueManagment.Team("Hapoel Kfar Saba",teamOwner);
//    Domain.LeagueManagment.Team macabiHaifa= new Domain.LeagueManagment.Team("Macabi Haifa",teamOwner);
//    Domain.LeagueManagment.Team macabiTelAviv= new Domain.LeagueManagment.Team("Macabi Tel Aviv",teamOwner);
//    Domain.LeagueManagment.Team beitarYerushalaim= new Domain.LeagueManagment.Team("Beitar Yerushalaim",teamOwner);
//    Domain.LeagueManagment.Team hapoelRaanana= new Domain.LeagueManagment.Team ("Hapoel Raanana",teamOwner);
//    Domain.Users.Coach coach1= new Domain.Users.Coach(ms,"ali baba","0523456789","coach@gmail.com","coach123","coach123");
//
//    public UserTest() throws Exception {
//    }
//
//
//    /**Or**/
//    @Test
//    public void searchByName() {
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        try {
//            user.searchByName(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("name null",e.getMessage());
//
//        }
//        try {
//            user.searchByName("");
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("name empty",e.getMessage());
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchByName("tami"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp2);
//            Assert.assertEquals(ans2,user.searchByName("Or"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    /**Or**/
//    @Test
//    public void searchByKeyWord() throws Exception {
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        pp1.addRecords("record1");
//        pp1.addRecords("record2");
//        pp1.addRecords("record3");
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        try {
//            user.searchByKeyWord(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("keyWord null",e.getMessage());
//
//        }
//        try {
//            user.searchByKeyWord("");
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("keyWord empty",e.getMessage());
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchByKeyWord("tami"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp1);
//            Assert.assertEquals(ans2,user.searchByKeyWord("record2"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    /**Or**/
//
//    @Test
//    public void searchByLeague() throws Exception {
//        ms.setCurrSeason(season2020);
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//        hapoelBeerSheva.addPlayer(player1);
//        beitarYerushalaim.addPlayer(player2);
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague2= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague2,beitarYerushalaim,macabiTelAviv,hapoelRaanana);
//        season2019.addLeagueWithTeams(league2,teamsForLeague1);
//
//        try {
//            user.searchByLeague(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("leagueName null",e.getMessage());
//
//        }
//        try {
//            user.searchByLeague("");
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("leagueName empty",e.getMessage());
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchByLeague("tami"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp1);
//            ans2.add(pp3);
//            Assert.assertEquals(ans2,user.searchByLeague("LIGAT HAALUFUT"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    @Test
//    public void searchBySeason() {
//        ms.setCurrSeason(season2020);
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//        hapoelBeerSheva.addPlayer(player1);
//        beitarYerushalaim.addPlayer(player2);
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague2= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague2,beitarYerushalaim,macabiTelAviv,hapoelRaanana);
//        season2019.addLeagueWithTeams(league2,teamsForLeague2);
//
//        try {
//            user.searchBySeason(0);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("year not valid",e.getMessage());
//
//        }
//        try {
//            user.searchBySeason(2030);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("year not valid",e.getMessage());
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchBySeason(2003));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp2);
//            Assert.assertEquals(ans2,user.searchBySeason(2019));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    @Test
//    public void searchByTeamName() {
//        ms.setCurrSeason(season2020);
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//        hapoelBeerSheva.addPlayer(player1);
//        beitarYerushalaim.addPlayer(player2);
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague2= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague2,beitarYerushalaim,macabiTelAviv,hapoelRaanana);
//        season2019.addLeagueWithTeams(league2,teamsForLeague2);
//
//        try {
//            user.searchByTeamName(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("teamName null",e.getMessage());
//
//        }
//        try {
//            user.searchByTeamName("");
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("teamName empty",e.getMessage());
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchByTeamName("not in system"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp1);
//            ans2.add(pp3);
//            Assert.assertEquals(ans2,user.searchByTeamName("Hapoel"));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//
//    }
//
//    @Test
//    public void searchAllPlayers() {
//        ms.setCurrSeason(season2020);
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//        hapoelBeerSheva.addPlayer(player1);
//        beitarYerushalaim.addPlayer(player2);
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchAllPlayers());
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague2= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague2,beitarYerushalaim,macabiTelAviv,hapoelRaanana);
//        season2019.addLeagueWithTeams(league2,teamsForLeague2);
//
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp1);
//            ans2.add(pp2);
//            Assert.assertEquals(ans2,user.searchAllPlayers());
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    @Test
//    public void searchAllCoaches() {
//        ms.setCurrSeason(season2020);
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//        hapoelBeerSheva.addPlayer(player1);
//        beitarYerushalaim.addPlayer(player2);
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchAllCoaches());
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//
//        coach1.setPrivatePage(pp1);
//        pp1.setPageOwner(coach1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//
//
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp1);
//            Assert.assertEquals(ans2,user.searchAllCoaches());
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    @Test
//    public void searchAllTeams() {
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans=new LinkedHashSet<Domain.PrivatePage>();
//            Assert.assertEquals(ans,user.searchAllTeams());
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//        ms.setCurrSeason(season2020);
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//        hapoelBeerSheva.addPlayer(player1);
//        beitarYerushalaim.addPlayer(player2);
//        player1.setPrivatePage(pp1);
//        pp1.setPageOwner(player1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague2= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague2,beitarYerushalaim,macabiTelAviv,hapoelRaanana);
//        season2019.addLeagueWithTeams(league2,teamsForLeague2);
//
//        try {
//            LinkedHashSet<Domain.PrivatePage> ans2=new LinkedHashSet<Domain.PrivatePage>();
//            ans2.add(pp3);
//            Assert.assertEquals(ans2,user.searchAllTeams());
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//
//
//    }
//
//
//    @Test
//    public void filterOnlyTeams() {
//        coach1.setPrivatePage(pp1);
//        pp1.setPageOwner(coach1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//        LinkedHashSet<Domain.PrivatePage> pp= new LinkedHashSet<>();
//        Collections.addAll(pp,pp1,pp2,pp3);
//        LinkedHashSet<Domain.PrivatePage> ans= new LinkedHashSet<>();
//        ans.add(pp3);
//        try {
//            user.filterOnlyTeams(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("searchResults null",e.getMessage());
//
//        }
//
//        try {
//            user.filterOnlyTeams(new LinkedHashSet<>());
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("no results in list",e.getMessage());
//        }
//        try {
//            Assert.assertEquals(ans,user.filterOnlyTeams(pp));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    @Test
//    public void filterOnlyPlayers() {
//        coach1.setPrivatePage(pp1);
//        pp1.setPageOwner(coach1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//        LinkedHashSet<Domain.PrivatePage> pp= new LinkedHashSet<>();
//        Collections.addAll(pp,pp1,pp2,pp3);
//        LinkedHashSet<Domain.PrivatePage> ans= new LinkedHashSet<>();
//        ans.add(pp2);
//        try {
//            user.filterOnlyPlayers(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("searchResults null",e.getMessage());
//
//        }
//
//        try {
//            user.filterOnlyPlayers(new LinkedHashSet<>());
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("no results in list",e.getMessage());
//        }
//        try {
//            Assert.assertEquals(ans,user.filterOnlyPlayers(pp));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }
//
//    @Test
//    public void filterOnlyCoachs() {
//        coach1.setPrivatePage(pp1);
//        pp1.setPageOwner(coach1);
//        player2.setPrivatePage(pp2);
//        pp2.setPageOwner(player2);
//        hapoelKfarSaba.setPrivatePage(pp3);
//        pp3.setPageOwner(hapoelKfarSaba);
//        LinkedHashSet<Domain.PrivatePage> pp= new LinkedHashSet<>();
//        Collections.addAll(pp,pp1,pp2,pp3);
//        LinkedHashSet<Domain.PrivatePage> ans= new LinkedHashSet<>();
//        ans.add(pp1);
//        try {
//            user.filterOnlyCoachs(null);
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("searchResults null",e.getMessage());
//
//        }
//
//        try {
//            user.filterOnlyCoachs(new LinkedHashSet<>());
//            fail("expected exception was not occurred.");
//        } catch (Exception e) {
//            Assert.assertEquals(Exception.class, e.getClass());
//            Assert.assertEquals("no results in list",e.getMessage());
//        }
//        try {
//            Assert.assertEquals(ans,user.filterOnlyCoachs(pp));
//        } catch (Exception e) {
//            fail("no exception was expected");
//        }
//    }


}