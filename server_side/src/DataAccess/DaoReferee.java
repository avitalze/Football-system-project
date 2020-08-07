package DataAccess;

import DB.Tables.tables.records.CoachsRecord;
import DB.Tables.tables.records.RefereesRecord;
import Domain.Users.Referee;
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
import static DB.Tables.Tables.REFEREES;

public class DaoReferee implements Dao<String>{
    @Override
    public List<String> get(List<String> keys) throws ParseException {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String Key= keys.get(0);
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        RefereesRecord refereesRecord=create.selectFrom(REFEREES)
                .where(REFEREES.USERNAME.eq(Key)).fetchOne();
        /** key noy found in table  **/
        if (refereesRecord == null || refereesRecord.size()==0){
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <refereesRecord.size() ; i++) {
            if(refereesRecord.get(i) instanceof String){
                result.add(refereesRecord.get(i).toString());
            }
        }
        result=insertNull(result);
        return result;
    }

    public List<String> insertNull(List<String> list){
        if(list.size()==1){
            list.add(null);
        }
        return list;
    }

    @Override
    public List<List<String>> getAll(String collName, String filter) {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        if( collName==null && filter==null){
            /** return all rows in table **/
            Result<Record> result=create.select().from(REFEREES).fetch();

            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //2 !
            for(int i=0;i< numOfCols;i++){
                List <String> currCol = (List<String>)result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    ans.get(j).add(currCol.get(j));

                }
            }
            // insert null for difult
            for (int i = 0; i < ans.size(); i++) {
                ans.set(i,insertNull(ans.get(i)));
            }
            return ans;

        }
        /** fiLter **/
        ResultSet rs=null;
        Result<Record> result=null;
        int numOfCols=0;
        String sql="SELECT * FROM referees WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
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
        // insert null for difult
        for (int i = 0; i < ans.size(); i++) {
            ans.set(i,insertNull(ans.get(i)));
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
        Result<Record> isExist = create.select().from(REFEREES)
                .where(REFEREES.USERNAME.eq(key)).fetch();

        if(isExist.size()== 0){ // key not exist
            /**add new row to DB **/
            try{
                strings=insertNull(strings);
                create.insertInto(REFEREES,
                        REFEREES.USERNAME, REFEREES.QUALIFICATION)
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

        String key= keys.get(0);
        /** select retrieval row from table  **/
        RefereesRecord refereesRecord =create.selectFrom(REFEREES)
                .where(REFEREES.USERNAME.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(refereesRecord != null) {

            /**update row in  DB **/
            refereesRecord.setQualification(string.get(0));
            try{
                refereesRecord.store();

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

        create.delete(REFEREES)
                .where(REFEREES.USERNAME.eq(key))
                .execute();


    }
}
