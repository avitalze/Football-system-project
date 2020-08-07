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

public class TeamManager extends ManagmentActions implements NotificationsUser {
    private TeamRole teamRole;
    private Team team;
    //private HashSet<TeamSubscription> mySubscriptions;
    private static final Logger LOG = LogManager.getLogger();
    private HashSet<TeamManagerPermissions> teamManagerPermissions;
    private HashSet<Notification> notificationsList;
    private boolean gotTeamManagerNotification;

    public TeamManager(TeamRole teamRole,Team team, HashSet<TeamManagerPermissions> pers) {
        this.team = team;
        this.team.setTeamManager(this);
        mySubscriptions = new HashSet<>();
        teamManagerPermissions = new HashSet<>();
        this.teamManagerPermissions.addAll(pers);
        this.teamRole= teamRole;
        notificationsList=new HashSet<>();
        gotTeamManagerNotification=false;
    }


    public TeamManager(TeamRole teamRole) {
        this.team = team;
        mySubscriptions = new HashSet<>();
        teamManagerPermissions = new HashSet<>();
        this.teamRole= teamRole;
        notificationsList=new HashSet<>();
        gotTeamManagerNotification=false;
    }
    //<editor-fold desc="getters and setters">

    public HashSet<TeamSubscription> getMySubscriptions() {
        return mySubscriptions;
    }

    @Override
    public String getUserNameOfAction() {
        return teamRole.getUserName();
    }

    public void setMySubscriptions(HashSet<TeamSubscription> mySubscriptions) {
        this.mySubscriptions = mySubscriptions;
    }

    public Team getTeam() { return team; }

    public void setTeam(Team team) { this.team = team; }

    public TeamRole getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(TeamRole teamRole) {
        this.teamRole = teamRole;
    }

    public HashSet<TeamManagerPermissions> getTeamManagerPermissions() {
        return teamManagerPermissions;
    }

    public void setTeamManagerPermissions(HashSet<TeamManagerPermissions> teamManagerPermissions) {
        this.teamManagerPermissions = teamManagerPermissions;
    }

    //</editor-fold>

    //<editor-fold desc="add remove and edit">
    /**
     * adi
     * this team manager subscribes a fan to become a team owner of a specific team
     * @param fan - the person you want to make a team owner
     * @param team - the team you want to add a team owner
     * @return the new team owner
     * @throws Exception
     */
    //TODO test-V
    @Override
    public TeamRole subscribeTeamOwner(Fan fan, Team team) throws Exception{
        if(this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditTeamOwner)){
            return super.subscribeTeamOwner(fan,team);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
    }
    /**
     * adi
     * this team manager removes a team owner that he subscribed in the past
     * @param tO - team owner to remove
     * @param team - the team that the team owner will be removed from
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void removeTeamOwner (TeamOwner tO, Team team)throws Exception{
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditTeamOwner)) {
            super.removeTeamOwner(tO,team);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
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
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditCoach)) {
            super.removeAndReplaceCoach(coachToRemove, coachToAdd, newCoachRoleAtTeam, team);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
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
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditCoach)) {
            super.editCoachRole(coach,role);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
    }
    /**
     * adi
     * add a player to a team
     * @param player - player to add
     * @param role - the players role
     * @param team - the team to add the player
     */
    //TODO test
    @Override
    public void addPlayer(TeamRole player, String role, Team team) throws Exception{
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditPlayer)) {
            super.addPlayer(player,role,team);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
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
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditPlayer)) {
            super.removePlayer(player,team);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
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
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditPlayer)) {
            super.editPlayerRole(player,role);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
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
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditField)) {
            super.removeAndReplaceField(fieldtoRemove,fieldToAdd,team);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
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
        if (this.teamManagerPermissions.contains(TeamManagerPermissions.addRemoveEditField)) {
            super.editFieldName(field,name);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
    }


    //</editor-fold>

    /**or**/
    //TODO test-V
    @Override
    public void addIncomeToTeam(Team team,String typeOfIncome, long amount) throws Exception {
        if(this.teamManagerPermissions.contains(TeamManagerPermissions.addToBudgetControl)){
            super.addIncomeToTeam(team,typeOfIncome,amount);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }

    }

    /**Or
     *
     * @param team
     * @param typeOfExpense
     * @param amount- positive!!! in budget conrtol in becomes negative
     * @throws Exception
     */
    //TODO test-V
    @Override
    public void addExpenseToTeam(Team team,String typeOfExpense, long amount) throws Exception {
        if(this.teamManagerPermissions.contains(TeamManagerPermissions.addToBudgetControl)){
            super.addExpenseToTeam(team,typeOfExpense,amount);
        }
        else{
            LOG.error("This user doesn't have the permission to do this action");
            throw new Exception("This user doesn't have the permission to do this action");
        }
    }

    //<editor-fold desc="Notifications Handler">
    /**
     * get notifications about:
     *      1.close team forever.
     *      2. remove team by team owner.
     *      3.reopen team by team owner.
     * @param o
     * @param arg
     * @codeBy Eden
     */
    @Override
    public void update(Observable o, Object arg) {
        //call fan team role



        /**notification about close team forever*/
        if (o instanceof Team && arg instanceof String && ((String) arg).contains("removed")) {
            notificationsList.add(new Notification(o, arg, false));
        }
        /***team deleted by team owner*/
        if (o instanceof Team) {
            if (arg.equals("team deleted by team owner")) {// the team was deleted by system manager and not active any more
                notificationsList.add(new Notification(o, arg, false));
            }
            /**team reOpen by team owner**/
            else if (arg.equals("team reopened by team owner")) {
                notificationsList.add(new Notification(o, arg, false));

            }
        }
         if(o instanceof Match){
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
    @Override
    public void MarkAsReadNotification(Notification not){
        not.setRead(true);
    }

    @Override
    public String checkNotificationAlert() {
        if(gotTeamManagerNotification && teamRole.gotFanNotification){
            gotTeamManagerNotification =false;
            teamRole.gotFanNotification=false;
            return "gotTeamManagerNotification&gotFanNotification";
        }
        else if(teamRole.gotFanNotification){
            teamRole.gotFanNotification=false;
            return "gotFanNotification";
        }
        else if(gotTeamManagerNotification){
            gotTeamManagerNotification =false;
            return "gotTeamOwnerNotification";
        }
        return "";
    }

    @Override
    public HashSet<Notification> getNotificationsList() {
        return notificationsList;
    }

    /***
     * @return only the unread notifications . if not have return null
     * @codeBy Eden
     */
    @Override
    public HashSet<Notification> getUnReadNotifications(){
        HashSet<Notification> unRead=new HashSet<>();
        for(Notification n: notificationsList){
            if(n.isRead()==false){
                unRead.add(n);
            }
        }
        return unRead;
    }
    //</editor-fold>

}
