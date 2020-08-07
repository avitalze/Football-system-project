package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.Controllers.UserController;
import Domain.MainSystem;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class LogIn {
    UserController userController=new UserController();
    SystemOperationsController operationsController=new SystemOperationsController();
    /**Or**/
    @Test
    public void login() throws ParseException {
        try {
            operationsController.initSystemFromDB();
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
        MainSystem ms= MainSystem.getInstance();

        String ans= userController.login("try","password");
        Assert.assertEquals(ans,"details not correct, no fan in system");

         ans= userController.login("oralfasi","123456");
        Assert.assertEquals(ans,"RFA");

    }

}
