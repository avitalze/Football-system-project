package Service;

import Domain.Controllers.FanController;

import javax.ws.rs.*;
import java.sql.SQLException;

@Path("/FanApplication")
public class FanApplication {
    FanController fanController = new FanController();

    @Path("/hello")
    @GET
    @Produces("text/plain")
    public String hello() {
        return "I'm working!";
    }

    @Path("/fanIsTeamRole/{username}")
    @GET
    @Produces("text/plain")
    public String fanIsTeamRole(@PathParam("username") String userName) {
        return fanController.fanIsTeamRole(userName);
    }

    @Path("/setFanDetails/{username}/{name}/{password}/{phoneNumber}/{email}")
    @GET
    @Produces("text/plain")
    public String setFanDetails(@PathParam("username")String userName,@PathParam("name")String name,
                                @PathParam("password")String password,@PathParam("phoneNumber") String phoneNumber,
                                @PathParam("email")String email){
        if(password.equals("null")){
            password=null;
        }
      return fanController.setFanDetails(userName, name, password, phoneNumber, email);
    }

    /**
     * return all notifications of fan and mark them as read
     * @param userName
     * @return
     */
    @Path("/getFanNotifications/{username}")
    @GET
    @Produces("text/plain")
    public String getFanNotifications(@PathParam("username") String userName){
        return fanController.getFanNotifications(userName);
    }

    /**
     * return all the maches of fan follw
     * @param userName
     * @return
     */
    @Path("/getUserMachesFollows/{username}")
    @GET
    @Produces("text/plain")
    public String getUserMachesFollows(@PathParam("username")String userName){
       return fanController.getUserMachesFollows(userName);
    }

    /**
     *  add Match to matches follow of fan
     * @param userName
     * @param match
     * @return
     */
    @Path("/addMatchToFanMatchesFollow/{username}/{match}")
    @GET
    @Produces("text/plain")
    public String addMatchToFanMatchesFollow(@PathParam("username")String userName,@PathParam("match") String match)  {
        return fanController.addMatchToFanMatchesFollow(userName, match);
    }

    /**
     * check for new Notification
     * @param username
     * @return
     */
    @Path("/checkForNewNotifications/{username}")
    @GET
    @Produces("text/plain")
    public String checkForNewNotifications(@PathParam("username")String username){
        return fanController.checkForNewNotifications(username);
    }

}
