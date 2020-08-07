package Service;


import Domain.Controllers.SystemOperationsController;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class YourServletContextListener implements ServletContextListener {

    SystemOperationsController systemOperationsController= new SystemOperationsController();

    public void contextInitialized(ServletContextEvent event) {
        System.out.println("server is UP !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //TODO change the init so that it will not throw exceptions
        try {
            systemOperationsController.initSystemFromDB();
            systemOperationsController.initExternalSystems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("server is DOWN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}