package Service;
import Domain.Controllers.TeamManagementController;

import javax.ws.rs.*;

@Path("/TeamManagementApplication")
public class TeamManagementApplication {
    TeamManagementController teamManagementController = new TeamManagementController();

    /**
     * adi
     * @param username
     * @param name
     */
    @Path("/requestNewTeam/{username}/{name}")
    @GET
    @Produces("text/plain")
    public String requestNewTeam(@PathParam("username")String username, @PathParam("name")String name) {
        return teamManagementController.requestNewTeam(username, name);
    }

    /**
     * adi
     * @param userName
     * @param teamName
     * @param playerNames
     * @param coachUserName
     * @param nameOfNewField
     */
    @Path("/makeTeamActive/{userName}/{teamName}/{playerNames}/{coachUserName}/{nameOfNewField}")
    @GET
    @Produces("text/plain")
    public String makeTeamActive(@PathParam("userName")String userName,@PathParam("teamName") String teamName,
                                 @PathParam("playerNames")String playerNames ,@PathParam("coachUserName") String coachUserName,
                                 @PathParam("nameOfNewField")String nameOfNewField){
       return teamManagementController.makeTeamActive(userName, teamName, playerNames, coachUserName, nameOfNewField);
    }


    @Path("/getMyApprovedTeams/{username}")
    @GET
    @Produces("text/plain")
    public String getMyApprovedTeams(@PathParam("username")String userName){
        return teamManagementController.getMyApprovedTeams(userName);
    }


    @Path("/becomeRole/{username}/{role}")
    @GET
    @Produces("text/plain")
    public String becomeRole(@PathParam("username")String userName,@PathParam("role") String role){
        return teamManagementController.becomeRole(userName, role);
    }

    //<editor-fold desc="getters">
    @Path("/getAllTeamRolesThatArentCoachWithTeam")
    @GET
    @Produces("text/plain")
    public String getAllTeamRolesThatArentCoachWithTeam(){
        return teamManagementController.getAllTeamRolesThatArentCoachWithTeam();
    }

    @Path("/getAllTeamRolesThatArentPlayerWithTeam")
    @GET
    @Produces("text/plain")
    public String getAllTeamRolesThatArentPlayerWithTeam(){
        return teamManagementController.getAllTeamRolesThatArentPlayerWithTeam();
    }

    @Path("/getMyRoles/{userName}")
    @GET
    @Produces("text/plain")
    public String getMyRoles (@PathParam("userName")String userName){
       return teamManagementController.getMyRoles(userName);
    }

    @Path("/getWhatICanBecome/{userName}")
    @GET
    @Produces("text/plain")
    public String getWhatICanBecome (@PathParam("userName")String userName){
        return teamManagementController.getWhatICanBecome(userName);
    }
    //</editor-fold>

}
