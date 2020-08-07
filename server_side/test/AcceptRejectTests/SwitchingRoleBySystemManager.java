package AcceptRejectTests;

import Domain.Controllers.SystemManagerController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.*;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.fail;

public class SwitchingRoleBySystemManager {
    SystemManagerController managerController=new SystemManagerController();
    SystemOperationsController operationsController=new SystemOperationsController();
    MainSystem system;
    SystemManager sm;
    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsEden();
        system= MainSystem.getInstance();
        sm=operationsController.showAllSystemManagers().get(0);
        /*********************/
        /**to add*/
        TeamRole toAdd=(TeamRole)operationsController.getUserByUserName("ali");
        /**to delete*/
        TeamRole toDelete=(TeamRole)operationsController.getUserByUserName("alon");
        Assert.assertTrue(toDelete.getCoach().getCoachTeam()!=null);

        /**switch coaches*/
        managerController.replaceCoachAtTeam(toAdd.getCoach(),toDelete.getCoach().getCoachTeam(),sm);
        Assert.assertTrue(toDelete.getCoach()!=null);
        Assert.assertTrue(toDelete.getCoach().getCoachTeam()==null);
        Assert.assertTrue(toAdd.getCoach().getTeamRole()!=null);
        Assert.assertTrue(toAdd.getCoach().getTeamRole().getCoach()==toAdd.getCoach());


        /**switch founder*/
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
        Team t2=first.getName().equals("macabi")?second:first;

        Fan f1=new Fan(system, "Oren", "0549716910","yossi@gmail.com", "Oren", "Yossi123" ,MainSystem.birthDateFormat.parse("02-11-1996"));
        TeamRole orenTeamOwner=new TeamRole(f1);
        orenTeamOwner.becomeTeamOwner();
        sm.replaceTeamOwnerFounder(orenTeamOwner.getTeamOwner(),moshe,t2);
        Assert.assertTrue(t2.getFounder()==orenTeamOwner.getTeamOwner());
        Assert.assertTrue(t2.getTeamOwners().contains(orenTeamOwner.getTeamOwner()));
        Assert.assertTrue(t2.getTeamOwners().contains(moshe));
        /***add player to team */
        Fan f2=new Fan(system, "Ori", "0549716910","yossi@gmail.com", "Ori", "Yossi123" ,MainSystem.birthDateFormat.parse("02-11-1996"));
        TeamRole player =new TeamRole(f2);
        player.becomePlayer();

        managerController.addPlayerToTeam(player.getPlayer(),t2,sm);
        Assert.assertTrue(t2.getPlayers().contains(player.getPlayer()));
        Assert.assertTrue(player.getPlayer().getTeam()==t2);
    }

    @Test
    public void reject() throws Exception{
        /*****system init*****/
        operationsController.deleteSystem();
        operationsController.initSystemObjectsEden();
        system= MainSystem.getInstance();
        sm=operationsController.showAllSystemManagers().get(0);
        /*********************/
        /**replace coach without team */
        TeamRole toDelete=(TeamRole)operationsController.getUserByUserName("ali");
        TeamRole toAdd=(TeamRole)operationsController.getUserByUserName("alon");
        try {
            managerController.replaceCoachAtTeam(toAdd.getCoach(),toDelete.getCoach().getCoachTeam(), sm);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("cannot replace those coaches"));
        }


        /***switch founder with another team owner:*/
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
        Team t2=first.getName().equals("macabi")?second:first;
        User ilan=operationsController.getUserByUserName("Ilan");
        try {
            managerController.replaceTeamFounder(sm,((TeamRole)ilan).getTeamOwner(),moshe,t2);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("fail!The team owner you want to add already exist"));
        }

    }
}
