package AcceptRejectTests;


import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.Events.Event;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Service.RfaApplication;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.fail;

/** RFA delete referee from system . UC: 31. **/

public class DeleteReferee {
    RfaController rfaController=new RfaController();
    SystemOperationsController operationsController=new SystemOperationsController();


    @Test
    public void accept() throws Exception {
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma = MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa = operationsController.getAllRFA().get(0);
        SystemManager sm = operationsController.showAllSystemManagers().get(0);

        /***  Successful removal of a referee  ***/
        List<Referee> allReferees = operationsController.showAllReferee();
        Referee refereeToDell=allReferees.get(0);
        try {
            rfaController.deleteReferee(refereeToDell,rfa);
        }
        catch (Exception e){
            fail();
        }

        try{

            /***  referee deleted from system referees list   ***/
            Assert.assertFalse(operationsController.showAllReferee().contains(refereeToDell));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }

    }


    @Test
    public void reject() throws Exception {
        /*****system init*****/
        operationsController.deleteSystem();
        operationsController.initSystemObjectsAvital();
        MainSystem ma = MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa = operationsController.getAllRFA().get(0);
        SystemManager sm = operationsController.showAllSystemManagers().get(0);


        List<Referee> allReferees = operationsController.showAllReferee();
        /*** Inlay referee for the game **/
        Season currSeason= operationsController.getCurrSeason();
        rfa.addReferee("refereeToDell","0526621646","yossi@gmail.com","refereeToDell","ds123456678","ds",MainSystem.birthDateFormat.parse("02-11-1996"));
        LinkedHashSet<Referee> allRefsHash = operationsController.getAllREfereeInHasSet();
        ArrayList<Referee> allRefs = new ArrayList<>(allRefsHash);
        Referee refereeToDell=allRefs.get(3);
        HashSet<Team> allTeamsHash=operationsController.showAllTeams();
        ArrayList<Team> allTeams= new ArrayList<>(allTeamsHash);
        Team t1= allTeams.get(0);
        Team t2= allTeams.get(1);

        Match m1 = new Match(0,0,t1,t2, new Field("f1"), new HashSet<Event>(), new HashSet<Referee>()
                , refereeToDell,"17-09-2020 20:00:00");




        /** try delete referee that  belongs to mach in future - not success **/
        try {
            rfaController.deleteReferee(refereeToDell,rfa);
            fail();
        }
        catch (Exception e){
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("You can not delete this referee",e.getMessage());
        }

        try{

            /***  referee not deleted from system referees list   ***/
            Assert.assertTrue(operationsController.showAllReferee().contains(refereeToDell));
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }

    }







}

