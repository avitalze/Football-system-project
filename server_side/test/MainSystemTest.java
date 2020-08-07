import Domain.*;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Season;
import Domain.Users.*;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MainSystemTest {
    MainSystem ms= MainSystem.getInstance();

    /**or**/
    @Test
    public void removeUser() {
        User user= new User(ms);

        Assert.assertTrue(ms.removeUser(user));
        Assert.assertFalse(ms.getUsers().contains(user));
    }

    /**or**/
    @Test
    public void addUser() {
        User user= new User(ms);
        Assert.assertTrue(ms.getUsers().contains(user));
        Assert.assertFalse(ms.addUser(user));
    }


    @Test
    /**or**/
    public void removeLeague() {
        try {
            League league= new League("Laliga",ms);
            Assert.assertTrue(ms.getLeagues().size()==1);
            Assert.assertTrue(ms.removeLeague(league));
            Assert.assertTrue(ms.getLeagues().size()==0);
        } catch (Exception e) {
            fail();
            e.printStackTrace();

        }
    }

    @Test
    /**or**/
    public void addLeague() {
        try {
            League league= new League("Laliga",ms);
            Assert.assertTrue(ms.getLeagues().size()==1);
            Assert.assertFalse(ms.addLeague(league));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    /**or**/
    public void removeSeason() {
        try {
            SchedulingPolicy sp= new SchedualeOption1();
            CalculationPolicy cp= new CalculateOption1();
            Season s= new Season(ms,sp,cp,2004);
            Assert.assertTrue(ms.getSeasons().size()==1);
            Assert.assertTrue(ms.removeSeason(s));
            Assert.assertTrue(ms.getSeasons().size()==0);
        } catch (Exception e) {
            fail();
            e.printStackTrace();

        }
    }

    @Test
    /**or**/
    public void addSeason() {
        try {
            SchedulingPolicy sp= new SchedualeOption1();
            CalculationPolicy cp= new CalculateOption1();
            Season s= new Season(ms,sp,cp,2004);
            Assert.assertTrue(ms.getSeasons().size()==1);
            Assert.assertFalse(ms.addSeason(s));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    /**or**/
    public void removeActiveTeam() {
        try {
            TeamStub team = new TeamStub("hapoel beersheva");
            ms.addActiveTeam(team);
            Assert.assertTrue(ms.getActiveTeams().size()==1);
            Assert.assertTrue(ms.removeActiveTeam(team));
            Assert.assertTrue(ms.getSeasons().size()==0);
        } catch (Exception e) {
            fail();
            e.printStackTrace();

        }
    }

    @Test
    /**or**/
    public void addActiveTeam() {
        try {
            TeamStub team = new TeamStub("hapoel beersheva");
            ms.addActiveTeam(team);
            Assert.assertTrue(ms.getActiveTeams().size()==1);
            Assert.assertFalse(ms.addActiveTeam(team));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void addTeamName() {

       Assert.assertTrue(ms.addTeamName("teamName"));
        Assert.assertFalse(ms.addTeamName("teamName"));
    }

    @Test
    public void removeUserName() {
        Assert.assertFalse(ms.removeUserName("userName"));
        ms.addUserName("userName");
        Assert.assertTrue(ms.removeUserName("userName"));
    }

    @Test
    public void removeTeamName() {

        Assert.assertFalse(ms.removeTeamName("teamName"));
        ms.addTeamName("teamName");
        Assert.assertTrue(ms.removeTeamName("teamName"));
    }

    @Test
    /**or**/
    public void firstStartSystem() throws ParseException {
        ms.startSystem();
        Assert.assertTrue(ms.getSystemManagers().size()==1);
        Assert.assertTrue(ms.getExtSystem()!=null);

    }

    @Test
    /**or**/
    public void startSystem() throws ParseException {
        ms.startSystem();
        Assert.assertTrue(ms.getExtSystem()!=null);
        Assert.assertTrue(ms.getSystemManagers().size()==1);
    }

    @Test
    /**or**/
    public void numOfRFA() throws ParseException {
        Assert.assertEquals(0,ms.numOfRfa());
        Rfa rfa= new Rfa(ms,"rfa123","0542150912","oralfasi@gmail.com","rfa1234","rfa1234",MainSystem.birthDateFormat.parse("09-12-1995"));
        Assert.assertEquals(1,ms.numOfRfa());
    }


    @Test
    public void checkValidDetails() {
        try {
            ms.checkValidDetails("name",null,"user101","0542150192","email@gmail.com");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("user name not valid",e.getMessage());
        }
        try {
            ms.checkValidDetails("name","user2","user1","0542150192","email@gmail.com");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("password not valid",e.getMessage());
        }
        try {
            ms.checkValidDetails("name","user2","user101","0542192","email@gmail.com");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("phone number not valid",e.getMessage());
        }
        try {
            ms.checkValidDetails("name","user2","user101","0542150912","email@gmail");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("email not valid",e.getMessage());
        }
    }


//    @Test
//    public void logIn() {
//        try {
//            ms.logIn("user",null);
//            Assert.fail();
//        } catch (Exception e) {
//            assertEquals(Exception.class, e.getClass());
//            assertEquals("password null",e.getMessage());
//        }
//
//        try {
//            ms.logIn("username","1234");
//            Assert.fail();
//        } catch (Exception e) {
//            assertEquals(Exception.class, e.getClass());
//            assertEquals("password not valid",e.getMessage());
//        }
//
//        try {
//            SystemManager manager= new SystemManager(ms,"systemManager","0542150912","sys@gmail.com","system101","sys1234",MainSystem.birthDateFormat.parse("01-05-1997"));
//            User userLog= ms.logIn("system101","sys1234");
//            assertEquals(manager,userLog);
//            //assertEquals("password not valid",e.getMessage());
//        } catch (Exception e) {
//            Assert.fail();
//
//        }
//
//        try {
//           ms.logIn("usernotsys","no1234523");
//            Assert.fail();
//        } catch (Exception e) {
//            assertEquals(Exception.class, e.getClass());
//            assertEquals("details not correct, no fan in system",e.getMessage());
//        }
//
//    }

    @Test
    public void signInAsTeamOwner() {

        ms.setUsers(new LinkedList<>());
        try {
            ms.signInAsTeamOwner("name1","0542150912","name1@gmail.co.il","towner","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.assertTrue(ms.getUserNames().contains("towner"));
            User u= ms.getUsers().get(0);
            Assert.assertTrue(u instanceof TeamRole);
            Assert.assertTrue(((TeamRole)u).isTeamOwner());
        } catch (Exception e) {
            Assert.fail();

        }
        try {
            ms.signInAsTeamOwner("name1","0542150912","name1@gmail.co.il","towner","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("user name not valid",e.getMessage());
        }

    }

    @Test
    public void signInAsCoach() {

        ms.setUsers(new LinkedList<>());
        try {
            ms.signInAsCoach("name1","0542150912","name1@gmail.co.il","coach","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.assertTrue(ms.getUserNames().contains("coach"));
            User u= ms.getUsers().get(0);
            Assert.assertTrue(u instanceof TeamRole);
            Assert.assertTrue(((TeamRole)u).isCoach());
        } catch (Exception e) {

            Assert.fail();

        }
        try {
            ms.signInAsCoach("name1","0542150912","name1@gmail.co.il","coach","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("user name not valid",e.getMessage());
        }

    }

    @Test
    public void signInAsPlayer() {

        ms.setUsers(new LinkedList<>());
        try {
            ms.signInAsPlayer("name1","0542150912","name1@gmail.co.il","player","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.assertTrue(ms.getUserNames().contains("player"));
            User u= ms.getUsers().get(0);
            Assert.assertTrue(u instanceof TeamRole);
            Assert.assertTrue(((TeamRole)u).isPlayer());
        } catch (Exception e) {
            Assert.fail();

        }
        try {
            ms.signInAsPlayer("name1","0542150912","name1@gmail.co.il","player","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("user name not valid",e.getMessage());
        }

    }

    @Test
    public void signInAsFan() {

        ms.setUsers(new LinkedList<>());
        try {
            ms.signInAsFan("name1","0542150912","name1@gmail.co.il","fan","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.assertTrue(ms.getUserNames().contains("fan"));
            User u= ms.getUsers().get(0);
            Assert.assertTrue(u instanceof Fan);
        } catch (Exception e) {
            Assert.fail();

        }
        try {
            ms.signInAsPlayer("name1","0542150912","name1@gmail.co.il","fan","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("user name not valid",e.getMessage());
        }

    }




    @Test
    public void signInAsRFA() {

        ms.setUsers(new LinkedList<>());
        try {
            ms.signInAsRFA("name1","0542150912","name1@gmail.co.il","rfa","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.assertTrue(ms.getUserNames().contains("rfa"));
            User u= ms.getUsers().get(0);
            Assert.assertTrue(u instanceof Rfa);
        } catch (Exception e) {
            Assert.fail();

        }
        try {
            ms.signInAsRFA("name1","0542150912","name1@gmail.co.il","rfa","password123",MainSystem.getInstance().birthDateFormat.parse(("01-01-1885")),false);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("user name not valid",e.getMessage());
        }

    }



}