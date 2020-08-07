package DataAccess.DbAdapter;

import Domain.LeagueManagment.Match;
import Domain.Notifications.Notification;

import java.util.List;

public class RefereeNotificationAdapter implements DbObject<Notification> {

    public String refereeName = "";

    public void setRefereeName(String name){
        this.refereeName = name;
    }

    @Override
    public Notification ToObj(List<String> fields) throws Exception {

        String readOrNot = fields.get(2);
        Notification notification;
        if(readOrNot.equals("1")) {
            notification = new Notification(null, fields.get(1), true);
        }
        else{
            notification = new Notification(null, fields.get(1), false);
        }

        return notification;
    }

    @Override
    public List<String> ToDB(Notification obj) {
        return null;
    }
}
