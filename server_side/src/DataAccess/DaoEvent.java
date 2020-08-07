package DataAccess;

import DB.Tables.tables.Events;
import DB.Tables.tables.records.EventsRecord;
import DB.Tables.tables.records.TeamrolesRecord;
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
import java.util.Optional;

import static DB.Tables.Tables.EVENTS;
import static DB.Tables.Tables.TEAMROLES;

public class DaoEvent implements Dao<String> {


    @Override
    public List<String> get(List<String> keys) throws ParseException{
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        int key=Integer.parseInt(keys.get(0)) ;
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        EventsRecord eventsRecord =create.selectFrom(EVENTS) // ##
                .where(EVENTS.EVENT_ID.eq(key)).fetchOne();
        /** key noy found in table  **/
        if (eventsRecord == null || eventsRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <eventsRecord.size() ; i++) {
            if(eventsRecord.get(i) instanceof LocalDateTime){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                result.add(((LocalDateTime) eventsRecord.get(i)).format(formatter));
                //dd-MM-yyyy HH:mm:ss
            }
            else{
                result.add(eventsRecord.get(i).toString());
            }
        }
        return result;
    }

    @Override
    public List<List<String>> getAll(String collName, String filter) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        if( collName==null && filter==null){
            /** return all rows in table **/
            Result<Record> result=create.select().from(EVENTS).fetch(); //##

            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //8!
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
        if(collName.equals("date") || collName.equals("match_date")){
            DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            filter=out.format(in.parse(filter));
            //outSDF.format(inSDF.parse(startDate))
        }

        ResultSet rs=null;
        Result<Record> result=null;
        int numOfCols=0;
        String sql="SELECT * FROM events WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
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
                    if(currCol.get(j) instanceof  LocalDateTime){
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
        int key= Integer.parseInt(strings.get(0));
        Result<Record> isExist = create.select().from(EVENTS)
                .where(EVENTS.EVENT_ID.eq(key)).fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{
                /** convert to date format of DB **/
                DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                //filter=out.format(in.parse(filter));
                String eventDate=out.format(in.parse(strings.get(1)));
                String matchDate=out.format(in.parse(strings.get(3)));
                int minInMtach=Integer.parseInt(strings.get(7));

                create.insertInto(EVENTS,
                        EVENTS.EVENT_ID, EVENTS.DATE, EVENTS.REFEREE
                        ,EVENTS.MATCH_DATE,EVENTS.HOME_TEAM_MATCH,
                        EVENTS.AWAY_TEAM_MATCH,EVENTS.NAME, EVENTS.MINUTE_IN_MATCH)
                        .values(key , LocalDateTime.parse(eventDate,out), strings.get(2)
                                ,LocalDateTime.parse(matchDate,out),strings.get(4),strings.get(5),
                                strings.get(6),minInMtach).execute();
                //LocalDateTime.parse(str, formatter);
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

        int key= Integer.parseInt(keys.get(0));
        /** select retrieval row from table  **/
        EventsRecord eventsRecord=create.selectFrom(EVENTS)
                .where(EVENTS.EVENT_ID.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(eventsRecord != null) {
            /** convert to date format of DB **/
            DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            //filter=out.format(in.parse(filter));
            String eventDate=out.format(in.parse(strings.get(0)));
            String matchDate=out.format(in.parse(strings.get(2)));
            int minInMtach=Integer.parseInt(strings.get(6));
            /**update row in  DB **/
            eventsRecord.setDate(LocalDateTime.parse(eventDate,out));
            eventsRecord.setReferee(strings.get(1));
            eventsRecord.setMatchDate(LocalDateTime.parse(matchDate,out));
            eventsRecord.setHomeTeamMatch(strings.get(3));
            eventsRecord.setAwayTeamMatch(strings.get(4));
            eventsRecord.setName(strings.get(5));
            eventsRecord.setMinuteInMatch(minInMtach);

            try{
                eventsRecord.store();

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
        int key=Integer.parseInt(strings.get(0)) ;

        create.delete(EVENTS)
                .where(EVENTS.EVENT_ID.eq(key))
                .execute();

    }

    public int getMaxEventId () {
        int max=0;
        DSLContext create = DBHandler.getDSLConnect();

        /** return all rows in table **/
        Result<Record> result=create.select().from(EVENTS).fetch(); //##

        /** insert coll values to ans  **/
        int numOfCols=result.fields().length; //8!
            List <?> currCol = result.getValues(0);
            for (int j = 0; j <result.size() ; j++) {
                   if(Integer.parseInt(String.valueOf((Integer)currCol.get(j)))>max){
                       max=Integer.parseInt(String.valueOf((Integer)currCol.get(j)));
                   }
            }

        return max;

    }
}
