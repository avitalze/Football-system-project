package DataAccess;

import DB.Tables.tables.records.SeasonsRecord;
import DB.Tables.tables.records.TeamrolesRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static DB.Tables.Tables.*;

public class DaoSeasons implements Dao<String>{


    @Override
    public List<String> get(List<String> keys) throws ParseException {

        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String Key= keys.get(0);
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        SeasonsRecord seasonsRecord =create.selectFrom(SEASONS)
                .where(SEASONS.YEAR.eq(Key)).fetchOne();
        /** key noy found in table  **/
        if (seasonsRecord == null || seasonsRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <seasonsRecord.size() ; i++) {
            result.add(seasonsRecord.get(i).toString());
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
            Result<Record> result=create.select().from(SEASONS).fetch();

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
                    ans.get(j).add(currCol.get(j).toString());
                }
            }
            return ans;

        }
        /** filter **/
        ResultSet rs=null;
        Result<Record> result=null;
        int numOfCols=0;
        String sql="SELECT * FROM seasons WHERE "+collName+"= '" + filter + "'"; //!!!!!!!!!!!!!!!!!!!!!!!!!
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
        Result<Record> isExist = create.select().from(SEASONS)
                .where(SEASONS.YEAR.eq(key)).fetch();

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
                create.insertInto(SEASONS,
                        SEASONS.YEAR, SEASONS.SCHEDULINGPOLICY, SEASONS.CALCULATIONPOLICY,SEASONS.CURRENT_SEASON)
                        .values(key , strings.get(1), strings.get(2),Byte.valueOf(strings.get(3))).execute();
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
        SeasonsRecord seasonsRecord=create.selectFrom(SEASONS)
                .where(SEASONS.YEAR.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(seasonsRecord != null) {
            /** set null to difots value - 0 **/
            int size=values.size();
            for (int i = 0; i < size; i++) {
                if(values.get(i)==null){
                    values.add(i,"0");
                }
            }
            /**update row in  DB **/
            seasonsRecord.setSchedulingpolicy(values.get(0));
            seasonsRecord.setCalculationpolicy(values.get(1));
            seasonsRecord.setCurrentSeason(Byte.valueOf(values.get(2)));

            try{
                seasonsRecord.store();

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

        create.delete(SEASONS)
                .where(SEASONS.YEAR.eq(key))
                .execute();


    }


}
