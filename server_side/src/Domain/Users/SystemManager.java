package Domain.Users;

import Domain.*;
import Domain.Events.Event;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;
import Domain.Notifications.Notification;
import Domain.Notifications.NotificationsUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class SystemManager extends Fan implements NotificationsUser {

    public static  HashSet<Complaint> complaints=new HashSet<>();//*
    private static final Logger LOG = LogManager.getLogger("SystemManager");
    private HashSet<Notification> notifications;

    private boolean gotSMNotification;

    /***
     * create system manager from fan:
     *      1.adding system manager to system.
     *      2.remove fan from system.
     * @param fan
     * @param ms
     */
    public SystemManager(Fan fan, MainSystem ms) {
        super(ms, fan.getName(), fan.getPhoneNumber(), fan.getEmail(), fan.getUserName(), fan.getPassword(), fan.getDateOfBirth());
//        this.complaints = new HashSet<>();
        notifications=new HashSet<>();
        /**remove fan from system*/
        ms.removeUser(fan);
        LOG.info(String.format("%s - %s", this.getUserName(), "Added to system"));
        gotSMNotification=false;

    }

    public SystemManager (MainSystem ms, String name, String phoneNumber, String email, String userName, String password, Date date) {
        super(ms,name,phoneNumber,email,userName,password,date);
//        this.complaints = new HashSet<>();
        notifications=new HashSet<>();
        LOG.info(String.format("%s - %s", this.getUserName(), "Added to system"));
        gotSMNotification=false;
    }


    //<editor-fold desc="getters and setters">

    public HashSet<Complaint> getComplaints() {
        return complaints;
    }



    /**
     * create new Domain.Users.SystemManager , connect to him all the complaints
     * @param fan
     * @codeBy Eden
     */
    public SystemManager addNewSystemManager(Fan fan) throws Exception {
        if(fan==null){
            LOG.error("user is null");
            throw new Exception("user is null");
        }
        /**constructor make connection to system**/
        SystemManager newSystemManager=new SystemManager(fan,system);
        /**delete Fan from system*/
        system.removeUser(fan);
        /**add the new system Manager to be observer of all complaints **/
        for(Complaint c:this.getComplaints()){
            c.addObserver(newSystemManager);
        }
        LOG.info(String.format("%s - %s", this.getUserName(), "add new System Manager , user name: "+newSystemManager.getUserName()));

        return  newSystemManager;

    }


    /**OR**/
    /*
    public boolean signInAsSystemManager(String name, String phoneNumber, String email, String userName, String password){
        // first check valid details
        if(checkValidDetails(userName,password,phoneNumber)){
            Domain.Users.SystemManager newSM= new Domain.Users.SystemManager(system,name,phoneNumber,email,userName,password);
            system.removeUser(this);
            system.addUser(newSM);
            return true;
        }
        return false;
    }
    */
    /**Eden*/
    public static void answerToComplaint (Complaint com, String ans){
        if(SystemManager.complaints.contains(com)){
            com.setAnswer(ans);
            com.send(ans);
        }
    }
    /**Eden*/
    public static void addComplaint(Complaint complaint) {
        complaints.add(complaint);
        List<SystemManager> sm= MainSystem.getInstance().getSystemManagers();
        for(SystemManager cur:sm) {
            complaint.addObserver(cur);
        }
    }

    /**
     * Show details about the system events.
     * @return FileReader to the Log file
     * @throws FileNotFoundException
     * @codeby Eden
     */
    public FileReader showSystemInfo() throws FileNotFoundException {
        File file =new File("LOG.text");
        return new FileReader(file);
    }


    /**
     * Switch between team owner which is founder to another team owner.
     * @param toDelete- the team owner to delete
     * @param toAdd- the team owner to subscribe as founder
     * @param team
     * @return the removed objects
     * @throws Exception
     * @codeBy Eden
     */
//TODO test - V avital
    public void replaceTeamOwnerFounder(TeamOwner toAdd, TeamOwner toDelete, Team team) throws Exception {
        if(toAdd==null||toDelete==null){
            throw  new Exception("null input");
        }
        /**toAdd is already team owner of team**/
        if(team.getTeamOwners().contains(toAdd)){
            throw  new Exception("fail!The team owner you want to add already exist");
        }
        if(team.getFounder()==toDelete){
            team.setFounder(toAdd);
            team.getTeamOwners().add(toAdd);
            toAdd.addNewTeam(team);
        }
        else{
            LOG.error("wrong team owner and team");
            throw new Exception("wrong team owner and team"); //actual
        }

        LOG.info(String.format("%s - %s", this.getUserName(), "Replace %s founder from:%s to:%s",team.getName(),toDelete.getTeamRole().getUserName(),toAdd.getTeamRole().getUserName()));

    }


    //<editor-fold desc="Remove Domain.Users.User">
    /**
     * remove user if it a valid system operation.
     * @param userToDelete
     * @return list of removed object
     * @throws Exception if the remove isnt valid
     * @codeBy Eden
     */
    //TODO test - aviral V - prob with fanRemove!
    public List<Object> removeUser(Fan userToDelete) throws Exception {
        List<Object> objectsDeleted=new LinkedList<>();
        boolean isFan=true;


        if(userToDelete instanceof Rfa) {
            /***must have at least one RFA at system**/
            if(system.numOfRfa()==1){
                LOG.error("There is only one RFA left, you cannot delete it");
                throw new Exception("There is only one RFA left, you cannot delete it ");
            }
            objectsDeleted=deleteRfa(((Rfa)userToDelete));
            isFan=false;
        }
        if(userToDelete instanceof SystemManager) {
            if(userToDelete==this){
                throw  new Exception("You Cannot Delete Yourself!!!");
            }
            /***must have at least one system Manager at system**/
            if(system.getSystemManagers().size()==1){
                LOG.error("There is only one System Manager left, you cannot delete it");
                throw new Exception("There is only one System Manager left, you cannot delete it ");// not throw
            }
            objectsDeleted=deleteSystemManager(((SystemManager)userToDelete));
            isFan=false;
        }
        if(userToDelete instanceof Referee) {
            objectsDeleted=deleteReferee(((Referee)userToDelete));
            isFan=false;

        }


        if(userToDelete instanceof TeamRole){
            objectsDeleted= teamRoleRemove(((TeamRole)userToDelete));
            isFan=false;

        }

        if(userToDelete instanceof Fan&& isFan){ // only fun
            objectsDeleted=fanRemove(((Fan)userToDelete));// prob with dell fan
        }
        LOG.info(String.format("%s - %s", this.getUserName(), "remove %s from system",userToDelete.getUserName()));
        if(userToDelete!=null){
            system.getUserNames().remove(userToDelete.getUserName());
        }
        return objectsDeleted;
    }


    /**
     * remove fan from system , delete his all complaints-not relevant anyMore.
     * @param userToDelete
     * @return
     * @codeBy Eden
     */
    //TODO test - avital - need to check- prob with complain
    private List<Object> fanRemove(Fan userToDelete) {
        List<Object> res=new LinkedList<>();
        res.add(userToDelete);
        /**remove fan complaints from system manager**/
        for(Complaint c:userToDelete.getMyComplaints()){
            SystemManager.complaints.remove(c);
            res.add(complaints);
        }
        /**remove fan from private page subscriptions**/
        for(PrivatePage p: userToDelete.getMyPages()){
            p.getFans().remove(userToDelete);
        }
        /***remove user name from system*/
        system.getUserNames().remove(userToDelete.getUserName());
        /**remove user from system**/
        system.removeUser(userToDelete);
        return res;
    }



    /**
     * delete Domain.LeagueManagment.Team Manager and delete all his subscribe .
     * @param userToDelete
     * @codeBy Eden
     */
    //TODO test- avital V
    private List<Object> deleteTeamManager(TeamManager userToDelete) throws Exception {
        List<Object> res=new LinkedList<>();
        res.add(userToDelete);
        if(userToDelete.getTeam()!=null) {
            userToDelete.getTeam().setTeamManager(null);
        }
        HashSet<TeamSubscription> allSub= userToDelete.getMySubscriptions();

        LinkedList<TeamSubscription> allSubsList=new LinkedList<>();
        for(TeamSubscription sub: allSub){
            allSubsList.add(sub);
        }
        for(int i=0; i<allSubsList.size(); i++){
            TeamSubscription sub=allSubsList.get(i);
            res.add(sub.role);
            if(sub.role instanceof TeamOwner){
                userToDelete.removeTeamOwner(((TeamOwner)sub.role),sub.team);
            }
        }
        userToDelete.setTeam(null);

        /**delete all the subscriptions of userToDelete from other Owners and Managers*/
        LinkedList<TeamOwner> teamOwners=system.getAllTeamOwners();
        for(TeamOwner to:teamOwners){
            LinkedList<TeamSubscription> subscriptionsList=new LinkedList<>();
            for(TeamSubscription sub: to.getMySubscriptions()){
                subscriptionsList.add(sub);
            }
            for(int i=0; i< subscriptionsList.size();i++) {
                if(subscriptionsList.get(i).role==userToDelete){
                    to.getMySubscriptions().remove(subscriptionsList.get(i));
                }
            }
        }
        LinkedList<TeamManager> teamMa=system.getAllTeamManagers();
        for(TeamManager tm:teamMa){
            LinkedList<TeamSubscription> subscriptionsList=new LinkedList<>();
            for(TeamSubscription sub: tm.getMySubscriptions()){
                subscriptionsList.add(sub);
            }
            for(int i=0; i< subscriptionsList.size();i++) {
                if(subscriptionsList.get(i).role==userToDelete){
                    tm.getMySubscriptions().remove(subscriptionsList.get(i));
                }
            }
        }
        return res;
    }




    /****
     * Domain.Users.TeamRole handling:
     *  1.check if remove is valid ->(coach\Domain.Users.TeamOwner\Domain.Users.TeamManager\Domain.Users.Player)
     *  2.if coach!=null -> handlingCoachRemove
     *  3.if Domain.Users.TeamOwner!=null->handlingTeamOwnerRemove
     *  4.if Domain.Users.TeamManager!=null->handlingTeamManagerRemove
     *  5.if player!=null->handlingPlayerRemove
     *  6.disconnect user from system.
     *
     * @return lost of removed objects.
     * @exception if the Deletion isn't valid
     * @codeBy Eden
     */
    //TODO test -avital V
    private List<Object> teamRoleRemove(TeamRole userToRemove) throws Exception {
        isValidRemove(userToRemove);
        /*******if not valid - exception thrown*******/
        List<Object> removedObjects=new LinkedList<>();
        /**if coach!=null -> handlingCoachRemove*/
        if(userToRemove.getCoach()!=null)  {
            removedObjects.addAll(deleteCoach(userToRemove.getCoach()));
        }
        /**if player!=null->handlingPlayerRemove*/
        if(userToRemove.getPlayer()!=null)  {
            removedObjects.addAll(deletePlayer(userToRemove.getPlayer()));
        }
        /**if Domain.Users.TeamManager!=null->handlingTeamManagerRemove*/
        if(userToRemove.getTeamManager()!=null)  {
            removedObjects.addAll(deleteTeamManager(userToRemove.getTeamManager()));
        }
        /**if Domain.Users.TeamOwner!=null->handlingTeamOwnerRemove*/
        if(userToRemove.getTeamOwner()!=null)  {
            removedObjects.addAll(deleteTeamOwner(userToRemove.getTeamOwner()));
        }

        /**disconnect user from system*/
        system.removeUser(userToRemove);

        return removedObjects;
    }

    /**
     * check if Domain.Users.SystemManager can remove the Domain.Users.TeamRole:
     *      1.if is coach and he connect to any team, throws Exception.
     *      2.if is player belong to any Domain.LeagueManagment.Team which hasn't more than 11 player throws Exception.
     *      3.if is Domain.Users.TeamManager->always valid
     *      4.if is Domain.Users.TeamOwner and he is a founder of any Domain.LeagueManagment.Team from Domain.LeagueManagment.Team list which he hold->throws Exception
     * @param userToRemove
     * @throws Exception
     */
    //TODO test - avital V
    private void isValidRemove(TeamRole userToRemove) throws Exception{
        /**if is coach-> check if coach is not connected to any team ->
         if connect throws Exception("you have to replace team coach")*/
        if(userToRemove.getCoach()!=null) {
            if(userToRemove.getCoach().getCoachTeam()!=null){
                LOG.error("cannot have team with no coach");
                throw new Exception("you have to subscribe another coach to "+(userToRemove.getCoach()).getCoachTeam().getName()+" team first");
            }
        }
        /**if is player-> check if the player belong to any Team , if so , check if team has more than 11 player ->
         if not throws Exception ("you have to add more players to team before delete ")*/
        if(userToRemove.getPlayer()!=null) {
            if (userToRemove.getPlayer().getTeam() != null) {
                if (userToRemove.getPlayer().getTeam().getPlayers().size() <= 11) {
                    LOG.error("cannot have team with less than 11 players");
                    throw new Exception("You Cannot Delete player From " + userToRemove.getPlayer().getTeam().getName() + " team ,any team have to be at least 11 Players!");
                }
            }
        }

        /**if is teamOwner-> if he is a founder of any team from  team list which he hold->
         *throws Exception("you have to replace team founder")*/
        if(userToRemove.getTeamOwner()!=null){
            LinkedList<Team> OwnerTeams =userToRemove.getTeamOwner().getTeams();
            for(Team t: OwnerTeams){
                if(t.getFounder()==userToRemove.getTeamOwner()){
                    LOG.error("cannot delete founder of team");
                    throw new Exception(userToRemove.getName()+" is founder of:"+ " "+t.getName() +"please replace the team's fonder");
                }
            }
        }


    }

    /**
     * delete Domain.LeagueManagment.Team Owner only if he isn't a Founder of any team and delete all his subscribe .
     * @param userToDelete
     * @throws Exception if team owner is founder of any team.
     * @codeBy Eden
     */
    //TODO test - avital V
    private List<Object> deleteTeamOwner(TeamOwner userToDelete) throws Exception {
        List<Object> res=new LinkedList<>();
        res.add(userToDelete);

        /**delete owner from deleted teams**/
        LinkedList<Team>teams= userToDelete.getDeletedTeams();
        for(Team t : teams){
            t.getTeamOwners().remove(userToDelete);
        }
        /***remove all the subscription**/
        HashSet<TeamSubscription> subscriptions= userToDelete.getMySubscriptions();//check if not empty?
        LinkedList<TeamSubscription> subscriptionsList=new LinkedList<>();
        for(TeamSubscription sub: subscriptions){
            subscriptionsList.add(sub);
        }
        for(int i=0; i< subscriptionsList.size();i++){
            TeamSubscription sub=subscriptionsList.get(i);
            if(sub.role instanceof TeamOwner){
                userToDelete.removeTeamOwner(((TeamOwner) sub.role), sub.team);
            }

            if(sub.role instanceof TeamManager){
                userToDelete.removeTeamManager(((TeamManager)sub.role),sub.team);
            }
        }
//        for(TeamSubscription sub: subscriptions){
//
//        }

        /**remove team request from RFA because the requests are not relevant**/
        for(Team t:userToDelete.getRequestedTeams()){
            Rfa.teamRequests.remove(t);
            res.add(t);
        }

        /**delete tewmOwner from teams **/
        LinkedList<Team>teams2= userToDelete.getTeams();
        for(Team t : teams2){
            t.getTeamOwners().remove(userToDelete);
        }
        for(Team t : teams2){
            userToDelete.getTeams().remove(t);
        }

        /**delete all the subscriptions of userToDelete from other Owners and Managers*/
        LinkedList<TeamOwner> teamOwners=system.getAllTeamOwners();
        for(TeamOwner to:teamOwners){
            LinkedList<TeamSubscription> subscriptionsList2=new LinkedList<>();
            for(TeamSubscription sub: to.getMySubscriptions()){
                subscriptionsList2.add(sub);
            }
            for(int i=0; i< subscriptionsList2.size();i++) {
                if(subscriptionsList2.get(i).role==userToDelete){
                    to.getMySubscriptions().remove(subscriptionsList2.get(i));
                }
            }
        }

        LinkedList<TeamManager> teamMa=system.getAllTeamManagers();
        for(TeamManager tm:teamMa){
            subscriptionsList=new LinkedList<>();
            for(TeamSubscription sub: tm.getMySubscriptions()){
                subscriptionsList.add(sub);
            }
            for(int i=0; i< subscriptionsList.size();i++) {
                if(subscriptionsList.get(i).role==userToDelete){
                    tm.getMySubscriptions().remove(subscriptionsList.get(i));
                }
            }
        }
        return res;
    }

    /**
     * delete Domain.Users.Player only if the team has at least 11 other players
     * @param userToDelete
     * @throws Exception
     * @codeBy Eden
     */
    //TODO test -avital V
    private List<Object> deletePlayer(Player userToDelete) throws Exception {
        List<Object> res=new LinkedList<>();
        if(userToDelete.getTeam()!=null) {
            userToDelete.getTeam().getPlayers().remove(userToDelete);
        }
        res.add(userToDelete);
        return res;
    }

    /**
     * delete Domain.Users.Coach .
     * @param userToDelete
     * @codeBy Eden
     */
    //TODO test-avital V
    private List<Object> deleteCoach(Coach userToDelete) {
        List<Object> re=new LinkedList<>();
        re.add(userToDelete);
        return  re;
    }

    /**
     * delete Domain.Users.Referee only if he hasn't any future match .
     * @param userToDelete
     * @return list with the referee object.
     * @throws Exception if referee has future matches.
     * @codeBy Eden
     */
//TODO test-avital V
    private List<Object> deleteReferee(Referee userToDelete) throws Exception {
        //check the all matches that the referee is refereeing
        for (Match m : userToDelete.getMatches()) {
            if (m.getStartDate().after(new Date(System.currentTimeMillis()))) {
                LOG.error("You cannot remove referee , he has matches in this season");
                throw new Exception("You cannot remove referee , he has matches in this season");
            }
        }
        system.removeUser(userToDelete);
        List<Object> res=new LinkedList<>();
        res.add(userToDelete);
        return res;
    }

    /**
     * delete System Manager.
     * @param userToDelete
     * @codeBy Eden
     */
    //TODO test -avital V
    private List<Object> deleteSystemManager(SystemManager userToDelete) {
        system.removeUser(userToDelete);
        List<Object> res=new LinkedList<>();
        res.add(userToDelete);
        return res;
    }
    /**
     * delete RFA , disconnect from budget control and from system
     * @param userToDelete
     * @codeBy Eden
     */
    //TODO test-avital V
    private List<Object> deleteRfa(Rfa userToDelete){
        //userToDelete.getBudgetControl().removeRfa(userToDelete);
        system.removeUser(userToDelete);
        List<Object> res=new LinkedList<>();
        res.add(userToDelete);
        return res;
    }
    //</editor-fold>

    //<editor-fold desc="Domain.LeagueManagment.Team Remove">
    /**
     * remove team from system ,
     * if team does not have future Matches(not belong to current season).
     * doesnt delete all team matches.
     * disconnect the team owners and managers.
     * delete the team's subscriptions from their subscriptions list.
     * disconnect the coach
     * disconnect the players
     * @param teamToRemove
     * @codeBy Eden
     */
    //TODO test -V
    public void removeTeamFromSystem(Team teamToRemove) throws Exception {

        checkValidTeam(teamToRemove);
        if(teamToRemove.isActive()) {
            /**delete team from owner*/
            HashSet<TeamOwner> teamOwners = teamToRemove.getTeamOwners();
            for (TeamOwner curTO : teamOwners) {
                teamToRemove.addObserver(curTO);
                curTO.getTeams().remove(teamToRemove);
                /** delete the team's subscriptions from team owner subscriptions list**/
                HashSet<TeamSubscription> toRemove = new HashSet<>();
                for (TeamSubscription ts : curTO.getMySubscriptions()) {
                    if (ts.team == teamToRemove) {
                        toRemove.add(ts);
                    }
                }
                curTO.getMySubscriptions().removeAll(toRemove);
            }

            /**remove team manager from team*/
            TeamManager teamManager = teamToRemove.getTeamManager();
            if (teamManager != null) {
                teamToRemove.addObserver(teamManager);
                teamManager.setTeam(null);
                teamManager.getTeamRole().deleteTeamManager();
            }

            /**remove coach*/
            teamToRemove.getCoach().setCoachTeam(null);
            /**remove players*/
            HashSet<Player> players = teamToRemove.getPlayers();
            for (Player p : players) {
                p.setPlayerTeam(null);
            }

            /***remove team name from system*/
            system.getTeamNames().remove(teamToRemove.getName());

            /**remove from system*/
            system.getActiveTeams().remove(teamToRemove);
            teamToRemove.sendNotiAbouteClose();
        }
        /**if not active team**/
        else{
            /**remove team from - team owners deleted list****/
            for(TeamOwner cur: teamToRemove.getTeamOwners()){//!!
                cur.getDeletedTeams().remove(teamToRemove);
                teamToRemove.addObserver(cur);
            }
            teamToRemove.addObserver(teamToRemove.getTeamManager());
            teamToRemove.sendNotiAbouteClose();
        }
        LOG.info(String.format("%s - %s", this.getUserName(), "remove team :%s from system",teamToRemove.getName()));


    }

    /***
     * check if team deletion is valid
     * @param teamToRemove
     * @throws Exception
     * @codeBy Eden
     */
    //TODO test - avital V
    private void checkValidTeam(Team teamToRemove) throws Exception {
        if(teamToRemove.getLeaguePerSeason().containsKey(system.getCurrSeason())){
            LOG.error("cannot delete team in current season");
            throw new Exception("cannot delete team in current season");
        }
        Date currDate= new Date(System.currentTimeMillis());
        for (Match m:teamToRemove.getHome()) {
            if(m.getStartDate().after(currDate)){
                throw  new Exception("cannot delete team with future matches");
            }
        }

    }


    //</editor-fold>

    //<editor-fold desc="Notifications Handler">
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
    public HashSet<Notification> getNotificationsList() {
        return notifications;
    }

    /***
     * @return only the unread notifications . if not have return null
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

    @Override
    public String checkNotificationAlert() {
        if(gotSMNotification && gotFanNotification){
            gotSMNotification =false;
            gotFanNotification=false;
            return "gotSMNotification&gotFanNotification";
        }
        else if(gotFanNotification){
            gotFanNotification=false;
            return "gotFanNotification";
        }
        else if(gotSMNotification){
            gotSMNotification =false;
            return "gotSMNotification";
        }
        return "";
    }


    /**
     * Get notifications about:
     *  1.Get new complaint.
     *  2.remove team by team owner
     *  3.reopen team by team owner
     *
     * @param o
     * @param arg
     * @codeBy Or and Eden
     */
    @Override
    public void update(Observable o, Object arg) {


        /**get new complaint*/
        if(o instanceof  Complaint){
            if(o instanceof Complaint){
                /**if it's not an answer notify*/
                if(((Complaint)o).getAnswer()==null){
                    notifications.add(new Notification(o,arg,false));
                }
                /**if its an answer notify - remove from notifications(because system manager already handled )*/
                else{
                    for(Notification noti : notifications){
                        if(noti.getSender()==o){
                            noti.setRead(true);
                        }

                    }
                }
            }
        }

        /**team reOpen by team owner**/
        else if (arg.equals("team reopened by team owner")) {
            notifications.add(new Notification(o, arg, false));

        }
        /***team deleted by team owner*/
        if (o instanceof Team) {
            if (arg.equals("team deleted by team owner")) {// the team was deleted by system manager and not active any more
                notifications.add(new Notification(o, arg, false));
            }
        }
        else if(o instanceof Match){
            if(arg instanceof Event){
                super.update(o, arg);
            }
        }
    }
// added!!!!!!!1
    /**
     * replace Coach At Team in order to delete coach user(cannot delete coach with team)
     * @param coachToReplace
     * @param t
     * @return
     */
    public boolean replaceCoachAtTeam(Coach coachToReplace, Team t) {
       if(coachToReplace!=null&&t!=null&&t.getCoach()!=null&&coachToReplace.getCoachTeam()==null){
           t.getCoach().setCoachTeam(null);
           t.setCoach(coachToReplace);
           coachToReplace.setCoachTeam(t);
           return true;
       }
       return false;
    }
    // added!!!!!!!1
    public boolean addPlayerToTeam(Player p, Team t) throws Exception {
        if(p.getTeam()==null&&p!=null&&t!=null){
            t.addPlayer(p);
            p.setPlayerTeam(t);
            return true;
        }
        return false;
    }
    //</editor-fold>
}

