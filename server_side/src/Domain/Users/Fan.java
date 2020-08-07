package Domain.Users;

import Domain.*;
import Domain.Controllers.SystemOperationsController;
import Domain.Events.Event;
import Domain.LeagueManagment.Match;
import Domain.Notifications.Notification;
import Domain.Notifications.NotificationsUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Fan extends User implements NotificationsUser {

    private List<PrivatePage> myPages;
    private List<Complaint> myComplaints;
    /**Do we need this? if a fan wants to follow a game it is added to his observable list **/
    private LinkedList <Match> matchesFollow;
    private LinkedList<String> searchHisroty;
    private static final Logger LOG = LogManager.getLogger("Fan");
    HashSet<Notification> notificationHashSet;

    //from subscription:
    private String name;
    private String phoneNumber;
    private String email;
    private String userName;
    private String password;
    private Date dateOfBirth;
    private boolean sendByEmail;

    public boolean gotFanNotification;

    public Fan(MainSystem ms, String name, String phoneNumber, String email, String userName, String password, Date date) {
        super(ms);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.dateOfBirth=date;
        myPages = new LinkedList<>();
        myComplaints = new LinkedList<>();
        matchesFollow=new LinkedList<>();
        this.searchHisroty= new LinkedList<>();
        notificationHashSet=new HashSet<>();
        //add userName to the hashset
        ms.addUserName(userName);
        gotFanNotification=false;
        sendByEmail=false;
    }


    public Fan( String name, String phoneNumber, String email, String userName, String password, Date date) {
        super(MainSystem.getInstance());
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.dateOfBirth=date;
        myPages = new LinkedList<>();
        myComplaints = new LinkedList<>();
        matchesFollow=new LinkedList<>();
        this.searchHisroty= new LinkedList<>();
        notificationHashSet=new HashSet<>();
        //add userName to the hashset
//        MainSystem.getInstance().addUserName(userName);
        gotFanNotification=false;
        sendByEmail=false;
    }
    //<editor-fold desc="getters and setters">
    public List<PrivatePage> getMyPages() {
        return myPages;
    }
    public void unfollow(PrivatePage p){
        myPages.remove(p);
        p.getFans().remove(this);
    }
    public void setMyPages(List<PrivatePage> myPages) {
        this.myPages = myPages;
    }

    public List<Complaint> getMyComplaints() {
        return myComplaints;
    }

    public void setMyComplaints(List<Complaint> myComplaints) {
        this.myComplaints = myComplaints;
    }

    public LinkedList<Match> getMatchesFollow() {
        return matchesFollow;
    }

    public void setMatchesFollow(LinkedList<Match> matchesFollow) {
        this.matchesFollow = matchesFollow;
    }

    public void setSearchHisroty(LinkedList<String> searchHisroty) {
        this.searchHisroty = searchHisroty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        SystemOperationsController systemOperationsController=new SystemOperationsController();
        try {
            this.password =  systemOperationsController.sha256(password) ; //!!!!
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isSendByEmail() {
        return sendByEmail;
    }

    public void setSendByEmail(boolean sendByEmail) {
        this.sendByEmail = sendByEmail;
    }

    //</editor-fold>


    /**Eden*/
    public void addMatchFollow(Match m){
        matchesFollow.add(m);
        m.addObserver(this);
    }



    /**Eden*/
    public void addNewComplaint(String comp) throws Exception {
        Complaint complaint=new Complaint(this,system);
        myComplaints.add(complaint);
        complaint.setContent(comp);
        complaint.send("new Domain.Complaint from: "+userName);

        complaint.addObserver(this);
    }

    /**Eden*/
    public void addToSearchHistory(String search){
        searchHisroty.add(search);
    }

    /**Eden*/
    public LinkedList<String> getSearchHisroty() {
        return searchHisroty;
    }


    /**Or**/
    public void subToPage(PrivatePage privatePage) throws Exception {
        if(privatePage== null){
            LOG.error("private page null");
            throw new Exception("private page null");
        }
        myPages.add(privatePage);
        privatePage.addFan(this);
        LOG.info(String.format("%s - %s", getUserName(), "subscriped to page"));
    }


    public void addComplaint(){

    }

    /**OR**/
    public void addPage(PrivatePage page){
        myPages.add(page);
    }


    public static void sendEmail(String to, String messageToSend){
        String mailSenderFile = "C:\\Users\\User\\Documents\\MailSender.exe";
        try
        {
            String subject = "You have a new notification from The FootBall Management System" ;
            new ProcessBuilder(mailSenderFile,to,subject,messageToSend).start();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //<editor-fold desc="Notifications Handler">
    /**Eden*/
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof  Match){
            if(arg instanceof Event) {
                if(system.userLoggedIn(this)){
                    gotFanNotification=true;
                }
                if(isSendByEmail()==true){
                    sendEmail(this.email,"You have a new notification about a game you are following.\n"+
                            "The event: \n"+ arg.toString());
                }
                notificationHashSet.add(new Notification(o, arg, false));
            }
//            /**change place or time event */
//            /**Game result*/
//            if(arg instanceof String){
//                /**result event**/
//                if(((String)arg).contains("Result") ){
//
//                }
//                else{
//                    /**change place event */
//                    if(((String)arg).contains("Place") ){
//
//                    }
//                    else{
//                        /**change time event */
//                        if(((String)arg).contains("Time") ){
//
//                        }
//                    }
//                }
//            }
//            else{
//                if(arg instanceof Domain.EventsAdapter.Goal){
//                    return;
//                }
//                if(arg instanceof Domain.EventsAdapter.RedCard){
//                    return;
//                }
//                if(arg instanceof Domain.EventsAdapter.YellowCard){
//                    return;
//                }
//                if(arg instanceof Domain.EventsAdapter.OffSide){
//                    return;
//                }
//                if(arg instanceof Domain.EventsAdapter.Offense){
//                    return;
//                }
//                if(arg instanceof Domain.EventsAdapter.Injury){
//                    return;
//                }
//                if(arg instanceof Domain.EventsAdapter.Replacement){
//                    return;
//                }
//            }
        }
        if(o instanceof Complaint){
            Complaint comp=((Complaint)o);
            String ans=comp.getAnswer();
            if(system.userLoggedIn(this)){
                gotFanNotification=true;
            }
            if(isSendByEmail()==true){
                //send email with notification
                //send(this.email, );
            }
            notificationHashSet.add(new Notification(o, "Answer: "+ans, false));

        }
    }


    /**
     * mark notification as readen
     * @param not-unread notification to mark as read
     * @codeBy Eden
     */
    @Override
    public void MarkAsReadNotification(Notification not){
        not.setRead(true);
        //we dont want the alert to show if the user is in the notification inbox
        gotFanNotification=false;
    }

    @Override
    public String checkNotificationAlert() {
        if(gotFanNotification){
            gotFanNotification=false;
            return "gotFanNotification";
        }
        return "";
    }


    @Override
    public HashSet<Notification> getNotificationsList() {
        gotFanNotification=false;
        return notificationHashSet;
    }

    /***
     * @return only the unread notifications . if not have return null
     * @codeBy Eden
     */
    @Override
    public HashSet<Notification> getUnReadNotifications(){
        gotFanNotification=false;
        HashSet<Notification> unRead=new HashSet<>();
        for(Notification n: notificationHashSet){
            if(n.isRead()==false){
                unRead.add(n);
            }
        }
        return unRead;
    }
    //</editor-fold>

    public HashSet<Notification> getUnReadNotificationsAsFan(){
        gotFanNotification=false;
        HashSet<Notification> unRead=new HashSet<>();
        for(Notification n: notificationHashSet){
            if(n.isRead()==false){
                unRead.add(n);
            }
        }
        return unRead;
    }


    /***just for test**/
    @Override
    public String toString() {
        return "{"+userName  +
                '}';
    }


    public HashSet<Notification> getFanNotification() {
        gotFanNotification=false;
        return notificationHashSet;
    }



}
