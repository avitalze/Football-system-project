package Domain.Users;
import Domain.*;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;

public abstract class ManagmentActions {
    protected HashSet<TeamSubscription> mySubscriptions;
    private static final Logger LOG = LogManager.getLogger("ManagmentActions");
    public ManagmentActions() {
        mySubscriptions= new HashSet<>();
    }


    /**
     * adi
     * this team owner subscribes a fan to become a team owner of a specific team
     * @param fan - the person you want to make a team owner
     * @param team - the team you want to add a team owner
     * @return the new team owner
     * @throws Exception
     */
    //TODO test-V
    public TeamRole subscribeTeamOwner(Fan fan, Team team) throws Exception{
        if (fan == null || team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        // check if already team owner of this team
        if (fan instanceof TeamRole && ((TeamRole) fan).isTeamOwner() && team.getTeamOwners().contains(((TeamRole) fan).getTeamOwner())){
            LOG.error("Already team Owner of this team");
            throw new Exception("Already team Owner of this team");
        }
        TeamRole teamRole;
        // check if already team owner of different team
        if (fan instanceof TeamRole && ((TeamRole) fan).isTeamOwner()){
            ((TeamRole) fan).getTeamOwner().addNewTeam(team);

            team.addTeamOwner(((TeamRole) fan).getTeamOwner());
            teamRole = ((TeamRole) fan);
        }
        // check if already teamRole
        else if (fan instanceof TeamRole){
            ((TeamRole) fan).becomeTeamOwner();
            ((TeamRole) fan).getTeamOwner().addNewTeam(team);
            team.addTeamOwner(((TeamRole) fan).getTeamOwner());
            teamRole = ((TeamRole) fan);
        }
        // else just a fan
        else{
            teamRole = new TeamRole(fan);
            teamRole.becomeTeamOwner();
            teamRole.getTeamOwner().addNewTeam(team);
            team.addTeamOwner(teamRole.getTeamOwner());
        }
        TeamSubscription sub = new TeamSubscription(teamRole.getTeamOwner(), team, teamRole);
        mySubscriptions.add(sub);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "subscribe team Owner :"+fan.getName()+"to team: "+team.getName()));
        return teamRole;
    }


    /**
     * adi
     * this team owner removes a team owner that he subscribed in the past
     * @param tO - team owner to remove
     * @param team - the team that the team owner will be removed from
     * @throws Exception
     */
    //TODO test-V
    public void removeTeamOwner (TeamOwner tO, Team team)throws Exception{
        if (tO == null || team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        team.removeTeamOwner(tO);
        LinkedList<TeamSubscription> subscriptionsList=new LinkedList<>();
        for(TeamSubscription sub: mySubscriptions){
            subscriptionsList.add(sub);
        }
        for(int i=0; i< subscriptionsList.size();i++){
            TeamSubscription sub=subscriptionsList.get(i);
            if (sub.user.equals(tO.getTeamRole()) && sub.role.equals(tO)){
                mySubscriptions.remove(sub);
            }
            break;
        }
       subscriptionsList=new LinkedList<>();
        for(TeamSubscription sub: tO.mySubscriptions){
            subscriptionsList.add(sub);
        }
        for(int i=0; i< subscriptionsList.size();i++){
            TeamSubscription sub=subscriptionsList.get(i);
            if (sub.team.equals(team)) {
                if (sub.role instanceof TeamOwner){
                    tO.removeTeamOwner((TeamOwner) sub.role, sub.team);
                }
                else{
                    tO.removeTeamManager((TeamManager) sub.role, sub.team);
                }
            }
        }
        tO.removeTeam(team);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "remove team Owner :"+tO.getTeamRole().getName()+"to team: "+team.getName()));
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
    public void removeAndReplaceCoach(Coach coachToRemove, TeamRole coachToAdd, String newCoachRoleAtTeam, Team team) throws Exception {
        if (coachToRemove == null || coachToAdd == null || team == null || newCoachRoleAtTeam==null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        if (team.getCoach().equals(coachToRemove)) {
            team.removeCoach(coachToRemove);
            coachToRemove.setCoachTeam(null);
            if (!coachToAdd.isCoach()){
                coachToAdd.becomeCoach();
            }
            team.addCoach(coachToAdd.getCoach());
            coachToAdd.getCoach().setCoachTeam(team);
            coachToAdd.getCoach().setRoleAtTeam(newCoachRoleAtTeam);
        }
        else {
            LOG.error("This Coach doesn't exist in this team");
            throw new Exception("This Coach doesn't exist in this team");
        }
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "remove coach :"+coachToRemove.getTeamRole().getName()+"and replave with coach:+"+coachToAdd.getName()+"to team: "+team.getName()));
    }

    /**
     * adi
     * edit coach's role
     * @param coach - the coach to edit
     * @param role - the new role
     */
    //TODO test-V
    public void editCoachRole(Coach coach, String role) throws Exception {
        if (coach == null || role == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        coach.setRoleAtTeam(role);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "edit coach "+coach.getTeamRole().getName()+" role to be :"+role));
    }

    /**
     * adi
     * add a player to a team
     * @param player - player to add
     * @param role - the players role
     * @param team - the team to add the player
     */
    //TODO test-V
    public void addPlayer(TeamRole player, String role, Team team) throws Exception {
        if (player == null || role == null || team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        if (!player.isPlayer()){
            player.becomePlayer();
        }

        if(player.getPlayer().getTeam()==null) {
            team.addPlayer(player.getPlayer());
            player.getPlayer().setPlayerTeam(team);
            player.getPlayer().setRoleAtField(role);
        }
        else{
            LOG.error("This player is already in another team");
            throw new Exception("This player is already in another team");
        }
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "add player :"+player.getName()+"to team: "+team.getName()));
    }

    /**
     * adi
     * remove a player from a team (only if team has more than 11 players)
     * @param player - player to remove
     * @param team - the team to remove the player
     * @throws Exception
     */
    //TODO test-V
    public void removePlayer (Player player, Team team) throws Exception {
        if (player == null || team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        team.removePlayer(player);
        player.setPlayerTeam(null);

        LOG.info(String.format("%s - %s", getUserNameOfAction(), "remove player : "+player.getTeamRole().getName()+"from team: "+team.getName()));
    }

    /**
     * adi
     * edit the players role
     * @param player - the player to edit
     * @param role - the new role
     */
    //TODO test-V
    public void editPlayerRole(Player player, String role) throws Exception {
        if (player == null || role == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        player.setRoleAtField(role);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "edit player "+player.getTeamRole().getName()+" role to be :"+role));
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
    public void removeAndReplaceField (Field fieldtoRemove, Field fieldToAdd, Team team) throws Exception {
        if (fieldtoRemove == null || fieldToAdd == null || team == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        team.removeField(fieldtoRemove);
        fieldtoRemove.removeTeam(team);
        team.setField(fieldToAdd);
        fieldToAdd.addTeam(team);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "remove field "+fieldtoRemove.getNameOfField()+" add fiels: "+fieldToAdd.getNameOfField()+" to team: "+team.getName()));
    }

    /**
     * adi
     * edit the fields name
     * @param field - the field to edit
     * @param name - the new name
     */
    //TODO test-V
    public void editFieldName(Field field, String name) throws Exception {
        if (field == null || name == null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        field.setNameOfField(name);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "edit field name to be: "+field.getNameOfField()));
    }

    /**OR
     * add income to certain team
     * @param team
     * @param typeOfIncome
     * @param amount
     * @throws Exception
     */
    //TODO test-V
    public void addIncomeToTeam(Team team,String typeOfIncome, long amount) throws Exception {
        if(team==null || typeOfIncome==null){
            throw  new NullPointerException();
        }
        team.addIncome(typeOfIncome,amount);

        LOG.info(String.format("%s - %s", getUserNameOfAction(), "add income, type: "+typeOfIncome+", amount: "+amount+" to team: "+team.getName()));
    }

    /**or
     * add income to certain team
     * @param team
     * @param typeOfExpense
     * @param amount
     * @throws Exception
     */
    //TODO test-V
    public void addExpenseToTeam(Team team,String typeOfExpense, long amount) throws Exception {
        if(team==null || typeOfExpense==null){
            throw  new NullPointerException();
        }
        team.addExpense(typeOfExpense,amount);
        LOG.info(String.format("%s - %s", getUserNameOfAction(), "add expense, type: "+typeOfExpense+", amount: "+amount+" to team: "+team.getName()));
    }

    public HashSet<TeamSubscription> getMySubscriptions() {
        return mySubscriptions;
    }

    public abstract String getUserNameOfAction();
}
