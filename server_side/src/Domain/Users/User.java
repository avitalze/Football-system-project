package Domain.Users;

import java.util.*;

import Domain.*;
import Domain.Enums.TeamManagerPermissions;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User {
    protected MainSystem system;
    //protected HashSet<TeamManagerPermissions> permissions;
    private static final Logger LOG = LogManager.getLogger("User");


    public User(MainSystem ms){
        system = ms;
        system.addUser(this);

    }


    //<editor-fold desc="getters and setters">
    public MainSystem getSystem() {
        return system;
    }

    public void setSystem(MainSystem system) {
        this.system = system;
    }

    //</editor-fold>

    //<editor-fold desc="Search Functions">
    //or
    //the user chooses to search by name and enters the name in the text box
    public LinkedHashSet<PrivatePage> searchByName(String name) throws Exception {
        if(name==null){
            throw new Exception("name null");
        }
        if(name.length()==0){
            throw new Exception("name empty");
        }
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
        for (User user:system.getUsers()) {
            if(user instanceof PageOwner){
                if(((PageOwner)user).getPage() != null){
                    if(user instanceof Fan && ((Fan)user).getName().contains(name)){
                            ans.add(((PageOwner)user).getPage());
                    }
                }
            }
        }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Name: "+name);
        }
        writeToLOG("searched by name: "+name);
        return ans;
    }

    //or
    //the user chooses to search by key word and enters the words in the text box
    public LinkedHashSet<PrivatePage> searchByKeyWord(String keyWord) throws Exception {
        if(keyWord==null){
            throw new Exception("keyWord null");
        }
        if(keyWord.length()==0){
            throw new Exception("keyWord empty");
        }
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
        PageOwner curr;
        for (User user:system.getUsers()) {
            if(user instanceof PageOwner){// get only the page owners
                curr=(PageOwner)user;
                if( curr.getPage() != null){// check if they have a page
                    if(curr.getPage().getRecordsAsString().contains(keyWord)){
                        ans.add(curr.getPage());
                    }
                }
            }
        }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("KeyWord: "+keyWord);
        }

        writeToLOG("searched by key word: "+keyWord);
        return ans;
    }

    //or
    //the user chooses to search by league and chooses the league name from the options
    public LinkedHashSet<PrivatePage> searchByLeague(String leagueName) throws Exception {
        if(leagueName==null){
            throw new Exception("leagueName null");
        }
        if(leagueName.length()==0){
            throw new Exception("leagueName empty");
        }
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
        for (League l:system.getLeagues()) {
            if(l.getName().equals(leagueName)){
                for (Season s:l.getTeamsInSeason().keySet()) {
                    if (s.equals(system.getCurrSeason())) {// get only the current season
                        for (Team t : l.getTeamsInSeason().get(s)) {
                            ans.addAll(getPrivatePageforTeam(t));
                        }
                    }
                }
            }
        }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Domain.LeagueManagment.League: "+leagueName);
        }

        writeToLOG("searched league name: "+leagueName);
        return ans;
    }

    //or
    //the user chooses to search by season and chooses the season year from the options
    public LinkedHashSet<PrivatePage> searchBySeason(int seasonYear) throws Exception {
        if(seasonYear<1970 || seasonYear>2021){// not a year
            throw new Exception("year not valid");
        }
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
        for (Season s:system.getSeasons()) {
            if(s.getYear()== seasonYear){
                for (HashSet<Team> hashT:s.getTeamsInCurrentSeasonLeagues().values()) {
                    for (Team t:hashT) {
                        ans.addAll(getPrivatePageforTeam(t));
                    }
                }
            }

        }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Domain.LeagueManagment.Season: "+seasonYear);
        }

        writeToLOG("searched season year: "+seasonYear);
        return ans;
    }

    //or
    //the user chooses to search by team and chooses the team name from the options
    public LinkedHashSet<PrivatePage> searchByTeamName(String teamName) throws Exception {
        if(teamName==null){
            throw new Exception("teamName null");
        }
        if(teamName.length()==0){
            throw new Exception("teamName empty");
        }
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
            for (Team t:system.getActiveTeams()) {
               if(t.getName().contains(teamName)){
                   ans.addAll(getPrivatePageforTeam(t));
               }
            }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Domain.LeagueManagment.Team Name: "+teamName);
        }

        writeToLOG("searched team name: "+teamName);
        return ans;
    }


    //or - private func helps with search
    private LinkedHashSet<PrivatePage> getPrivatePageforTeam(Team t){
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
            //go through all the players
        if(t.getPlayers()!= null) {
            for (Player p : t.getPlayers()) {
                if (p.getPage() != null) {
                    if (!ans.contains(p.getPage())) {// no duplicates
                        ans.add(p.getPage());
                    }
                }
            }
        }
        if(t.getCoach()!= null) {
            // coach page
            if (t.getCoach().getPage() != null) {
                ans.add(t.getCoach().getPage());
            }
        }
            //team's page
            if(t.getPage() != null){
                ans.add(t.getPage());
            }
        return ans;

    }

    //or
    //the user chooses to search all players , returns all the player that have pages
    public LinkedHashSet<PrivatePage> searchAllPlayers(){
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
        for (Player player:system.getAllPlayer()) {
            if(player.getPage() != null){
                ans.add(player.getPage());
            }
        }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Search all Players");
        }

        writeToLOG("searched all players in system");
        return ans;
    }

    //or
    //the user chooses to search all coaches , returns all the coaches who have pages
    public LinkedHashSet<PrivatePage> searchAllCoaches(){
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
        for (Coach coach:system.getAllCoach()) {
                if(coach.getPage() != null) {
                    if (!ans.contains(coach.getPage())) {
                        ans.add(coach.getPage());
                    }
                }
            }


        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Search all Coaches");
        }

        writeToLOG("searched all coaches in system");
        return ans;
    }

    //or
    //the user chooses to search all teams , returns all the teams who have pages
    public LinkedHashSet<PrivatePage> searchAllTeams(){
        LinkedHashSet<PrivatePage> ans= new LinkedHashSet<>();
            for (Team t:system.getActiveTeams()) {
                if( t.getPage() != null ){
                    ans.add(t.getPage());
                }
            }

        //if fan- save the search
        if(this instanceof Fan){
            ((Fan)this).addToSearchHistory("Search All Teams");
        }

        writeToLOG("searched all teams in system");
        return ans;
    }

    //</editor-fold>

    //<editor-fold desc="Filter Functions">

    /**OR**/
    public LinkedHashSet<PrivatePage> filterOnlyTeams(LinkedHashSet<PrivatePage> searchResults) throws Exception {
        LinkedHashSet<PrivatePage> newSearchResults= new LinkedHashSet<>();
        if(searchResults==null){
            throw new Exception("searchResults null");
        }
        if(searchResults.size() ==0){
            throw new Exception("no results in list");
        }
        for (PrivatePage privatePage:searchResults) {
            if((privatePage.getPageOwner() instanceof Team) ){
               newSearchResults.add(privatePage);
            }
        }

        writeToLOG("search filter- only teams");
        return newSearchResults;
    }

    /**OR**/
    public LinkedHashSet<PrivatePage> filterOnlyPlayers(LinkedHashSet<PrivatePage> searchResults) throws Exception {
        LinkedHashSet<PrivatePage> newSearchResults= new LinkedHashSet<>();
        if(searchResults==null){
            throw new Exception("searchResults null");
        }
        if(searchResults.size() ==0){
            throw new Exception("no results in list");
        }
        for (PrivatePage privatePage:searchResults) {
            if((privatePage.getPageOwner() instanceof Player) ){
                newSearchResults.add(privatePage);
            }
        }

        writeToLOG("search filter- only players");
        return newSearchResults;
    }

    /**OR**/
    public LinkedHashSet<PrivatePage> filterOnlyCoachs(LinkedHashSet<PrivatePage> searchResults) throws Exception {
        LinkedHashSet<PrivatePage> newSearchResults= new LinkedHashSet<>();
        if(searchResults==null){
            throw new Exception("searchResults null");
        }
        if(searchResults.size() ==0){
            throw new Exception("no results in list");
        }
        for (PrivatePage privatePage:searchResults) {
            if(privatePage.getPageOwner() instanceof Coach){
                newSearchResults.add(privatePage);
            }
        }

        writeToLOG("search filter- only coaches");
        return newSearchResults;
    }

    /**OR**/
    public LinkedHashSet<PrivatePage> filterByLeagueName(LinkedHashSet<PrivatePage> searchResults, String leagueName) throws Exception {
        LinkedHashSet<PrivatePage> newSearchResults= new LinkedHashSet<>();
        if(searchResults==null){
            throw new Exception("searchResults null");
        }
        if(searchResults.size() ==0){
            throw new Exception("no results in list");
        }
        if(leagueName==null){
            throw new Exception("leagueName null");
        }
        if(leagueName.length() ==0){
            throw new Exception("leagueName empty");
        }
        Season currS= system.getCurrSeason();
        for (PrivatePage privatePage:searchResults) {
            //check if player's team is in this league
            if((privatePage.getPageOwner() instanceof Player) ){//if false
                if( (((Player) privatePage.getPageOwner()).getTeam().getLeaguePerSeason().get(currS).getName()== leagueName)){
                    newSearchResults.add(privatePage);
                }
            }
            //check if coach's team is in this league
            else if((privatePage.getPageOwner() instanceof Coach) ){//if false
                if( (((Coach) privatePage.getPageOwner()).getCoachTeam().getLeaguePerSeason().get(currS).getName()== leagueName)){
                    newSearchResults.add(privatePage);
                }
            }
            //check if team is in this league
            else if((privatePage.getPageOwner() instanceof Team)){//if false
                if( (((Team) privatePage.getPageOwner()).getLeaguePerSeason().get(currS).getName()== leagueName)){
                    newSearchResults.add(privatePage);
                }
            }
        }

        writeToLOG("search filter- by league name: "+leagueName);
        return newSearchResults;
    }

    /**OR**/
    public LinkedHashSet<PrivatePage> filterBySeasonYear(LinkedHashSet<PrivatePage> searchResults, int seasonYear) throws Exception {
        LinkedHashSet<PrivatePage> newSearchResults= new LinkedHashSet<>();
        if(searchResults==null){
            throw new Exception("searchResults null");
        }
        if(searchResults.size() ==0){
            throw new Exception("no results in list");
        }
        if(seasonYear<1900 || seasonYear>2020){
            throw new Exception("year not valid");
        }
        for (PrivatePage privatePage:searchResults) {
            //check if player's team is in this season
            if((privatePage.getPageOwner() instanceof Player) ){
                if( (((Player) privatePage.getPageOwner()).getTeam().playedInSeason(seasonYear))){//if false
                    newSearchResults.add(privatePage);
                }
            }
            //check if coach's team is in this season
            else if((privatePage.getPageOwner() instanceof Coach) ){
                if( (((Coach) privatePage.getPageOwner()).getCoachTeam().playedInSeason(seasonYear))){//if false
                    newSearchResults.add(privatePage);
                }
            }
            //check if team is in this season
            else if((privatePage.getPageOwner() instanceof Team)){
                if((((Team) privatePage.getPageOwner()).playedInSeason(seasonYear))){// if false
                    newSearchResults.add(privatePage);
                }
            }
        }

        writeToLOG("search filter- by season year : "+seasonYear);
        return newSearchResults;
    }

    /**OR**/
    public LinkedHashSet<PrivatePage> filterByTeamName(LinkedHashSet<PrivatePage> searchResults, String teamName) throws Exception {
        LinkedHashSet<PrivatePage> newSearchResults= new LinkedHashSet<>();
        if(searchResults==null){
            throw new Exception("searchResults null");
        }
        if(searchResults.size() ==0){
            throw new Exception("no results in list");
        }
        if(teamName==null){
            throw new Exception("teamName null");
        }
        if(teamName.length() ==0){
            throw new Exception("teamName empty");
        }
        for (PrivatePage privatePage:searchResults) {
            //check player's team
            if((privatePage.getPageOwner() instanceof Player) ){
                if( (((Player) privatePage.getPageOwner()).getTeam().getName().equals(teamName))){//if false
                    newSearchResults.add(privatePage);
                }
            }
            //check if coach's team
            else if((privatePage.getPageOwner() instanceof Coach) ){
                if( (((Coach) privatePage.getPageOwner()).getCoachTeam().getName().equals(teamName))){//if false
                    newSearchResults.add(privatePage);
                }
            }
            //check if team is in this season
            else if((privatePage.getPageOwner() instanceof Team)){
                if( (((Team) privatePage.getPageOwner()).getName().equals(teamName))){// if false
                    newSearchResults.add(privatePage);
                }
            }
        }

        writeToLOG("search filter- by team name: "+teamName);
        return newSearchResults;
    }

    //</editor-fold>



    private void writeToLOG(String message){
        if(this instanceof Fan){
            LOG.info(String.format("%s - %s", ((Fan)this).getUserName(), message));
        }
        else{
            LOG.info(String.format("%s - %s", "guest", message));
        }
    }
}
