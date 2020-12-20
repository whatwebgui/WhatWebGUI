package ehu.isad.controllers.db;

import org.sqlite.core.DB;

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
        String query = "select count(*) as sum from history where date like '%" + Calendar.getInstance().get(Calendar.YEAR)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+i+" %'";
        ResultSet rs = db.execSQL(query);
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
}
