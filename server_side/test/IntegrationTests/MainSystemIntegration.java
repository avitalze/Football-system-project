package IntegrationTests;

import Domain.MainSystem;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class MainSystemIntegration {
    MainSystem ms= MainSystem.getInstance();


    @Test
    /**or*
     * Integration : LoginMain System- External System
     */
    public void firstStartSystem() throws ParseException {
        ms.startSystem();
        Assert.assertTrue(ms.getSystemManagers().size()==1);
        Assert.assertTrue(ms.getExtSystem()!=null);
        Assert.assertEquals(ms,ms.getExtSystem().getMs());

    }

    @Test
    /**or*
     * Integration : LoginMain System- External System
     */
    public void startSystem() throws ParseException {
        ms.startSystem();
        Assert.assertTrue(ms.getExtSystem()!=null);
        Assert.assertTrue(ms.getSystemManagers().size()==1);
        Assert.assertEquals(ms,ms.getExtSystem().getMs());
    }
}
