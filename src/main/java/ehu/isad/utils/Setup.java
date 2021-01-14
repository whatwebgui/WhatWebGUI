package ehu.isad.utils;

import ehu.isad.controllers.db.DBController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Setup {

    public static String[] getList(String type) {
        String query = "select name from plugins where type='"+ type +"'";
        ResultSet rs = DBController.getController().execSQL(query);
        var plugins = new ArrayList<String>();
        try {
            while (rs.next()) {
                plugins.add(rs.getString("name"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return plugins.toArray(new String[plugins.size()]);
    }

    public static String[] getList() {
        String[] firstArray = getList("cms");
        String[] secondArray = getList("server");
        int fal = firstArray.length;
        int sal = secondArray.length;
        String[] result = new String[fal + sal];
        System.arraycopy(firstArray, 0, result, 0, fal);
        System.arraycopy(secondArray, 0, result, fal, sal);
        return result;
    }



}
