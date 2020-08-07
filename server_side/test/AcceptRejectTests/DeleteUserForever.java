package AcceptRejectTests;

import Domain.Controllers.SystemManagerController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
//import Domain.Main;
import Domain.MainSystem;
import Domain.Users.*;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.fail;

public class DeleteUserForever {
    SystemManagerController managerController=new SystemManagerController();
    SystemOperationsController operationsController=new SystemOperationsController();
    MainSystem system;
    SystemManager sm;

    /**
     * accept tests for "Delete user from system by system manager"
     * @throws Exception
     * @codeBy Eden
     */
    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsEden();
        system= MainSystem.getInstance();
        sm=operationsController.showAllSystemManagers().get(0);
        /**delete team owner with subscriptions*/
        accept1();
        /**delete Rfa**/
        accept2();
        /**delete systemManager*/
        accept3();
        /**delete referee*/
        accept4();
        /**delete teamManager*/
        accept5();
        /**delete player*/
        accept6();
        /**delete coach*/
        accept7();
        /**delete fan*/
        accept8();
    }

    private void accept1() throws Exception {
        /***delete Team Owner with subscriptions,
         *post: delete user and remove his subscriptions */
        List<TeamOwner> teamOwners=operationsController.showAllTeamOwner();
        /**DELETE avi(subscribe avi and moshe to be owners of macabi, moshe still team manager (ilan subscribr him))**/
        TeamOwner teamOwnerToDelete=null;
        TeamOwner moshe=null;
        TeamOwner ilan=null;
        TeamOwner kobi=null;
        for(TeamOwner to: teamOwners){
            if(to.getTeamRole().getUserName().equals("Avi")){
                teamOwnerToDelete=to;
            }
            if(to.getTeamRole().getUserName().equals("Moshe")){
                moshe=to;
            }
            if(to.getTeamRole().getUserName().equals("Ilan")){
                ilan=to;
            }
            if(to.getTeamRole().getUserName().equals("kobi")){
                kobi=to;
            }
        }
        managerController.deleteUserForever(teamOwnerToDelete.getTeamRole(),sm);

        HashSet<Team> teams=operationsController.showAllTeams();
        Iterator<Team > iter =teams.iterator();
        Team first=iter.next();
        Team second=iter.next();
        Team t1=first.getName().equals("macabi")?first:second;
        Team t2=first.getName().equals("macabi")?second:first;

        /***check if avi deleted from system**/
        assertTrue(!system.getUsers().contains(teamOwnerToDelete.getTeamRole()));
        assertTrue(!system.getUserNames().contains(teamOwnerToDelete.getTeamRole().getUserName()));
        /***check t1 subscriptions **/
        /**avi and moshe deleted from ownership*/
        assertTrue(!t1.getTeamOwners().contains(moshe));
        assertTrue(!moshe.getTeams().contains(t1));
        assertTrue(!t1.getTeamOwners().contains(teamOwnerToDelete));
        assertTrue(!teamOwnerToDelete.getTeams().contains(t1));

        /**moshe is still the manager*/
        assertTrue(t1.getTeamManager()==moshe.getTeamRole().getTeamManager());
        /**ilan is still team Owner*/
        assertTrue(t1.getTeamOwners().contains(ilan));

        /*****************************/

        /***check t2 subscriptions **/
        assertTrue(t2.getTeamManager()==null);
        assertTrue(!teamOwnerToDelete.getTeams().contains(t2));
        /**ilan deleted from team owner(becouse avi subscribe him)**/
        assertTrue(!t2.getTeamOwners().contains(ilan));
        assertTrue(!ilan.getTeams().contains(t2));
        /**kobi deleted from tewm owner(becouse avi subscribe ilan witch subscribe him)*/
        assertTrue(!t2.getTeamOwners().contains(kobi));
        assertTrue(!kobi.getTeams().contains(t2));
        /****************************/
    }
    private void accept2() throws Exception {
        /**delete RFA*/
        List<Rfa> rfas=operationsController.getAllRFA();
        Rfa rfa = rfas.get(0);
        managerController.deleteUserForever(rfa,sm);
        User shsh= operationsController.getUserByUserName(rfa.getUserName());
        boolean isExist= system.getUserNames().contains("DanaBandana");
        Assert.assertTrue(operationsController.getUserByUserName(rfa.getUserName())==null);

    }
    private void accept3() throws Exception {
        /**delete system manager*/
        List<SystemManager> systemManagers=operationsController.getAllSystemManager();
        SystemManager currentSM=systemManagers.get(0)==sm?systemManagers.get(1):systemManagers.get(0);
        managerController.deleteUserForever(currentSM,sm);
        Assert.assertTrue(operationsController.getUserByUserName(currentSM.getUserName())==null);

    }
    private void accept4() throws Exception {
        /***delete referee*/
        List<Referee> referees=operationsController.showAllReferee();
        managerController.deleteUserForever(referees.get(0),sm);
        Assert.assertTrue(operationsController.getUserByUserName(referees.get(0).getUserName())==null);
    }
    private void accept5() throws Exception{
        /***delete team manager with subscriptions*/
        /**replace moshe with another team owner at t2*/
        List<TeamOwner> teamOwners=operationsController.showAllTeamOwner();
        TeamOwner moshe=null;
        for(TeamOwner to: teamOwners) {
            if (to.getTeamRole().getUserName().equals("Moshe")) {
                moshe = to;
            }
        }
        HashSet<Team> teams=operationsController.showAllTeams();
        Iterator<Team > iter =teams.iterator();
        Team first=iter.next();
        Team second=iter.next();
        Team t1=first.getName().equals("macabi")?first:second;
        Team t2=first.getName().equals("macabi")?second:first;

        Fan f1=new Fan(system, "Oren", "0549716910","yossi@gmail.com", "Oren", "Yossi123" ,MainSystem.birthDateFormat.parse("02-11-1996"));
        TeamRole orenTeamOwner=new TeamRole(f1);
        orenTeamOwner.becomeTeamOwner();
        sm.replaceTeamOwnerFounder(orenTeamOwner.getTeamOwner(),moshe,t2);

        managerController.deleteUserForever(moshe.getTeamRole(),sm);
        /**deleted from system*/
        assertTrue(!system.getUsers().contains(moshe.getTeamRole()));
        assertTrue(!system.getUserNames().contains(moshe.getTeamRole().getUserName()));

        /**deleted from t1 as team manager**/
        assertTrue(t1.getTeamManager()==null);

        /**deleted from t2 as owner */
        assertTrue(!t2.getTeamOwners().contains(moshe));
        assertTrue(!moshe.getTeams().contains(t2));



    }
    private void accept6() throws Exception{
        /***delete player with team witch have more than 11 players**/
        List<Player> players= operationsController.getAllPlayers();
        Player playerWithTeam=null;
        TeamRole teamRole=null;
        Team t1=null;
        for(Player p: players){
            if (p.getTeam()!=null&&p.getTeam().getPlayers().size()>11){
                playerWithTeam=p;
                teamRole=p.getTeamRole();
                t1=p.getTeam();
                break;
            }
        }
        managerController.deleteUserForever(playerWithTeam.getTeamRole(),sm);
        /**deleted from system*/
        assertTrue(!system.getUsers().contains(teamRole));
        assertTrue(!system.getUserNames().contains(teamRole.getUserName()));
        /**delete from team*/
        assertTrue(!t1.getPlayers().contains(playerWithTeam));


        /**delete player without team*/
        Player playerWithoutTeam=null;
        for(Player p: players){
            if (p.getTeam()==null){
                playerWithoutTeam=p;
                break;
            }
        }
        managerController.deleteUserForever(playerWithoutTeam.getTeamRole(),sm);
        /**deleted from system*/
        assertTrue(!system.getUsers().contains(playerWithoutTeam.getTeamRole()));
        assertTrue(!system.getUserNames().contains(playerWithoutTeam.getTeamRole().getUserName()));

    }
    private void accept7() throws Exception{
        User coach =operationsController.getUserByUserName("ali");
        managerController.deleteUserForever(((TeamRole)coach),sm);
        /**deleted from system*/
        assertTrue(!system.getUsers().contains(coach));
        assertTrue(!system.getUserNames().contains(((TeamRole)coach).getUserName()));



    }
    private void accept8() throws Exception{
        /**remove fan from system **/
        User alona=operationsController.getUserByUserName("alona");
        HashSet<Team> teams=operationsController.showAllTeams();
        Iterator<Team > iter =teams.iterator();
        Team first=iter.next();
        Team second=iter.next();
        Team t1=first.getName().equals("macabi")?first:second;

        assertTrue(t1.getPrivatePage().getFans().contains(alona));
        managerController.deleteUserForever(((Fan)alona),sm);
        assertTrue(!t1.getPrivatePage().getFans().contains(alona));
        /**deleted from system*/
        assertTrue(!system.getUsers().contains(alona));
        assertTrue(!system.getUserNames().contains(((Fan)alona).getUserName()));


    }

    /**
     * reject tests for "Delete user from system by system manager"
     * @throws Exception
     * @codeBy Eden
     */
    @Test
    public void reject() throws Exception {
        /*****system init*****/
        operationsController.deleteSystem();
        operationsController.initSystemObjectsEden();
        system= MainSystem.getInstance();
        sm=operationsController.showAllSystemManagers().get(0);
        /******************************/

        reject1();
        reject2();
        reject3();
        reject4();
        reject5();
        reject6();
        reject7();


    }

    private void reject1()throws Exception{
        /**delete the last rfa*/
        List<Rfa> rfas=operationsController.getAllRFA();
        SystemManager sm=operationsController.showAllSystemManagers().get(0);
        managerController.deleteUserForever(rfas.get(0),sm);
        try {
            managerController.deleteUserForever(rfas.get(0), sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("There is only one RFA left, you cannot delete it ",e.getMessage());
        }
    }
    private void reject2()throws Exception{

        /**system Manager delete yourself***/
        List<SystemManager> systemManagers=operationsController.getAllSystemManager();
        SystemManager currentSM=systemManagers.get(0)==sm?systemManagers.get(0):systemManagers.get(1);
        try {
            managerController.deleteUserForever(currentSM,sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("You Cannot Delete Yourself!!!",e.getMessage());
        }
        /******************************/


        /**system Manager delete the last one***/
        systemManagers=operationsController.getAllSystemManager();
        currentSM=systemManagers.get(0)==sm?systemManagers.get(1):systemManagers.get(0);
        managerController.deleteUserForever(currentSM,sm);

        try {
            currentSM=systemManagers.get(0);
            managerController.deleteUserForever(currentSM,sm);


            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("You Cannot Delete Yourself!!!",e.getMessage());
        }
        /******************************/

    }
    private void reject3()throws Exception{


        /**delete Referee with future games */
        List<Referee> referees=operationsController.showAllReferee();
        Referee rf=referees.get(0);
        rf.getMatches().add(new Match(MainSystem.birthDateFormat.parse("02-11-2030")));
        try {
            managerController.deleteUserForever(referees.get(0),sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("You cannot remove referee , he has matches in this season",e.getMessage());
        }
        /******************************/


    }
    private void reject4()throws Exception{
        /**deleteTeamOwner witch is  a founder*/
        List<TeamOwner> teamOwners=operationsController.showAllTeamOwner();
        TeamOwner moshe=null;
        TeamOwner ilan=null;
        for(TeamOwner to: teamOwners){
            if(to.getTeamRole().getUserName().equals("Moshe")){
                moshe=to;
            }
            if(to.getTeamRole().getUserName().equals("Ilan")){
                ilan=to;
            }
        }

        try {
            managerController.deleteUserForever(ilan.getTeamRole(),sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains(" is founder of:"));
        }


    }
    private void reject5()throws Exception{
        /**delete team manager witch is team founder of another team**/
        TeamManager moshes=((TeamRole)operationsController.getUserByUserName("Moshe")).getTeamManager();
        try {
            managerController.deleteUserForever(moshes.getTeamRole(),sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains(" is founder of:"));
        }
        /*************************************************************/

    }
    private void reject6()throws Exception{


        /**delete player with team witch no have more than 11 players **/
        List<Player> players= operationsController.getAllPlayers();
        TeamRole teamRole=null;
        for(Player p: players){
            if (p.getTeam()!=null&&p.getTeam().getPlayers().size()==11){
                teamRole=p.getTeamRole();
                break;
            }
        }
        try {
            managerController.deleteUserForever(teamRole,sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains(" team ,any team have to be at least 11 Players!"));
        }
        /*****************************/

    }
    private void reject7()throws Exception{
        /**delete coach with team*/
        User coach =operationsController.getUserByUserName("alon");
        try {
            managerController.deleteUserForever((TeamRole)coach,sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("you have to subscribe another coach to "));
        }
    }

}
