package Domain.Controllers;

import DataAccess.*;
import Domain.*;
import Domain.Enums.TeamManagerPermissions;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.Notifications.Notification;
import Domain.Users.*;
import javafx.scene.control.Alert;
import sun.awt.image.ImageWatched;

import java.security.acl.Permission;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class TeamManagementController {
    DaoTeamRequests daoTeamRequests=new DaoTeamRequests();
    DaoApprovedTeamReq daoApprovedTeamReq=new DaoApprovedTeamReq();
    DaoTeams daoTeams =new DaoTeams();
    DaoPlayer daoPlayer=new DaoPlayer();
    DaoCoaches daoCoaches=new DaoCoaches();
    DaoTeamOwnersTeams daoTeamOwnersTeams=new DaoTeamOwnersTeams();
    DaoTeamRole daoTeamRole=new DaoTeamRole();
    DaoFields daoFields =new DaoFields();

    private SystemOperationsController sOController = new SystemOperationsController();

    /**
     * adi
     * @param userName
     * @param name
     * @throws Exception
     */
    public String requestNewTeam(String userName, String name){
        try {
            TeamRole user = (TeamRole) sOController.getUserByUserName(userName);
            user.getTeamOwner().requestNewTeam(name);
            LinkedList<String> record =new LinkedList<>();
            record.add(userName);
            record.add(name);
            daoTeamRequests.save(record);
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "";
    }

    /**
     * adi
     * @param userName
     * @param teamName
     * @param playerNamesStr
     * @param coachUserName
     * @param nameOfNewField
     * @throws Exception
     */
    public String makeTeamActive(String userName, String teamName, String playerNamesStr , String coachUserName, String nameOfNewField) {
        try {
            TeamRole user = (TeamRole) sOController.getUserByUserName(userName);
            Team team = new Team();
            for (Team t : user.getTeamOwner().getApprovedTeams()) {
                if (t.getName().equals(teamName)) {
                    team = t;
                    break;
                }
            }
            if(MainSystem.getInstance().checkIfFieldExists(nameOfNewField)){
                return "Field name already exists";
            }
            Field field = new Field(nameOfNewField);
            MainSystem.getInstance().addField(field);

            /**add new field to fields table:**/
            LinkedList<String> fieldRec=new LinkedList<>();
            fieldRec.add(nameOfNewField);
            daoFields.save(fieldRec);
            LinkedList<String> teamRecord=new LinkedList<>();
            teamRecord.add(teamName);
            teamRecord.add(null);
            teamRecord.add(userName);
            teamRecord.add(coachUserName);
            teamRecord.add(nameOfNewField);
            teamRecord.add("0");
            LinkedList<String> toRemove = new LinkedList<>();
            toRemove.add(userName);
            toRemove.add(teamName);
            daoApprovedTeamReq.delete(toRemove);
            daoTeams.save(teamRecord);

            List<String> playerNames = Arrays.asList(playerNamesStr.split(";"));
            HashSet<TeamRole> players = new HashSet<>();
            for (String pName : playerNames) {
                TeamRole p = (TeamRole) sOController.getUserByUserName(pName);
                players.add(p);
                if (p.isPlayer()) {
                    /**player- add team to db**/
                    LinkedList<String> records = new LinkedList<>();
                    LinkedList<String> name = new LinkedList<>();
                    name.add(pName);
                    records.add(teamName);
                    records.add(null);
                    daoPlayer.update(name, records);
                }
                else{
                    LinkedList<String> records = new LinkedList<>();
                    records.add(pName);
                    records.add(teamName);
                    records.add(null);
                    daoPlayer.save(records);

                    //update to teamRole :
                    List <String> teamRoleKey=new LinkedList<>();
                    teamRoleKey.add(p.getUserName());


                    List<String> teamRoleRec=new LinkedList<>();
                    teamRoleRec.add("1");
                    if(p.isCoach()){
                        teamRoleRec.add("1");
                    }
                    else{
                        teamRoleRec.add("0");
                    }

                    if(p.isTeamOwner()){
                        teamRoleRec.add("1");
                    }
                    else{
                        teamRoleRec.add("0");
                    }
                    if(p.isTeamManager()){
                        teamRoleRec.add("1");
                    }
                    else{
                        teamRoleRec.add("0");
                    }
                    daoTeamRole.update(teamRoleKey,teamRoleRec);
                }
            }
            TeamRole coach = (TeamRole) sOController.getUserByUserName(coachUserName);
            /**coach- add team to db**/
            List<String> keys=new LinkedList<>();
            keys.add(coachUserName);
            LinkedList<String> records =new LinkedList<>();
            records.add(teamName);
            records.add(null);
            LinkedList<String> name=new LinkedList<>();
            //coach not exist
            try {
                name.add(coachUserName);
                daoCoaches.get(keys);
                daoCoaches.update(name,records);

            }
            catch(ParseException e){
                List <String> teamRoleKey=new LinkedList<>();
                teamRoleKey.add(coachUserName);

                //update in team roles:
                List<String> teamRoleRec=new LinkedList<>();
                if(coach.isPlayer()){
                    teamRoleRec.add("1");
                }
                else{
                    teamRoleRec.add("0");
                }
                teamRoleRec.add("1");
                if(coach.isTeamOwner()){
                    teamRoleRec.add("1");
                }
                else{
                    teamRoleRec.add("0");
                }
                if(coach.isTeamManager()){
                    teamRoleRec.add("1");
                }
                else{
                    teamRoleRec.add("0");
                }
                name.add(coachUserName);
                daoTeamRole.update(teamRoleKey,teamRoleRec);

                //save new record in coach:
                List <String> coachRec=new LinkedList<>();
                coachRec.add(coachUserName);
                coachRec.add(teamName);
                coachRec.add(null);
                daoCoaches.save(coachRec);
            }


            user.getTeamOwner().makeTeamActive(team, players, coach, field);

            /**teamOwner*/
            LinkedList<String> key=new LinkedList<>();
            key.add(userName);
            key.add(teamName);
            daoTeamOwnersTeams.save(key);

//            /**delete team*/
//            List<String> team_key=new LinkedList<>();
//            team_key.add(teamName);
//            daoTeams.delete(team_key);


        }

        catch (Exception e){
            /********Role Back******/
            List<String > approvedTeam=new LinkedList<>();
            approvedTeam.add(userName);
            approvedTeam.add(teamName);
            try {
                daoApprovedTeamReq.save(approvedTeam);
            } catch (SQLException e1) {
                return e.getMessage();
            }
            //teamOwnerTeam
            List<String > teamOwnerTeam=new LinkedList<>();
            teamOwnerTeam.add(userName);
            teamOwnerTeam.add(teamName);
            daoTeamOwnersTeams.delete(teamOwnerTeam);

            //players
            List<String> playerNames = Arrays.asList(playerNamesStr.split(";"));
            HashSet<TeamRole> players = new HashSet<>();
            for (String pName : playerNames) {
                TeamRole p = (TeamRole) sOController.getUserByUserName(pName);
                players.add(p);
                /**player- add team to db**/
                LinkedList<String> name=new LinkedList<>();
                LinkedList<String> records =new LinkedList<>();
                name.add(pName);
                records.add(null);
                records.add(null);
                daoPlayer.update(name,records);
            }

            //coach
            LinkedList<String> records =new LinkedList<>();
            records.add(null);
            records.add(null);
            LinkedList<String> name=new LinkedList<>();
            name.add(coachUserName);
            daoCoaches.update(name,records);

            List<String > team_key=new LinkedList<>();
            team_key.add(teamName);
            daoTeams.delete(team_key);

            return e.getMessage();
        }
        return "ok";
    }

    public String getMyApprovedTeams(String userName){
        TeamRole user = (TeamRole) sOController.getUserByUserName(userName);
        if (user.isTeamOwner()) {
            if (user.getTeamOwner().getApprovedTeams() == null) {
                return null;
            }
            LinkedList<Team> teams = user.getTeamOwner().getApprovedTeams();
            if (teams != null && teams.size() != 0) {
                //LinkedList<String> teamNames = new LinkedList<>();
                String teamNames = new String();
                for (Team t : teams) {
                    //teamNames.add(t.getName());
                    teamNames += t.getName() + ";";
                }
                return teamNames;
            }
        }
        return null;
    }
    /**
     * adi
     * @param user
     * @param team
     * @throws Exception
     */
    public void deleteTeam(TeamOwner user, Team team) throws Exception {
        user.deleteTeam(team);
    }

    /**
     * adi
     * @param user
     * @param team
     * @param players
     * @param coach
     * @param nameOfNewField
     * @throws Exception
     */
    public void reopenTeam(TeamOwner user, Team team, HashSet<Player> players, Coach coach, String nameOfNewField) throws Exception {
        Field field = new Field(nameOfNewField);
        user.reopenTeam(team, players, coach, field);
    }
    /**
     * adi
     * @param user
     * @param fanToSubscribe
     * @param team
     * @return
     * @throws Exception
     */
    public TeamRole subscribeTeamOwner(TeamRole user, Fan fanToSubscribe, Team team) throws Exception {
        if (user.isTeamOwner()){
            return user.getTeamOwner().subscribeTeamOwner(fanToSubscribe, team);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            return user.getTeamManager().subscribeTeamOwner(fanToSubscribe, team);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }
    /**
     * adi
     * @param user
     * @param tOToRemove
     * @param team
     * @throws Exception
     */
    public void removeTeamOwner (TeamRole user, TeamOwner tOToRemove, Team team) throws Exception{
        if (user.isTeamOwner()){
            user.getTeamOwner().removeTeamOwner(tOToRemove, team);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().removeTeamOwner(tOToRemove, team);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param fanToSubscribe
     * @param team
     * @param per
     * @return
     * @throws Exception
     */
    public TeamRole subscribeTeamManager(TeamOwner user, Fan fanToSubscribe, Team team, HashSet<TeamManagerPermissions> per) throws Exception {
        return user.subscribeTeamManager(fanToSubscribe, team, per);
    }

    /**
     * adi
     * @param user
     * @param tMToRemove
     * @param team
     * @throws Exception
     */
    public void removeTeamManager (TeamOwner user, TeamManager tMToRemove, Team team) throws Exception{
        user.removeTeamManager(tMToRemove, team);
    }

    /**
     * adi
     * @param user
     * @param coachToRemove
     * @param coachToAdd
     * @param newCoachRoleAtTeam
     * @param team
     * @throws Exception
     */
    public void removeAndReplaceCoach(TeamRole user, Coach coachToRemove, TeamRole coachToAdd, String newCoachRoleAtTeam, Team team) throws Exception {
        if (user.isTeamOwner()){
            user.getTeamOwner().removeAndReplaceCoach(coachToRemove, coachToAdd, newCoachRoleAtTeam, team);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().removeAndReplaceCoach(coachToRemove, coachToAdd, newCoachRoleAtTeam, team);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param coach
     * @param role
     * @throws Exception
     */
    public void editCoachRole(TeamRole user, Coach coach, String role) throws Exception{
        if (user.isTeamOwner()){
            user.getTeamOwner().editCoachRole(coach, role);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().editCoachRole(coach, role);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param playerToAdd
     * @param role
     * @param team
     * @throws Exception
     */
    public void addPlayer(TeamRole user, TeamRole playerToAdd, String role, Team team) throws Exception {
        if (user.isTeamOwner()){
            user.getTeamOwner().addPlayer(playerToAdd, role, team);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().addPlayer(playerToAdd, role, team);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param player
     * @param team
     * @throws Exception
     */
    public void removePlayer (TeamRole user, Player player, Team team) throws Exception {
        if (user.isTeamOwner()){
            user.getTeamOwner().removePlayer(player, team);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().removePlayer(player, team);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param player
     * @param role
     * @throws Exception
     */
    public void editPlayerRole(TeamRole user, Player player, String role) throws Exception{
        if (user.isTeamOwner()){
            user.getTeamOwner().editPlayerRole(player, role);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().editPlayerRole(player, role);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param nameOfNewField
     * @param team
     * @throws Exception
     */
    public void removeAndReplaceField (TeamRole user, String nameOfNewField, Team team) throws Exception {
        Field fieldtoRemove = team.getField();
        Field fieldToAdd = new Field(nameOfNewField);
        if (user.isTeamOwner()){
            user.getTeamOwner().removeAndReplaceField(fieldtoRemove, fieldToAdd, team);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().removeAndReplaceField(fieldtoRemove, fieldToAdd, team);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param name
     * @throws Exception
     */
    public void editFieldName(TeamRole user, Team team, String name) throws Exception{
        if (user.isTeamOwner()){
            user.getTeamOwner().editFieldName(team.getField(), name);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().editFieldName(team.getField(), name);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param team
     * @param typeOfIncome
     * @param amount
     * @throws Exception
     */
    public void addIncomeToTeam(TeamRole user, Team team, String typeOfIncome, long amount) throws Exception {
        if (user.isTeamOwner()){
            user.getTeamOwner().addIncomeToTeam(team, typeOfIncome, amount);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().addIncomeToTeam(team, typeOfIncome, amount);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * adi
     * @param user
     * @param team
     * @param typeOfExpense
     * @param amount
     * @throws Exception
     */
    public void addExpenseToTeam(TeamRole user, Team team ,String typeOfExpense, long amount) throws Exception {
        if (user.isTeamOwner()){
            user.getTeamOwner().addExpenseToTeam(team, typeOfExpense, amount);
        }
        // the function in team manager checks if has permission
        else if (user.isTeamManager()){
            user.getTeamManager().addExpenseToTeam(team, typeOfExpense, amount);
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }
    }

    /**
     * mark list of notifications as read.
     * @param user
     * @param read
     */
    public void markAsReadNotification (TeamRole user ,HashSet<Notification> read) throws Exception{
        if (user.isTeamOwner()){
            for(Notification n: read){
                if(user.getTeamOwner().getNotificationsList().contains(n)) {
                    user.getTeamOwner().MarkAsReadNotification(n);
                }
            }        }
        else if (user.isTeamManager()){
            for(Notification n: read){
                if(user.getTeamManager().getNotificationsList().contains(n)) {
                    user.getTeamManager().MarkAsReadNotification(n);
                }
            }
        }
        // user isn't teamOwner or teamManager
        else{
            throw new Exception("user doesn't have the permission to do this action");
        }

    }
    public String becomeRole(String userName, String role){
        TeamRole user = (TeamRole) sOController.getUserByUserName(userName);
        List<String> key=new LinkedList<>();
        key.add(userName);
        List<String> records= new LinkedList<>();
        if(user.isPlayer()||role.equals("Player")){
            records.add("1");
        }
        else{
            records.add("0");

        }
        if(user.isCoach()||role.equals("Coach")){
            records.add("1");
        }
        else{
            records.add("0");

        }
        if(user.isTeamOwner()||role.equals("Team Owner")){
            records.add("1");
        }
        else{
            records.add("0");

        }
        if(user.isTeamManager()){
            records.add("1");
        }
        else{
            records.add("0");
        }
        daoTeamRole.update(key,records);

        if(role.equals("Team Owner")){
            return user.becomeTeamOwner();
        }
        else if(role.equals("Coach")){
            return user.becomeCoach();
        }
        else if(role.equals("Player")){
            return user.becomePlayer();
        }
        return "fail";
    }

    //<editor-fold desc="getters">

    /**
     * adi
     * get all the possible users i can subscribe to become team owner
     * @return list of possible users
     */
    public LinkedList<Fan> getAllPossibleSubscribeTeamOwner(TeamRole user){
        LinkedList<Fan> allFans = MainSystem.getInstance().getAllFans();
        LinkedList<Fan> ans = new LinkedList<>();
        for(Fan fan : allFans){
            if (!(fan instanceof SystemManager) && !(fan instanceof Rfa) && !(fan instanceof Referee)){
                if (!(fan instanceof TeamRole)){
                    ans.add(fan);
                }
                else if(fan instanceof TeamRole) {
                    ans.add(fan);
                }
            }
        }
        if (ans.contains(user)){
            ans.remove(user);
        }
        return ans;
    }
    /**
     * adi
     * get all the possible users i can subscribe to become team manager
     * @return list of possible users
     */
    public LinkedList<Fan> getAllPossibleSubscribeTeamManager(TeamRole user){
        LinkedList<Fan> allFans = MainSystem.getInstance().getAllFans();
        LinkedList<Fan> ans = new LinkedList<>();
        for(Fan fan : allFans){
            if (!(fan instanceof SystemManager) && !(fan instanceof Rfa) && !(fan instanceof Referee)){
                if (!(fan instanceof TeamRole)){
                    ans.add(fan);
                }
                else if(fan instanceof TeamRole && !((TeamRole) fan).isTeamManager()) {
                    ans.add(fan);
                }
            }
        }
        if (ans.contains(user)) {
            ans.remove(user);
        }
        return ans;
    }
    /**
     * adi
     * get teams for team owner
     * @param user
     * @return
     */
    public LinkedList<Team> getAllMyTeams (TeamOwner user){
        return user.getTeams();
    }
    /**
     * adi
     * get teams for team manager
     * @param user
     * @return
     */
    public Team getAllMyTeams (TeamManager user){
        return user.getTeam();
    }
    /**
     * adi
     * get all team owners of specific team
     * @param team
     * @return
     */
    public HashSet<TeamOwner> getAllTeamOwners (Team team){
        return team.getTeamOwners();
    }

    /**
     * adi
     * @return
     */
    public LinkedList<TeamRole> getAllTeamRoles (){
        return MainSystem.getInstance().getTeamRoles();
    }

    /**
     * adi
     * @param team
     * @return
     */
    public Coach getCoach (Team team){
        return team.getCoach();
    }
    /**
     * adi
     * @return
     */
    public String getAllTeamRolesThatArentCoachWithTeam(){
        LinkedList<TeamRole> allTeamRole = MainSystem.getInstance().getTeamRoles();
        //LinkedList<String> ans = new LinkedList<>();
        String ans = new String();
        for(TeamRole teamRole : allTeamRole){
            if(teamRole.getCoach() == null){
                //ans.add(teamRole.getUserName());
                ans += teamRole.getUserName() + ";";
            }
            else if(teamRole.getCoach().getCoachTeam() == null){
                //ans.add(teamRole.getUserName());
                ans += teamRole.getUserName() + ";";
            }
        }
        return ans;
    }
    public LinkedList<TeamRole> getAllTeamRolesThatArentCoachWithTeamObject(){
        LinkedList<TeamRole> allTeamRole = MainSystem.getInstance().getTeamRoles();
        LinkedList<TeamRole> ans = new LinkedList<>();
        for(TeamRole teamRole : allTeamRole){
            if(teamRole.getCoach() == null){
                ans.add(teamRole);
            }
            else if(teamRole.getCoach().getCoachTeam() == null){
                ans.add(teamRole);
            }
        }
        return ans;
    }
    /**
     * adi
     * @return
     */
    public String getAllTeamRolesThatArentPlayerWithTeam(){
        LinkedList<TeamRole> allTeamRole = MainSystem.getInstance().getTeamRoles();
        //LinkedList<String> ans = new LinkedList<>();
        String ans = new String();
        for(TeamRole teamRole : allTeamRole){
            if(teamRole.getPlayer() == null){
                //ans.add(teamRole.getUserName());
                ans += teamRole.getUserName() + ";";
            }
            else if(teamRole.getPlayer().getTeam() == null){
                //ans.add(teamRole.getUserName());
                ans += teamRole.getUserName() + ";";
            }
        }
        return ans;
    }
    public LinkedList<TeamRole> getAllTeamRolesThatArentPlayerWithTeamObject(){
        LinkedList<TeamRole> allTeamRole = MainSystem.getInstance().getTeamRoles();
        LinkedList<TeamRole> ans = new LinkedList<>();
        for(TeamRole teamRole : allTeamRole){
            if(teamRole.getPlayer() == null){
                ans.add(teamRole);
            }
            else if(teamRole.getPlayer().getTeam() == null){
                ans.add(teamRole);
            }
        }
        return ans;
    }
    /**
     * adi
     * @param team
     * @return
     */
    public HashSet<Player> getAllPlayersOfTeam (Team team){
        return team.getPlayers();
    }
    /**
     * adi
     * @param team
     * @return
     */
    public Field getField (Team team){
        return team.getField();
    }

    /**
     * adi
     * @param userName
     * @return
     */
    public String getMyRoles (String userName){
        TeamRole user = (TeamRole) sOController.getUserByUserName(userName);
        //LinkedList<String> myRoles = new LinkedList<>();
        String myRoles = new String();
        if (user.isTeamOwner()){
            //myRoles.add("Team Owner");
            myRoles += "Team Owner;";
        }
        if (user.isTeamManager()){
            //myRoles.add("Team Manager");
            myRoles += "Team Manager;";
        }
        if (user.isCoach()){
            //myRoles.add("Coach");
            myRoles += "Coach;";
        }
        if (user.isPlayer()){
            //myRoles.add("Player");
            myRoles += "Player;";
        }
        return myRoles;
    }

    /**
     * adi
     * @param userName
     * @return
     */
    public String getWhatICanBecome (String userName){
        TeamRole user = (TeamRole) sOController.getUserByUserName(userName);
        //LinkedList<String> canBecome = new LinkedList<>();
        String canBecome = new String();
        if (!user.isTeamOwner()){
            //canBecome.add("Team Owner");
            canBecome += "Team Owner;";
        }
        if (!user.isCoach()){
            //canBecome.add("Coach");
            canBecome += "Coach;";
        }
        if (!user.isPlayer()){
            //canBecome.add("Player");
            canBecome += "Player;";
        }
        return canBecome;
    }
    //</editor-fold>

}

