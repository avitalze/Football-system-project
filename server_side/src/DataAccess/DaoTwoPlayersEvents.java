package DataAccess;

import DB.Tables.tables.records.EventsRecord;
import DB.Tables.tables.records.EventsTwoPlayersRecord;
import DB.Tables.tables.records.FanMatchesFollowRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import java.security.Key;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static DB.Tables.Tables.*;
import static DB.Tables.Tables.EVENTS;

/**
 * table name : events_two_players
 * 1 keys!
 * **/

public class DaoTwoPlayersEvents implements Dao<String> {
    @Override
    public List<String> get(List<String> keys) throws ParseException{
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();

        int key=Integer.parseInt(keys.get(0)) ;

        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        EventsTwoPlayersRecord eventsTwoPlayersRecord =create.selectFrom(EVENTS_TWO_PLAYERS)
                .where(EVENTS_TWO_PLAYERS.EVENT_ID.eq(key))
                .fetchOne();

        /** key not found in table  **/
        if (eventsTwoPlayersRecord == null || eventsTwoPlayersRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <eventsTwoPlayersRecord.size() ; i++) {
            result.add(eventsTwoPlayersRecord.get(i).toString());
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
            Result<Record> result=create.select().from(EVENTS_TWO_PLAYERS).fetch();

            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //3!
            for(int i=0;i< numOfCols;i++){
                List <?> currCol = result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                        ans.get(j).add(currCol.get(j).toString());
                }
            }
            return ans;
        }
        /** filter **/
        ResultSet rs=null;
        Result<Record> result=null;
        int numOfCols=0;
        String sql="SELECT * FROM events_two_players WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
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
                        ans.get(j).add(currCol.get(j).toString());
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
        Result<Record> isExist = create.select().from(EVENTS_TWO_PLAYERS)
                .where(EVENTS_TWO_PLAYERS.EVENT_ID.eq(key)).fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{

                create.insertInto(EVENTS_TWO_PLAYERS,
                        EVENTS_TWO_PLAYERS.EVENT_ID, EVENTS_TWO_PLAYERS.PLAYER1, EVENTS_TWO_PLAYERS.PLAYER2)
                        .values(key , strings.get(1),strings.get(2)).execute();

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
        EventsTwoPlayersRecord eventsTwoPlayersRecord=create.selectFrom(EVENTS_TWO_PLAYERS)
                .where(EVENTS_TWO_PLAYERS.EVENT_ID.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(eventsTwoPlayersRecord != null) {

            /**update row in  DB **/
            eventsTwoPlayersRecord.setPlayer1(strings.get(0));
            eventsTwoPlayersRecord.setPlayer2(strings.get(1));

            try{
                eventsTwoPlayersRecord.store();

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

        create.delete(EVENTS_TWO_PLAYERS)
                .where(EVENTS_TWO_PLAYERS.EVENT_ID.eq(key))
                .execute();

    }
}
