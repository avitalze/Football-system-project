package DataAccess;

import DB.Tables.tables.records.FansRecord;
import DB.Tables.tables.records.TeamsRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static DB.Tables.Tables.FANS;
import static DB.Tables.Tables.TEAMS;

public class DaoTeams implements Dao<String> {
    @Override
    public List<String> get(List<String> keys)throws ParseException {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String fanKey= keys.get(0);
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        TeamsRecord teamsRecord =create.selectFrom(TEAMS)
                .where(TEAMS.TEAMNAME.eq(fanKey)).fetchOne();
        /** key noy found in table  **/
        if (teamsRecord== null || teamsRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }


        for (int i = 0; i <teamsRecord.size() ; i++) {
            String temp=teamsRecord.get(i).toString();//!!

            if(teamsRecord.get(i).toString().equals("") || teamsRecord.get(i).toString().equals(null)){
                result.add(null);
            }
                result.add(teamsRecord.get(i).toString());
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
            Result<Record> result=create.select().from(TEAMS).fetch();
            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //6 !
            for(int i=0;i< numOfCols;i++){
                List <?> currCol = result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    if(currCol.get(j).toString().equals("")){
                        ans.get(j).add(null);
                    }
                    else{
                        ans.get(j).add(currCol.get(j).toString());
                    }
                }
            }
            return ans;
        }
        /** do filter **/

        ResultSet rs=null;
        Result<Record> result=null;
        ResultSetMetaData rsmd=null;
        int numOfCols=0;
        List<List<String>> ans=null;
        String sql="SELECT * FROM teams WHERE "+collName+"= '" + filter + "'";
        try {
            rs=DBHandler.getConnection().createStatement().executeQuery(sql);
            result=DBHandler.getDSLConnect().fetch(rs);
            rsmd=rs.getMetaData();
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
                    if(currCol.get(j).equals("")){
                        ans.get(j).add(null);
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
        String key= strings.get(0);
        Result<Record> isExist = create.select().from(TEAMS)
                .where(TEAMS.TEAMNAME.eq(key)).fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{
                /** set null to difots value - 0 **/
                int size=strings.size();
                for (int i = 1; i < size; i++) {
                    if(strings.get(i)==null){
                        strings.set(i,"");
                    }
                }

                create.insertInto(TEAMS,
                        TEAMS.TEAMNAME,
                        TEAMS.TEAMMANAGER,
                        TEAMS.FOUNDER,
                        TEAMS.COACH,
                        TEAMS.FIELD,
                        TEAMS.SCORE)
                        .values(key , strings.get(1), strings.get(2),strings.get(3)
                                ,strings.get(4),Integer.parseInt(strings.get(5)))
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

        String key= keys.get(0);
        /** select retrieval row from table  **/
        TeamsRecord teamsRecord=create.selectFrom(TEAMS)
                .where(TEAMS.TEAMNAME.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(teamsRecord != null) {

            /**update row in  DB **/
            teamsRecord.setTeammanager(strings.get(0));
            teamsRecord.setFounder(strings.get(1));
            teamsRecord.setCoach(strings.get(2));
            teamsRecord.setField(strings.get(3));
            teamsRecord.setScore(Integer.parseInt(strings.get(4)));

            try{
                teamsRecord.store();

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
        String key= strings.get(0);

        create.delete(TEAMS)
                .where(TEAMS.TEAMNAME.eq(key))
                .execute();


    }
}
