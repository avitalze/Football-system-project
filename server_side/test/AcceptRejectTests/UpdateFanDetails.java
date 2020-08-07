package AcceptRejectTests;

import Domain.Controllers.FanController;
import Domain.Controllers.SystemOperationsController;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.SystemManager;
import org.junit.Assert;
import org.junit.Test;

public class UpdateFanDetails {
    SystemOperationsController systemOperationsController = new SystemOperationsController();
    FanController fanController=new FanController();

    @Test
    public void UpdateDetails() throws Exception {
        MainSystem system = MainSystem.getInstance();
        system.startSystem();
        SystemManager marioSystemManager = system.getSystemManagers().get(0);//there is only one system manager now (the default)

        Fan f1=new Fan(system, "amir dadon", "0549716910","ami@gmail.com", "amir", "111111" , MainSystem.birthDateFormat.parse("01-03-1994"));

        String ans = fanController.setFanDetails("amir","amir dadi","123456","0545233681","avi@gmail.com");

        Assert.assertTrue(ans.equals("ok"));

        Assert.assertTrue(f1.getUserName().equals("amir"));
        Assert.assertTrue(f1.getName().equals("amir dadi"));
        Assert.assertTrue(f1.getPassword().equals(systemOperationsController.sha256("123456")));
        Assert.assertTrue(f1.getPhoneNumber().equals("0545233681"));
        Assert.assertTrue(f1.getEmail().equals("avi@gmail.com"));


    }


}
