import Domain.BudgetControl.BudgetControl;
import Stubs.TeamStub;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class BudgetControlTest {
    /**OR**/
    TeamStub team= new TeamStub("hapoel Ranana");
    BudgetControl bc= team.getBudgetControl();
    @Test
    public void addIncome() {
        try {
            bc.addIncome("reason",-200);
            fail("expected exception was not occurred.");
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("amount of income is negative",e.getMessage());

        }
        try {
            bc.addIncome(null,200);
            fail("expected exception was not occurred.");
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("type of income not valid",e.getMessage());
        }
        try {
            bc.addIncome("something",200);
           Assert.assertTrue(bc.getBalance()==200);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void addExpense() {
        try {
            bc.addExpense("reason",-200);
            fail("expected exception was not occurred.");
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("amount of expense is negative",e.getMessage());
        }
        try {
            bc.addExpense(null,200);
            fail("expected exception was not occurred.");
        } catch (Exception e) {
            Assert.assertEquals(Exception.class, e.getClass());
            Assert.assertEquals("type of expense not valid",e.getMessage());
        }
        try {
            bc.addIncome("something",300);
            bc.addExpense("something",200);
            Assert.assertTrue(bc.getBalance()==100);
        } catch (Exception e) {
            fail();
        }
    }
}