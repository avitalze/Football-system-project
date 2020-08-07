package AcceptRejectTests;

import Domain.MainSystem;
import Domain.Users.TeamRole;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class SignIn {

    SystemOperationsApplication operationsController=new SystemOperationsApplication();

    /**Or**/
    @Test
    public void signin() throws ParseException {
        MainSystem ms= MainSystem.getInstance();
        try {
            String ans= operationsController.signUp("Player","or","0542150912","or@gmail.com","oruser","orpassword", "01-03-2000","false");
            Assert.assertEquals("ok",ans);
            Assert.assertTrue(ms.getUsers().size()==1);
            Assert.assertTrue(ms.getUsers().get(0) instanceof TeamRole);
            Assert.assertTrue(((TeamRole)ms.getUsers().get(0)).isPlayer());
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        String ans= operationsController.signUp("Coach","or","0542150912","or@gmail.com","oruser","orpassword", "01-03-2000","false");
        Assert.assertEquals("user name is not valid.",ans);
        Assert.assertTrue(ms.getUsers().size()==1);

    }
}
