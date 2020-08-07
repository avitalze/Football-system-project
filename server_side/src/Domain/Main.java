package Domain;

import Domain.Controllers.FanController;
import Domain.Controllers.RefereeController;
import Domain.Controllers.SystemOperationsController;
import Service.RefereeApplication;
import Service.RfaApplication;
import Domain.Controllers.UserController;
import Service.SystemOperationsApplication;
import Service.TeamManagementApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.reflections.Reflections;


public class Main {

    private static final Logger LOG = LogManager.getLogger("LoginMain");

    public static void main(String [] args) throws Exception {
//        LOG.info("tryyyyyyyyyyy");
//
//        LOG.error("erorrrrr");
//        SystemOperationsController systemOperationsController = new SystemOperationsController();
//        systemOperationsController.initSystemFromDB();
//        RfaApplication rfaApplication = new RfaApplication();
//        rfaApplication.DefinePoliciesToSeason("2020","CalculateOption1","SchedualeOption1","amir");

//        MainSystem ms = MainSystem.getInstance();
//        RefereeApplication refereeApplication = new RefereeApplication();
//        rfaApplication.answerRequest("amir","real","false");
//        String matches = refereeApplication.displayAllMatches("or");
//        refereeApplication.createGoalEvent("or",matches,"player26");
//        refereeApplication.createInjuryEvent("or",matches,"player26");
//        refereeApplication.createRedCardEvent("or",matches,"player26");
//        refereeApplication.createReplaceEvent("or",matches,"player26","player27");
//        refereeApplication.createExtraTimeEvent("or",matches,"10");
//        int x=0;
//        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy HH:MM:ss");
//
//       String s =  "04-06-2020 20:00:00";
//       Date d = MainSystem.simpleDateFormat.parse(s);

//       String matchDate = MainSystem.simpleDateFormat.format(d);

//        MainSystem ms = MainSystem.getInstance();
//        TeamStub t1 = new TeamStub("team1");
//        TeamStub t2 = new TeamStub("team2");
//
//        Referee ref1 = new Referee(ms, "ref1", "0546145795", "ref2@gmail.com", "ref2123", "ref2123", "a", MainSystem.birthDateFormat.parse("08-09-1995"));
//
//        Match match = new Match(0,0,t1,t2,new Field("a"),new HashSet<>(),
//                new HashSet<>(),ref1,"04-06-2020 20:00:00");
//
//        String s =  match.toString();
//        String[] arrayOfTeamsAndDate =s.split(",");
//        String d = arrayOfTeamsAndDate[1];
//
//        Date matchDate = MainSystem.simpleDateFormat.parse(d);
//
//        int x=0;


        /**LOGGER FORAMT**/
//        LOG.info(String.format("%s - %s", "try", "action?"));
//        LOG.error("something");

//
//        Reflections reflections = new Reflections("Domain");
//        Set<Class<? extends SchedulingPolicy>> classes = reflections.getSubTypesOf(SchedulingPolicy.class);
//
//        String s = "Domain.Scheduling";
//        for (Class class1:classes) {
//            System.out.println(class1.getSimpleName());
//        }




       // MainSystem.getInstance().checkValidDetails(null,null,"","","");
//        MainSystem ms = MainSystem.getInstance();
//        Fan yossi = new Fan(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123" ,MainSystem.birthDateFormat.parse("02-11-1996"));
//        Team team1 = new Team();
//        Team team2 = new Team();
//        Field field1 = new Field("field1");
//        Field field2 = new Field("field2");
//        HashSet<Event> events = new HashSet<>();
//        HashSet<Referee> referees = new HashSet<>();
//        //Domain.Users.TeamOwner tOYossi = new Domain.Users.TeamOwner(yossi, ms, team1);
//        Fan moshe = new Fan(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123" ,MainSystem.birthDateFormat.parse("02-11-1996"));
//        Fan david = new Fan(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123" ,MainSystem.birthDateFormat.parse("02-11-1996"));
//        Referee mos = new Referee(moshe,ms);
//        Match m = new Match(5,5,team1,team2,field1,events,referees,mos,"15-04-2020 21:20:00");
//        /**DATE**/
//        Date d = m.getStartDate();
//        Date noe = new Date(System.currentTimeMillis());
//        if(m.getStartDate().after(new Date(System.currentTimeMillis()))){
//            System.out.println("true");
//        }
//        else{
//            System.out.println("false");
//        }
       // Date d1 = new Date();
//        /**Add minutes to specific date**/
        //d1 = DateUtils.addMinutes(d1,90);
//        d1 = DateUtils.addDays(d,1);
//        int x;
//
//        /**CREATE DATE BY FORMAT**/
//        String pattern = "dd-M-yyyy hh:mm:ss";
//        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
//        String date = simpleDateFormat.format(new Date());
//        System.out.println(date);
//
//
////        Subscription yossi = new Subscription(ms, "Yossi Hamelech", "0549716910","yossi@gmail.com", "YossiHamelech", "Yossi123" );
////        Domain.LeagueManagment.Team team = new Domain.LeagueManagment.Team();
////        Domain.Users.TeamOwner tOYossi = new Domain.Users.TeamOwner(yossi, ms, team);
////        Subscription moshe = new Subscription(ms, "Moshe Hamelech", "0549715678","moshe@gmail.com", "MosheHamelech", "Moshe123" );
////        Subscription david = new Subscription(ms, "David Hamelech", "0541235678","david@gmail.com", "DavidHamelech", "David123" );
//        PrivatePage pp1=new PrivatePage();
//        PrivatePage pp2= new PrivatePage();

        /**CREATE DATE BY FORMAT**/
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
//        Date date;
//            try {
//                date = sdf.parse("09-12-1995");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
////    Domain.Users.Player player1=new Domain.Users.Player(ms, "Or Hamalcha", "0542150912","oralf@gmail.com", "OrHamalcha", "Or1234",date);

        /*
//
//        Domain.MainSystem ms = new Domain.MainSystem();
//        /**CREATE SEASON 2020 IN LEAGUE- curr season**/
//        Domain.LeagueManagment.League league1 = new Domain.LeagueManagment.League("LIGAT HAALUFUT", ms);
//        Domain.LeagueManagment.Season season2020 = new Domain.LeagueManagment.Season(ms,null,2020);
//        ms.setCurrSeason(season2020);
//        Domain.LeagueManagment.Team hapoelBeerSheva= new Domain.LeagueManagment.Team("Hapoel Beer Sheva");
//        Domain.LeagueManagment.Team hapoelKfarSaba= new Domain.LeagueManagment.Team("Hapoel Kfar Saba");
//        Domain.LeagueManagment.Team macabiHaifa= new Domain.LeagueManagment.Team("Macabi Haifa");
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague1= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague1,hapoelBeerSheva,hapoelKfarSaba,macabiHaifa);
//        season2020.addLeagueWithTeams(league1,teamsForLeague1);
//
//        /**CREATE SEASON 2019 IN LEAGUE**/
//        Domain.LeagueManagment.Season season2019= new Domain.LeagueManagment.Season(ms,null,2019);
//        Domain.LeagueManagment.League league2 = new Domain.LeagueManagment.League("LIGAT HAAL",ms);
//        Domain.LeagueManagment.Team macabiTelAviv= new Domain.LeagueManagment.Team("Macabi Tel Aviv");
//        Domain.LeagueManagment.Team beitarYerushalaim= new Domain.LeagueManagment.Team("Beitar Yerushalaim");
//        Domain.LeagueManagment.Team hapoelRaanana= new Domain.LeagueManagment.Team ("Hapoel Raanana");
//
//        HashSet<Domain.LeagueManagment.Team> teamsForLeague2= new HashSet<Domain.LeagueManagment.Team>();
//        Collections.addAll(teamsForLeague2,beitarYerushalaim,macabiTelAviv,hapoelRaanana);
//        season2019.addLeagueWithTeams(league2,teamsForLeague1);

        //SystemOperationsApplication.initSystemObjectsAdi();
        /**********/
        FanController fc=new FanController();
        SystemOperationsController sop =new SystemOperationsController();
        sop.initSystemFromDB();
//        System.out.println(uc.login("ilan","e111111"));
        SystemOperationsApplication soa=new SystemOperationsApplication();
//        System.out.println(soa.signUp("TeamOwner","eden","0526621646","eden04060925@gmail.com","ilan","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player66","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player57","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player58","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player59","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player60","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player61","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player62","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player63","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player64","e111111","04-04-1994","true"));
//        System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player65","e111111","04-04-1994","true"));
////        System.out.println(soa.signUp("Coach","eden","0526621646","eden04060925@gmail.com","coach12","e111111","04-04-1994","true"));
//        for(int i=67; i<=167;i++){
//            System.out.println(soa.signUp("Player","player","0526621646","eden04060925@gmail.com","player"+i,"e111111","04-04-1994","true"));
//
//        }

//        String players="player46;player47;player48;player49;player50;player51;player52;player53;player54;player55;player56";
//        UserController uc=new UserController();
//        System.out.println(uc.login("ilan","e111111"));
//        fc.addMatchToFanMatchesFollow("ilan",)
        //        TeamManagementApplication te=new TeamManagementApplication();
//        te.makeTeamActive("ilan","amazingTeam",players,"coach12","bashField1");
//        /**********/





    }
}