package Service;

import Domain.Controllers.SystemOperationsController;

import javax.ws.rs.*;
import java.util.*;

@Path("/SystemOperationsApplication")
public class SystemOperationsApplication {
    SystemOperationsController systemOperationsController = new SystemOperationsController();


    /**
     * return all matches in system that have not yet happened - String format
     */
    @Path("/getAllMatchsInSytem")
    @GET
    @Produces("text/plain")
    public String getAllMatchsInSytem(){
        return systemOperationsController.getAllMatchsInSytem();
    }


    /**
     * return list with all private details of the fan
     * list : name, Password, PhoneNumber, Email, DateOfBirth
     * @param userName
     * @return list of details of fan
     */
    @Path("/getPrivateDetails/{username}")
    @GET
    @Produces("text/plain")
    public String getPrivateDetails(@PathParam("username")String userName) {
        return systemOperationsController.getPrivateDetails(userName);
    }

    @Path("/signUp/{role}/{name}/{phoneNumber}/{email}/{username}/{password}/{dateOfBirth}/{sendByEmail}")
    @POST
    @Produces("text/plain")
    public String  signUp(@PathParam("role")String role,@PathParam("name") String name,@PathParam("phoneNumber") String phoneNumber,
                          @PathParam("email") String email, @PathParam("username")String userName,
                          @PathParam("password")String password,@PathParam("dateOfBirth") String dateOfBirth,
                          @PathParam("sendByEmail")String sendEmail) {
       return systemOperationsController.signUp(role, name, phoneNumber, email, userName, password, dateOfBirth,sendEmail);
    }

}
