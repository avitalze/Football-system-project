package IntegrationTests;

import Domain.Enums.TeamManagerPermissions;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.TeamRole;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;

public class TeamRoleIntegration {
    MainSystem system = MainSystem.getInstance();
    Fan f1=new Fan(system,"f1","ee","e","f1","E",MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole teamRole=new TeamRole(f1);
    Team team= new Team();

    public TeamRoleIntegration() throws ParseException {
    }

    /**or
     * Integration - TeamRole-Player
     */
    @Test
    public void becomePlayer() {
        /**player null **/
        try{
            Assert.assertEquals(true,teamRole.becomePlayer());
            Assert.assertTrue(teamRole.isPlayer());
            Assert.assertEquals(teamRole, teamRole.getPlayer().getTeamRole());
            Assert.assertNull(teamRole.getPlayer().getTeam());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**player not null - already player**/
        try {
            Assert.assertEquals(false,teamRole.becomePlayer());
            Assert.assertTrue(teamRole.isPlayer());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }

    }


    /**or
     * Integration - TeamRole-Coach
     */
    @Test
    public void becomeCoach() {
        /**coach is null **/
        try{
            Assert.assertEquals(true,teamRole.becomeCoach());
            Assert.assertTrue(teamRole.isCoach());
            Assert.assertEquals(teamRole, teamRole.getCoach().getTeamRole());
            Assert.assertNull(teamRole.getCoach().getCoachTeam());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**coach not null - already coach**/
        try {
            Assert.assertEquals(false,teamRole.becomeCoach());
            Assert.assertTrue(teamRole.isCoach());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    /**or
     * Integration - TeamRole-TeamManager
     */
    @Test
    public void becomeTeamManager() {
        HashSet<TeamManagerPermissions> per=new HashSet<>();
        per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
        /**teamManger is null **/
        try{
            Assert.assertEquals(true,teamRole.becomeTeamManager(team,per));
            Assert.assertTrue(teamRole.isTeamManager());
            Assert.assertEquals(teamRole, teamRole.getTeamManager().getTeamRole());
            Assert.assertEquals(team,teamRole.getTeamManager().getTeam());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**teamManger not null - already teamManger**/
        try {
            Assert.assertEquals(teamRole.becomeTeamManager(team,per),false);
            Assert.assertTrue(teamRole.isTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    /**or
     * Integration - TeamRole-TeamOwner
     */
    @Test
    public void becomeTeamOwner() {
        /**TeamOwner is null **/
        try{
            Assert.assertEquals(teamRole.becomeTeamOwner(),true);
            Assert.assertTrue(teamRole.isTeamOwner());
            Assert.assertEquals(teamRole, teamRole.getTeamOwner().getTeamRole());
            Assert.assertEquals(0,teamRole.getTeamOwner().getTeams().size());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamOwner not null - already TeamOwner**/
        try {
            Assert.assertEquals(false,teamRole.becomeTeamOwner());
            Assert.assertTrue(teamRole.isTeamOwner());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    /**or
     * Integration - TeamRole-Player
     */
    @Test
    public void deletePlayer() {
        /**player is not null **/
        try{
            teamRole.becomePlayer();
            Assert.assertEquals(true,teamRole.deletePlayer());
            Assert.assertFalse(teamRole.isPlayer());
            Assert.assertNull(teamRole.getPlayer());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**player already null **/
        try{
            Assert.assertEquals(false,teamRole.deletePlayer());
            Assert.assertFalse(teamRole.isPlayer());
            Assert.assertNull(teamRole.getPlayer());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    /**or
     * Integration - TeamRole-Coach
     */
    @Test
    public void deleteCoach() {
        /**Coach is not null **/
        try{
            teamRole.becomeCoach();
            Assert.assertEquals(true,teamRole.deleteCoach());
            Assert.assertFalse(teamRole.isCoach());
            Assert.assertNull(teamRole.getCoach());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        try{
            Assert.assertEquals(false,teamRole.deleteCoach());
            Assert.assertFalse(teamRole.isCoach());
            Assert.assertNull(teamRole.getCoach());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }

    }

    /**or
     * Integration - TeamRole-TeamManager
     */
    @Test
    public void deleteTeamManager() {
        /**TeamManager is not null **/
        try{
            HashSet<TeamManagerPermissions> per=new HashSet<>();
            per.add(TeamManagerPermissions.addRemoveEditTeamOwner);
            teamRole.becomeTeamManager(team,per);
            Assert.assertEquals(true, teamRole.deleteTeamManager());
            Assert.assertFalse(teamRole.isTeamManager());
            Assert.assertNull(teamRole.getTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamManager  already null **/
        try{
            Assert.assertEquals(false,teamRole.deleteTeamManager());
            Assert.assertFalse(teamRole.isTeamManager());
            Assert.assertNull(teamRole.getTeamManager());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }

    /**or
     * Integration - TeamRole-TeamOwner
     */
    @Test
    public void deleteTeamOwner() {
        /**TeamOwner is not null **/
        try{
            teamRole.becomeTeamOwner();
            Assert.assertEquals(true,teamRole.deleteTeamOwner());
            Assert.assertFalse(teamRole.isTeamOwner());
            Assert.assertNull(teamRole.getTeamOwner());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
        /**TeamOwner already null **/
        try{
            Assert.assertEquals(false,teamRole.deleteTeamOwner());
            Assert.assertFalse(teamRole.isTeamOwner());
            Assert.assertNull(teamRole.getTeamOwner());
        }catch (Exception e){
            Assert.fail("test fail");
            e.printStackTrace();
        }
    }


}
