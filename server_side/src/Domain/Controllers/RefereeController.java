package Domain.Controllers;

import DataAccess.*;
import Domain.Events.Event;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Notifications.Notification;
import Domain.Users.Fan;
import Domain.Users.Player;
import Domain.Users.Referee;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class RefereeController {
    // TODO: 21/04/2020 next iteration

    SystemOperationsController systemOperationsController = new SystemOperationsController();
    DaoEvent daoEvent = new DaoEvent();
    DaoTwoPlayersEvents daoTwoPlayersEvents = new DaoTwoPlayersEvents();
    DaoOnePlayerEvents daoOnePlayerEvents = new DaoOnePlayerEvents();
    DaoExtraTimeEvent daoExtraTimeEvent = new DaoExtraTimeEvent();
    DaoMatch daoMatch = new DaoMatch();
    DaoNotificaionsReferee daoNotificaionsReferee = new DaoNotificaionsReferee();
    DaoNotificationFan daoNotificationFan = new DaoNotificationFan();
    DaoTeams daoTeams = new DaoTeams();

    public Referee getRefereeByUserName(String refName) {
        List<Referee> allReferees = MainSystem.getInstance().getAllReferees();

        for (Referee ref : allReferees) {
            if (ref.getUserName().contains(refName)) {
                return ref;
            }
        }
        return null;
    }

    //get list of players name
    public LinkedList<String> getPlayersFromTeamInList(HashSet<Player> players) {

        LinkedList<String> listOfPlayers = new LinkedList<>();
        for (Player player : players) {
            listOfPlayers.add(player.getTeamRole().getUserName());
        }

        return listOfPlayers;
    }

    /**
     * @param nameOfReferee
     * @param match
     * @return LinkedList<String> of players at specific match
     * @throws ParseException
     */
    public String displayPlayersAtMatch(String nameOfReferee, String match) {
        try {
            Match m = this.matchObjectFromString(match, nameOfReferee);

            Team away = m.getAwayTeam();
            Team home = m.getHomeTeam();

            HashSet<Player> awayPlayers = away.getPlayers();
            LinkedList<String> awayPlayersName = this.getPlayersFromTeamInList(awayPlayers);

            HashSet<Player> homePlayers = home.getPlayers();
            LinkedList<String> homePlayersName = this.getPlayersFromTeamInList(homePlayers);

            //LinkedList<String> allPlayers = awayPlayersName;
            String allPlayers = new String();
            for (String pName : homePlayersName) {
                allPlayers += pName + ";";
            }

            for (String pAName : awayPlayersName) {
                allPlayers += pAName + ";";
            }

            //allPlayers.addAll(homePlayersName);
            return allPlayers;
        }
        catch (ParseException e){
            return "parse error";
        }
        catch (Exception e){
            return "error"+ e.getMessage();
        }

    }

    //check if 2 players at the same team
    public boolean checkIfAtTheSameTeam(String firstPlayer, String secondPlayer, String nameOfReferee, String match) throws ParseException {

        if (!firstPlayer.equals(secondPlayer)) {

            Match m = this.matchObjectFromString(match, nameOfReferee);

            Team away = m.getAwayTeam();
            HashSet<Player> awayPlayers = away.getPlayers();
            LinkedList<String> awayPlayersName = this.getPlayersFromTeamInList(awayPlayers);

            Team home = m.getHomeTeam();
            HashSet<Player> homePlayers = home.getPlayers();
            LinkedList<String> homePlayersName = this.getPlayersFromTeamInList(homePlayers);

            if (awayPlayersName.contains(firstPlayer)) {
                if (awayPlayersName.contains(secondPlayer)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (homePlayersName.contains(firstPlayer)) {
                    if (homePlayersName.contains(secondPlayer)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param nameOfReferee
     * @return match that the referee judging that take place right now - probably just one
     */
    public String displayAllMatches(String nameOfReferee) {

        //LinkedList<String> listOfMatches = new LinkedList<>();
        Referee currentReferee = this.getRefereeByUserName(nameOfReferee);
        Date currentDate = new Date(System.currentTimeMillis());
        for (Match match : currentReferee.getMatches()) {
            //check if the game is takes place right now
            if (currentDate.after(match.getStartDate()) && currentDate.before(DateUtils.addMinutes(match.getStartDate(), match.getNumOfMinutes()))) {
                //listOfMatches.add(match.toString());
                return match.toString();
            }
        }
        return "";
        //return listOfMatches;
    }

    /**
     * This function return all the matches that the referee is a main referee, and already over
     *
     * @param nameOfReferee
     * @return
     */
    public String getAllMatches(String nameOfReferee) {

        //LinkedList<String> listOfMatches = new LinkedList<>();
        String listOfMatches = "";
        Referee currentReferee = this.getRefereeByUserName(nameOfReferee);
        Date currentDate = new Date(System.currentTimeMillis());

        for (Match match : currentReferee.getMatches()) {
            if (match.getMainReferee().getUserName().equals(nameOfReferee)) {
                if (currentDate.after(DateUtils.addMinutes(match.getStartDate(), match.getNumOfMinutes()))) {
                    //listOfMatches.add(match.toString());
                    listOfMatches += match.toString() + ";";
                }
            }
        }

        return listOfMatches;
    }


    public String createReportOfMatch(String match, String nameOfReferee) {
        try {
            //LinkedList<String> listOfEventsInMatch = new LinkedList<>();
            String listOfEventsInMatch = new String();
            Referee currentReferee = this.getRefereeByUserName(nameOfReferee);
            Match m = this.matchObjectFromString(match, nameOfReferee);

            HashSet<Event> allEvents = currentReferee.createReport(m);
            for (Event e : allEvents) {
                //listOfEventsInMatch.add(e.toString());
                listOfEventsInMatch += e.toString() + ";";
            }
            return listOfEventsInMatch;
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
        //return null;
    }

    /**
     * This function convert string of match to Match object
     * @param match
     * @param nameOfReferee
     * @return Match object
     * @throws ParseException
     */
    public Match matchObjectFromString(String match, String nameOfReferee) throws ParseException {

        String[] arrayOfTeamsAndDate = match.split(",");
        String date = arrayOfTeamsAndDate[1];

        String TeamsString = arrayOfTeamsAndDate[0];
        String[] arrayOfTeams = TeamsString.split("-");

        Team homeTeam = this.systemOperationsController.getTeambyTeamName(arrayOfTeams[0]);

        Team awayTeam = this.systemOperationsController.getTeambyTeamName(arrayOfTeams[1]);


        Date matchDate = MainSystem.simpleDateFormat.parse(date);

        Referee ref = this.getRefereeByUserName(nameOfReferee);

        for (Match m : ref.getMatches()) {
            if (m.getAwayTeam().equals(awayTeam) && m.getHomeTeam().equals(homeTeam) && m.getStartDate().equals(matchDate)) {
                return m;
            }
        }

        return null;
    }

    //<editor-fold desc="save to DB">
    public void saveInEventTableDB(Event e, String referee, Match currentMatch) throws SQLException {

        List<String> eventRecord = new LinkedList<>();
        eventRecord.add(0, String.valueOf(e.getId()));
        eventRecord.add(1, MainSystem.simpleDateFormat.format(e.getDateTime()));
        eventRecord.add(2, referee);
        eventRecord.add(3, MainSystem.simpleDateFormat.format(currentMatch.getStartDate()));
        eventRecord.add(4, currentMatch.getHomeTeam().getName());
        eventRecord.add(5, currentMatch.getAwayTeam().getName());
        eventRecord.add(6, e.getName());
        eventRecord.add(7, String.valueOf(e.getMinuteOfMatch()));
        daoEvent.save(eventRecord);
    }

    public void saveInOnePlayerEventTableDB(Event e, String player) throws SQLException {
        List<String> Record = new LinkedList<>();
        Record.add(0, String.valueOf(e.getId()));
        Record.add(1, player);
        daoOnePlayerEvents.save(Record);
    }

    public void saveInTwoPlayerEventTableDB(Event e, String player1, String player2) throws SQLException {
        List<String> Record = new LinkedList<>();
        Record.add(0, String.valueOf(e.getId()));
        Record.add(1, player1);
        Record.add(2, player2);
        daoTwoPlayersEvents.save(Record);
    }

    public void saveInExtraTimeEventTableDB(Event e, String time) throws SQLException {
        List<String> Record = new LinkedList<>();
        Record.add(0, String.valueOf(e.getId()));
        Record.add(1, time);
        daoExtraTimeEvent.save(Record);
    }

    public void saveInFanNotifications(Event e, String fanUserName, Match currentMatch) throws SQLException {
        List<String> Record = new LinkedList<>();
        Record.add(0,MainSystem.simpleDateFormat.format(currentMatch.getStartDate()));
        Record.add(1,currentMatch.getHomeTeam().getName());
        Record.add(2,currentMatch.getAwayTeam().getName());
        Record.add(3,fanUserName);
        Record.add(4,String.valueOf(e.getId()));
        Record.add(5,"0");
        daoNotificationFan.save(Record);
    }

    public void updateInTeamTable(Team team, Match currentMatch){
        List<String> listKey = new LinkedList<>();
        listKey.add(team.getName());

        List<String> listRecord = new LinkedList<>();
        if(team.getTeamManager()==null) {
            listRecord.add(null);
        }
        else{
            listRecord.add(team.getTeamManager().getUserNameOfAction());
        }
        listRecord.add(team.getFounder().getUserNameOfAction());
        listRecord.add(team.getCoach().getTeamRole().getUserName());
        listRecord.add(team.getField().getNameOfField());
        listRecord.add(String.valueOf(team.getScore()));

        daoTeams.update(listKey,listRecord);
    }

    public void updateInMatchTable(Match match){
        List<String> listKey = new LinkedList<>();
        listKey.add(MainSystem.simpleDateFormat.format(match.getStartDate()));
        listKey.add(match.getHomeTeam().getName());
        listKey.add(match.getAwayTeam().getName());

        List<String> listRecord = new LinkedList<>();
        listRecord.add(String.valueOf(match.getHomeScore()));
        listRecord.add(String.valueOf(match.getGuestScore()));
        listRecord.add(match.getField().getNameOfField());
        listRecord.add(match.getMainReferee().getUserName());
        listRecord.add(String.valueOf(match.getNumOfMinutes()));

        daoMatch.update(listKey,listRecord);
    }

    //</editor-fold>

    public LinkedList<Fan> allFansFollowsPerMatch(Match match){
        LinkedList<Fan> listOfFollowers = new LinkedList<>();

        LinkedList<Fan> allFans = MainSystem.getInstance().getAllFans();
        for (Fan fan: allFans) {
            if(fan.getMatchesFollow().contains(match)){
                listOfFollowers.add(fan);
            }
        }

        return listOfFollowers;
    }

    //<editor-fold desc="create events">
    public String createGoalEvent(String referee, String match, String player) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player playerMakeEvent = this.systemOperationsController.getPlayerByUserName(player);
            Event e = currentReferee.createGoalEvent(playerMakeEvent, currentMatch);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //onePlayerEvent table
            saveInOnePlayerEventTableDB(e, player);

            //update score of teams - match table
            List<String> matchKeys = new LinkedList<>();
            matchKeys.add(0, MainSystem.simpleDateFormat.format(currentMatch.getStartDate()));
            matchKeys.add(1, currentMatch.getHomeTeam().getName());
            matchKeys.add(2, currentMatch.getAwayTeam().getName());

            List<String> matchRecord = new LinkedList<>();
            matchRecord.add(0, String.valueOf(currentMatch.getHomeScore()));
            matchRecord.add(1, String.valueOf(currentMatch.getGuestScore()));
            matchRecord.add(2, currentMatch.getField().getNameOfField());
            matchRecord.add(3, currentMatch.getMainReferee().getUserName());
            matchRecord.add(4, String.valueOf(currentMatch.getNumOfMinutes()));
            daoMatch.update(matchKeys, matchRecord);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }

            return "ok";

        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " + e.getMessage();
        }

    }

    public String createInjuryEvent(String referee, String match, String player) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player playerMakeEvent = this.systemOperationsController.getPlayerByUserName(player);
            Event e = currentReferee.createInjuryEvent(playerMakeEvent, currentMatch);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //onePlayerEvent table
            saveInOnePlayerEventTableDB(e, player);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }
            return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
    }

    public String createOffenseEvent(String referee, String match, String player) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player playerMakeEvent = this.systemOperationsController.getPlayerByUserName(player);
            Event e = currentReferee.createOffenseEvent(playerMakeEvent, currentMatch);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //onePlayerEvent table
            saveInOnePlayerEventTableDB(e, player);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }

            return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e) {
            return "error " + e.getMessage();
        }
    }

    public String createOffSideEvent(String referee, String match, String player) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player playerMakeEvent = this.systemOperationsController.getPlayerByUserName(player);
            Event e = currentReferee.createOffSideCardEvent(playerMakeEvent, currentMatch);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //onePlayerEvent table
            saveInOnePlayerEventTableDB(e, player);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }

             return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
    }

    public String createRedCardEvent(String referee, String match, String player) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player playerMakeEvent = this.systemOperationsController.getPlayerByUserName(player);
            Event e = currentReferee.createRedCardEvent(playerMakeEvent, currentMatch);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //onePlayerEvent table
            saveInOnePlayerEventTableDB(e, player);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }

            return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
    }

    public String createYellowCardEvent(String referee, String match, String player) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player playerMakeEvent = this.systemOperationsController.getPlayerByUserName(player);
            Event e = currentReferee.createYellowCardEvent(playerMakeEvent, currentMatch);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //onePlayerEvent table
            saveInOnePlayerEventTableDB(e, player);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }

            return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
    }

    public String createReplaceEvent(String referee, String match, String firstPlayer, String secondPlayer) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            Player player1 = this.systemOperationsController.getPlayerByUserName(firstPlayer);
            Player player2 = this.systemOperationsController.getPlayerByUserName(secondPlayer);
            //choose that player1 and player2 different and exist in the same team
            if (checkIfAtTheSameTeam(firstPlayer, secondPlayer, referee, match)) {
                Event e = currentReferee.createReplacementEvent(player1, player2, currentMatch);
                int id = daoEvent.getMaxEventId()+1;
                e.setId(id);
                currentReferee.addEventsDuringMatch(currentMatch, e);

                /**save in tables**/
                //Events table
                saveInEventTableDB(e, referee, currentMatch);
                //TwoPlayersEvent table
                saveInTwoPlayerEventTableDB(e, firstPlayer, secondPlayer);

                //save in fan notifications table
                LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
                for (Fan fan: fanLinkedList){
                    saveInFanNotifications(e,fan.getUserName(),currentMatch);
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Notice that the players must be from the same team");
                alert.show();
            }
            return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
    }

    public String createExtraTimeEvent(String referee, String match, String time) {
        try {
            Referee currentReferee = this.getRefereeByUserName(referee);
            Match currentMatch = this.matchObjectFromString(match, referee);
            int minutes = Integer.parseInt(time);
            Event e = currentReferee.createExtraTimeEvent(currentMatch, minutes);
            int id = daoEvent.getMaxEventId()+1;
            e.setId(id);
            currentReferee.addEventsDuringMatch(currentMatch, e);
            /**save in tables**/
            //Events table
            saveInEventTableDB(e, referee, currentMatch);
            //ExtraTime Event table
            saveInExtraTimeEventTableDB(e, time);
            //save in match table - DB
            updateInMatchTable(currentMatch);

            //save in fan notifications table
            LinkedList<Fan> fanLinkedList = this.allFansFollowsPerMatch(currentMatch);
            for (Fan fan: fanLinkedList){
                saveInFanNotifications(e,fan.getUserName(),currentMatch);
            }

            return "ok";
        } catch (ParseException p){
            return "parse error";
        }catch (Exception e){
            return  "error " +e.getMessage();
        }
    }

    //</editor-fold>

    /**
     * return all unread notifications of referee
     * @param userName
     * @return
     */
    public String getRefereeUnreadNotifications(String userName){

        String refereeNotificationsString = new String();
        Referee referee = this.getRefereeByUserName(userName);

        HashSet<Notification> refereeNotifications = referee.getUnReadNotifications();
        for (Notification notification : refereeNotifications) {
            refereeNotificationsString += notification.getContent().toString() + ";";
        }

        return refereeNotificationsString;
    }

    /**
     * return referee qualification
     * @param nameOfReferee
     * @return
     */
    public String getQualificationOfReferee(String nameOfReferee){
        try {
            Referee ref = this.getRefereeByUserName(nameOfReferee);
            return ref.getQualification();
        }
        catch (Exception e){
            return "Error-"+e.getMessage();
        }

    }


    public void updateInRefereesNotificationTable(String refereeUserName, String notificationContent, Match match){
//        String[] notificationSplit = notificationContent.split("-");
//        String home = notificationSplit[1];
//        String away = notificationSplit[3];
//        String date = notificationSplit[5];
        List<String> NotificationsKeysList = new LinkedList<>();
        NotificationsKeysList.add(0, MainSystem.simpleDateFormat.format(match.getStartDate()));
        NotificationsKeysList.add(1,match.getHomeTeam().getName());
        NotificationsKeysList.add(2,match.getAwayTeam().getName());
        NotificationsKeysList.add(3,refereeUserName);
        NotificationsKeysList.add(4,notificationContent);

        List<String> NotificationsIsReadOrNot = new LinkedList<>();
        NotificationsIsReadOrNot.add(0,"1");

        daoNotificaionsReferee.update(NotificationsKeysList,NotificationsIsReadOrNot);
    }

    public String markNotificationAsRead(String notificationContent, String referee){
        try {
            Referee ref = this.getRefereeByUserName(referee);
            for (Notification notification : ref.getUnReadNotifications()) {
                if (notification.getContent().equals(notificationContent)) {
                    notification.setRead(true);
                    Match match = (Match) notification.getSender();
                    //update in table of referees notifications that notification is read
                    updateInRefereesNotificationTable(referee, notificationContent, match);
                    return "ok";
                }
            }
        }
        catch (Exception e){
            return "Error - "+ e.getMessage();
        }
        return "";
    }

    public String getMatchScore(String match, String username) {
        try {
            Match currentMatch = this.matchObjectFromString(match, username);
            String score = currentMatch.getHomeScore() +" - "+ currentMatch.getGuestScore();
            return score;
        }
        catch (Exception e){
            return  "error " +e.getMessage();
        }
    }
}
