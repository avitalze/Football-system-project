package AcceptRejectTests;


import Domain.Controllers.RfaController;
import Domain.Controllers.SystemOperationsController;
import Domain.MainSystem;
import Domain.Users.Referee;
import Domain.Users.Rfa;
import Domain.Users.SystemManager;
import Service.RfaApplication;
import Service.SystemOperationsApplication;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

/** rfa Adding a new  referee to system . UC: 30. **/


public class AddNewReferee {

    RfaController rfaController=new RfaController();
    SystemOperationsController operationsController=new SystemOperationsController();


    @Test
    public void accept() throws Exception{
        /*****system init*****/
        operationsController.initSystemObjectsAvital();
        MainSystem ma= MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa=operationsController.getAllRFA().get(0);
        SystemManager sm=operationsController.showAllSystemManagers().get(0);

        /** referee added to system **/
        String refUserName="refRoni";
        try {
            rfaController.addReferee(rfa,"roni","0542344654","a@gmail.com",refUserName,
                    "123456","bla",MainSystem.birthDateFormat.parse("02-11-1996"));
        }
        catch (Exception e){
            fail();
        }

        /** referee added to system  referees list **/
        try{
            Referee myReferi=null;
            List<Referee> allReferee=operationsController.showAllReferee();
            for(Referee ref: allReferee){
                if(ref.getUserName().equals(refUserName)){
                    myReferi=ref;
                    break;
                }
            }
            Assert.assertTrue(myReferi != null);
        } catch (Exception e) {
            Assert.fail("test fail");
            e.printStackTrace();
        }

    }

    @Test
    public void reject() throws Exception{
        /*****system init*****/
        operationsController.deleteSystem();
        operationsController.initSystemObjectsAvital();
        MainSystem ma= MainSystem.getInstance();

        /*****get RFA *****/
        Rfa rfa=operationsController.getAllRFA().get(0);
        SystemManager sm=operationsController.showAllSystemManagers().get(0);

        /** try add referee that his UserName  already exists in the system - not success **/
        try {
            List <Referee> allRefs= operationsController.showAllReferee();
            rfaController.addReferee(rfa,"rafi","0542344654","a@gmail.com","reffRafi",
                    "123456","bla",MainSystem.birthDateFormat.parse("02-11-1996"));
            fail("test fail");
        }
        catch (Exception e){//
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("user name not valid",e.getMessage());
        }




    }

}
