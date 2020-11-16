package ehu.isad.controllers.db;

public class WhatWebDB {

    private static WhatWebDB instance = new WhatWebDB();
    private static DBController dbcontroller = DBController.getController();

    private WhatWebDB() {
    }

    public static WhatWebDB getInstance() {
        return instance;
    }

    public void clearDB(){
        String del1 = "DELETE FROM ";
        String del2 = "DELETE FROM sqlite_sequence WHERE name = '";
        String[] tables = {"scans","targets"};
        for (String table : tables) {
            dbcontroller.execSQL(del1+table);
            dbcontroller.execSQL(del2+table+"';");
        }
    }

}
