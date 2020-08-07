package AcceptRejectTests;

import Domain.Controllers.PageOwnerController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Fan;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;

import static junit.framework.TestCase.fail;

public class PageManagement {
    SystemOperationsController operationsController=new SystemOperationsController();
    PageOwnerController pageManagementController=new PageOwnerController();
    MainSystem system;

    /**
     * accept test for all private page operations
     * @throws Exception
     * @codeBy Eden
     */
    @Test
    public void accept() throws Exception {
        operationsController.initSystemObjectsEden();
        system= MainSystem.getInstance();
        /***add team private page**/
        HashSet<Team> teams=operationsController.showAllTeams();
        Iterator<Team > iter =teams.iterator();
        Team first=iter.next();
        Team second=iter.next();
        /**already have page**/
        Team t1=first.getName().equals("macabi")?first:second;
        /**do not have page**/
        Team t2=first.getName().equals("macabi")?second:first;
        pageManagementController.createPrivatePage(t2);
        Assert.assertTrue(t2.getPrivatePage()!=null);

        /**add records to private page*/
        String s1="my first record";
        String s2="my second record";
        pageManagementController.addRecordToPage(t2,s1);
        pageManagementController.addRecordToPage(t2,s2);
        Assert.assertTrue(t2.getPrivatePage().getRecordsAsString().contains(s1));
        Assert.assertTrue(t2.getPrivatePage().getRecordsAsString().contains(s2));

        /**remove recoreds from page*/
        pageManagementController.removeRecordFromPage(t2,s1);
        Assert.assertTrue(!t2.getPrivatePage().getRecordsAsString().contains(s1));
        Assert.assertTrue(t2.getPrivatePage().getRecordsAsString().contains(s2));

        /**follow page*/
        Fan alona=((Fan)operationsController.getUserByUserName("alona"));
        ((Fan) alona).subToPage(t2.getPrivatePage());
        Assert.assertTrue(((Fan) alona).getMyPages().contains(t2.getPrivatePage()));
        Assert.assertTrue(((Fan) alona).getMyPages().contains(t1.getPrivatePage()));
        Assert.assertTrue(t2.getPrivatePage().getFans().contains(alona));

        /**unfollow page */
        alona.unfollow(t2.getPrivatePage());
        Assert.assertTrue(!((Fan) alona).getMyPages().contains(t2.getPrivatePage()));
        Assert.assertTrue(((Fan) alona).getMyPages().contains(t1.getPrivatePage()));
        Assert.assertTrue(!t2.getPrivatePage().getFans().contains(alona));

        /*****delete page***/
        Assert.assertTrue(((Fan) alona).getMyPages().contains(t1.getPrivatePage()));
        Assert.assertTrue(t1.getPrivatePage().getFans().contains(alona));
        pageManagementController.deletePrivatePage(t1);
        Assert.assertTrue(t1.getPrivatePage()==null);
        Assert.assertTrue(!((Fan) alona).getMyPages().contains(t1.getPrivatePage()));





    }

    /**
     * reject test for all private page operations
     * @throws Exception
     * @codeBy Eden
     */
    @Test
    public void reject() throws Exception{
        operationsController.deleteSystem();
        operationsController.initSystemObjectsEden();
        system= MainSystem.getInstance();
        /***add team private page**/
        HashSet<Team> teams=operationsController.showAllTeams();
        Iterator<Team > iter =teams.iterator();
        Team first=iter.next();
        Team second=iter.next();
        /**already have page**/
        Team t1=first.getName().equals("macabi")?first:second;
        /**do not have page**/
        Team t2=first.getName().equals("macabi")?second:first;

        /***create 2 pages ***/
        try {
            pageManagementController.createPrivatePage(t1);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("already"));
        }



    }

}
