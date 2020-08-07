package DataAccess;

import DB.Tables.tables.records.FanMatchesFollowRecord;
import DB.Tables.tables.records.MatchesRecord;
import DB.Tables.tables.records.SeasonLeagueTeamRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static DB.Tables.Tables.*;

/**
 * table name : matches
 * 3 keys!
 * **/

public class DaoMatch implements Dao<String> {
    private DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @Override
    public List<String> get(List<String> keys) throws ParseException {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();

        String key0Date=out.format(in.parse(keys.get(0)));
        LocalDateTime dateTime = LocalDateTime.parse(key0Date, out);
        String key1= keys.get(1);
        String key2=keys.get(2);

        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        MatchesRecord matchesRecord =create.selectFrom(MATCHES)
                .where(MATCHES.DATE.ge(dateTime))
                .and(MATCHES.HOME_TEAM.eq(key1))
                .and(MATCHES.AWAY_TEAM.eq(key2))
                .fetchOne();
        /** key noy found in table  **/
        if (matchesRecord == null || matchesRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <matchesRecord.size() ; i++) {
            if(matchesRecord.get(i) instanceof LocalDateTime){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                result.add(((LocalDateTime) matchesRecord.get(i)).format(formatter));
                //dd-MM-yyyy HH:mm:ss
            }
            else{
                result.add(matchesRecord.get(i).toString());
            }

        }
        return result;
    }

    @Override
    public List<List<String>> getAll(String collName, String filter) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();

        if( collName==null && filter==null){
            /** return all rows in table **/
            Result<Record> result=create.select().from(MATCHES).fetch();

            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //4!
            for(int i=0;i< numOfCols;i++){
                List <?> currCol = result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    if(currCol.get(j) instanceof LocalDateTime){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        ans.get(j).add(((LocalDateTime) currCol.get(j)).format(formatter));
                    }
                    else if( currCol.get(j) instanceof Timestamp){
                        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(currCol.get(j));
                        ans.get(j).add((formattedDate));
                    }
                    else{
                        ans.get(j).add(currCol.get(j).toString());
                    }
                }
            }
            return ans;

        }
        /** filter **/
        if(collName.equals("date")){
            DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            filter=out.format(in.parse(filter));
        }
        ResultSet rs=null;
        Result<Record> result=null;
        int numOfCols=0;
        String sql="SELECT * FROM matches WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
        List<List<String>> ans=null;
        try {
            rs=DBHandler.getConnection().createStatement().executeQuery(sql);
            result=DBHandler.getDSLConnect().fetch(rs);
            ResultSetMetaData rsmd=rs.getMetaData();
            numOfCols=rsmd.getColumnCount();
            /** iinitialize List<List<String>> **/
            ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            for(int i=0;i< numOfCols;i++){
                List <?> currCol = result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    if( currCol.get(j) instanceof Timestamp){
                        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(currCol.get(j));
                        ans.get(j).add((formattedDate));
                    }
                    else if(currCol.get(j) instanceof LocalDateTime){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        ans.get(j).add(((LocalDateTime) currCol.get(j)).format(formatter));
                    }
                    else{
                        ans.get(j).add(currCol.get(j).toString());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;

    }

    @Override
    public void save(List<String> strings) throws SQLException {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);
        /** check if key not already exist in DB  **/

        String key0Date=out.format(in.parse(strings.get(0)));
        LocalDateTime dateTime = LocalDateTime.parse(key0Date, out);
        String key1=strings.get(2);
        String key2=strings.get(3);

        Result<Record> isExist = create.select().from(MATCHES)
                .where(MATCHES.DATE.ge(dateTime))
                .and(MATCHES. HOME_TEAM.eq(key1)
                        .and(MATCHES.AWAY_TEAM.eq(key2)))
                .fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{
                /** convert to date format of DB **/
                String matchDate=out.format(in.parse(strings.get(0)));
                LocalDateTime local=LocalDateTime.parse(matchDate,out);
                create.insertInto(MATCHES,
                        MATCHES.DATE
                        ,MATCHES.HOME_TEAM
                        ,MATCHES.AWAY_TEAM
                        ,MATCHES.HOME_SCORE
                        ,MATCHES.AWAY_SCORE
                        ,MATCHES.FIELD
                        ,MATCHES.MAIN_REFEREE
                        ,MATCHES.DURATION_TIME)
                        .values(local,strings.get(1),strings.get(2),Integer.parseInt(strings.get(3)),
                                Integer.parseInt(strings.get(4)),
                                strings.get(5),strings.get(6),Integer.parseInt(strings.get(7)))
                        .execute();

                System.out.println("new row add"); // dell ! !

            }catch (DataAccessException e){
                e.printStackTrace();
                throw new SQLException("added failed, foreign key not exist.");
            }
        }
        else {
            throw new SQLException("added failed, key already exist.");
        }
    }

    @Override
    public void update(List<String> keys, List<String> strings) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String key0Date=out.format(in.parse(keys.get(0)));
        LocalDateTime dateTime = LocalDateTime.parse(key0Date, out);
        String key1=keys.get(1);
        String key2=keys.get(2);

        /** select retrieval row from table  **/
        MatchesRecord matchesRecord=create.selectFrom(MATCHES)
                .where(MATCHES.DATE.eq(dateTime))
                .and(MATCHES.HOME_TEAM.ge(key1))
                .and(MATCHES.AWAY_TEAM.eq(key2))
                .fetchOne();

        /** check if key exist in DB  **/
        if(matchesRecord != null) {
            /**update row in  DB **/
            matchesRecord.setHomeScore(Integer.parseInt(strings.get(0)));;
            matchesRecord.setAwayScore(Integer.parseInt(strings.get(1)));
            matchesRecord.setField(strings.get(2));
            matchesRecord.setMainReferee(strings.get(3));
            matchesRecord.setDurationTime(Integer.parseInt(strings.get(4)));

            try{
                matchesRecord.store();

                /** foreign key not exist  **/
            }catch (DataAccessException e){
                //e.printStackTrace();
                System.out.println("foreign key not exist. need to change it");
            }
        }
    }

    @Override
    public void delete(List<String> strings) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String key0Date=out.format(in.parse(strings.get(0)));
        LocalDateTime dateTime = LocalDateTime.parse(key0Date, out);
        String key1=strings.get(1);
        String key2=strings.get(2);

        create.delete(MATCHES)
                .where(MATCHES.DATE.ge(dateTime))
                .and(MATCHES.HOME_TEAM.eq(key1)
                        .and(MATCHES.AWAY_TEAM.eq(key2)))
                .execute();

    }
}
