package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.UserController;
import Domain.MainSystem;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class LogOut {

    UserController userController=new UserController();
    SystemOperationsController operationsController=new SystemOperationsController();
    /**Or**/
    @Test
    public void logout() throws ParseException {
        try {
            operationsController.initSystemFromDB();
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
        MainSystem ms= MainSystem.getInstance();

        String ans= userController.logOut("try");
        Assert.assertEquals(ans,"user is not a fan");

        userController.login("oralfasi","123456");
        ans=userController.logOut("oralfasi");
        Assert.assertEquals(ans,"success");

    }
}
