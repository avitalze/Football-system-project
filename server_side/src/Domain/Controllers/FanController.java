package Domain.Controllers;

import DataAccess.DaoFans;
import DataAccess.DaoFanMatchesFollow;
import DataAccess.DaoNotificationFan;
import Domain.Events.Event;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Notifications.Notification;
import Domain.Users.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class FanController {
    SystemOperationsController systemOperationsController=new SystemOperationsController();
    DaoFans daoFans= new DaoFans();

    DaoFanMatchesFollow daoFanMatchesFollow = new DaoFanMatchesFollow();
    DaoNotificationFan daoNotificationFan=new DaoNotificationFan();

    public String fanIsTeamRole(String userName) {
        Fan fan= (Fan) systemOperationsController.getUserByUserName(userName);
        if( fan instanceof TeamRole){
            return "true";
        }
        return "false";
    }

    /***
     *must call after login.
     * @param user
     * @return
     *  @codeBy Eden
     */
    public HashSet<Notification> showNotifications(Fan user){
        return user.getUnReadNotifications();
    }




    /**
     * change user details , return list of changed fields
     * send parameter you want to change or null if you dont want to change
     *
     * @param user
     * @param password
     * @param phoneNumber
     * @param email
     * @param date
     * @return list of fields that changed.
     * @codeBy Eden
     */
    public List<String> changePrivateDetails(Fan user,String name ,String password, String phoneNumber, String email, Date date) {
        List<String> changed = new LinkedList<>();
        //add name
        changed.add(name);

        user.setName(name);
        //password length is 6 or more
        if (password != null && password.length() >= 6) {
            changed.add(password);
            user.setPassword(password);
        }
        // phone number is 10 digits
        if (phoneNumber != null && (phoneNumber.matches("^[0-9]*$") && phoneNumber.length() == 10)) {
            changed.add(phoneNumber);
            user.setPhoneNumber(phoneNumber);
        }
        //email contains @
        if (email != null && email.contains("@") && ((email.contains(".com") || email.contains("il")))) {
            changed.add(email);
            user.setEmail(email);
        }
        if (date != null) {
            changed.add("birthday");
            user.setDateOfBirth(date);

        }
        List<String> key=new LinkedList<>();
        List<String> detailsToInsert=new LinkedList<>();
        key.add(user.getUserName());
        List<String> oldDetialts =null;
        try {
            oldDetialts=daoFans.get(key);
        } catch (ParseException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
        if(password == null){
            password=oldDetialts.get(2);
        }
        else{
            try {
                password= systemOperationsController.sha256(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        detailsToInsert.add(name);
        detailsToInsert.add(password);
        detailsToInsert.add(phoneNumber);
        detailsToInsert.add(email);
        detailsToInsert.add(oldDetialts.get(5));
        detailsToInsert.add(oldDetialts.get(6));

        daoFans.update(key,detailsToInsert);

        return changed;

    }

    //
    public String setFanDetails(String userName,String name, String password, String phoneNumber, String email){
        Fan fan= (Fan) systemOperationsController.getUserByUserName(userName);
        Date dateFormat= null;
        List<String> changes= changePrivateDetails(fan,name,password,phoneNumber,email,dateFormat);
        if(changes.isEmpty()){
            return "noting is changed !! ";
        }
        else return "ok";

    }

    /**
     * return all notifications of fan and mark them as read
     * @param userName
     * @return
     */
    public String getFanNotifications(String userName){
        //LinkedList<String> fanNotificationsString=new LinkedList<>();
        String fanNotificationsString = new String();
        Fan fan= (Fan) systemOperationsController.getUserByUserName(userName);
        HashSet<Notification> fanNotifications= fan.getFanNotification();
        for (Notification noti:fanNotifications) {
            //fanNotificationsString.add(noti.getContent().toString());
            fanNotificationsString += noti.getContent().toString() + ";";
            noti.setRead(true);
            Match m= (Match)noti.getSender();

            List<String> key=new LinkedList<>();
            key.add(MainSystem.simpleDateFormat.format(m.getStartDate()));
            key.add(m.getHomeTeam().getName());
            key.add(m.getAwayTeam().getName());
            key.add(userName);

            List<String> recordData=new LinkedList<>();
            key.add(((Event)(noti.getContent())).getId()+"");
            recordData.add("1");

            daoNotificationFan.update(key,recordData);
        }
        //markAsReadNotification
        return fanNotificationsString;
    }

    /***
     * mark list of notifications as read.
     * @param f
     * @param read
     * @codeBy Eden
     */
    public void markAsReadNotification(Fan f, HashSet<Notification> read) {
        for (Notification n : read) {
            if (f.getNotificationsList().contains(n)) {
                f.MarkAsReadNotification(n);
            }
        }
    }

    /**
     * return all the maches of fan follw
     * @param userName
     * @return
     */
    public String getUserMachesFollows(String userName){
        Fan fan= (Fan) systemOperationsController.getUserByUserName(userName);
        LinkedList<Match> fanMachesFollw= fan.getMatchesFollow();
        //LinkedList<String> fanMachesFollwsString = new LinkedList<>();
        String fanMachesFollwsString = new String();
        for (Match match:fanMachesFollw) {
            //fanMachesFollwsString.add(match.toString());
            fanMachesFollwsString += match.toString() + ";";
        }
        return fanMachesFollwsString;
    }

    /**
     *  add Match to matches follow of fan
     * @param userName
     * @param match
     * @return
     */
    public String addMatchToFanMatchesFollow(String userName, String match) {
        String massage = "Error - match not added";
        try {
            Fan fan = (Fan) systemOperationsController.getUserByUserName(userName);
            HashSet<Match> allMatchesInSystem = systemOperationsController.getAllCurrMatchs();

            //        String[] arrayOfTeamsAndDate = match.split(",");
//        String date = arrayOfTeamsAndDate[1];
//        String TeamsString = arrayOfTeamsAndDate[0];
//        String[] arrayOfTeams = TeamsString.split("-");
//        Team homeTeam = this.systemOperationsController.getTeambyTeamName(arrayOfTeams[0]);
//        Team awayTeam = this.systemOperationsController.getTeambyTeamName(arrayOfTeams[1]);
//        Date matchDate = MainSystem.simpleDateFormat.parse(date);

            for (Match m : allMatchesInSystem) {
                if (m.toString().equals(match)) {
                    fan.addMatchFollow(m);
                    //save in match follow table
                    List<String> fanMatchesRecord = new LinkedList<>();
                    fanMatchesRecord.add(0, fan.getUserName());
                    fanMatchesRecord.add(1, MainSystem.simpleDateFormat.format(m.getStartDate()));
                    fanMatchesRecord.add(2, m.getHomeTeam().getName());
                    fanMatchesRecord.add(3, m.getAwayTeam().getName());
                    daoFanMatchesFollow.save(fanMatchesRecord);

                    massage = "ok";
                    return massage;
                }
            }
        }
        catch (Exception e){
            return "Error - "+e.getMessage();
        }

        return massage;
    }

    /**
     * check for new Notification
     * @param username
     * @return
     */
    public String checkForNewNotifications(String username){
        User user= systemOperationsController.getUserByUserName(username);
        if(user==null){
            return "user not found- delete later";
        }
        if(user instanceof Referee){
            return ((Referee)user).checkNotificationAlert();
        }
        else if(user instanceof Rfa){
            return ((Rfa)user).checkNotificationAlert();
        }
        else if(user instanceof Fan){
            return ((Fan)user).checkNotificationAlert();
        }
        else{
            return "user not instance of fan";
        }
    }

}

