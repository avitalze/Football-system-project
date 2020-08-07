package Domain.Users;

import Domain.*;
import Domain.Enums.TeamManagerPermissions;
import Domain.Events.Event;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.Notifications.Notification;
import Domain.Notifications.NotificationsUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TeamOwner extends ManagmentActions implements NotificationsUser {
    private TeamRole teamRole;
    private LinkedList<Team> teams;
    //private HashSet<TeamSubscription> mySubscriptions; //TODO moved to managment actions
    private LinkedList<Team> requestedTeams;
    private LinkedList<Team> deletedTeams;
    private LinkedList<Team> approvedTeams;
    private static final Logger LOG = LogManager.getLogger("TeamOwner");
    private HashSet<Notification> notifications;

    private boolean gotTeamOwnerNotification;

    //team owner founder- with no team.
    public TeamOwner(TeamRole teamRole) {
        this.teams = new LinkedList<>();
        mySubscriptions = new HashSet<>();
        this.teamRole = teamRole;
        this.requestedTeams=new LinkedList<>();
        this.deletedTeams= new LinkedList<>();
        this.approvedTeams= new LinkedList<>();
        this.teamRole= teamRole;
        this.notifications= new HashSet<>();
        gotTeamOwnerNotification=false;
    }


    /**OR
     * request the opening new team
     * @param name
     */
    //TODO test-V
    public void requestNewTeam(String name) throws Exception {
        if(teamRole.system.getTeamNames().contains(name)){
            LOG.error("team name not unique, already exist in system");
            throw new Exception("team name not unique, already exist in system");
        }
        Team t= new Team(name,this);
        //the request is sent in the Constructor
        requestedTeams.add(t);
    }

    /**Or
     *  check if the team can become active
     *  if can- send to become active function in team
     * @param team
     * @param tempPlayers
     * @param coach
     * @param field
     * @throws Exception - one of the parameters is null, team was not approved by RFA
     */
    //TODO test-V
    public void makeTeamActive(Team team, HashSet<TeamRole> tempPlayers , TeamRole coach, Field field) throws Exception{
        if(team == null || tempPlayers == null || coach == null || field==null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        if(!approvedTeams.contains(team)){
            LOG.error("this team is not approved by RFA");
            throw  new Exception("this team is not approved by RFA");
        }
        HashSet<Player> players = new HashSet<>();
        for (TeamRole p : tempPlayers){
            if (!p.isPlayer()){
                p.becomePlayer();
            }
            players.add(p.getPlayer());
        }
        if (!coach.isCoach()){
            coach.becomeCoach();
        }
        team.becomeActive(players,coach.getCoach(),field);
        this.teams.add(team);
        this.approvedTeams.remove(team);
    }

    /**OR
     * this function checks if the team can be reopened- if can- send to team delete function
     * @param team
     * @throws Exception - if team is null,team is playing in current season, team has a future match
     */
    //TODO test-V
    public void deleteTeam(Team team) throws Exception {
        if(team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        if(team.getLeaguePerSeason().containsKey(teamRole.system.getCurrSeason())){
            LOG.error("team is play in the current season ,cannot delete the team until the end of the season");
            throw new Exception("team is play in the current season ,you cannot delete the team until the end of the season");
        }
        Date currDate= new Date(System.currentTimeMillis());
        for (Match m:team.getHome()) {
            if(m.getStartDate().after(currDate)){
                LOG.error("cannot delete team with future matches");
                throw  new Exception("cannot delete team with future matches");
            }
        }
        for( Match m:team.getAway()){
            if(m.getStartDate().after(currDate)){
                LOG.error("cannot delete team with future matches");
                throw  new Exception("cannot delete team with future matches");
            }
        }
        team.deleteTeamByTeamOwner();
    }

    /**OR
     * reopen deleted team- this function checks if the team can be reopened
     * if yes- send to team reopen function
     * @param team
     * @param players
     * @param coach
     * @param field
     * @throws Exception
     */
    //TODO test- V
    public void reopenTeam(Team team, HashSet<Player> players, Coach coach, Field field) throws Exception
    {
        if(team ==null || players==null|| coach==null || field==null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        if(!deletedTeams.contains(team)){
            LOG.error("the team was not deleted");
            throw new Exception("the team was not deleted");
        }
        team.reopenTeam(players,coach,field,this);
        deletedTeams.remove(team);
        teams.add(team);
    }


    //<editor-fold desc="add remove and edit">

    @Override
    public TeamRole subscribeTeamOwner(Fan fan, Team team) throws Exception{
        return super.subscribeTeamOwner(fan,team);
    }

    /**
     * adi
     * this team owner removes a team owner that he subscribed in the past
     * @param tO - team owner to remove
     * @param team - the team that the team owner will be removed from
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void removeTeamOwner (TeamOwner tO, Team team)throws Exception{
        super.removeTeamOwner(tO,team);

        /**Notifications - Eden***/
        team.deleteObserver(tO);
        tO.deleteTeamNotification(team,this);
        /*************************/

    }

    /**
     * add notification about delete you from team owner
     * @param removedTeam
     * @param theOneRemoveYou
     * @codeBy
     */
    private void deleteTeamNotification(Team removedTeam, TeamOwner theOneRemoveYou) {
        notifications.add(new Notification(removedTeam, "you removed from team owner by -"+theOneRemoveYou.getTeamRole().getUserName(),false));
    }

    /**
     * adi
     * this team owner subscribes a fan to become a team manager of a specific team
     * @param fan - the person you want to make a team manager
     * @param team - the team you want to add a team manager
     * @param per - the team managers allowed permissions
     * @return the new team manager
     * @throws Exception
     */
    //TODO test-V
    // only team owner
    public TeamRole subscribeTeamManager(Fan fan, Team team, HashSet<TeamManagerPermissions> per) throws Exception{
        if (fan == null  || team == null || per == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        // check if already team manager of this team
        if (fan instanceof TeamRole && ((TeamRole) fan).isTeamManager() && team.getTeamManager().equals(((TeamRole) fan).getTeamManager())){
            LOG.error("Already Team Manager of this team");
            throw new Exception("Already Team Manager of this team");
        }
        TeamRole teamRole;
        // check if already teamRole
        if (fan instanceof TeamRole){
            ((TeamRole) fan).becomeTeamManager(team, per);
            ((TeamRole) fan).getTeamManager().setTeam(team);
            teamRole = ((TeamRole) fan);
        }
        // else just a fan
        else{
            teamRole = new TeamRole(fan);
            teamRole.becomeTeamManager(team, per);
            //teamRole.getTeamManager().setTeam(team);
        }
        TeamSubscription sub = new TeamSubscription(teamRole.getTeamManager(), team, teamRole);
        mySubscriptions.add(sub);
        LOG.info(String.format("%s - %s", teamRole.getUserName(), " add as team manager: "+fan.getUserName()+", to team: "+team.getName()));
        return teamRole;

    }
    /**
     * adi
     * this team owner removes a team manager that he subscribed in the past
     * @param tM - team manager to remove
     * @param team - the team that the team manager will be removed from
     * @throws Exception
     */
    //TODO test-V
    // only team owner!!!!!!!!!
    public void removeTeamManager (TeamManager tM, Team team) throws Exception{
        if (tM == null || team == null || team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        team.removeTeamManager(tM);
        for(TeamSubscription sub : mySubscriptions){
            if (sub.user.equals(tM.getTeamRole()) && sub.role.equals(tM)){
                mySubscriptions.remove(sub);
            }
            break;
        }
        for (TeamSubscription sub : tM.getMySubscriptions()) {
            if (sub.team.equals(team)) {
                tM.removeTeamOwner((TeamOwner) sub.role, sub.team);

            }
        }
        tM.setTeam(null);
        tM.getTeamRole().deleteTeamManager();
        team.deleteObserver(tM);

        LOG.info(String.format("%s - %s", teamRole.getUserName(), " remove team manager"+tM.getTeamRole().getUserName()+", from team: "+team.getName()));

    }

    /**
     * adi
     * remove the current coach and replace with a new one
     * @param coachToRemove - the coach to remove
     * @param coachToAdd - the coach to add
     * @param newCoachRoleAtTeam - the new coaches role
     * @param team - the team that will have the changes
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void removeAndReplaceCoach(Coach coachToRemove, TeamRole coachToAdd, String newCoachRoleAtTeam, Team team) throws Exception {
        super.removeAndReplaceCoach(coachToRemove,coachToAdd,newCoachRoleAtTeam,team);
    }

    /**
     * adi
     * edit coach's role
     * @param coach - the coach to edit
     * @param role - the new role
     */
    //TODO test-V
    @Override
    public void editCoachRole(Coach coach, String role) throws Exception {
        super.editCoachRole(coach,role);
    }

    /**
     * adi
     * add a player to a team
     * @param player - player to add
     * @param role - the players role
     * @param team - the team to add the player
     */
    //TODO test-V
    @Override
     public void addPlayer(TeamRole player, String role, Team team) throws Exception {
        super.addPlayer(player,role,team);
    }


    /**
     * adi
     * remove a player from a team (only if team has more than 11 players)
     * @param player - player to remove
     * @param team - the team to remove the player
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void removePlayer (Player player, Team team) throws Exception {
        super.removePlayer(player,team);
    }

    /**
     * adi
     * edit the players role
     * @param player - the player to edit
     * @param role - the new role
     */
    //TODO test-V
    @Override
    public void editPlayerRole(Player player, String role) throws Exception {
        super.editPlayerRole(player,role);
    }

    /**
     * adi
     * remove the current field and replace with new field
     * @param fieldtoRemove - the field to remove
     * @param fieldToAdd - the new field to add
     * @param team - the team to change its field
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void removeAndReplaceField (Field fieldtoRemove, Field fieldToAdd, Team team) throws Exception {
        super.removeAndReplaceField(fieldtoRemove,fieldToAdd,team);
    }

    /**
     * adi
     * edit the fields name
     * @param field - the field to edit
     * @param name - the new name
     */
    //TODO test-V
    @Override
    public void editFieldName(Field field, String name) throws Exception {
        super.editFieldName(field,name);
    }
    //</editor-fold>

    //<editor-fold desc="setters and getters">


    public void addNewTeam(Team team) {
        this.teams.add(team);
        LOG.info(String.format("%s - %s", teamRole.getUserName(), " new team was added: "+team.getName()));
    }


    public void removeTeam(Team team) throws Exception {
        if (teams.contains(team)){
            teams.remove(team);
        }
        else{
            LOG.error("team not belong to this owner");
            throw new Exception("team not belong to this owner");
        }
        LOG.info(String.format("%s - %s", teamRole.getUserName(), "remove team from active teams: "+team.getName()));
    }

    public LinkedList<Team> getTeams() {
        return teams;
    }

    public HashSet<TeamSubscription> getMySubscriptions() {
        return mySubscriptions;
    }

    @Override
    public String getUserNameOfAction() {
        return teamRole.getUserName();
    }


    public TeamRole getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(TeamRole teamRole) {
        this.teamRole = teamRole;
    }

    public void setTeams(LinkedList<Team> teams) {
        this.teams = teams;
    }

    public LinkedList<Team> getRequestedTeams() {
        return requestedTeams;
    }

    public void setRequestedTeams(LinkedList<Team> requestedTeams) {
        this.requestedTeams = requestedTeams;
    }

    public LinkedList<Team> getDeletedTeams() {
        return deletedTeams;
    }

    public void setDeletedTeams(LinkedList<Team> deletedTeams) {
        this.deletedTeams = deletedTeams;
    }

    public LinkedList<Team> getApprovedTeams() {
        return approvedTeams;
    }

    public void setApprovedTeams(LinkedList<Team> approvedTeams) {
        this.approvedTeams = approvedTeams;
    }

    //</editor-fold>

    /**OR
     * add income to certain team
     * @param team
     * @param typeOfIncome
     * @param amount
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void addIncomeToTeam(Team team,String typeOfIncome, long amount) throws Exception {
        super.addIncomeToTeam(team,typeOfIncome,amount);
    }

    /**or
     * add income to certain team
     * @param team
     * @param typeOfExpense
     * @param amount
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void addExpenseToTeam(Team team,String typeOfExpense, long amount) throws Exception {
        super.addExpenseToTeam(team,typeOfExpense,amount);
    }

    //<editor-fold desc="Notifications Handler" >
    /**
     * Notifications about:
     *      1. team can be open\not
     *      2.team removed forever
     *      3.team removed by team owner
     *      4.team reOpen
     *@codeBy OR and Eden
     * **/
    @Override
    public void update(Observable o, Object arg) {


        if(o instanceof Team){
            /**team request answer***/
            if(arg.equals(true)){// the team can be open
               requestedTeams.remove(o);
               approvedTeams.add((Team)o);
               //notifications.add(new Notification(o,"Team "+((Team)o).getName()+" can be open",false));
            }
            else if(arg.equals(false)){// the team cant be open
                ((Team)o).deleteObservers();
                requestedTeams.remove((Team)o);
                notifications.add(new Notification(o,"Sorry but team "+((Team)o).getName()+" cant be open",false));

            }
        }

        /**notification about close team forever*/
        if(o instanceof Team && arg instanceof  String && ((String)arg).contains("removed")){
            notifications.add(new Notification(o,arg,false));
        }
        /**team removed by team owner**/
        if(o instanceof Team){
            if( arg.equals("team deleted by team owner")){// the team was deleted by system manager and not active any more
                notifications.add(new Notification(o,arg,false));
            }
            /**team reOpen by team owner**/
            else if(arg.equals("team reopened by team owner")){
                notifications.add(new Notification(o,arg,false));

            }
        }
        else if(o instanceof Match){
            if(arg instanceof Event){
                teamRole.update(o, arg);
            }
        }
    }

    /**
     * mark notification as readen
     * @param not-unread notification to mark as read
     * @codeBy Eden
     */
    public void MarkAsReadNotification(Notification not){
        not.setRead(true);
    }

    @Override
    public String checkNotificationAlert() {
        if(gotTeamOwnerNotification && teamRole.gotFanNotification){
            gotTeamOwnerNotification =false;
            teamRole.gotFanNotification=false;
            return "gotTeamOwnerNotification&gotFanNotification";
        }
        else if(teamRole.gotFanNotification){
            teamRole.gotFanNotification=false;
            return "gotFanNotification";
        }
        else if(gotTeamOwnerNotification){
            gotTeamOwnerNotification =false;
            return "gotTeamOwnerNotification";
        }
        return "";
    }

    public HashSet<Notification> getNotificationsList() {
        return notifications;
    }

    /***
     * @return only the unread notifications . if not have return null - notify when user connect
     * @codeBy Eden
     */
    @Override
    public HashSet<Notification> getUnReadNotifications(){
        HashSet<Notification> unRead=new HashSet<>();
        for(Notification n: notifications){
            if(n.isRead()==false){
                unRead.add(n);
            }
        }
        return unRead;
    }
    //</editor-fold>

    /**yarden**/
    public boolean checkIfTeamInRequestTeam(String teamName){
        boolean ans=false;
        for (Team t: this.getRequestedTeams()) {
            if(t.getName().equals(teamName)){
                ans=true;
                break;
            }
        }
        return ans;
    }

    /**or**/
    public void removeMySubscription(TeamSubscription sub){
        this.mySubscriptions.remove(sub);
    }


    /**or**/
    public void addActiveTeam(Team t){
        this.teams.add(t);
    }

    /**or**/
    public void addDeletedTeam(Team t){
        this.deletedTeams.add(t);
        LOG.info(String.format("%s - %s", teamRole.getUserName(), "add deleted team to list : "+t.getName()));
    }

    /**or**/
    public void removeDeletedTeam(Team t){
        this.deletedTeams.remove(t);
        LOG.info(String.format("%s - %s", teamRole.getUserName(), "remove deleted team from list : "+t.getName()));
    }

    public void addRecordToTeamPage(Team t,String record) throws Exception {
        t.addRecordToPage(record);
    }

    /**or**/
    public void removeRecordFromPage(Team t,String record) throws Exception {
        t.removeRecordFromPage(record);
    }

    /**or**/
    public boolean createPrivatePage(Team t){
        return t.createPrivatePage();
    }

    /**or**/
    public boolean deletePrivatePage(Team t){
        return t.deletePrivatePage();
    }
}
