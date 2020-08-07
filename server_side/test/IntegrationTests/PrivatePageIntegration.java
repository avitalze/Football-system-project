package IntegrationTests;

import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.TeamRole;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class PrivatePageIntegration {
    MainSystem ms = MainSystem.getInstance();
    Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910", "yossi@gmail.com", "YossiHamelech", "Yossi123", MainSystem.birthDateFormat.parse("02-11-1996"));
    TeamRole teamOwnerRole = new TeamRole(yossi);
    Team t = new Team();

    public PrivatePageIntegration() throws ParseException {
    }

    /**or
     * Integration : PrivatePage- PageOwner- Team- TeamOwner
     */
    @Test
    public void createPP() {
        teamOwnerRole.becomeTeamOwner();
        teamOwnerRole.getTeamOwner().addNewTeam(t);
        t.getTeamOwners().add(teamOwnerRole.getTeamOwner());

        Assert.assertTrue(teamOwnerRole.getTeamOwner().createPrivatePage(t));
        Assert.assertTrue(t.getPrivatePage() != null);
        Assert.assertFalse(teamOwnerRole.getTeamOwner().createPrivatePage(t));

    }

    /**or
     * Integration : PrivatePage- PageOwner- Team- TeamOwner
     */
    @Test
    public void deletePP() {
        teamOwnerRole.becomeTeamOwner();
        teamOwnerRole.getTeamOwner().addNewTeam(t);
        t.getTeamOwners().add(teamOwnerRole.getTeamOwner());

        Assert.assertFalse(teamOwnerRole.getTeamOwner().deletePrivatePage(t));
        Assert.assertTrue(t.getPrivatePage() == null);
        teamOwnerRole.getTeamOwner().createPrivatePage(t);
        Assert.assertTrue(teamOwnerRole.getTeamOwner().deletePrivatePage(t));
        Assert.assertNull(t.getPrivatePage());

    }

    /**or
     * Integration : PrivatePage- PageOwner- Team- TeamOwner
     */
    @Test
    public void addRecord() {
        teamOwnerRole.becomeTeamOwner();
        teamOwnerRole.getTeamOwner().addNewTeam(t);
        t.getTeamOwners().add(teamOwnerRole.getTeamOwner());
        teamOwnerRole.getTeamOwner().createPrivatePage(t);

        Assert.assertEquals(0,t.getPrivatePage().getRecords().size());
        try {
            teamOwnerRole.getTeamOwner().addRecordToTeamPage(t,"new record");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(1,t.getPrivatePage().getRecords().size());
    }

    /**or
     * Integration : PrivatePage- PageOwner- Team- TeamOwner
     */
    @Test
    public void removeRecord() {
        teamOwnerRole.becomeTeamOwner();
        teamOwnerRole.getTeamOwner().addNewTeam(t);
        t.getTeamOwners().add(teamOwnerRole.getTeamOwner());
        teamOwnerRole.getTeamOwner().createPrivatePage(t);

        Assert.assertEquals(0,t.getPrivatePage().getRecords().size());
        try {
            teamOwnerRole.getTeamOwner().addRecordToTeamPage(t,"new record");
            Assert.assertEquals(1,t.getPrivatePage().getRecords().size());
            teamOwnerRole.getTeamOwner().removeRecordFromPage(t,"new record");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertEquals(0,t.getPrivatePage().getRecords().size());
    }
}

