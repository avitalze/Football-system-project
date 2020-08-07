package Service;

import Domain.Controllers.RefereeController;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/RefereeApplication")
public class RefereeApplication {
    RefereeController refereeController = new RefereeController();


    /**
     *
     * @param username
     * @param match
     * @return LinkedList<String> of players at specific match
     */
    @Path("/displayPlayersAtMatch/{username}/{match}")
    @GET
    @Produces("text/plain")
    public String displayPlayersAtMatch(@PathParam("username")String username, @PathParam("match")String match) {
        return refereeController.displayPlayersAtMatch(username, match);
    }


    /**
     *
     * @param username
     * @return match that the referee judging that take place right now - probably just one
     */
    @Path("/displayAllMatches/{username}")
    @GET
    @Produces("text/plain")
    public String displayAllMatches(@PathParam("username")String username){
        return refereeController.displayAllMatches(username);
    }

    /**
     * This function return all the matches that the referee is a main referee, and already over
     * @param username
     * @return
     */
    @Path("/getAllMatches/{username}")
    @GET
    @Produces("text/plain")
    public String getAllMatches(@PathParam("username")String username) {
        return refereeController.getAllMatches(username);
    }

    /**
     * This function return referee's qualification
     * @param username
     * @return
     */
    @Path("/getQualificationOfReferee/{username}")
    @GET
    @Produces("text/plain")
    public String getQualificationOfReferee(@PathParam("username")String username) {
        return refereeController.getQualificationOfReferee(username);
    }

    @Path("/createReportOfMatch/{match}/{username}")
    @GET
    @Produces("text/plain")
    public String createReportOfMatch(@PathParam("match")String match, @PathParam("username")String username) {
        return refereeController.createReportOfMatch(match, username);
    }

    @Path("/getMatchScore/{match}/{username}")
    @GET
    @Produces("text/plain")
    public String getMatchScore(@PathParam("match")String match, @PathParam("username")String username) {
        return refereeController.getMatchScore(match, username);
    }

    @Path("/createGoalEvent/{username}/{match}/{player}")
    @GET
    @Produces("text/plain")
    public String createGoalEvent(@PathParam("username")String username,@PathParam("match") String match,
                                  @PathParam("player")String player) {
        return refereeController.createGoalEvent(username, match, player);
    }

    @Path("/createInjuryEvent/{username}/{match}/{player}")
    @GET
    @Produces("text/plain")
    public String createInjuryEvent(@PathParam("username")String username,@PathParam("match") String match,
                                    @PathParam("player")String player) {
        return refereeController.createInjuryEvent(username, match, player);
    }

    @Path("/createOffenseEvent/{username}/{match}/{player}")
    @GET
    @Produces("text/plain")
    public String createOffenseEvent(@PathParam("username")String username,@PathParam("match") String match,
                                     @PathParam("player")String player) {
        return refereeController.createOffenseEvent(username, match, player);
    }

    @Path("/createOffSideEvent/{username}/{match}/{player}")
    @GET
    @Produces("text/plain")
    public String createOffSideEvent(@PathParam("username")String username,@PathParam("match") String match,
                                     @PathParam("player")String player) {
        return refereeController.createOffSideEvent(username, match, player);
    }

    @Path("/createRedCardEvent/{username}/{match}/{player}")
    @GET
    @Produces("text/plain")
    public String createRedCardEvent(@PathParam("username")String username,@PathParam("match") String match,
                                     @PathParam("player")String player) {
        return refereeController.createRedCardEvent(username, match, player);
    }

    @Path("/createYellowCardEvent/{username}/{match}/{player}")
    @GET
    @Produces("text/plain")
    public String createYellowCardEvent(@PathParam("username")String username,@PathParam("match") String match,
                                        @PathParam("player")String player) {
        return refereeController.createYellowCardEvent(username, match, player);

    }

    @Path("/createReplaceEvent/{username}/{match}/{firstPlayer}/{secondPlayer}")
    @GET
    @Produces("text/plain")
    public String createReplaceEvent(@PathParam("username")String username, @PathParam("match") String match,
                                     @PathParam("firstPlayer")String firstPlayer, @PathParam("secondPlayer") String secondPlayer){
        return refereeController.createReplaceEvent(username, match, firstPlayer, secondPlayer);
    }

    @Path("/createExtraTimeEvent/{username}/{match}/{time}")
    @GET
    @Produces("text/plain")
    public String createExtraTimeEvent(@PathParam("username")String username,@PathParam("match") String match,
                                     @PathParam("time")String time) {
        return refereeController.createExtraTimeEvent(username, match, time);
    }

    /**
     * return all unread notifications of referee
     * @param userName
     * @return
     */
    @Path("/getRefereeUnreadNotifications/{username}")
    @GET
    @Produces("text/plain")
    public String getRefereeUnreadNotifications(@PathParam("username") String userName){
        return refereeController.getRefereeUnreadNotifications(userName);
    }

    /**
     * mark notification as read
     * @param notification
     * @param refereeUserName
     */
    @Path("/markNotificationAsRead/{notification}/{refereeUserName}")
    @GET
    @Produces("text/plain")
    public String markNotificationAsRead(@PathParam("notification") String notification, @PathParam("refereeUserName") String refereeUserName){
        return refereeController.markNotificationAsRead(notification,refereeUserName);
    }

}
