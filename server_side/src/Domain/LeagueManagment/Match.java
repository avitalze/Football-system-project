package Domain.LeagueManagment;

import Domain.Events.*;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.MainSystem;
import Domain.Users.Referee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Observable;

public class  Match extends Observable {
    private int homeScore;
    private int guestScore;
    private Team awayTeam;
    private Team homeTeam;
    private Field field;
    private HashSet<Event> events;
    private Referee mainReferee;
    private HashSet<Referee> referees;
    private Date startDate;
    private int numOfMinutes;
    private static final Logger LOG = LogManager.getLogger("Match");

    public Match(int homeScore, int guestScore, Team awayTeam, Team homeTeam, Field field, HashSet<Event> events, HashSet<Referee> referees
            , Referee mainReferee, String date) throws Exception {
        if (awayTeam != null && homeTeam != null && field != null && events != null && referees != null) {
            this.homeScore = homeScore;
            this.guestScore = guestScore;
            this.awayTeam = awayTeam;
            this.homeTeam = homeTeam;
            this.field = field;
            this.events = events;
            this.referees = referees;
            this.mainReferee = mainReferee;
            this.startDate = MainSystem.simpleDateFormat.parse(date);
            this.numOfMinutes = 90;
            //update the referees about the matches
            for (Referee ref : referees) {
                ref.addMatchToList(this);
            }
            mainReferee.addMatchToList(this);
        } else {//also exception for wrong startDate format
            LOG.error("one of details to create match in null");
            throw new Exception("Please valid details that requires to create a match");
        }
    }

    //for DB
    public Match(String date, int homeScore, int guestScore, int numOfMinutes  ) throws ParseException {
        this.homeScore = homeScore;
        this.guestScore = guestScore;
        this.awayTeam = null;
        this.homeTeam = null;
        this.field = null;
        this.events = new HashSet<Event>();
        this.referees = new HashSet<Referee>();
        this.mainReferee = null;
        this.startDate = MainSystem.simpleDateFormat.parse(date);
        this.numOfMinutes = numOfMinutes;
    }


    public Match(Date date) throws Exception {
        this.startDate = date;
        this.numOfMinutes = 90;
        //update the referees about the matches
        System.out.println("THIS MATCH CONSTRUCTOR IS JUST FOR TESTS");
    }

    public int getNumOfMinutes() {
        return numOfMinutes;
    }

    public Referee getMainReferee() {
        return mainReferee;
    }

    public void setMainReferee(Referee mainReferee) {
        this.mainReferee = mainReferee;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Field getField() {
        return field;
    }

    public HashSet<Event> getEvents() {
        return events;
    }

    public HashSet<Referee> getReferees() {
        return referees;
    }

    public void setNumOfMinutes(int numOfMinutes) {
        this.numOfMinutes = numOfMinutes;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        setChanged();
        notifyObservers("The match time between team-" + homeTeam.getName() + " to team-" + awayTeam.getName() +
                " changed to-" + MainSystem.simpleDateFormat.format(this.getStartDate()));
    }

    public void setField(Field field) {
        this.field = field;
        setChanged();
        notifyObservers("The matching field between team-" + homeTeam.getName() + " to team-" + awayTeam.getName() +
                "at-" + MainSystem.simpleDateFormat.format(this.getStartDate()) + " changed to-" + this.field);
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
//        setChanged();
//        notifyObservers(homeTeam.getName()+" make GOAL, Team's score is: "+homeScore);
    }

    public void setGuestScore(int guestScore) {
        this.guestScore = guestScore;
//        setChanged();
//        notifyObservers(awayTeam.getName()+" make GOAL, Team's score is: "+guestScore);
    }

    // TODO: 03/05/2020 todo TEST
    public void addEventToList(Event e) {
        if (e != null) {
            if (e instanceof Goal || e instanceof ExtraTime || e instanceof Injury || e instanceof Offense ||
                    e instanceof RedCard || e instanceof OffSide || e instanceof Replacement || e instanceof YellowCard) {
                this.getEvents().add(e);
                LOG.info(String.format("%s - %s", e.getName(), "add event to match between " + getHomeTeam().getName() + " to ")
                        + getAwayTeam().getName());
                addEvent(e);
            }
        }
    }

    public void addEventToListInInit(Event e) {
        if (e != null) {
            if (e instanceof Goal || e instanceof ExtraTime || e instanceof Injury || e instanceof Offense ||
                    e instanceof RedCard || e instanceof OffSide || e instanceof Replacement || e instanceof YellowCard) {
                this.getEvents().add(e);
                LOG.info(String.format("%s - %s", e.getName(), "add event to match between " + getHomeTeam().getName() + " to ")
                        + getAwayTeam().getName());
            }
        }
    }

    /**** not supposed to be at referee ? just him legal to add event to his match ****/
    /**** hat about the player that did the event ? ****/
    /**** minute of game ****/
    /**** startDate - timeStamp in event ? ****/
    public void addEvent(Event e) {
        setChanged();
        notifyObservers(e);
    }


    @Override
    public String toString() {
        return this.getHomeTeam().getName()+"-"+this.getAwayTeam().getName()+","+MainSystem.simpleDateFormat.format(this.getStartDate());
    }
}
