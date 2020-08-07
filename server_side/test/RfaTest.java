import Domain.*;
import Domain.BudgetControl.BudgetReport;
import Domain.Events.Event;
import Domain.LeagueManagment.*;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.TeamRole;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class RfaTest {

    MainSystem ms = MainSystem.getInstance();
    Rfa nadav = new Rfa(ms,"nadav","052","nadav@","nadavS", "nadav123",MainSystem.birthDateFormat.parse("06-07-1992"));
    Rfa rfa = new Rfa(ms,"rfa","052","rfa@","rfa123", "rfa123",MainSystem.birthDateFormat.parse("06-07-1992"));
    HashMap<League, HashSet<Team>> teamsInCurrentSeasonLeagues = new HashMap<>();
    LinkedHashSet<Referee> referees = new LinkedHashSet<>();
    TeamStub t1 = new TeamStub("team1");
    TeamStub t2 = new TeamStub("team2");

    CalculationPolicy calculationPolicy1 = new CalculateOption1();
    SchedulingPolicy schedualeOption1 = new SchedualeOption1();

    public RfaTest() throws ParseException {
    }

    /**Yarden**/
    @Test
    public void addRefereeTest() throws ParseException {
        try {
            nadav.addReferee("moshe","0546145795","moshe@gmail.com","moshe235","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        Assert.assertTrue(ms.containsReferee("moshe235"));
        //invalid details

        try {
            nadav.addReferee("moshe", "0546", "moshe@gmail.com", "moshe458", "moshe458", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
            fail();
        }
        catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("phone number not valid",e.getMessage());
        }

    }

    /**Yarden**/
    @Test
    public void deleteReferee() throws Exception {

        /*referee is null*/
        try {
            nadav.deleteReferee(null);
            fail();
        }
        catch (Exception e){
            assertEquals(Exception.class, e.getClass());
            assertEquals("Referee is null",e.getMessage());
        }

        /*everything is ok*/
        Referee moshe = new Referee(ms,"moshe","0546145795","moseh@gmail.com","moshe123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
        TeamRole teamRole1 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden123", "yarden123", MainSystem.birthDateFormat.parse("08-09-1995"));
        TeamRole teamRole2 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden234", "yarden234", MainSystem.birthDateFormat.parse("09-09-1995"));
        TeamRole teamRole3 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden345", "yarden345", MainSystem.birthDateFormat.parse("10-09-1995"));
        TeamRole teamRole4 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden456", "yarden456", MainSystem.birthDateFormat.parse("11-09-1995"));
        TeamRole teamRole5 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden567", "yarden567", MainSystem.birthDateFormat.parse("12-09-1995"));
        TeamRole teamRole6 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden678", "yarden678", MainSystem.birthDateFormat.parse("13-09-1995"));
        TeamRole teamRole7 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden789", "yarden789", MainSystem.birthDateFormat.parse("14-09-1995"));
        TeamRole teamRole8 = new TeamRole(ms,"yarden","0546260171","yarden@gmail.com","yarden012", "yarden012", MainSystem.birthDateFormat.parse("15-09-1995"));

        Team t1 = new Team();
        Team t2 = new Team();

        teamRole1.becomePlayer();
        teamRole2.becomePlayer();
        teamRole3.becomePlayer();
        teamRole4.becomePlayer();
        teamRole5.becomePlayer();
        teamRole6.becomePlayer();
        teamRole7.becomePlayer();
        teamRole8.becomePlayer();

        t1.addPlayer(teamRole1.getPlayer());
        t1.addPlayer(teamRole2.getPlayer());
        t1.addPlayer(teamRole3.getPlayer());
        t1.addPlayer(teamRole4.getPlayer());

        t2.addPlayer(teamRole5.getPlayer());
        t2.addPlayer(teamRole6.getPlayer());
        t2.addPlayer(teamRole7.getPlayer());
        t2.addPlayer(teamRole8.getPlayer());

        t1.setName("Hapoel");
        Match m1 = new Match(0,0,t1,t2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"17-04-2020 20:00:00");

        nadav.deleteReferee(moshe);

        /*there is a future match in his list*/
        Referee ref = new Referee(ms,"ref","0546145795","moseh@gmail.com","ref123","ref123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
        Match m2 = new Match(0,0,t1,t2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , ref,"21-04-2022 20:00:00");
        try {
            nadav.deleteReferee(ref);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("You can not delete this referee",e.getMessage());
        }
    }

    /**Yarden**/
    @Test
    public void createNewLeagueTest(){

        /**null check**/
        try{
            nadav.createNewLeague(null,ms);
            fail();
        }
        catch (Exception e){
            assertEquals(Exception.class, e.getClass());
            assertEquals("Invalid parameters",e.getMessage());
        }

        try{
            nadav.createNewLeague("B",ms);
            Assert.assertTrue(ms.containsLeague("B"));
        }
        catch (Exception e){
            Assert.fail();
        }

        try{
            nadav.createNewLeague("B",ms);
            Assert.fail();
        }
        catch (Exception e){
            assertEquals(Exception.class, e.getClass());
            assertEquals("There is already league with the same name",e.getMessage());
        }


    }

    /**Yarden**/
    @Test
    public void defineSeasonToLeagueTest(){

        /**try to define past year**/
        try {
            League A = new League("A",ms);
            HashSet<Team> teams2 = new HashSet<>();

            teams2.add(t2);
            teamsInCurrentSeasonLeagues.put(A,teams2);
            SchedulingPolicy schedulingPolicy = new SchedualeOption1();
            CalculationPolicy calculationPolicy = new CalculateOption1();

            nadav.defineSeasonToLeague(schedulingPolicy, calculationPolicy,2011,A,teams2,new LinkedHashSet<>(),true);
            Assert.fail();

        }
        catch (Exception e){
            assertEquals(Exception.class, e.getClass());
            assertEquals("Invalid details",e.getMessage());
        }

        try {
            Referee ref1 = new Referee(ms,"ref1","0546145795","moseh@gmail.com","ref123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref2 = new Referee(ms,"ref2","0546145795","moseh@gmail.com","ref2123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
            Referee ref3 = new Referee(ms,"ref3","0546145795","moseh@gmail.com","ref3123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
            referees.add(ref1);
            referees.add(ref2);
            referees.add(ref3);
            HashSet<Team> teams1 = new HashSet<>();

            teams1.add(t1);
            League C = new League("C",ms);
            teamsInCurrentSeasonLeagues.put(C,teams1);
            SchedulingPolicy schedulingPolicy = new SchedualeOption1();
            CalculationPolicy calculationPolicy = new CalculateOption1();
            nadav.defineSeasonToLeague(schedulingPolicy, calculationPolicy,2021,C,teams1,referees,true);
            Assert.assertTrue(ms.getCurrSeason().getYear()==2021);
        }
        catch (Exception e){
            Assert.fail();
        }

    }

    /**Yarden**/
    @Test
    public void updateCurrSeasonTest(){
        Season s18 = new Season(ms,schedualeOption1,calculationPolicy1,2014);
        try {
            nadav.updateCurrSeason(1800);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("The season doesn't exist in the system",e.getMessage());
        }

        try{
            nadav.updateCurrSeason(2014);
            Assert.assertTrue(ms.getCurrSeason().equals(s18));
        }
        catch (Exception e){
            Assert.fail();
        }
    }

    /**Yarden**/
    @Test
    public void startSchedulingPolicyTest() throws Exception {

        HashMap<League, HashSet<Team>> teamPerLeague1 = new HashMap<League, HashSet<Team>>();
        League C = new League("C1", ms);
        League D = new League("D1", ms);

        TeamStub team11 = new TeamStub("team1");
        TeamStub team22 = new TeamStub("team2");
        TeamStub team33 = new TeamStub("team3");
        TeamStub team44 = new TeamStub("team4");
        TeamStub team55 = new TeamStub("team5");

        team11.setField(new Field("homeField1"));
        team22.setField(new Field("homeField2"));
        team33.setField(new Field("homeField3"));
        team44.setField(new Field("homeField4"));
        team55.setField(new Field("homeField5"));

        Referee moshe = new Referee(ms, "moshe", "0546145795", "moseh@gmail.com", "moshe123", "moshe123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref1@gmail.com", "ref1123", "ref1123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref2 = new Referee(ms, "ref2", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));

        LinkedHashSet<Referee> referees = new LinkedHashSet<>();
        referees.add(ref1);
        referees.add(ref2);

        LinkedHashSet<Team> group1 = new LinkedHashSet<>();
        LinkedHashSet<Team> group2 = new LinkedHashSet<>();
        //teams in league A
        group1.add(team11);
        group1.add(team22);
        group1.add(team33);
        //teams in league B
        group2.add(team44);
        group2.add(team55);

        Season s = new Season(ms,schedualeOption1, calculationPolicy1,2020);

        HashMap<Season,LinkedHashSet<Referee>> refereesInLeague = new HashMap<>();
        refereesInLeague.put(s,referees);
        D.setRefereesInLeague(refereesInLeague);
        C.setRefereesInLeague(refereesInLeague);

        s.addLeagueWithTeams(C,group1);
        s.addLeagueWithTeams(D,group2);

        rfa.startSchedulingPolicy(s);

        Assert.assertTrue(team11.getAway().size()+team11.getHome().size()==2);
        Assert.assertTrue(team44.getAway().size()+team44.getHome().size()==1);

    }

    /**Yarden**/
    @Test
    public void startCalculationPolicyTest() throws Exception {

        HashMap<League, HashSet<Team>> teamPerLeague = new HashMap<League, HashSet<Team>>();
        League A = new League("A2",ms);
        League B = new League("B2",ms);

        TeamStub team1 = new TeamStub("team1");
        TeamStub team2 = new TeamStub("team2");
        TeamStub team3 = new TeamStub("team3");
        TeamStub team4 = new TeamStub("team4");
        TeamStub team5 = new TeamStub("team5");
        TeamStub team6 = new TeamStub("team6");

        Referee moshe = new Referee(ms,"moshe","0546145795","moseh@gmail.com","moshe123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));

        Match m1 = new Match(6,4,team1,team2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"17-04-2020 20:00:00");

        Match m2 = new Match(4,2,team3,team1, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"14-04-2020 20:00:00");

        Match m3 = new Match(3,3,team5,team4, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"14-03-2020 20:00:00");

        team1.addMatchToAwayMatches(m1);
        team1.addMatchToHomeMatches(m2);

        team2.addMatchToHomeMatches(m1);
        team3.addMatchToAwayMatches(m2);

        team5.addMatchToAwayMatches(m3);
        team4.addMatchToHomeMatches(m3);

        HashSet<Team> group1 = new HashSet<>();
        HashSet<Team> group2 = new HashSet<>();
        group1.add(team1);
        group1.add(team2);
        group1.add(team3);
        group2.add(team4);
        group2.add(team5);

        Match m4 = new Match(3,3,team5,team6, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , moshe,"14-03-2020 20:00:00");

        group2.add(team6);
        team5.addMatchToAwayMatches(m4);
        team6.addMatchToHomeMatches(m4);

        Season s1 = new Season(ms,schedualeOption1, calculationPolicy1,2019);
        s1.addLeagueWithTeams(A,group1);
        s1.addLeagueWithTeams(B,group2);

        try {
            nadav.startCalculationPolicy(null);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("Season is null",e.getMessage());
        }

        nadav.startCalculationPolicy(s1);
        Assert.assertTrue(team1.getScore()==2 && team4.getScore()==1 && team5.getScore()==2 && team6.getScore()==1);

    }

    /**Yarden**/
    @Test
    public void firstRoleForBudgetTest(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        TeamStub t3 = new TeamStub("team13");
        try {
            Date d1 = sdf.parse("05-01-2020");
            Date d2 = sdf.parse("15-02-2020");
            Date d3 = sdf.parse("05-03-2020");
            Date d4 = sdf.parse("05-04-2020");
            Date d5 = sdf.parse("05-05-2020");

            LinkedList<BudgetReport> reports= new LinkedList<>();

            BudgetReport r1 = new BudgetReport("a", 500);
            r1.setNow(d1);
            BudgetReport r2 = new BudgetReport("a",166);
            r2.setNow(d2);
            BudgetReport r3 = new BudgetReport("a",-160);
            r3.setNow(d3);
            BudgetReport r4 = new BudgetReport("a",800);
            r4.setNow(d4);
            BudgetReport r5 = new BudgetReport("a",80);
            r5.setNow(d5);

            reports.add(r1);
            reports.add(r2);
            reports.add(r3);
            reports.add(r4);
            reports.add(r5);
            t3.setActive(true);
            ms.addActiveTeam(t3);

            t3.getBudgetControl().setIncomeAndExpenses(reports);
            HashSet<Team> teamsExceptions1 = nadav.firstRoleForBudget(2020);
            HashSet<Team>toCheck1 = new HashSet<>();
            toCheck1.add(t3); //#

            /**Jan to March in 2020**/
            Assert.assertEquals(teamsExceptions1,toCheck1);
            BudgetReport r6 = new BudgetReport("a",160);
            r6.setNow(d3);
            reports.add(r6);
            t3.getBudgetControl().setIncomeAndExpenses(reports);
            HashSet<Team> teamsExceptions2 = nadav.firstRoleForBudget(2020);
            Assert.assertTrue(teamsExceptions2.size()==0);

        }
        catch (Exception e){
            Assert.fail();
        }

        TeamStub t4 = new TeamStub("team4");
        try {
            Date d1 = sdf.parse("05-01-2019");
            Date d2 = sdf.parse("15-02-2019");
            Date d3 = sdf.parse("05-03-2019");
            Date d4 = sdf.parse("05-04-2019");
            Date d5 = sdf.parse("05-05-2019");
            Date d6 = sdf.parse("05-06-2019");
            Date d7 = sdf.parse("05-07-2019");
            Date d8 = sdf.parse("05-08-2019");
            Date d9 = sdf.parse("05-09-2019");
            Date d10 = sdf.parse("05-10-2019");
            Date d11 = sdf.parse("05-11-2019");
            Date d12 = sdf.parse("05-12-2019");

            LinkedList<BudgetReport> reports= new LinkedList<>();

            BudgetReport r1 = new BudgetReport("a", 500);
            r1.setNow(d1);
            BudgetReport r2 = new BudgetReport("a",166);
            r2.setNow(d2);
            BudgetReport r2_1 = new BudgetReport("a",-166);
            r2_1.setNow(d2);
            BudgetReport r3 = new BudgetReport("a",160);
            r3.setNow(d3);
            BudgetReport r4 = new BudgetReport("a",800);
            r4.setNow(d4);
            BudgetReport r5 = new BudgetReport("a",180);
            r5.setNow(d5);
            BudgetReport r6 = new BudgetReport("a",622);
            r6.setNow(d6);
            BudgetReport r7 = new BudgetReport("a",622);
            r7.setNow(d7);
            BudgetReport r8 = new BudgetReport("a",622);
            r8.setNow(d8);
            BudgetReport r9 = new BudgetReport("a",622);
            r9.setNow(d9);
            BudgetReport r9_1 = new BudgetReport("a",-622);
            r9_1.setNow(d9);
            BudgetReport r10 = new BudgetReport("a",622);
            r10.setNow(d10);
            BudgetReport r11 = new BudgetReport("a",622);
            r11.setNow(d11);
            BudgetReport r12 = new BudgetReport("a",622);
            r12.setNow(d12);

            reports.add(r1);
            reports.add(r2);
            reports.add(r2_1);
            reports.add(r3);
            reports.add(r4);
            reports.add(r5);
            reports.add(r6);

            t4.setActive(true);
            ms.addActiveTeam(t4);

            t4.getBudgetControl().setIncomeAndExpenses(reports);
            HashSet<Team> teamsExceptions2 = nadav.firstRoleForBudget(2019);
            /**There isn't exception teams**/
            Assert.assertTrue(teamsExceptions2.size()==2);

            reports.add(r7);
            reports.add(r8);
            reports.add(r9);
            reports.add(r9_1);
            reports.add(r10);
            reports.add(r11);
            reports.add(r12);
            t4.getBudgetControl().setIncomeAndExpenses(reports);
            teamsExceptions2 = nadav.firstRoleForBudget(2019);
            Assert.assertTrue(teamsExceptions2.size()==1);
            ms.removeActiveTeam(t4);
            ms.removeActiveTeam(t3);
        }
        catch (Exception e){
            Assert.fail();
        }
    }

    /**Yarden**/
    @Test
    public void secondRoleForBudgetTest(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        TeamStub t3 = new TeamStub("team3");
        try {
            Date d1 = sdf.parse("05-01-2020");
            Date d2 = sdf.parse("15-02-2020");
            Date d3 = sdf.parse("05-03-2020");
            Date d4 = sdf.parse("05-04-2020");
            Date d5 = sdf.parse("05-05-2020");
            Date d6 = sdf.parse("05-06-2020");

            LinkedList<BudgetReport> reports= new LinkedList<>();

            BudgetReport r1 = new BudgetReport("a", 1500);
            r1.setNow(d1);
            BudgetReport r2 = new BudgetReport("a",1666);
            r2.setNow(d2);
            BudgetReport r3 = new BudgetReport("a",-600);
            r3.setNow(d3);
            BudgetReport r4 = new BudgetReport("a",800);
            r4.setNow(d4);
            BudgetReport r5 = new BudgetReport("a",2580);
            r5.setNow(d5);
            BudgetReport r6 = new BudgetReport("a",1622);
            r6.setNow(d6);

            reports.add(r1);
            reports.add(r2);
            reports.add(r3);
            reports.add(r4);
            reports.add(r5);
            reports.add(r6);
            t3.setActive(true);
            ms.addActiveTeam(t3);

            t3.getBudgetControl().setIncomeAndExpenses(reports);
            HashSet<Team> teamsExceptions1 = nadav.secondRoleForBudget(2020);

//            Assert.assertTrue(teamsExceptions1.size()==1);
        }
        catch (Exception e){
            Assert.fail();
        }

        TeamStub t4 = new TeamStub("team4");
        try {
            Date d1 = sdf.parse("05-01-2019");
            Date d2 = sdf.parse("15-02-2019");
            Date d3 = sdf.parse("05-03-2019");
            Date d4 = sdf.parse("05-04-2019");
            Date d5 = sdf.parse("05-05-2019");
            Date d6 = sdf.parse("05-06-2019");
            Date d7 = sdf.parse("05-07-2019");
            Date d8 = sdf.parse("05-08-2019");
            Date d9 = sdf.parse("05-09-2019");
            Date d10 = sdf.parse("05-10-2019");
            Date d11 = sdf.parse("05-11-2019");
            Date d12 = sdf.parse("05-12-2019");

            LinkedList<BudgetReport> reports= new LinkedList<>();

            BudgetReport r1 = new BudgetReport("a", 500);
            r1.setNow(d1);
            BudgetReport r2 = new BudgetReport("a",166);
            r2.setNow(d2);
            BudgetReport r2_1 = new BudgetReport("a",-166);
            r2_1.setNow(d2);
            BudgetReport r3 = new BudgetReport("a",660);
            r3.setNow(d3);
            BudgetReport r4 = new BudgetReport("a",1800);
            r4.setNow(d4);
            BudgetReport r5 = new BudgetReport("a",180);
            r5.setNow(d5);
            BudgetReport r6 = new BudgetReport("a",622);
            r6.setNow(d6);
            BudgetReport r7 = new BudgetReport("a",622);
            r7.setNow(d7);
            BudgetReport r8 = new BudgetReport("a",622);
            r8.setNow(d8);
            BudgetReport r9 = new BudgetReport("a",622);
            r9.setNow(d9);
            BudgetReport r9_1 = new BudgetReport("a",-622);
            r9_1.setNow(d9);
            BudgetReport r10 = new BudgetReport("a",622);
            r10.setNow(d10);
            BudgetReport r11 = new BudgetReport("a",622);
            r11.setNow(d11);
            BudgetReport r12 = new BudgetReport("a",622);
            r12.setNow(d12);

            reports.add(r1);
            reports.add(r2);
            reports.add(r2_1);
            reports.add(r3);
            reports.add(r4);
            reports.add(r5);
            reports.add(r6);
            reports.add(r7);
            reports.add(r8);
            reports.add(r9);
            reports.add(r9_1);
            reports.add(r10);
            reports.add(r11);
            reports.add(r12);
            t4.setActive(true);
            ms.addActiveTeam(t4);

            t4.getBudgetControl().setIncomeAndExpenses(reports);
            HashSet<Team> teamsExceptions2 = nadav.secondRoleForBudget(2019);
            /**There isn't exception teams**/
            Assert.assertTrue(teamsExceptions2.size()==1);
            ms.removeActiveTeam(t3);
            ms.removeActiveTeam(t4);

        }
        catch (Exception e){
            Assert.fail();
        }
    }

    /**or**/
    @Test
    public void answerRequestTest() throws ParseException {
        TeamStub team = new TeamStub("name");
        TeamRole owner= new TeamRole(ms,"coach","1234567890","coach@gmail.com","coach101","coach101",MainSystem.birthDateFormat.parse("01-11-2000"));
        owner.becomeTeamOwner();
        try {
            team.addTeamOwner(owner.getTeamOwner());
        } catch (Exception e) {
            fail();
        }
        Rfa rfa= new Rfa(ms,"nadav","052","nadav@gmail.com","nadavS", "nadav123", MainSystem.birthDateFormat.parse("01-02-1990"));
        rfa.getTeamRequests().add(team);

        try {
            rfa.answerRequest(team,true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        Assert.assertTrue(rfa.getTeamRequests().size()==0);

        try {
            rfa.answerRequest(team,true);
            fail();
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("team not in request list",e.getMessage());
        }
    }
}