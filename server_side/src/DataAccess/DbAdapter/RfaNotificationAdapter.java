package DataAccess.DbAdapter;

import Domain.Controllers.SystemOperationsController;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Notifications.Notification;
import Domain.Users.Fan;
import javafx.beans.Observable;

import java.util.List;

public class RfaNotificationAdapter implements DbObject<Notification> {

    SystemOperationsController systemOperationsController = new SystemOperationsController();

    @Override
    public Notification ToObj(List<String> fields) throws Exception {
        Team team = systemOperationsController.getTeambyTeamName(fields.get(0));

        String readOrNot = fields.get(2);
        Notification notification;
        if(readOrNot.equals("1")) {

            notification = new Notification(team, fields.get(1), true);
        }
        else{
           notification = new Notification(team, fields.get(1), false);
        }
        return notification;
    }

    @Override
    public List<String> ToDB(Notification obj) {
        return null;
    }
}
