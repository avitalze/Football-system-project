package AcceptRejectTests;

import Domain.Controllers.FanController;
import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Match;
import Domain.Main;
import Domain.MainSystem;
import Domain.Users.Fan;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;

public class AddMatchFollow {

    FanController controller= new FanController();
    SystemOperationsController operationsController=new SystemOperationsController();
    @Test
    public void addMatchFollow() throws Exception {

        operationsController.initSystemFromDB();
        HashSet<Match> allMatchesInSystem = operationsController.getAllCurrMatchs();
        Fan f= MainSystem.getInstance().getAllFans().get(0);
        LinkedList<Match> matches= new LinkedList<>(allMatchesInSystem);
        Match m=matches.get(0);
        int numM= f.getMatchesFollow().size();
        
        controller.addMatchToFanMatchesFollow(f.getUserName(),m.toString());
        Assert.assertEquals(numM+1, f.getMatchesFollow().size());
        //noo
        controller.addMatchToFanMatchesFollow(f.getUserName(),"not good");
        Assert.assertEquals(numM+1, f.getMatchesFollow().size());
    }
}
