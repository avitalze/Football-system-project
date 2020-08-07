package AcceptRejectTests;

import Domain.Controllers.SystemOperationsController;
import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.SystemManager;
import Domain.Users.User;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

public class InitSystem {

    SystemOperationsController operationsController=new SystemOperationsController();

    /**
     * Init system accept test
     * @throws ParseException
     * @codeBy Eden
     */
    @Test
    public void accept() throws ParseException {
        List<String> defaultSystemManager=operationsController.startSystem();
        MainSystem ma= MainSystem.getInstance();

        Assert.assertTrue(ma!=null);
        Assert.assertTrue(defaultSystemManager!=null);
        Assert.assertTrue(defaultSystemManager.get(0)!=null);
        Assert.assertTrue(defaultSystemManager.get(1)!=null);
        Assert.assertTrue(ma.getUserNames().contains(defaultSystemManager.get(0)));
        Assert.assertTrue(operationsController.getUserByUserName(defaultSystemManager.get(0)) instanceof SystemManager);
        User user=(operationsController.getUserByUserName(defaultSystemManager.get(0)));
        Assert.assertTrue(((Fan)(user)).getPassword().equals(defaultSystemManager.get(1)));


    }
}
