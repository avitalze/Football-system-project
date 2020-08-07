package Domain;

import Domain.ExternalSystems.*;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.Users.*;
import Stubs.StubExternalSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MainSystem {
    //private LinkedList<Complaint> complaints;// *??????
    public List<User> loginUsers;
    private LinkedList<League> leagues;//*
    private LinkedList<User> users;//*
    private LinkedList<Season> seasons;//*
    private HashSet<Field> fields;//*
    private Season currSeason;
    private StubExternalSystem extSystem;
    private HashSet<Team> activeTeams;
    private HashSet<String> userNames;
    private HashSet<String> teamNames;
    // public static final String pattern = "dd-M-yyyy hh:mm:ss";
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final SimpleDateFormat birthDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final Logger LOG = LogManager.getLogger("MainSystem");
    private static MainSystem mainSystem_instance = null;

    /**ExternalSystems **/
    private RFABudgetControlInterface rfaBudgetControl;
    private TaxLawInterface taxSystem;

    private MainSystem() {
        this.leagues = new LinkedList<>();
        this.users = new LinkedList<>();
        this.seasons = new LinkedList<>();
        this.currSeason = null;
        this.extSystem = new StubExternalSystem();
        this.activeTeams = new HashSet<>();
        this.userNames = new HashSet<>();
        this.teamNames = new HashSet<>();
        this.loginUsers= new LinkedList<>();
        this.fields = new HashSet<>();
        taxSystem=null;
        rfaBudgetControl=null;
    }


    //Singleton
    public static MainSystem getInstance() {
        if (mainSystem_instance == null) {
            mainSystem_instance = new MainSystem();
        }
        return mainSystem_instance;
    }

    /**
     * OR
     **/
    public LinkedList<Fan> getAllFans() {
        LinkedList<Fan> ans = new LinkedList<>();
        for (User user : users) {
            if (user instanceof Fan) {
                ans.add((Fan) user);
            }
        }
        return ans;
    }

    public Season getSeasonByYear(int year){
        for(Season s : seasons){
         if(s.getYear() == year)
            return s;
        }
        return null;
    }

    /**
     * OR
     **/
    public List<SystemManager> getSystemManagers() {
        List<SystemManager> res = new LinkedList<>();
        for (User u : users) {
            if (u instanceof SystemManager) {
                res.add(((SystemManager) u));
            }
        }
        return res;
    }

    /**
     * OR
     **/
    public LinkedList<Rfa> getRfas() {
        LinkedList<Rfa> res = new LinkedList<>();
        for (User u : users) {
            if (u instanceof Rfa) {
                res.add(((Rfa) u));
            }
        }
        return res;
    }

    /**
     * OR
     **/
    public LinkedList<TeamRole> getTeamRoles() {
        LinkedList<TeamRole> teamroles = new LinkedList<>();
        for (User user : users) {
            if (user instanceof TeamRole) {
                teamroles.add((TeamRole) user);
            }
        }
        return teamroles;
    }

    /**
     * OR
     **/
    public LinkedList<Player> getAllPlayer() {
        LinkedList<Player> players = new LinkedList<>();
        for (TeamRole teamRole : getTeamRoles()) {
            if (teamRole.isPlayer()) {
                players.add(teamRole.getPlayer());
            }
        }
        return players;
    }

    /**
     * Eden
     **/
    public LinkedList<TeamOwner> getAllTeamOwners() {
        LinkedList<TeamOwner> teamOwners = new LinkedList<>();
        for (TeamRole teamRole : getTeamRoles()) {
            if (teamRole.isTeamOwner()) {
                teamOwners.add(teamRole.getTeamOwner());
            }
        }
        return teamOwners;
    }


    /**
     * OR
     **/
    public LinkedList<Coach> getAllCoach() {
        LinkedList<Coach> coaches = new LinkedList<>();
        for (TeamRole teamRole : getTeamRoles()) {
            if (teamRole.isCoach()) {
                coaches.add(teamRole.getCoach());
            }
        }
        return coaches;
    }


    //<editor-fold desc="add and remove from lists">

    // adi
    //TODO test-V
    public boolean removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            return true;
        }
        return false;
    }

    // adi
    //TODO test-V
    public boolean addUser(User user) {
        if (users.contains(user)) {
            return false;
        }
        users.add(user);
        return true;
    }


    // or
    //TODO test-V
    public boolean removeLeague(League l) {
        if (leagues.contains(l)) {
            leagues.remove(l);
            return true;
        }
        return false;
    }

    // or
    //TODO test -V
    public boolean addLeague(League l) {
        if (leagues.contains(l)) {
            return false;
        }
        leagues.add(l);
        return true;
    }
    public boolean removeField(Field f) {
        if (fields.contains(f)) {
            fields.remove(f);
            return true;
        }
        return false;
    }

    public HashSet<Field> getFields() {
        return fields;
    }

    public void setFields(HashSet<Field> fields) {
        this.fields = fields;
    }

    // or
    //TODO test -V
    public boolean addField(Field f) {
        if (fields.contains(f)) {
            return false;
        }
        fields.add(f);
        return true;
    }
    public boolean checkIfFieldExists(String fieldName){
        for (Field f : fields){
            if (f.getNameOfField().equals(fieldName)){
                return true;
            }
        }
        return false;
    }

    // adi
    //TODO test-V
    public boolean removeSeason(Season s) {
        if (seasons.contains(s)) {
            seasons.remove(s);
            return true;
        }
        return false;
    }

    // adi
    //TODO test-V
    public boolean addSeason(Season s) {
        if (seasons.contains(s)) {
            return false;
        }
        seasons.add(s);
        LOG.info(String.format("%s - %s","MainSystem", "system was added new season:"+s.getYear()));
        return true;
    }

    //or
    //TODO test-V
    public boolean addActiveTeam(Team team) {
        if (!team.isActive() || activeTeams.contains(team)) {
            return false;
        }
        activeTeams.add(team);
        return true;
    }


    //or
    //TODO test-V
    public boolean removeActiveTeam(Team team) {
        if (!activeTeams.contains(team)) {
            return false;
        }
        activeTeams.remove(team);
        return true;
    }

    /**
     * OR
     **/
    //TODO test-
    public boolean addUserName(String name) {
        if (userNames.contains(name)) {
            return false;
        }
        userNames.add(name);
        return true;
    }

    /**
     * OR
     **/
    //TODO test-V
    public boolean addTeamName(String name) {
        if (teamNames.contains(name)) {
            return false;
        }
        teamNames.add(name);
        return true;
    }

    /**
     * OR
     **/
    //TODO test-v
    public boolean removeUserName(String name) {
        if (!userNames.contains(name)) {
            return false;
        }
        userNames.remove(name);
        return true;
    }

    /**
     * OR
     **/
    //TODO test-V
    public boolean removeTeamName(String name) {
        if (!teamNames.contains(name)) {
            return false;
        }
        teamNames.remove(name);
        return true;
    }
    //</editor-fold>


    //<editor-fold desc="getters and setters">


    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(LinkedList<League> leagues) {
        this.leagues = leagues;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(LinkedList<User> users) {
        this.users = users;
    }

    public LinkedList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(LinkedList<Season> seasons) {
        this.seasons = seasons;
    }

    public Season getCurrSeason() {
        return currSeason;
    }

    public void setCurrSeason(Season currSeason) {
        this.currSeason = currSeason;
    }

    public StubExternalSystem getExtSystem() {
        return extSystem;
    }

    public void setExtSystem(StubExternalSystem extSystem) {
        this.extSystem = extSystem;
    }

    public HashSet<Team> getActiveTeams() {
        return activeTeams;
    }

    public void setActiveTeams(HashSet<Team> activeTeams) {
        this.activeTeams = activeTeams;
    }

    public HashSet<String> getUserNames() {
        return userNames;
    }

    public HashSet<String> getTeamNames() {
        return teamNames;
    }

//</editor-fold>

    /**
     * OR
     **/
    //TODO test-V
    public void firstStartSystem() throws ParseException {

        //create user for system manager
        SystemManager defultSM = new SystemManager(this, "Defult system Manager", "0541234567", "defult@google.com", "systemManager", "systemManager101", MainSystem.birthDateFormat.parse("01-01-2000"));
        //link external systems....
        extSystem.connectToSystem(this);
        LOG.info(String.format("%s - %s", "", "system was started for the first time"));
    }

    /**
     * OR
     **/
    //TODO test-V
    public void startSystem() throws ParseException {
        //read things from DB.....
        if (getSystemManagers().size() == 0) {
            firstStartSystem();
        } else {
            //link external systems....
            extSystem.connectToSystem(this);
            LOG.info(String.format("%s - %s", "", "system was started"));
        }
    }

    public void initMainSystem(){
        rfaBudgetControl= new RFABudgetControlProxy(new RFABudgetControl());
        taxSystem=new TaxSystemProxy(new TaxLawSystem());
    }

    /**
     * check how many RFA users at system
     *
     * @return number of RFA
     * @codeBy Eden
     */
    //TODO test-V
    public int numOfRfa() {
        int sum = 0;
        for (User u : users) {
            if (u instanceof Rfa) {
                sum++;
            }
        }
        return sum;
    }

    public List<Referee> getAllReferees() {
        List<Referee> referees = new LinkedList<>();
        for (User r : users) {
            if (r instanceof Referee) {
                referees.add(((Referee) r));
            }
        }
        return referees;
    }

    public boolean containsReferee(String name) {
        boolean ans = false;

        for (Referee referee : this.getAllReferees()) {
            if (referee.getUserName().equals(name)) {
                ans = true;
                break;
            }
        }

        return ans;
    }

    public boolean containsLeague(String name) {
        boolean ans = false;

        for (League l : leagues) {
            if (l.getName().equals(name)) {
                ans = true;
                break;
            }
        }

        return ans;
    }

    public void addTeamForSysterm_TestsOnly(Team t) {
        activeTeams.add(t);
    }

    public LinkedList<TeamManager> getAllTeamManagers() {
        LinkedList<TeamManager> teamManagers = new LinkedList<>();
        for (TeamRole teamRole : getTeamRoles()) {
            if (teamRole.isTeamManager()) {
                teamManagers.add(teamRole.getTeamManager());
            }
        }
        return teamManagers;
    }

    public void setUserNames(HashSet<String> userNames) {
        this.userNames = userNames;
    }

    public void setTeamNames(HashSet<String> teamNames) {
        this.teamNames = teamNames;
    }

    //<editor-fold desc="Sign in Functions">

    /**OR**/
    //TODO test- V
    //TODO save user to DB - fan, teamRole, player
    public void signInAsPlayer(String name, String phoneNumber, String email, String userName, String password, Date dateOfBirth, boolean sendByEmail) throws Exception {
        // first check valid details
        checkValidDetails(name,userName,password,phoneNumber,email);
        TeamRole newPlayer= new TeamRole(this,name,phoneNumber,email,userName,password,dateOfBirth);
        newPlayer.becomePlayer();
        newPlayer.setSendByEmail(sendByEmail);
        /**save user to DB - fan, teamRole, player**/
        LOG.info(String.format("%s - %s", userName, "sign in as Player"));
    }

    /**OR**/
    //TODO test- V
    //TODO save user to DB - fan, teamRole, coach
    public void signInAsCoach(String name, String phoneNumber, String email, String userName, String password,Date dateOfBirth, boolean sendByEmail) throws Exception {
        // first check valid details
        checkValidDetails(name,userName,password,phoneNumber,email);
        TeamRole newCoach= new TeamRole(this,name,phoneNumber,email,userName,password,dateOfBirth);
        newCoach.becomeCoach();
        newCoach.setSendByEmail(sendByEmail);
        //save user to DB - fan, teamRole, coach
        LOG.info(String.format("%s - %s", userName, "sign in as Coach"));
    }


    /**OR**/
    //TODO test- V
    //TODO save user to DB - fan
    public void signInAsFan(String name, String phoneNumber, String email, String userName, String password,  Date dateOfBirth, boolean sendByEmail) throws Exception {
        // first check valid details
        checkValidDetails(name,userName,password,phoneNumber,email);
        Fan newFan= new Fan(this,name,phoneNumber,email,userName,password, dateOfBirth);
        newFan.setSendByEmail(sendByEmail);
        //save user to DB - fan
        LOG.info(String.format("%s - %s", userName, "sign in as Domain.Users.Fan"));
    }



    /**OR**/
    //TODO test- V
    //TODO save user to DB - rfa, fan
    public void signInAsRFA(String name, String phoneNumber, String email, String userName, String password,  Date dateOfBirth, boolean sendByEmail) throws Exception {
        // first check valid details
        checkValidDetails(name,userName,password,phoneNumber,email);
        Rfa newRFA= new Rfa(this,name,phoneNumber,email,userName,password,dateOfBirth);
        newRFA.setSendByEmail(sendByEmail);
        //save user to DB - rfa, fan
        LOG.info(String.format("%s - %s", userName, "sign in as RFA"));
    }

    /**OR**/
    //TODO test- V
    //TODO save user to DB - teamRole, fan
    public void signInAsTeamOwner(String name, String phoneNumber, String email, String userName, String password, Date dateOfBirth, boolean sendByEmail) throws Exception {
        // first check valid details
        checkValidDetails(name,userName,password,phoneNumber,email);
        TeamRole teamOwner= new TeamRole(this,name,phoneNumber,email,userName,password, dateOfBirth);
        teamOwner.becomeTeamOwner();
        teamOwner.setSendByEmail(sendByEmail);
        //save user to DB - teamRole, fan
        LOG.info(String.format("%s - %s", userName, "sign in as team owner"));
    }

    /**or
     * this function check if the details are valid
     * @param name- name not null
     * @param userName - unique and not null
     * @param password - more than 6 characters , not null
     * @param phoneNumber- 10 digits, not null
     * @param email- contains @, .com or .co.il
     * @throws Exception
     */
    //TODO test-V
    public void checkValidDetails(String name, String userName, String password, String phoneNumber, String email) throws Exception {
        //check name not null
        if(name==null){
            LOG.error("name not valid");
            throw new Exception("name not valid");
        }
        //check that username in unique
        if(userName==null || this.getUserNames().contains(userName)){
            LOG.error("user name not valid");
            throw new Exception("user name not valid");
        }
        //password length is 6 or more
        if(password==null ||password.length()<6){
            LOG.error("password not valid");
            throw new Exception("password not valid");
        }
        // phone number is 10 digits
        if(phoneNumber==null || !( phoneNumber.matches("^[0-9]*$") && phoneNumber.length()==10) ){
            LOG.error("phone number not valid");
            throw new Exception("phone number not valid");
        }
        //email contains @
        if(email==null ||! email.contains("@")){
            LOG.error("email not valid");
            throw new Exception("email not valid");
        }
        if( ! (email.contains(".com") || email.contains(".co.il"))){
            LOG.error("email not valid");
            throw new Exception("email not valid");
        }
    }
    //</editor-fold>

    public  void writeToLogError(String err){
        LOG.error(err);
    }
    /**OR
     * this function return the user that the userName and password are correct
     * null if there is no user that fits
     * @param userName
     * @param password
     * @return
             */
    public Fan logIn(String userName, String password) throws Exception {
        if(userName==null){
            LOG.error("userName null");
            throw new Exception("userName null");
        }
        if(userName.length()==0){
            LOG.error("userName empty");
            throw new Exception("userName empty");
        }
        if(password==null){
            LOG.error("password null");
            throw new Exception("password null");
        }
        if(password.length()<6){
            LOG.error("password not valid");
            throw new Exception("password not valid");
        }

        //search type of user
        //create an object and save as connect user
        for (Fan fan:getAllFans() ) {
            if(fan.getUserName().equals(userName) && fan.getPassword().equals(password)){
                LOG.info(String.format("%s - %s", userName, "loged in to system"));
                //add user to loged in users
                loginUsers.add(fan);
                return fan;
            }
        }
        LOG.error("details not correct, no fan in system");
        throw new Exception("details not correct, no fan in system");
    }

    /**OR
     * log out user from system
     * @param f
     * @throws Exception
     */
    public void logOut(Fan f) throws Exception {
        if( !loginUsers.contains(f)){
            LOG.error("fan is not logged in to system ");
            throw new Exception("fan is not logged in to system ");
        }
        LOG.info(String.format("%s - %s", f.getUserName(), "logged out from system"));
        loginUsers.remove(f);
    }

    public boolean userLoggedIn(User u){
        return loginUsers.contains(u);
    }

}
