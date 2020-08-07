package DataAccess;

import DB.Tables.tables.records.CoachsRecord;
import DB.Tables.tables.records.TeamrolesRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static DB.Tables.Tables.COACHS;
import static DB.Tables.Tables.TEAMROLES;

public class DaoTeamRole implements Dao<String > {
    @Override
    public List<String> get(List<String> keys) throws ParseException {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String Key= keys.get(0);
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        TeamrolesRecord teamrolesRecord=create.selectFrom(TEAMROLES)
                .where(TEAMROLES.USERNAME.eq(Key)).fetchOne();
        /** key noy found in table  **/
        if (teamrolesRecord == null || teamrolesRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <teamrolesRecord.size() ; i++) {
            result.add(teamrolesRecord.get(i).toString());
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
            Result<Record> result=create.select().from(TEAMROLES).fetch();

            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //5!
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
        String sql="SELECT * FROM teamroles WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
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
            //List<?> coll=result.getValues(1);
            //System.out.println(coll.get(1).toString());
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
        String key= strings.get(0);
        Result<Record> isExist = create.select().from(TEAMROLES)
                .where(TEAMROLES.USERNAME.eq(key)).fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{
                /** set null to difots value - 0 **/
                int size=strings.size();
                for (int i = 1; i < size; i++) {
                    if(strings.get(i)==null){
                        strings.set(i,"0");
                    }
                }
                create.insertInto(TEAMROLES,
                        TEAMROLES.USERNAME, TEAMROLES.PLAYER, TEAMROLES.COACH,TEAMROLES.TEAM_OWNER,TEAMROLES.TEAM_MANAGER)
                        .values(key , Byte.valueOf(strings.get(1)), Byte.valueOf(strings.get(2)),Byte.valueOf(strings.get(3)),Byte.valueOf(strings.get(4))).execute();
                            //Byte.valueOf(values.get(0))
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
    public void update(List<String> keys, List<String> values) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String key= keys.get(0);
        /** select retrieval row from table  **/
        TeamrolesRecord teamrolesRecord =create.selectFrom(TEAMROLES)
                .where(TEAMROLES.USERNAME.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(teamrolesRecord != null) {
            /** set null to difots value - 0 **/
            int size=values.size();
            for (int i = 0; i < size; i++) {
                if(values.get(i)==null){
                    values.add(i,"0");
                }
            }
            /**update row in  DB **/
            teamrolesRecord.setPlayer(Byte.valueOf(values.get(0)));
            teamrolesRecord.setCoach(Byte.valueOf(values.get(1)));
            teamrolesRecord.setTeamOwner(Byte.valueOf(values.get(2)));
            teamrolesRecord.setTeamManager(Byte.valueOf(values.get(3)));
            try{
                teamrolesRecord.store();

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

        create.delete(TEAMROLES)
                .where(TEAMROLES.USERNAME.eq(key))
                .execute();

    }
}
