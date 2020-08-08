package Presentation;


import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class CheckNotificationsTask extends ScheduledService<String> {

    String userName;
    public CheckNotificationsTask(String userName) {
        this.userName=userName;
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                System.out.println("checking notifications!!");
                String ans;
                //ans = fanApplication.checkForNewNotifications(userName);
                ans = ClientController.connectToServer("FanApplication", "checkForNewNotifications", userName);
                return ans;
            }
        };
    }
}




