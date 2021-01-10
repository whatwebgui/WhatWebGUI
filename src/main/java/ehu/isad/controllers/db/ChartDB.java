package ehu.isad.controllers.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartDB {
    private static final ChartDB instance = new ChartDB();
    private static final ChartDB controller = new ChartDB();
    private static final DBController db = DBController.getController();
    public static ChartDB getInstance() {
        return instance;
    }
    private ChartDB() {}
    public int getHowMany(Integer i) {
        ResultSet rs = db.execSQL(howManyQuery(i));
        try {
            rs.next();
            return rs.getInt("sum");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
    public ArrayList<String> cmsTypes() throws SQLException {
        ArrayList<String >cms = new ArrayList<>();
        String query = "";
        ResultSet rs = db.execSQL(query);
        while(rs.next()) cms.add(rs.getString("cms"));
        return cms;
    }

    public double getCount(String d) throws SQLException {
        String query = "";
        ResultSet rs = db.execSQL(query);
        rs.next();
        return rs.getInt("sum");
    }

    private String howManyQuery(Integer k) {
        if (k < 10 && Calendar.getInstance().get(Calendar.MONTH + 1) < 10) {
            return "select count(*) as sum from history where date like '%" + Calendar.getInstance().get(Calendar.YEAR)+"-0"+(Calendar.getInstance().get(Calendar.MONTH+1))+"-0"+k+"%'";
        } else if (k < 10) {
            return "select count(*) as sum from history where date like '%" + Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH+1))+"-0"+k+"%'";
        } else if (Calendar.getInstance().get(Calendar.MONTH + 1) < 10) {
            return "select count(*) as sum from history where date like '%" + Calendar.getInstance().get(Calendar.YEAR)+"-0"+(Calendar.getInstance().get(Calendar.MONTH+1))+"-"+k+"%'";
        } else {
            return "select count(*) as sum from history where date like '%" + Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH+1))+"-"+k+"%'";
        }
    }
}
