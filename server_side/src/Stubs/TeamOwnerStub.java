//import java.util.HashMap;
//public class TeamOwnerStub extends Domain.Users.TeamOwner{
//    public HashMap<Subscription, Domain.LeagueManagment.Team> mySubscriptions;
//
//    public TeamOwnerStub(Domain.MainSystem ms, String name, String phoneNumber, String email, String userName, String password) {
//        super(ms, name, phoneNumber, email, userName, password);
//    }
//
//    /***
//     * for remove user test.
//     * @codeBy Eden
//     */
//    public void addSub(){
//        TeamOwnerStub to1=new TeamOwnerStub(super.system,"ds","sdf","sfd","sdf","dsf");
//        TeamOwnerStub to2=new TeamOwnerStub(super.system,"ds","sdf","sfd","sdf","dsf");
//        TeamOwnerStub to3=new TeamOwnerStub(super.system,"ds","sdf","sfd","sdf","dsf");
//        TeamOwnerStub to4=new TeamOwnerStub(super.system,"ds","sdf","sfd","sdf","dsf");
//        super.system.addUser(to1);
//        super.system.addUser(to2);
//        super.system.addUser(to3);
//        super.system.addUser(to4);
//        TeamStub t1=new TeamStub();
//        TeamStub t2=new TeamStub();
//
//        mySubscriptions.put(to1,t1);
//        mySubscriptions.put(to2,t1);
//        mySubscriptions.put(to3,t2);
//        mySubscriptions.put(to4,t2);
//
//        /***to4 subscriptions:**/
//        /**another team*/
//        TeamStub t3=new TeamStub();
//        TeamOwnerStub to5=new TeamOwnerStub(super.system,"ds","sdf","sfd","sdf","dsf");
//        to4.mySubscriptions.put(to5,t3);
//        super.system.addUser(to5);
//
//        /**this team***/
//        TeamOwnerStub to6=new TeamOwnerStub(super.system,"ds","sdf","sfd","sdf","dsf");
//        to4.mySubscriptions.put(to6,t2);
//        super.system.addUser(to6);
//
//
//
//        /**
//         * test - delete this user from system ,
//         * all the team owners subscriptions deleted except to5 and to4 for team :t3
//         */
//
//
//
//    }
//}
