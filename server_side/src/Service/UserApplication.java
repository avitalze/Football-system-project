package Service;

import Domain.Controllers.UserController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/UserApplication")
public class UserApplication {
    UserController userController = new UserController();

    /**
     * login function
     * @param userName
     * @param password
     * @return
     * @throws Exception
     *  @codeBy Eden
     */
    @Path("/login/{userName}/{password}")
    @GET
    @Produces("text/plain")
    public String login(@PathParam("userName")String userName, @PathParam("password")String password ) {
        return userController.login(userName, password);
    }

    @Path("/logout/{userName}")
    @GET
    @Produces("text/plain")
    public String logout(@PathParam("userName")String userName )  {
        return userController.logOut(userName);
    }

    @Path("/haveUnreadNotifications/{userName}")
    @GET
    @Produces("text/plain")
    public boolean haveUnreadNotifications(@PathParam("userName")String userName ) {
        return userController.haveUnreadNotifications(userName);
    }

    @Path("/haveUnreadNotifications/{userRole}/{userName}")
    @GET
    @Produces("text/plain")
    public boolean haveUnreadNotifications(@PathParam("userRole")String userRole,@PathParam("userName") String userName) {
        return userController.haveUnreadNotifications(userRole, userName);

    }

//    public boolean haveTeamRequest(String userRole, String userName) {
//        return userController.haveUnreadRequest(userRole, userName);
//    }
}
