package IntegrationTests;

import Domain.Complaint;
import Domain.Enums.TeamManagerPermissions;
import Domain.Events.Event;
import Domain.LeagueManagment.*;
import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.MainSystem;
import Domain.TeamSubscription;
import Domain.Users.*;
import Stubs.SystemManagerStub_A;
import Stubs.TeamOwnerStub_A;
import Stubs.TeamRoleStub_A;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SystemManagerIntegration {

    MainSystem sysetm = MainSystem.getInstance();
    SystemManager sm=new SystemManager(sysetm,"avi","0545233681","a@gmail.com","avi123","1234567",MainSystem.birthDateFormat.parse("01-02-1990"));
   TeamRole teamRole =new TeamRole(sysetm,"stabi","0523893418","atab@gmaol.com","stab","1234556",MainSystem.birthDateFormat.parse("01-02-1990"));
    Team t = new Team();

    public SystemManagerIntegration() throws ParseException { }


    /**or
     * Intergation : SystemManager-MainSystem-Users
     * @throws ParseException
     */
    @Test
    public void removeUser() throws ParseException{
        MainSystem system =MainSystem.getInstance();

        Fan f1=new Fan(sysetm,"eden","ee","e","e","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Fan f2=new Fan(sysetm,"avital","ee","e","a","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Rfa rfa1=new Rfa(f1,system);


        /** dell RFA**/
        try {
            sm.removeUser(rfa1);
            Assert.assertFalse(system.getUsers().contains(rfa1));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }

        /**userToDelete instanceof SystemManager**/



        Fan f3=new Fan(sysetm,"or","ee","e","o","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        SystemManager systemManager1=new SystemManager(f3,system);
        /** dell systemManger**/
        try {
            sm.removeUser(systemManager1);
            Assert.assertFalse(system.getUsers().contains(systemManager1));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /** delete referee**/
        Fan f4=new Fan(sysetm,"adi","ee","e","ad","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Referee ref1Pass=new Referee(f4,system);
        Fan f5=new Fan(sysetm,"yardi","ee","e","y","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Referee ref2NotPass=new Referee(f5,system);
        Team t1 = new Team();
        Team t2 = new Team();
        try {
            Match m1Pass = new Match(0, 0, t1, t2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                    , ref1Pass, "17-04-2020 20:00:00");
            Match m2NotPass = new Match(0, 0, t1, t2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                    , ref2NotPass, "17-06-2020 20:00:00");
            /** cannot remove referee , he has matches in this season **/
            sm.removeUser(ref2NotPass);
            fail("expected exception was not occurred");
        }catch (Exception ex){
            Assert.assertEquals(Exception.class,ex.getClass());
            Assert.assertEquals("You cannot remove referee , he has matches in this season",ex.getMessage());
        }
        try{
            sm.removeUser(ref1Pass);
            Assert.assertFalse(system.getUsers().contains(ref1Pass));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**delete TeamRole**/
        /**coach**/
        TeamRole coachConnect= new TeamRole(system,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        coachConnect.becomeCoach();
        coachConnect.getCoach().setCoachTeam(t1);
        TeamRole coachNotConnect= new TeamRole(system,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        coachNotConnect.becomeCoach();
        /**cant dell coach connect to team **/
        try{
            sm.removeUser(coachConnect);
            fail("expected exception was not occurred");
        }
        catch (Exception ex){
            Assert.assertEquals(Exception.class,ex.getClass());
            Assert.assertEquals("you have to subscribe another coach to "+(coachConnect.getCoach()).getCoachTeam().getName() +" team first",ex.getMessage());
        }
        /**dell coach not connect to team **/
        try {
            sm.removeUser(coachNotConnect);
            Assert.assertFalse(system.getUsers().contains(coachNotConnect));
        } catch (Exception e) {
            fail("test fail");
            e.printStackTrace();
        }

        /**player belong to any Team which hasn't more than 11 player throws Exception**/
        TeamRole p1= new TeamRole(system,"mimi","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        p1.becomePlayer();
        p1.getPlayer().setPlayerTeam(t);
        try{
            sm.removeUser(p1);
            Assert.fail("expected exception was not occurred");
        }
        catch (Exception ex){
            Assert.assertEquals(Exception.class,ex.getClass());
            Assert.assertEquals("You Cannot Delete player From " + p1.getPlayer().getTeam().getName() + " team ,any team have to be at least 11 Players!",ex.getMessage());
        }
        /**dell player belong to any Team which has more than 11 player s*/
        //team with 11 players
        Team fullTeam=new Team();
        HashSet<Player> players= new HashSet<>();
        //TeamRole coach= new TeamRole(system,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        int counter=0;
        while(counter<11){
            TeamRole player= new TeamRole(system,"player", "1234567890","email@gmail.com","player"+counter,"player"+counter,MainSystem.birthDateFormat.parse("09-12-1995"));
            player.becomePlayer();
            player.getPlayer().setPlayerTeam(fullTeam);
            players.add(player.getPlayer());
            counter++;
        }
        p1.getPlayer().setPlayerTeam(fullTeam);
        players.add(p1.getPlayer());
        fullTeam.setPlayers(players);
        try {
            sm.removeUser(p1);
            Assert.assertFalse(system.getUsers().contains(p1));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }
/**delete teamOwner**/
        /**if is TeamOwner-> if he is a founder of any Team from Team list which he hold**/
        TeamRole teamOwner1= new TeamRole(system,"timi","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        teamOwner1.becomeTeamOwner();
        teamOwner1.getTeamOwner().addNewTeam(t);//!!!!!!!!!!!!!!!!!!!!!!!! set becauese of bad pull
        t.setFounder(teamOwner1.getTeamOwner());
        try{
            sm.removeUser(teamOwner1);
            fail("expected exception was not occurred");
        }
        catch (Exception ex){
            Assert.assertEquals(Exception.class,ex.getClass());
            Assert.assertEquals(teamOwner1.getName() + " is founder of: "+t.getName() +"please replace the team's fonder",ex.getMessage());
        }

        /**delete teamOwner and all his sub -not founder of teams**/
        //down :)

        /**dell team manager and all subs**/
        Fan f6=new Fan(sysetm,"f6","ee","e","f6","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        TeamRole founder=new TeamRole(f6);
        founder.becomeTeamOwner();
        Fan f7=new Fan(sysetm,"f7","ee","e","f7","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        //TeamRole teamRoleTeamMange = new TeamRole(f7);
        //TeamManager teamMange=null;
        //TeamOwner subTeamOwner=null;
        TeamRole subTeamOwner=null;
        TeamRole teamRoleTeamMange=null;
        try {
            founder.getTeamOwner().addNewTeam(fullTeam);
            HashSet<TeamManagerPermissions> per=new HashSet<>();
            per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
            teamRoleTeamMange = founder.getTeamOwner().subscribeTeamManager(f7, fullTeam, per);


            Fan f8=new Fan(sysetm,"f8","ee","e","f8","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            subTeamOwner=teamRoleTeamMange.getTeamManager().subscribeTeamOwner(f8,fullTeam);

        } catch (Exception ex) {
            //Assert.fail("test fail");
            ex.printStackTrace();
        }
        /** test if teamManage dell from system and subTeamOwner- dell from team  **/
        try{
            sm.removeUser(teamRoleTeamMange);
            Assert.assertFalse(system.getUsers().contains(teamRoleTeamMange));
            Assert.assertNull(fullTeam.getTeamManager());
            Assert.assertFalse(fullTeam.getTeamOwners().contains(subTeamOwner));

        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**delete teamOwner and all his sub -not founder of teams**/
        Team closeTeam=new Team();
        TeamRole co= new TeamRole(system,"co","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
        co.becomeCoach();
        co.getCoach().setCoachTeam(closeTeam);
        closeTeam.setCoach(co.getCoach());
        Field field=new Field("f1");
        closeTeam.setField(field);
        founder.getTeamOwner().addNewTeam(closeTeam);
        Fan f12=new Fan(sysetm,"f12","ee","e","f12","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Rfa rfa=new Rfa(f12,system);
        TeamRole subTO=null;
        TeamRole subSubTeamManager=null;
        TeamRole subSubTeamOwner=null;
        try{
            Fan f9=new Fan(sysetm,"f9","ee","e","f9","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            subTO = founder.getTeamOwner().subscribeTeamOwner(f7, fullTeam);
            subTO = founder.getTeamOwner().subscribeTeamOwner(f7, closeTeam);
            founder.getTeamOwner().deleteTeam(closeTeam);
            Fan f10=new Fan(sysetm,"f10","ee","e","f10","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            Fan f11=new Fan(sysetm,"f11","ee","e","f11","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            HashSet<TeamManagerPermissions> per=new HashSet<>();
            per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
            //subSubTeamManager = subTO.getTeamOwner().subscribeTeamManager(f10, fullTeam, per);

            subSubTeamOwner=subTO.getTeamOwner().subscribeTeamOwner(f11,fullTeam);
            subTO.getTeamOwner().requestNewTeam("lililo_nameOfTeam");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try{
            sm.removeUser(subTO);
            Assert.assertFalse(system.getUsers().contains(subTO));
            Assert.assertFalse(fullTeam.getTeamOwners().contains(subTO));
            Assert.assertFalse(closeTeam.getTeamOwners().contains(subTO));
            Assert.assertNull(fullTeam.getTeamManager()); //dell subs
            Assert.assertFalse(fullTeam.getTeamOwners().contains(subSubTeamOwner)); //dell subs
            Assert.assertFalse(Rfa.teamRequests.contains("lililo_nameOfTeam"));
            Assert.assertFalse(system.getActiveTeams().contains("lililo_nameOfTeam"));

        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**remove fan from system , delete his all complaints-not relevant anyMore.**/

        try{
            Fan fumToDell=new Fan(sysetm,"fumToDell","ee","e","fumToDell","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            Complaint c=new Complaint(fumToDell,system);
            fumToDell.addNewComplaint("aifffff");

            //test
            sm.removeUser(fumToDell);
            Assert.assertFalse(system.getUsers().contains(fumToDell));
            //Assert.assertFalse(SystemManager.complaints.contains(c)); //???
            //Assert.assertFalse(closeTeam.getTeamOwners().contains(subTO));

        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }





        /**need to delete the user**/
        //Domain.Users.SystemManager sm= new Domain.Users.SystemManager(system,"d","df","df","df","df");

        //system.removeUser(userToDelete); - check if remove from system
    }

    /**avutal
     * Integration : SystemManager-Team-Match-League
     * @throws ParseException
     */
    @Test
    public void removeTeamFromSystem()throws ParseException {
        TeamStub t1 = new TeamStub("team1");
        TeamStub t2 = new TeamStub("team2");
        TeamStub t3 = new TeamStub("team3");
        TeamStub t4 = new TeamStub("team4");
        TeamStub t5 = new TeamStub("team5");
        SchedulingPolicy schedulingPolicy = new SchedualeOption1();
        CalculationPolicy calculationPolicy = new CalculateOption1();
        League l =null;
        try {
            l = new League("A",sysetm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Season s = new Season(sysetm, schedulingPolicy, calculationPolicy,2020);

        HashSet<Team> teamsInLeag=new HashSet<Team>();
        HashSet<Team> teams2 = new HashSet<Team>();
        teamsInLeag.add(t1);
        teamsInLeag.add(t2);
        teamsInLeag.add(t3);
        Fan fRFA=new Fan(sysetm,"fRFA","ee","e","fRFA","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Rfa rfa1=new Rfa(fRFA,sysetm);
        LinkedHashSet<Referee> referees = new LinkedHashSet<>();
        Referee ref1 = new Referee(sysetm,"ref1","0546145795","moseh@gmail.com","ref123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref2 = new Referee(sysetm,"ref2","0546145795","moseh@gmail.com","ref2123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
        Referee ref3 = new Referee(sysetm,"ref3","0546145795","moseh@gmail.com","ref3123","moshe123","a",MainSystem.birthDateFormat.parse("08-09-1995"));
        referees.add(ref1);
        referees.add(ref2);
        referees.add(ref3);
        try {
            rfa1.defineSeasonToLeague(schedulingPolicy,calculationPolicy,2020,l,teamsInLeag,referees,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** check checkValidTeam private func**/


        /**test-Remove Active team**/
        Team activeTeam=new Team();
        activeTeam.setActive(true);
        sysetm.addActiveTeam(t1);//??
        activeTeam.setName("macabi");
        sysetm.addTeamName("macabi");
        Fan f31=new Fan(sysetm,"f31","ee","e","f31","E",
                MainSystem.birthDateFormat.parse("02-11-1996"));
        TeamRole teamOwnerActiveTeam=new TeamRole(f31);
        teamOwnerActiveTeam.becomeTeamOwner();
        teamOwnerActiveTeam.getTeamOwner().addNewTeam(activeTeam);
        Fan f32=null;
        Fan f33=null;
        TeamRole subTeamManager=null;
        TeamRole subCoach=null;
        TeamRole p1=null;
        try {
            activeTeam.setFounder(teamOwnerActiveTeam.getTeamOwner());
            activeTeam.addTeamOwner(teamOwnerActiveTeam.getTeamOwner());

            f32=new Fan(sysetm,"f32","ee","e","f32","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            HashSet<TeamManagerPermissions> permissions=new HashSet<>();
            permissions.add(TeamManagerPermissions.addRemoveEditPlayer);
            subTeamManager= teamOwnerActiveTeam.getTeamOwner().subscribeTeamManager(f32,activeTeam,permissions);
            f33=new Fan(sysetm,"f33","ee","e","f33","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            subCoach= new TeamRole(sysetm,"michael","0522150912","teamO@gmail.com","coach2232","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
            subCoach.becomeCoach();
            subCoach.getCoach().setCoachTeam(activeTeam);
            activeTeam.setCoach(subCoach.getCoach());

            p1= new TeamRole(sysetm,"mimi","0522150912","teamO@gmail.com","p1","coach2232",MainSystem.birthDateFormat.parse("09-12-1995"));
            p1.becomePlayer();
            //p1.getPlayer().setPlayerTeam(activeTeam);
            teamOwnerActiveTeam.getTeamOwner().addPlayer(p1,"ll",activeTeam);
            //subTeamOwner=teamOwnerActiveTeam.getTeamOwner().subscribeTeamOwner(f8,fullTeam);

            //teamOwnerActiveTeam.subscribeTeamOwner()

        } catch (Exception e) {
            e.printStackTrace();
        }
//check:
        try{
            sm.removeTeamFromSystem(activeTeam);
            /**delete team from owner*/
            Assert.assertFalse(teamOwnerActiveTeam.getTeamOwner().getTeams().contains(activeTeam));
            /** delete the team's subscriptions from team owner subscriptions list**/
            HashSet<TeamSubscription> sub= teamOwnerActiveTeam.getTeamOwner().getMySubscriptions();
            for(TeamSubscription ts: sub){
                assertTrue(ts.team.getName()!=t1.getName());
            }
            //Assert.assertFalse(teamOwnerActiveTeam.getTeamOwner().getMySubscriptions().contains(sub));//?
            /**remove team manager from team*/
            Assert.assertTrue(subTeamManager.getTeamManager()==null);
            /**remove coach*/
            Assert.assertTrue(subCoach.getCoach().getCoachTeam()==null);
            /**remove players*/
            Assert.assertTrue(p1.getPlayer().getTeam()==null);
            /***remove team name from system*/
            Assert.assertFalse(sysetm.getTeamNames().contains("macabi"));
            /**remove from system*/
            Assert.assertFalse(sysetm.getTeamNames().contains(activeTeam));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }

        /**if not active team**/
        Team notActiveTean=new Team();
        Fan f34=new Fan(sysetm,"f34","ee","e","f34","E",
                MainSystem.birthDateFormat.parse("02-11-1996"));
        TeamRole teamOwnerNotAT=new TeamRole(f34);
        teamOwnerNotAT.becomeTeamOwner();
        teamOwnerNotAT.getTeamOwner().addNewTeam(notActiveTean);
        Fan f44=null;
        TeamRole subTeamManagerNAteam=null;
        try {
            notActiveTean.addTeamOwner(teamOwnerNotAT.getTeamOwner());
            LinkedList<Team> deletedTeams=new LinkedList<>();
            deletedTeams.add(notActiveTean);
            teamOwnerNotAT.getTeamOwner().setDeletedTeams(deletedTeams);

            f44=new Fan(sysetm,"f44","ee","e","f44","E",MainSystem.birthDateFormat.parse("02-11-1996"));
            HashSet<TeamManagerPermissions> permissions=new HashSet<>();
            permissions.add(TeamManagerPermissions.addRemoveEditPlayer);
            subTeamManagerNAteam= teamOwnerNotAT.getTeamOwner().subscribeTeamManager(f44,notActiveTean,permissions);


        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            //problem with addObserver in test
            sm.removeTeamFromSystem(notActiveTean);
            /**remove team from - team owners deleted list****/
            Assert.assertFalse(teamOwnerNotAT.getTeamOwner().getDeletedTeams().contains(notActiveTean));


        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    /**or
     * Integration : SystemManager-System
     * @throws Exception
     */
    @Test
    public  void addNewSystemManager() throws Exception {
        MainSystem system=MainSystem.getInstance();
        Fan f1=new Fan(system,"eden","ee","e","e","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Fan f2=new Fan(system,"eden","ee","e","es","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Fan f3=new Fan(system,"eden","ee","e","esds","E",MainSystem.birthDateFormat.parse("02-11-1996"));

        SystemManager sy=new SystemManager(f1,system);
        Complaint c=new Complaint(f3,system);
        sy.addComplaint(c);

        SystemManager newSystemManager=sy.addNewSystemManager(f2);

        /**add to system*/
        Assert.assertTrue(system.getUsers().contains(newSystemManager));
        /**check if f1 and f2 removed from system*/
        Assert.assertTrue(!system.getUsers().contains(f1));
        Assert.assertTrue(!system.getUsers().contains(f2));


    }

    /**or
     * Integration : SystemManager-System-TeamRole-TeamOwner
     * @throws Exception
     */
    @Test
    public void replaceTeamOwnerFounder() throws Exception {
        /** check if teamOwner toAdd null**/
        teamRole.becomeTeamOwner();
        t.addTeamOwner(teamRole.getTeamOwner());
        t.setFounder(teamRole.getTeamOwner());
        teamRole.getTeamOwner().getTeams().add(t);


        Fan f2=new Fan(sysetm,"eden","ee","e","es","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        Fan f3=new Fan(sysetm,"eden","ee","e","es","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        //Fan f4=new Fan(sysetm,"lili","ee","e","lili","E",MainSystem.birthDateFormat.parse("02-11-1996"));
        SystemManager sm=new SystemManager(f3,sysetm);

        TeamRole newFounder=new TeamRole(f3);

        newFounder.becomeTeamOwner();
        sm.replaceTeamOwnerFounder(newFounder.getTeamOwner(),teamRole.getTeamOwner(),t);

        /**check if founder changes **/
        Assert.assertTrue(t.getFounder()==newFounder.getTeamOwner());
        /**check if new founder exist in team owners **/
        Assert.assertTrue(t.getTeamOwners().contains(newFounder.getTeamOwner()));

        /**check if the new founder hold the team*/
        Assert.assertTrue(newFounder.getTeamOwner().getTeams().contains(t));
        /**check if previous founder hold team***/
        Assert.assertTrue(teamRole.getTeamOwner().getTeams().contains(t));
        /**check if prec owner exist in team owners **/
        Assert.assertTrue(t.getTeamOwners().contains(teamRole.getTeamOwner()));


    }


}
