package Domain.Controllers;

import DataAccess.*;
import Domain.MainSystem;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DbMainTemp {



    public static void main(String[] args) throws Exception {

        DBHandler.conectToDB();
        DBHandler.conectToDB();
        //DaoRfa daoRfa=new DaoRfa();
        DaoFans daoFans=new DaoFans();
        List <String> k=new LinkedList<>();
        List <String> values=new LinkedList<>();
        List <String> newRow=new LinkedList<>();

        //k.add("01-06-2020 20:00:00");
        k.add("shimi");
        k.add("macabi hifa");
        k.add("hapoel TLV");
        k.add("or");
        k.add("The match time between macabi TLV to hapoel TLV changed to 2020-06-01");
        //The match time between macabi TLV to hapoel TLV changed to 2020-06-01
//        k.add("03-06-2020 20:00:00");
//        k.add("hapoel TLV");
//        k.add("macabi TLV");
//        k.add("or");
//        k.add("5");



//        values.add("player32");
//        values.add("player22");
//        values.add("gini999bini");
//        values.add("097827837");
//        values.add("gini@gmail");
//        values.add("03-06-2020");
//        values.add("SchedualeOption2");
//        values.add("CalculateOption1");
//        values.add("0");


        values.add("shimi");
        values.add("0");
//        values.add("90");
//        values.add("bashField2");
//        values.add("or");
//        values.add("10");

        //newRow.add("adi");
        newRow.add("01-06-2020 20:00:00");
        newRow.add("macabi TLV");
        newRow.add("hapoel TLV");
        newRow.add("or");
        newRow.add("lalallalalal");
        newRow.add("1");



        DaoFans x=new DaoFans();
//        x.get(k);
//        x.get(values);
        List<List<String>> ans= x.getAll(null,null);
        ans=  x.getAll("readed","1");
       // x.save(newRow);
        x.update(newRow,values);
        x.delete(newRow);



    }
}
