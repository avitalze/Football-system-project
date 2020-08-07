package Domain.Users;

import Domain.*;

import Domain.BudgetControl.BudgetReport;
import Domain.Events.Event;
import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.Notifications.Notification;
import Domain.Notifications.NotificationsUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.*;
import java.util.*;

public class Rfa extends Fan implements NotificationsUser {

    private static final Logger LOG = LogManager.getLogger("Rfa");
    public static HashSet<Team> teamRequests;
    public static HashSet<Notification> notifications;

    public boolean gotRFAnotification;
    /*FOR TESTS**/
    public Rfa(Fan fan, MainSystem ms) {
        super(ms, fan.getName(), fan.getPhoneNumber(), fan.getEmail(), fan.getUserName(), fan.getPassword(), fan.getDateOfBirth());
        this.teamRequests= new HashSet<>();
        this.notifications=new HashSet<>();
        system.removeUser(fan);
        gotRFAnotification =false;
    }
    public Rfa(Fan fan) {
        super(fan.getName(), fan.getPhoneNumber(), fan.getEmail(), fan.getUserName(), fan.getPassword(), fan.getDateOfBirth());
        this.teamRequests= new HashSet<>();
        this.notifications=new HashSet<>();
        gotRFAnotification =false;
    }
    public Rfa(MainSystem ms, String name, String phoneNumber, String email, String userName, String password, Date date) {
        super(ms,name,phoneNumber,email,userName,password,date);
        this.teamRequests= new HashSet<>();
        notifications=new HashSet<>();
        gotRFAnotification =false;
    }
    //<editor-fold desc="getters and setters">


    public static HashSet<Team> getTeamRequests() {
        return teamRequests;
    }

    //</editor-fold>

    //<editor-fold desc="rolls to budget control on teams">

    // TODO: 21/04/2020 TEST - V
    /**
     * This role - search by year and check if income for each month of the year bigger than 100
     * gets year to search on, If this year is the current year, look up to the current month excluding
     * @param year
     * @return HashSet<Team> of teams that do not fulfill the Rfa's role
     * @CodeBy yarden
     */
    public HashSet<Team> firstRoleForBudget(int year){

        HashSet<Team> budgetExceptionTeams = new HashSet<>();
        HashSet<Team> activeTeams = this.getSystem().getActiveTeams();
        boolean currentYear = false;

        //check if given year is the current year
        if(YearMonth.now().getYear() == year){
            currentYear = true;
        }

        int numOfMonthToFill = 0; // represent the number of months that we need to check
        if(currentYear){
            numOfMonthToFill = YearMonth.now().getMonthValue()-1;
        }
        else{
            numOfMonthToFill = 12;
        }

        //for each active team
        for (Team t: activeTeams) {

           HashMap<Integer,Integer> MonthAndAmountPerIncome = new HashMap<Integer, Integer>();//key=month, value=amount of income
            /**create number of nodes like number of months**/

            for(int i=1; i<=numOfMonthToFill; i++){
                MonthAndAmountPerIncome.put(i,0);
            }

            LinkedList<BudgetReport> incomeAndExpansePerTeam = t.getBudgetControl().getIncomeAndExpenses();
            //for each report - if it's income - add it to the relevant month
            for (BudgetReport r: incomeAndExpansePerTeam) {

                if (r.getAmount() > 0) {
                    LocalDate date = r.getNow().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    //always check if the year of report equal to the year that given
                    if(date.getYear()==year) {
                        int index = date.getMonthValue();//convert month to quarter
                        //if year is current year, check that report.month not in future months
                        //else, non filter
                        if(currentYear){
                            if( index < YearMonth.now().getMonthValue()) {
                                int currentAmount = MonthAndAmountPerIncome.get(index);
                                int newValue = (int) (currentAmount + r.getAmount());
                                MonthAndAmountPerIncome.put(index, newValue);
                            }
                        }
                        else{
                            int currentAmount = MonthAndAmountPerIncome.get(index);
                            int newValue = (int) (currentAmount + r.getAmount());
                            MonthAndAmountPerIncome.put(index, newValue);
                        }
                    }
                }
            }

            //for each month, check if income bigger than 100
            for (Map.Entry monthAndAmount: MonthAndAmountPerIncome.entrySet()) {
                if((int)monthAndAmount.getValue() < 100){
                    budgetExceptionTeams.add(t);
                    break;
                }
            }

        }
        LOG.info(String.format("%s - %s", this.getUserName(), "start first budget control role on teams"));
        return budgetExceptionTeams;
    }

    // TODO: 21/04/2020 TEST - V
    /**
     * This role - search by year and check if income for each quarterly bigger than 1000
     * gets year to search on, If this year is the current year, look up to the current quarter including
     * @param year
     * @return HashSet<Team> of teams that do not fulfill the Rfa's role
     * @CodeBy yarden
     */
    public HashSet<Team> secondRoleForBudget(int year){

        HashSet<Team> budgetExceptionTeams = new HashSet<>();
        HashSet<Team> activeTeams = this.getSystem().getActiveTeams();
        boolean currentYear = false;

        //check if given year is the current year
        if(YearMonth.now().getYear() == year){
            currentYear = true;
        }

        int numOfqurterToFill = 0; // represent the number of months that we need to check
        if(currentYear){
            numOfqurterToFill = ((YearMonth.now().getMonthValue()-1) / 3) + 1;
        }
        else{
            numOfqurterToFill = 4;
        }

        //for each active team
        for (Team t: activeTeams) {

            HashMap<Integer,Integer> quarterAndAmountPerIncome = new HashMap<Integer, Integer>();//key=quarter, value=amount of income

            /**create number of nodes -> represents number od quarters**/
            for(int i=1; i<=numOfqurterToFill; i++){
                quarterAndAmountPerIncome.put(i,0);
            }

            LinkedList<BudgetReport> incomeAndExpansePerTeam = t.getBudgetControl().getIncomeAndExpenses();
            //for each report - if it's income - add it to the relevant quarter
            for (BudgetReport r: incomeAndExpansePerTeam) {

                if (r.getAmount() > 0) {
                    LocalDate date = r.getNow().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    //always check if the year of report equal to the year that given
                    if(date.getYear()==year) {
                        int index = ((date.getMonthValue()-1)/3)+1;//convert month to quarter
                        //if year is current year, check that report.month not in future quarters
                        //else, non filter
                        if(currentYear){
                            if( index <= numOfqurterToFill && date.getMonthValue()<=YearMonth.now().getMonthValue()) {
                                int currentAmount = quarterAndAmountPerIncome.get(index);
                                int newValue = (int) (currentAmount + r.getAmount());
                                quarterAndAmountPerIncome.put(index, newValue);
                            }
                        }
                        else{
                            int currentAmount = quarterAndAmountPerIncome.get(index);
                            int newValue = (int) (currentAmount + r.getAmount());
                            quarterAndAmountPerIncome.put(index, newValue);
                        }
                    }
                }
            }

            //for each quarter, check if income bigger than 1000
            for (Map.Entry monthAndAmount: quarterAndAmountPerIncome.entrySet()) {
                if((int)monthAndAmount.getValue() < 1000){
                    budgetExceptionTeams.add(t);
                    break;
                }
            }
        }
        LOG.info(String.format("%s - %s", this.getUserName(), "start second budget control role on teams"));
        return budgetExceptionTeams;
    }

    //</editor-fold>



    /**
     * This function gets name of league and create new league, if there isn't already
     * league with the same name in the system
     * @param nameOfLeague
     * @param ms
     * @throws Exception
     * @CodeBy yarden
     */
    //TODO test - V
    public void createNewLeague(String nameOfLeague, MainSystem ms) throws Exception {
        if (nameOfLeague != null && ms != null) {
            League newLeague = new League(nameOfLeague, ms);
            LOG.info(String.format("%s - %s", this.getUserName(), "Create new league"));
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Invalid parameters");
        }

    }

    /**
     * This function gets all the parameters
     * in order to create new referee
     * and add him to the list of all the users
     * @param name
     * @param phoneNumber
     * @param email
     * @param userName
     * @param password
     * @param qualification
     * @param birthDate
     * @throws Exception
     * @CodeBy Yarden
     */
    //TODO test - V
    public void addReferee(String name, String phoneNumber, String email, String userName, String password, String qualification,Date birthDate) throws Exception {
        if( qualification == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        system.checkValidDetails(name, userName, password, phoneNumber,email);
        Referee newRef = new Referee(system, name, phoneNumber, email, userName, password, qualification,birthDate);
        newRef.setSendByEmail(false);
        LOG.info(String.format("%s - %s", this.getUserName(), "Add referee by Rfa"));
    }

    /**
     * This function gets Referee and remove him from the list of all the users
     * cannot remove if he has a match in the future
     * @param ref
     * @throws Exception
     * @CodeBy Yarden
     */
    //TODO test - V
    public void deleteReferee(Referee ref) throws Exception {
            //check the all matches that the referee is refereeing
            if(ref!=null) {
                    for (Match m : ref.getMatches()) {
                //can't delete, match still didn't take place
                if (m.getStartDate().after(new Date(System.currentTimeMillis()))) {
                    LOG.error("can not delete this referee");
                    throw new Exception("You can not delete this referee");
                }
            }
            system.removeUser(ref);
            LOG.info(String.format("%s - %s", this.getUserName(), "Remove referee by Rfa"));
        }else{
                LOG.error("Referee is null");
            throw new Exception("Referee is null");
        }
    }

    /**
     * Initializing Season To League by giving the policies, year and teams that will be in the league in this season
     * define just one league to season, if want more that one league - call again
     * @param schedule
     * @param calculate
     * @param year
     * @param l
     * @param teams
     * @CodeBy yarden
     */
    //TODO test - V
    public void defineSeasonToLeague(SchedulingPolicy schedule, CalculationPolicy calculate, int year, League l, HashSet<Team> teams, LinkedHashSet<Referee> referees,boolean defineCurrSeason) throws Exception {

        if(schedule==null || calculate==null || l==null || ( defineCurrSeason && year < Year.now().getValue() ) || referees.size()<3 ){
            LOG.error("one of parameters null");
            throw new Exception("Invalid details");
        }

        boolean seasonExist=false;
        //if season already exist
        for (Season s: this.getSystem().getSeasons()) {
            if(s.getYear() == year){
                seasonExist=true;
                if (defineCurrSeason) {
                    this.getSystem().setCurrSeason(s);
                }

                /**check if teams are valid - if already play in this season**/
                for (Team t: teams) {
                   if(t.getLeaguePerSeason().containsKey(s)){
                       LOG.error("There is team that already play in this season");
                       throw new Exception("There is team that already play in this season, check the team's list again");
                   }
                }

                HashMap<Season,LinkedHashSet<Referee>> refereesInLeague = new HashMap<>();
                refereesInLeague.put(s,referees);//#
                l.setRefereesInLeague(refereesInLeague);
                s.addLeagueWithTeams(l,teams);
                break;
            }
        }

        if(!seasonExist) {
            //season not exist in the system
            Season newSeason = new Season(this.system, schedule, calculate, year);
            if (defineCurrSeason) {
                this.getSystem().setCurrSeason(newSeason);
            }
            HashMap<Season,LinkedHashSet<Referee>> refereesInLeague = new HashMap<>();
            refereesInLeague.put(newSeason,referees);
            l.setRefereesInLeague(refereesInLeague);
            newSeason.addLeagueWithTeams(l, teams);
        }

        LOG.info(String.format("%s - %s", this.getUserName(), "define season to " + l.getName() + "by Rfa"));
    }

    /**
     * This function gets a year, check if there us season with this year and update
     * the attribute 'currSeason' in the system
     * @param year
     * @CodeBy yarden
     */
    public void updateCurrSeason(int year) throws Exception {
        boolean isSeasonExist=false;

        for (Season s: this.getSystem().getSeasons()) {
            if(s.getYear() == year){
                isSeasonExist=true;
                this.getSystem().setCurrSeason(s);
                LOG.info(String.format("%s - %s", this.getUserName(), "define curr season"));
                break;
            }
        }

        if(!isSeasonExist){
            LOG.error("The season doesn't exist in the system");
            throw new Exception("The season doesn't exist in the system");
        }
    }

    //TODO test - V
    /**
     * This function created in order to start the scheduling policy
     * @param season scheduling all the matches that will appear in this season
//     * @param referees to scheduling at matches
//     * @param mainRef to scheduling at matches
     * @throws Exception
     * @CodeBy yarden
     */
    public void startSchedulingPolicy(Season season) throws Exception {
        if(season==null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        season.getSchedulingPolicy().assign(season.getTeamsInCurrentSeasonLeagues(), season);
//        season.setPlaySchedualingPolicy(true);
        LOG.info(String.format("%s - %s", this.getUserName(), "start scheduling policy by the rfa"));
    }

    //TODO test - V
    /**
     * This function created in order to start the calculating policy
     * @param season - calculate all the leagues in this season
     * @throws Exception
     * @CodeBy yarden
     */
    public void startCalculationPolicy(Season season) throws Exception {
        boolean seasonExist =false;
        if(season==null){
            LOG.error("Season is null");
            throw new Exception("Season is null");
        }

        for (Season s:this.getSystem().getSeasons()) {
            if(s.equals(season)){
                seasonExist=true;
                break;
            }
        }

        if(seasonExist){
            season.getCalculationPolicy().calculate(season.getTeamsInCurrentSeasonLeagues());
//            season.setPlayCalculationPolicy(true);
            LOG.info(String.format("%s - %s", this.getUserName(), "start calculating policy by the rfa "));
        }
        else{
            LOG.error("This season doesn't exist in the system");
            throw new Exception("This season doesn't exist in the system");
        }
    }

    public Team getFromTeamRequests(String team){
        HashSet<Team> teams = getTeamRequests();
        for (Team t: teams) {
            if(t.getName().equals(team)){
                return t;
            }
        }
        return null;
    }

    /**Or**/
    @Override
    //TODO test
    public void update(Observable o, Object arg) {
        //call fan update
        if(o instanceof Team){
            if(arg.equals("request to open new team")){//open new team
                if(system.userLoggedIn(this)){
                    gotRFAnotification =true;
                }
                if(isSendByEmail()==true){
                    sendEmail(this.getEmail(),"You have a new team request.\n"+
                            "The team's name: \n"+ ((Team) o).getName());
                }
                this.teamRequests.add((Team)o);
                this.notifications.add(new Notification(o,arg,false));
            }
        }
        if(o instanceof  Match){
            if(arg instanceof Event){
                super.update(o,arg);
            }
        }
    }

    /**Or**/
    //TODO test- V
    public void answerRequest(Team team, boolean decision) throws Exception {
        if( !teamRequests.contains(team)){
            LOG.error("team not in request list");
            throw new Exception("team not in request list");
        }
        team.sendDecision(decision);
        Notification cur=null;
        /**get the current notification**/
        for(Notification n: notifications){
            if(n.getSender()==team){
                cur=n;
                break;
            }
        }
        if(cur!=null){
            cur.setRead(true);
        }
        teamRequests.remove(team);
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
        gotRFAnotification =false;
    }

    /**
     * get all notifications read and unread
     * @return
     */
    @Override
    public HashSet<Notification> getNotificationsList() {
        gotRFAnotification =false;
        return notifications;
    }

    /***
     * @return only the unread notifications . if not have return null - notify when user connect
     * @codeBy Eden
     */
    @Override
    public HashSet<Notification> getUnReadNotifications(){
        gotRFAnotification =false;
        HashSet<Notification> unRead=new HashSet<>();
        for(Notification n: notifications){
            if(n.isRead()==false){
                unRead.add(n);
            }
        }
        return unRead;
    }

    @Override
    public String checkNotificationAlert() {
        if(gotRFAnotification && gotFanNotification){
            gotRFAnotification =false;
            gotFanNotification=false;
            return "multipleNotifications";
        }
        else if(gotFanNotification){
            gotFanNotification=false;
            return "gotFanNotification";
        }
        else if(gotRFAnotification){
            gotRFAnotification =false;
            return "gotRFAnotification";
        }
        return "";
    }


}
