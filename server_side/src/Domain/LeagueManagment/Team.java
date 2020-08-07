package Domain.LeagueManagment;

import Domain.*;
import Domain.BudgetControl.BudgetControl;
import Domain.Users.*;
import org.apache.logging.log4j.LogManager;

import java.util.*;

import org.apache.logging.log4j.Logger;

public class Team extends Observable implements PageOwner {
    private static final Logger LOG = LogManager.getLogger("Team");
    private String name;
    private HashSet<Match> home;
    private HashSet<Match> away;
    private TeamManager teamManager;
    private HashMap<Season,League> leaguePerSeason;
    private TeamOwner founder;
    private HashSet<Player> players;
    private Coach coach;
    private HashSet<TeamOwner> teamOwners;
    private Field field;
    private PrivatePage privatePage;//added
    protected BudgetControl budgetControl;
    private boolean isActive;
    private MainSystem mainSystem;
    private int score;

//Open team and wait for approval
    public Team(String name, TeamOwner teamOwner){
        //TODO check the name in unique
        this.name = name;
        this.teamOwners = new HashSet<>();
        teamOwners.add(teamOwner);
        addObserver(teamOwner);
        this.founder=teamOwner;
        this.isActive=false;
        this.mainSystem= MainSystem.getInstance();

        this.leaguePerSeason = new HashMap<>();
        this.players = new LinkedHashSet<>();
        this.coach = null;
        this.teamManager = null;
        this.field = null;
        this.budgetControl= new BudgetControl(this);
        this.score = 0;
        this.home= new HashSet<>();
        this.away= new HashSet<>();

        //send request
        for (Rfa rfa:mainSystem.getRfas()) {
            addObserver(rfa);
        }
        setChanged();
        notifyObservers("request to open new team");
        addObserver(teamOwner);
        //add team name to hash set
        mainSystem.addTeamName(name);
    }





    public Team(String name, TeamOwner teamOwner,boolean isInitConstructor){
        //TODO check the name in unique
        this.name = name;
        this.teamOwners = new HashSet<>();
        teamOwners.add(teamOwner);
        addObserver(teamOwner);
        this.founder=teamOwner;
        this.isActive=false;
        this.mainSystem= MainSystem.getInstance();

        this.leaguePerSeason = new HashMap<>();
        this.players = new LinkedHashSet<>();
        this.coach = null;
        this.teamManager = null;
        this.field = null;
        this.budgetControl= new BudgetControl(this);
        this.score = 0;
        this.home= new HashSet<>();
        this.away= new HashSet<>();

        for (Rfa rfa:mainSystem.getRfas()) {
            addObserver(rfa);
        }
        //send request
        //add team name to hash set
        mainSystem.addTeamName(name);
    }

    //for DB
    public Team(String name, int score){
        this.name = name;
        this.teamOwners = new HashSet<>();
        this.isActive=false;
        this.mainSystem= MainSystem.getInstance();
        this.leaguePerSeason = new HashMap<>();
        this.players = new LinkedHashSet<>();
        this.coach = null;
        this.teamManager = null;
        this.field = null;
        this.budgetControl= new BudgetControl(this);
        this.home= new HashSet<>();
        this.away= new HashSet<>();
        this.score = score;
    }

    //just for tests!!!!!1
    public Team(){
        System.out.println("THIS TEAM CONSTRUCTOR IS ONLY FOR TESTS");
        this.name = null;
        this.teamOwners = new HashSet<>();
        this.isActive=false;
        this.mainSystem= MainSystem.getInstance();
        this.leaguePerSeason = new HashMap<>();
        this.players = new LinkedHashSet<>();
        this.coach = null;
        this.teamManager = null;
        this.field = null;
        this.budgetControl= new BudgetControl(this);
        this.home= new HashSet<>();
        this.away= new HashSet<>();
    }

    //just for tests!!!!!!!
    public Team(String name){
        System.out.println("THIS TEAM CONSTRUCTOR IS ONLY FOR TESTS");
        this.name = name;
        this.teamOwners = new HashSet<>();
        this.isActive=false;
        this.mainSystem= MainSystem.getInstance();
        this.leaguePerSeason = new HashMap<>();
        this.players = new LinkedHashSet<>();
        this.coach = null;
        this.teamManager = null;
        this.field = null;
        this.budgetControl= new BudgetControl(this);
        this.home= new HashSet<>();
        this.away= new HashSet<>();
    }


    //<editor-fold desc="getters and setters">
    public void setName(String name) {
        this.name = name;
    }

    public void setHome(HashSet<Match> home) {
        this.home = home;
    }

    public void setAway(HashSet<Match> away) {
        this.away = away;
    }

    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    public void setPlayers(HashSet<Player> players) {
        this.players = players;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setTeamOwners(HashSet<TeamOwner> teamOwners) {
        this.teamOwners = teamOwners;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public HashSet<Match> getHome() {
        return home;
    }

    public HashSet<Match> getAway() {
        return away;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public HashSet<Player> getPlayers() {
        return players;
    }

    public Coach getCoach() {
        return coach;
    }

    public HashSet<TeamOwner> getTeamOwners() {
        return teamOwners;
    }

    public Field getField() {
        return field;
    }

    public void setLeaguePerSeason(HashMap<Season, League> leaguePerSeason) {
        this.leaguePerSeason = leaguePerSeason;
    }

    public PrivatePage getPrivatePage() {
        return privatePage;
    }

    public void setPrivatePage(PrivatePage privatePage) {
        this.privatePage = privatePage;
    }

    public HashMap<Season, League> getLeaguePerSeason() {
        return leaguePerSeason;
    }

    public TeamOwner getFounder() {
        return founder;
    }

    public void setFounder(TeamOwner founder) {
        this.founder = founder;
    }

    public BudgetControl getBudgetControl() {
        return budgetControl;
    }

    public void setBudgetControl(BudgetControl budgetControl) {
        this.budgetControl = budgetControl;
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public void setActive(boolean active) {
        isActive = active;
    }

    //</editor-fold>

    //<editor-fold desc="add and remove functions">
    // adi
    //TODO test - V
    public void addTeamOwner(TeamOwner tO) throws Exception {
        if(tO!=null) {
            teamOwners.add(tO);
            addObserver(tO);
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("TeamOwner is null");
        }
    }
    // adi
    //TODO test - V
    public void removeTeamOwner(TeamOwner tO)throws Exception{
        if(tO!=null) {
            if (teamOwners.contains(tO)) {
                teamOwners.remove(tO);
            } else {
                LOG.error("TeamOwner doesn't exist in this team");
                throw new Exception("TeamOwner doesn't exist in this team");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("TeamOwner is null");
        }
    }
    // adi
    //TODO test - V
    public void removeTeamManager(TeamManager tM)throws Exception{
        if(tM!=null) {
            if (tM.equals(teamManager)) {
                teamManager = null;
            } else {
                LOG.error("this TeamManager doesn't exist in this team");
                throw new Exception("This TeamManager doesn't exist in this team");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("TeamManager is null");
        }
    }
    //adi
    //TODO test - V
    public void addCoach(Coach c) throws Exception {
        if(c!=null) {
            if (this.coach == null) {
                coach = c;
            }
            else{
                LOG.error("There is coach to this team");
                throw new Exception("There is coach to this team");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Coach is null");
        }
    }
    // adi
    //TODO test - V
    public void removeCoach(Coach coachToRemove)throws Exception{
        if(coachToRemove != null) {
            if(this.coach !=null) {
                if (this.coach.equals(coachToRemove)) {
                    coach = null;
                } else {
                    LOG.error("This Coach doesn't exist in this team");
                    throw new Exception("This Coach doesn't exist in this team");
                }
            }
            else{
                LOG.error("There isn't coach to remove in this team");
                throw new Exception("There isn't coach to remove in this team");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Coach is null");
        }
    }
    //adi
    //TODO test - V
    public void addPlayer(Player p) throws Exception {

        if(p!=null) {
                players.add(p);
//                p.setActiveOnTeam(true);
        } else {
            LOG.error("one of parameters null");
            throw new Exception("Player is null");
        }
    }
    //adi
    //TODO test - V
    public void removePlayer(Player p)throws Exception{
       if(p!=null) {
           if (players.contains(p)) {
               if (players.size() > 11) {
                   players.remove(p);
               } else {
                   LOG.error("The team has only 11 or less players");
                   throw new Exception("The team has only 11 or less players");
               }
           } else {
               LOG.error("This Player doesn't exist in this team");
               throw new Exception("This Player doesn't exist in this team");
           }
       }
       else {
           LOG.error("one of parameters null");
           throw new Exception("Player is null");
       }
    }

    //adi
    //TODO test - V
    public void removeField(Field f)throws Exception{
        if(f!=null) {
            if(this.field!=null) {
                if (this.field.equals(f)) {
                    field = null;
                } else {
                    LOG.error("This field doesn't exist in this team");
                    throw new Exception("This field doesn't exist in this team");
                }
            }
            else{
                LOG.error("There isn't field to remove");
                throw new Exception("There isn't field to remove");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Field is null");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Page Owner Functions">

    /**Or**/
    //TODO test
    @Override
    public PrivatePage getPage() {
        return privatePage;
    }
    /**Or**/
    @Override
    public void setPage(PrivatePage p) {
        this.privatePage=p;
    }
    /**Or**/
    @Override
    public String getOwnerName() {
        return name;
    }



    //</editor-fold>

    /**
     * this function connect the team to the current season and current league.
     * The function called only!!!! from Season\League while adding the team
     * and connect them to the teams at current season and current league.
     * @param s- season
     * @param l- league
     */
    //TODO test - V
    public void addLeagueAndSeason(Season s, League l){
        if(s!=null && l!=null) {
            leaguePerSeason.put(s, l);
            LOG.info(String.format("%s - %s", getName(), "add team to league:"+l.getName()+" and season"+s.getYear()));
        }
        else{
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
    }

    /**OR
     * this function checks if the team has this season in the hash map
     * @param seasonYear
     * @return
     */
    public boolean playedInSeason(int seasonYear){
        for (Season s: getLeaguePerSeason().keySet()) {
            if(s.getYear()==seasonYear){
                return true;
            }
        }
        return false;
    }
    
    /**OR
     * add income to budget control
     * @param typeOfIncome
     * @param amount
     * @throws Exception
     */
    //TODO test - V
    public void addIncome(String typeOfIncome, long amount) throws Exception {
        if(typeOfIncome!=null) {
            this.budgetControl.addIncome(typeOfIncome, amount);
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("type of income not valid");
        }
    }

    /**Or
     * add expense in budget control
     * @param typeOfExpense
     * @param amount
     * @throws Exception
     */
    //TODO test - V
    public void addExpense(String typeOfExpense, long amount) throws Exception {
        if(typeOfExpense!=null) {
            this.budgetControl.addExpense(typeOfExpense, amount);
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("type of expanse not valid");
        }
    }

    /**Or**/
    public boolean isActive(){
        return isActive;
    }

    /**Or
     * when team is deleted by team owner
     * delete the connections between player to team.
     */
    //TODO test
    public void deleteTeamByTeamOwner() throws Exception {
        for (Player p:players) {
            p.setPlayerTeam(null);
        }
        coach.setCoachTeam(null);
        field.removeTeam(this);
        if(privatePage!=null){
            privatePage.setPageOwner(null);
            privatePage=null;
        }
        if(teamManager != null){
            teamManager.setTeam(null);
            teamManager.getTeamRole().deleteTeamManager();
            addObserver(teamManager);
        }

        isActive=false;
        mainSystem.removeActiveTeam(this);
        founder=null;

        //remove the subscriptions of all team owners
        for (TeamOwner teamOwner:teamOwners) {
            Iterator<TeamSubscription> iter= teamOwner.getMySubscriptions().iterator();
            TeamSubscription sub;
            while (iter.hasNext()){
                sub=iter.next();
                if(sub.team.equals(this)){
                    teamOwner.removeMySubscription(sub);
                }
            }
            //remove the team from activeTeams and move to deletedTeams
            teamOwner.removeTeam(this);
            teamOwner.addDeletedTeam(this);
        }

        //notify systemManagers
        for (SystemManager sm: mainSystem.getSystemManagers()) {
            addObserver(sm);
        }
        /**add team owner - Eden**/
        for (TeamOwner to:getTeamOwners()) {
          addObserver(to);
        }
        setChanged();
        notifyObservers("team deleted by team owner");

        LOG.info(String.format("%s - %s", name, "team was deleted by team owner"));
    }

    /**OR
     * team becomes active
     * @param players
     * @param coach
     * @param field
     * @throws Exception
     */
    //TODO test - V
    public void becomeActive(HashSet<Player> players, Coach coach, Field field) throws Exception {
        if(players!=null && coach!=null && field!=null) {
            if (players.size() < 11) {
                LOG.error(" number of players are less than 11");
                throw new Exception("The number of players are less than 11");
            }
            this.players = players;
            for (Player player : players) {
                if (player.getTeam() == null) {
                    player.setPlayerTeam(this);
                } else {
                    LOG.error("one of the players team is not null");
                    throw new Exception("one of the players team is not null");
                }
            }
            this.coach = coach;
            this.coach.setCoachTeam(this);
            this.field = field;
            this.field.addTeam(this);

            //add team to active list in system
            this.isActive = true;
            this.mainSystem.addActiveTeam(this);

            LOG.info(String.format("%s - %s", name, "team was activated"));
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Invalid parameters");
        }
    }

    /**OR
     * the team owner calls this function when he want to reOpen the team
     * he is the only team owner now
     * @param players- at least 11
     * @param coach
     * @param field
     * @param newFounder- send himself
     * @throws Exception
     */
    //TODO test - V
    public void reopenTeam(HashSet<Player> players, Coach coach, Field field, TeamOwner newFounder) throws Exception {
        if(newFounder ==null || players==null|| coach==null || field==null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        this.founder=newFounder;
        becomeActive(players,coach,field);

        //remove all the other team owners
//        Iterator<TeamOwner> iter= teamOwners.iterator();
//        TeamOwner teamOwner;
//        while (iter.hasNext()){
//            teamOwner= iter.next();
//            if(!teamOwner.equals(newFounder)){
//                teamOwner.removeDeletedTeam(this);
//                teamOwners.remove(teamOwner);
//            }
//        }
        LinkedList<TeamOwner> teamOwnersList = new LinkedList<>();
        for(TeamOwner tO: teamOwners){
            teamOwnersList.add(tO);
        }
        for(int i=0; i< teamOwnersList.size();i++){
            TeamOwner tO = teamOwnersList.get(i);
            if(!tO.equals(newFounder)){
                tO.removeDeletedTeam(this);
                teamOwners.remove(tO);
            }
        }

        //notify system managers
        for (SystemManager sm: mainSystem.getSystemManagers()) {
            addObserver(sm);
        }
        /**notify - all team owners **/
        for(TeamOwner t: getTeamOwners()){
            addObserver(t);
        }
        if(teamManager!=null) {
            addObserver(teamManager);
        }
        setChanged();
        notifyObservers("team reopened by team owner");
        /**remove all observer except founder teamOwner***/
        deleteObservers();
        addObserver(founder);
        LOG.info(String.format("%s - %s", name, "team was re-opened"));
    }


    /**Yarden**/
    //TODO test
    public void addMatchToHomeMatches(Match match) throws Exception {
        if(match!=null) {
            this.getHome().add(match);
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Match is null");
        }
    }

    /**Yarden**/
    //TODO test
    public void addMatchToAwayMatches(Match match) throws Exception {
        if(match!=null) {
            this.getAway().add(match);
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Match is null");
        }
    }

    public void sendDecision(boolean decision){
        setChanged();
        notifyObservers(decision);
    }

    public void sendNotiAbouteClose() {
        setChanged();
        notifyObservers(name+" removed from system by system manager");
    }


    public void setAllPlayersNotActive(){
        for (Player p: this.getPlayers()) {
            p.setActiveOnTeam(false);
        }
    }

}
