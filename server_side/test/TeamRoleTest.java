import Domain.*;
import Domain.Users.*;
import Stubs.*;
import Domain.Enums.*;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;


public class TeamRoleTest {

    MainSystem system = MainSystem.getInstance();
    Fan f1=new Fan(system,"f1","ee","e","f1","E",MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole teamRole=new TeamRole(f1);
    TeamStub t1 = new TeamStub("team1");
    Fan f2=new Fan(system,"f2","ee","e","f2","E",MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole fullTR=new TeamRole(f2);
    Boolean ansReturnFromFunc=null;
    Fan f3=new Fan(system,"f3","ee","e","f3","E",MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole testTeamRole=new TeamRole(f3);

    public TeamRoleTest() throws ParseException {
    }

//    @Test
//    public void becomePlayer() {
//        /**player null **/
//        try{
//            ansReturnFromFunc= teamRole.becomePlayer();
//            Assert.assertEquals(ansReturnFromFunc,true);
//            Assert.assertTrue(teamRole.isPlayer());
//        }catch (Exception e){
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//        /**player not null - already player**/
//        try {
//                ansReturnFromFunc= teamRole.becomePlayer();
//                Assert.assertEquals(ansReturnFromFunc,false);
//                Assert.assertTrue(teamRole.isPlayer());
//        }catch (Exception e){
//                Assert.fail("test fail");
//                e.printStackTrace();
//        }
//
//    }


//    @Test
//    //avital
//    public void becomeCoach() {
//        /**coach is null **/
//        try{
//            ansReturnFromFunc= teamRole.becomeCoach();
//            Assert.assertEquals(ansReturnFromFunc,true);
//            Assert.assertTrue(teamRole.isCoach());
//        }catch (Exception e){
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//        /**coach not null - already coach**/
//        try {
//            ansReturnFromFunc= teamRole.becomeCoach();
//            Assert.assertEquals(ansReturnFromFunc,false);
//            Assert.assertTrue(teamRole.isCoach());
//        }catch (Exception e){
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//    }

    @Test
    public void becomeTeamManager() {
        HashSet<TeamManagerPermissions> per=new HashSet<>();
        per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
        /**teamManger is null **/
        try{
            ansReturnFromFunc= teamRole.becomeTeamManager(t1,per);
            Assert.assertEquals(ansReturnFromFunc,true);
            Assert.assertTrue(teamRole.isTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**teamManger not null - already teamManger**/
        try {
            ansReturnFromFunc= teamRole.becomeTeamManager(t1,per);
            Assert.assertEquals(ansReturnFromFunc,false);
            Assert.assertTrue(teamRole.isTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }


//    @Test
//    public void becomeTeamOwner() {
//        /**TeamOwner is null **/
//        try{
//            ansReturnFromFunc= teamRole.becomeTeamOwner();
//            Assert.assertEquals(ansReturnFromFunc,true);
//            Assert.assertTrue(teamRole.isTeamOwner());
//        }catch (Exception e){
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//        /**TeamOwner not null - already TeamOwner**/
//        try {
//            ansReturnFromFunc= teamRole.becomeTeamOwner();
//            Assert.assertEquals(ansReturnFromFunc,false);
//            Assert.assertTrue(teamRole.isTeamOwner());
//        }catch (Exception e){
//            Assert.fail("test fail");
//            e.printStackTrace();
//        }
//    }

    @Test
    public void deletePlayer() {
        /**player is not null **/
        try{
            fullTR.becomePlayer();
            ansReturnFromFunc= fullTR.deletePlayer();
            Assert.assertEquals(ansReturnFromFunc,true);
            Assert.assertFalse(fullTR.isPlayer());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**player already null **/
        try{
            ansReturnFromFunc= fullTR.deletePlayer();
            Assert.assertEquals(ansReturnFromFunc,false);
            Assert.assertFalse(fullTR.isPlayer());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void deleteCoach() {
        /**Coach is not null **/
        try{
            fullTR.becomeCoach();
            ansReturnFromFunc= fullTR.deleteCoach();
            Assert.assertEquals(ansReturnFromFunc,true);
            Assert.assertFalse(fullTR.isCoach());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**Coach already null **/
        try{
            ansReturnFromFunc= fullTR.deleteCoach();
            Assert.assertEquals(ansReturnFromFunc,false);
            Assert.assertFalse(fullTR.isCoach());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTeamManager() {
        /**TeamManager is not null **/
        try{
            HashSet<TeamManagerPermissions> per=new HashSet<>();
            per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
            fullTR.becomeTeamManager(t1,per);
            ansReturnFromFunc= fullTR.deleteTeamManager();
            Assert.assertEquals(ansReturnFromFunc,true);
            Assert.assertFalse(fullTR.isTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamManager  already null **/
        try{
            ansReturnFromFunc= fullTR.deleteTeamManager();
            Assert.assertEquals(ansReturnFromFunc,false);
            Assert.assertFalse(fullTR.isTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTeamOwner() {
        /**TeamOwner is not null **/
        try{
            fullTR.becomeTeamOwner();
            ansReturnFromFunc= fullTR.deleteTeamOwner();
            Assert.assertEquals(ansReturnFromFunc,true);
            Assert.assertFalse(fullTR.isTeamOwner());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamOwner already null **/
        try{
            ansReturnFromFunc= fullTR.deleteTeamOwner();
            Assert.assertEquals(ansReturnFromFunc,false);
            Assert.assertFalse(fullTR.isTeamOwner());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void isPlayer() {
        /**player is null **/
        try{
            ansReturnFromFunc= testTeamRole.isPlayer();
            Assert.assertEquals(ansReturnFromFunc,false);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**player is not null  **/
        try{
            testTeamRole.becomePlayer();
            ansReturnFromFunc= testTeamRole.isPlayer();
            Assert.assertEquals(ansReturnFromFunc,true);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void isCoach() {
        /**Coach is null **/
        try{
            ansReturnFromFunc= testTeamRole.isCoach();
            Assert.assertEquals(ansReturnFromFunc,false);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**Coach is not null  **/
        try{
            testTeamRole.becomeCoach();
            ansReturnFromFunc= testTeamRole.isCoach();
            Assert.assertEquals(ansReturnFromFunc,true);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void isTeamManager() {
        /**TeamManager is null **/
        try{
            ansReturnFromFunc= testTeamRole.isTeamManager();
            Assert.assertEquals(ansReturnFromFunc,false);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamManager is not null  **/
        try{
            HashSet<TeamManagerPermissions> per=new HashSet<>();
            per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
            testTeamRole.becomeTeamManager(t1,per);
            ansReturnFromFunc= testTeamRole.isTeamManager();
            Assert.assertEquals(ansReturnFromFunc,true);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    @Test
    public void isTeamOwner() {
        /**TeamOwner is null **/
        try{
            ansReturnFromFunc= testTeamRole.isTeamOwner();
            Assert.assertEquals(ansReturnFromFunc,false);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamOwner is not null  **/
        try{
            testTeamRole.becomeTeamOwner();
            ansReturnFromFunc= testTeamRole.isTeamOwner();
            Assert.assertEquals(ansReturnFromFunc,true);
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }
}
/*
        try{
            sm.removeUser(sm);
            fail("expected exception was not occurred");
        }
        catch (Exception ex){
            Assert.assertEquals(Exception.class,ex.getClass());
            Assert.assertEquals("You Cannot Delete Yourself!!!",ex.getMessage());
            */

