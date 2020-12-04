package ehu.isad.controllers.db;

import ehu.isad.model.HistoryModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryDB {

    private static final HistoryDB instance = new HistoryDB();
    private static final DBController dbcontroller = DBController.getController();

    private HistoryDB() {   }

    public static HistoryDB getInstance() {
        return instance;
    }

    public void addToHistoryDB(String target, String tabformat){
        String query = "INSERT INTO history(target,tab,date) VALUES ('"+target+"','"+tabformat+"',DATETIME())";
        dbcontroller.execSQL(query);
    }

    public List<HistoryModel> getFromHistoryDB(){
        String query = "SELECT target,tab,date FROM history ORDER BY id DESC";
        List<HistoryModel> list = new ArrayList<>();
        ResultSet rs = dbcontroller.execSQL(query);
        try {
            while (rs.next()) {
                String target = rs.getString("target");
                String tab = rs.getString("tab");
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("date"));
                list.add(new HistoryModel(target,tab,date));
            }
        } catch(SQLException | ParseException e){
            e.printStackTrace();
        }
        return list;
    }
}
