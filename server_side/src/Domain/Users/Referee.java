package Domain.Users;

import Domain.Events.*;
import Domain.MainSystem;
import Domain.LeagueManagment.Match;
import Domain.Notifications.Notification;
import Domain.Notifications.NotificationsUser;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;

public class Referee extends Fan implements NotificationsUser {

    private LinkedList<Match> matches;
    private HashSet<Notification> refNotifications;

    private String qualification;
    private static final Logger LOG = LogManager.getLogger("Referee");

    private boolean gotRefereeNotification;

    public Referee(Fan fan, MainSystem ms){
        super(ms, fan.getName(), fan.getPhoneNumber(), fan.getEmail(), fan.getUserName(), fan.getPassword(), fan.getDateOfBirth());
        matches = new LinkedList<>();
        refNotifications =new HashSet<>();
        gotRefereeNotification=false;
    }

    //for DB
    public Referee(Fan fan, String qualification){
        super(fan.getName(),fan.getPhoneNumber(), fan.getEmail(), fan.getUserName(), fan.getPassword(), fan.getDateOfBirth());
        matches = new LinkedList<>();
        this.qualification = qualification;
        refNotifications =new HashSet<>();
        gotRefereeNotification=false;
    }
    //for DB
    public Referee (String name, String phoneNumber, String email, String userName, String password, Date date, String qualification) {
        super(MainSystem.getInstance(), name,phoneNumber,email,userName,password,date);
        matches = new LinkedList<>();
        this.qualification=qualification;
        refNotifications =new HashSet<>();
        gotRefereeNotification=false;
    }


    public Referee(Fan fan, MainSystem ms, String qualification){
        super(ms, fan.getName(), fan.getPhoneNumber(), fan.getEmail(), fan.getUserName(), fan.getPassword(),fan.getDateOfBirth());
        matches = new LinkedList<>();
        this.qualification = qualification;
        refNotifications =new HashSet<>();
        gotRefereeNotification=false;
    }

    public Referee (MainSystem ms, String name, String phoneNumber, String email, String userName, String password, String qualification, Date date) {
        super(ms,name,phoneNumber,email,userName,password,date);
        matches = new LinkedList<>();
        this.qualification=qualification;
        refNotifications =new HashSet<>();
        gotRefereeNotification=false;
    }


    //<editor-fold desc="getters and setters">

    public LinkedList<Match> getMatches() {
        return matches;
    }


    public String getQualification() { return qualification; }

    /**Yarden**/
    public void setQualification(String qualification) {
        this.qualification = qualification;
        LOG.info(String.format("%s - %s", this.getUserName(), "set qualification to referee"));
    }
    //</editor-fold>



    /**
     * Call after create specific event
     * this function add event to match list
     * @param match
     * @param event
     * @throws Exception
     * @CodeBy Yarden
     */
    public void addEventsDuringMatch(Match match, Event event) throws Exception {
       //checking whether this is a game the referee is judging
        if(this.getMatches().contains(match)){

        Date currentDate = new Date(System.currentTimeMillis());
        //check if the game is takes place right now
        if(currentDate.after(match.getStartDate()) && currentDate.before(DateUtils.addMinutes(match.getStartDate(),match.getNumOfMinutes()))) {
            match.addEventToList(event);
        }//referee's match
            else{
            LOG.error("ref try to add event to match not judging");
                 throw new Exception("You do not have a permission to add events to match you do not judging");
            }
        }//take place right now
        else{
            LOG.error("ref try to add event to match before/after match");
            throw new Exception("You do not have a permission to add events after / before the match");
        }
    }

    /**Yarden**/
    /**
     * !! NOT FOR 3+4 ITERATION !!
     * This function can call just by the main Referee
     * he can edit events just after the match is over
     * each type of event must be replace by the same type (old goal event by new goal event)
     * @param match
     * @param event
     * @param eventToReplace
     * @throws Exception
     */
    // TODO: 18/04/2020 finisih implement
    public void editEventsSchedule(Match match, Event event, Event eventToReplace) throws Exception {
        //just if you are a main referee
        if(match.getMainReferee().equals(this)) {
            //if the game is over
            Date currentDate = new Date(System.currentTimeMillis());
            if(currentDate.after(DateUtils.addMinutes(match.getStartDate(),match.getNumOfMinutes()))) {
                HashSet<Event> events = match.getEvents();
                // function to compare both events
            }
            else{
                LOG.error("You can not edit events until the match is over");
                throw new Exception("You can not edit events until the match is over");
            }

            LOG.info(String.format("%s - %s", this.getUserName(), "edit events schedule by main referee"));
        }
        else{
            LOG.error("You do not have a permission to edit events");
            throw new Exception("You do not have a permission to edit events");
        }

    }

    /**
     * This function create reports of all the events in the game
     * Just the main Referee can call this function, and just after the match is over
     * @param match
     * @return
     * @throws Exception
     * @CodeBy yarden
     */
    public HashSet<Event> createReport(Match match) throws Exception {
        //just if you are a main referee
        if(match.getMainReferee().equals(this)){
            if(this.getMatches().contains(match)){
                //if the game is over
                Date currentDate = new Date(System.currentTimeMillis());
                if(currentDate.after(DateUtils.addMinutes(match.getStartDate(),match.getNumOfMinutes()))) {
                    LOG.info(String.format("%s - %s", this.getUserName(), "create report by main referee"));
                    return match.getEvents();
                }
                else{
                    LOG.error("You can not create report until the match is over");
                    throw new Exception("You can not create report until the match is over");
                }
            }
            else{
                LOG.error("You do not have a permission to create report to match you do not judging");
                throw new Exception("You do not have a permission to create report to match you do not judging");
            }
        }
        else{
            LOG.error("You do not have a permission to edit events right now");
            throw new Exception("You do not have a permission to edit events right now");
        }
    }

    /**Yarden**/
    /**
     * This function show the matches that are still not take place
     * @return LinkedList<Match>
     */
    public LinkedList<Match> showMatches(){

        LinkedList<Match> matchesToShow = new LinkedList<>();

        for (Match m: matches) {
            if (m.getStartDate().after(new Date(System.currentTimeMillis()))){
                //add just match that still not take place
                matchesToShow.add(m);
//                System.out.println("Date:"+m.getStartDate().toString()+""+"At field:"+m.getField().getNameOfField()+""+"Guest score:"+m.getGuestScore()
//                        +"Home score:"+""+ m.getHomeScore());
            }
        }
        LOG.info(String.format("%s - %s", this.getUserName(), "Show matches to referee"));
        return matchesToShow;
    }

    /**Yarden**/
    public void addMatchToList(Match match){
        this.getMatches().add(match);
        match.addObserver(this);
    }

    /**Yarden**/
    // all validation checking is in each constructor
    //<editor-fold desc="events creation">
    public Event createGoalEvent(Player p, Match match) throws Exception {
        if (p != null && match != null) {
            if (this.matches.contains(match)) {
                return (new Goal(this, match, p));
            } else {
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    public Event createYellowCardEvent(Player p, Match match) throws Exception {
        if(p!=null && match!=null) {
            if (this.matches.contains(match)) {
                return (new YellowCard(this, match, p));
            }
            else{
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    public Event createRedCardEvent(Player p, Match match) throws Exception {
        if(p!=null && match!=null) {
            if (this.matches.contains(match)) {
                return (new RedCard(this, match, p));
            }
            else{
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    public Event createOffSideCardEvent(Player p, Match match) throws Exception {
        if (p != null && match != null) {
            if (this.matches.contains(match)) {
                return (new OffSide(this, match, p));
            }
            else{
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    public Event createOffenseEvent(Player p, Match match) throws Exception {
        if(p!=null && match!=null) {
            if (this.matches.contains(match)) {
                return (new Offense(this, match, p));
            }
            else{
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    public Event createInjuryEvent(Player p, Match match) throws Exception {
        if(p!=null && match!=null) {
            if (this.matches.contains(match)) { //check if referee-matches table contains this match - in controller
                return (new Injury(this, match, p));
            }
            else{
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }


    public Event createReplacementEvent(Player p1,Player p2, Match match) throws Exception {
            if(p1!=null && p2!=null && match!=null) {
                if (this.matches.contains(match)) {
                    return (new Replacement(this, match, p1, p2));
                }
                else{
                    throw new Exception("You do not judge this match");
                }
            }
            else{
                LOG.error("one of parameters null");
                throw new NullPointerException();
            }
    }


    public Event createExtraTimeEvent(Match match, int time) throws Exception {
        if(match!= null && time > 0) {
            if (this.matches.contains(match)) {
                return (new ExtraTime(this, match, time));
            }
            else{
                throw new Exception("You do not judge this match");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    //</editor-fold>

    /**Referee Notifications**/
    /**Yarden**/
    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof Match) {
            if (arg instanceof String) {
                if(system.userLoggedIn(this)){
                    gotRefereeNotification=true;
                }
                if(isSendByEmail()==true){
                        sendEmail(this.getEmail(),"You have a new notification about a game you are judging.\n"+
                                "The notification: \n"+ arg);
                }
                this.refNotifications.add(new Notification(o, arg, false));
            }
            else{
                if(arg instanceof  Event){
                    super.update(o,arg);
                }
            }
        }
    }

    /**
     * mark notification as readen
     * @param not-unread notification to mark as read
     * @codeBy Eden
     */
    @Override
    public void MarkAsReadNotification(Notification not){
        not.setRead(true);
        //we dont want the alert to show if the user is in the notification inbox
        gotRefereeNotification=false;
    }

    /**
     * get all refNotifications read and unread
     * @return
     */
    @Override
    public HashSet<Notification> getNotificationsList() {
        //we dont want the alert to show if the user is in the notification inbox
        gotRefereeNotification=false;
        return refNotifications;
    }

    /***
     * @return only the unread refNotifications . if not have return null - notify when user connect
     * @codeBy Eden
     */
    @Override
    public HashSet<Notification> getUnReadNotifications(){
        //we dont want the alert to show if the user is in the notification inbox
        gotRefereeNotification=false;
        HashSet<Notification> unRead=new HashSet<>();
        for(Notification n: refNotifications){
            if(n.isRead()==false){
                unRead.add(n);
            }
        }
        return unRead;
    }

    @Override
    public String checkNotificationAlert() {
        if(gotRefereeNotification && gotFanNotification){
            gotRefereeNotification =false;
            gotFanNotification=false;
            return "multipleNotifications";
        }
        else if(gotFanNotification){
            gotFanNotification=false;
            return "gotFanNotification";
        }
        else if(gotRefereeNotification){
            gotRefereeNotification =false;
            return "gotRefereeNotification";
        }
        return "";
    }
}
