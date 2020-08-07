package DataAccess;

import DB.Tables.tables.records.EventsRecord;
import DB.Tables.tables.records.FansRecord;
import DB.Tables.tables.records.RfasRecord;
import Domain.MainSystem;
import org.jooq.*;
import org.jooq.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static DB.Tables.Tables.*;
import static DB.Tables.Tables.EVENTS;

public class DaoFans implements Dao<String> {



    @Override
    public List<String > get(List<String> keys) throws ParseException {
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        String fanKey= keys.get(0);
        List<String> result=new LinkedList<>();
        /** select retrieval row from table  **/
        FansRecord fansRecord =create.selectFrom(FANS)
                .where(FANS.USERNAME.eq(fanKey)).fetchOne();
        /** key noy found in table  **/
        if (fansRecord == null || fansRecord.size()==0){
            //return null;
            throw new ParseException("key noy found in table",0);
        }

        for (int i = 0; i <fansRecord.size() ; i++) {
            if(fansRecord.get(i) instanceof LocalDate){
//                String dateFormat= DBHandler.convertToDateFormat(fansRecord.getBirthday().toString());
//                result.add(dateFormat);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                result.add(((LocalDate) fansRecord.get(i)).format(formatter));
            }
            else{
                result.add(fansRecord.get(i).toString());
            }
        }
        return result;
    }


    @Override
    public List<List<String>> getAll(String collName, String filter){
        /** check connection to DB  **/
        DBHandler.conectToDB();
        DSLContext create = DBHandler.getDSLConnect();//DSL.using(connection, SQLDialect.MARIADB);

        if( collName==null && filter==null){
            /** return all rows in table **/
            Result<Record> result=create.select().from(FANS).fetch();
            /** iinitialize List<List<String>> **/
            List<List<String>> ans=new ArrayList<>(result.size());
            for(int i=0; i<result.size(); i++){
                List<String> temp = new LinkedList<>();
                ans.add(temp);
            }
            /** insert coll values to ans  **/
            int numOfCols=result.fields().length; //7 !
            for(int i=0;i< numOfCols;i++){
                List <?> currCol = result.getValues(i);
                for (int j = 0; j <result.size() ; j++) {
                    if(currCol.get(j) instanceof LocalDate){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        ans.get(j).add(((LocalDate) currCol.get(j)).format(formatter));
                    }
                    else{
                        ans.get(j).add(currCol.get(j).toString());
                    }
                }
            }
            return ans;
        }
        /** do filter **/
        if(collName.equals("birthday")){
            DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy");
            filter=out.format(in.parse(filter));
        }

        ResultSet rs=null;
        Result<Record> result=null;
        ResultSetMetaData rsmd=null;
        int numOfCols=0;
        List<List<String>> ans=null;
        String sql="SELECT * FROM fans WHERE "+collName+"= '" + filter + "'";
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
                    if(currCol.get(j) instanceof Date){
                        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                        //String s = formatter.format(currCol.get(j));
                        ans.get(j).add(formatter.format(currCol.get(j)));
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
        Result<Record> isExist = create.select().from(FANS)
                .where(FANS.USERNAME.eq(key)).fetch();

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
                /** convert to date format of DB **/
                DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String birthday=out.format(in.parse(strings.get(5)));

                create.insertInto(FANS,
                        FANS.USERNAME,
                        FANS.FULLNAME,
                        FANS.PASSWORD_HASH,
                        FANS.PHONENUMBER,
                        FANS.EMAIL,
                        FANS.BIRTHDAY,
                        FANS.TO_EMAIL)
                        .values(key , strings.get(1), strings.get(2),strings.get(3)
                                ,strings.get(4),LocalDate.parse(birthday,out), Byte.valueOf(strings.get(6)))
                        .execute();
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

        String key= keys.get(0);
        /** select retrieval row from table  **/
        FansRecord fansRecord=create.selectFrom(FANS)
                .where(FANS.USERNAME.eq(key)).fetchOne();

        /** check if key exist in DB  **/
        if(fansRecord != null) {
            /** convert to date format of DB **/
            DateTimeFormatter out = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter in= DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String birthdate=out.format(in.parse(strings.get(4)));

            /**update row in  DB **/
            fansRecord.setFullname(strings.get(0));
            fansRecord.setPasswordHash(strings.get(1));
            fansRecord.setPhonenumber(strings.get(2));
            fansRecord.setEmail(strings.get(3));
            fansRecord.setBirthday(LocalDate.parse(birthdate,out));
            fansRecord.setToEmail(Byte.valueOf(strings.get(5)));
            try{
                fansRecord.store();

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

        create.delete(FANS)
                .where(FANS.USERNAME.eq(key))
                .execute();


    }
}
