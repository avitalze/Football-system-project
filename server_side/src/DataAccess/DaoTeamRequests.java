package DataAccess;

import DB.Tables.tables.records.CoachsRecord;
import DB.Tables.tables.records.TeamOwnerRequestsRecord;
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
import static DB.Tables.Tables.TEAM_OWNER_REQUESTS;

/** table name: team_owner_requests **/

/** key - 2 values**/
public class DaoTeamRequests implements Dao<String> {
    @Override
    public List<String> get(List<String> keys) throws ParseException{
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();
        String key1= keys.get(0);
        String key2=keys.get(1);
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        TeamOwnerRequestsRecord teamOwnerRequestsRecord =create.selectFrom(TEAM_OWNER_REQUESTS)
                .where(TEAM_OWNER_REQUESTS.TEAM_OWNER.eq(key1))
                .and(TEAM_OWNER_REQUESTS.TEAM_NAME.eq(key2))
                .fetchOne();
        /** key noy found in table  **/
        if (teamOwnerRequestsRecord == null || teamOwnerRequestsRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <teamOwnerRequestsRecord.size() ; i++) {
            result.add(teamOwnerRequestsRecord.get(i).toString());
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
            Result<Record> result=create.select().from(TEAM_OWNER_REQUESTS).fetch();

            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //2!
            for(int i=0;i< numOfCols;i++){
                List <String> currCol = (List<String>)result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    ans.get(j).add(currCol.get(j));

                }
            }
            return ans;

        }
        /** filter **/
        ResultSet rs=null;
        Result<Record> result=null;
        int numOfCols=0;
        String sql="SELECT * FROM team_owner_requests WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
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
                List <String> currCol = (List<String>)result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    ans.get(j).add(currCol.get(j));

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
        String key0= strings.get(0);
        String key1= strings.get(1);
        Result<Record> isExist = create.select().from(TEAM_OWNER_REQUESTS)
                .where(TEAM_OWNER_REQUESTS.TEAM_OWNER.eq(key0))
                .and(TEAM_OWNER_REQUESTS.TEAM_NAME.eq(key1))
                .fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{
                create.insertInto(TEAM_OWNER_REQUESTS,
                        TEAM_OWNER_REQUESTS.TEAM_OWNER, TEAM_OWNER_REQUESTS.TEAM_NAME)
                        .values(strings.get(0), strings.get(1)).execute();

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
    public void update(List<String> keys, List<String> string) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String key0= keys.get(0);
        String key1= keys.get(1);
        /** select retrieval row from table  **/
        TeamOwnerRequestsRecord teamOwnerRequestsRecord=create.selectFrom(TEAM_OWNER_REQUESTS)
                .where(TEAM_OWNER_REQUESTS.TEAM_OWNER.eq(key0))
                .and(TEAM_OWNER_REQUESTS.TEAM_NAME.eq(key1))
                .fetchOne();

        /** check if key exist in DB  **/
        if(teamOwnerRequestsRecord != null) {

            /**update row in  DB **/
            teamOwnerRequestsRecord.setTeamOwner(key0);;
            teamOwnerRequestsRecord.setTeamName(key1);
            try{
                teamOwnerRequestsRecord.store();

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
        String key0= strings.get(0);
        String key1= strings.get(1);

        create.delete(TEAM_OWNER_REQUESTS)
                .where(TEAM_OWNER_REQUESTS.TEAM_OWNER.eq(key0))
                .and(TEAM_OWNER_REQUESTS.TEAM_NAME.eq(key1))
                .execute();

    }
}
