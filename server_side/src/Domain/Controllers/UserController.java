package Domain.Controllers;

import DataAccess.DaoFans;
import DataAccess.DaoReferee;
import DataAccess.DaoRfa;
import DataAccess.DaoTeamRole;
import DataAccess.DbAdapter.FanAdapter;
import DataAccess.DbAdapter.RefereeAdapter;
import DataAccess.DbAdapter.TeamRoleAdapter;
import Domain.Main;
import Domain.MainSystem;
import Domain.Users.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserController{
    SystemOperationsController smc=new SystemOperationsController();
    FanAdapter fanAdapter=new FanAdapter();
    RefereeAdapter refereeAdapter=new RefereeAdapter();
    TeamRoleAdapter teamRoleAdapter=new TeamRoleAdapter();

    DaoFans fansTable=new DaoFans();
    DaoRfa rfaTable=new DaoRfa();
    DaoReferee refereeTable=new DaoReferee();
    DaoTeamRole teamRoleTable=new DaoTeamRole();
    /**
     * login function
     * @param userName
     * @param password
     * @return
     * @throws Exception
     *  @codeBy Eden
     */
        public String login(String userName , String password ) {
            try {
                //Todo this function returns exceptions, need to catch them here and return the message
                Fan f = MainSystem.getInstance().logIn(userName, smc.sha256(password));
                if (f == null) {
                    //Todo change to return string message and not exception
                    throw new Exception("Incorrect user name or password");
                }
                if (f instanceof Rfa) {
                    return "RFA";
                }
                if (f instanceof Referee) {
                    return "Referee";
                }
                return "Fan";
            } catch(Exception e){
                //Todo change the return
                return e.getMessage();
            }
        }

    /**OR
     * log out from system
     * @param username
     * @return
     */
    public String logOut(String username){
        User u=smc.getUserByUserName(username);
        if(u instanceof Fan){
            try {
                MainSystem.getInstance().logOut((Fan)u);
                return "success";
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "user is not a fan";
    }

    public boolean haveUnreadNotifications(String userName ) {
        User u = smc.getUserByUserName(userName);
        if(u==null){
            return false;
        }
        if(((Fan)(u)).getUnReadNotificationsAsFan().size()>0){
            return true;
        }
        return false;
    }


    public boolean haveUnreadNotifications(String userRole, String userName) {
        User u = smc.getUserByUserName(userName);
        if(u==null){
            return false;
        }
        if(u instanceof Rfa){
            return Rfa.getTeamRequests().size()>0;
        }
        if(u instanceof Referee){
            return ((Referee) u).getUnReadNotifications().size()>0;
        }
        return false;
    }
//
//    public boolean haveUnreadRequest(String userRole, String userName) {
//    }
}
