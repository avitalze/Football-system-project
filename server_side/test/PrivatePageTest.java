import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.PageOwner;
import Domain.PrivatePage;
import Domain.Users.Fan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class PrivatePageTest {

    PrivatePage privatePage= new PrivatePage();

    @Before
    public void name() {
        privatePage.setPageOwner(new Team());
    }

    @Test
    public void addRemoveRecord() {
        try {
            privatePage.addRecords("newRecord");
            Assert.assertTrue(privatePage.getRecords().contains("newRecord"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            privatePage.addRecords("");
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("record not valid",e.getMessage());
        }

        try {
            privatePage.removeRecord("newRecord");
            Assert.assertFalse(privatePage.getRecords().contains("newRecord"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void addFan() throws ParseException {
        Assert.assertTrue(privatePage.getFans().size()==0);
        Fan f= new Fan(MainSystem.getInstance(),"fan1","0542150912","fan@gmail.com","fan101","fan101",MainSystem.birthDateFormat.parse("01-12-1998"));
        try {
            privatePage.addFan(null);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("fan null",e.getMessage());
        }
        try {
            privatePage.addFan(f);
            Assert.assertTrue(privatePage.getFans().contains(f));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        try {
            privatePage.addFan(f);
            Assert.fail();
        } catch (Exception e) {
            assertEquals(Exception.class, e.getClass());
            assertEquals("this fan is already in fan list",e.getMessage());
        }
    }

    @Test
    public void getRecordAsString() {
        try {
            privatePage.addRecords("123");
            privatePage.addRecords("567");

            Assert.assertEquals("123,567,",privatePage.getRecordsAsString());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}