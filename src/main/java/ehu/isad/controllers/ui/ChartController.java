package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.ChartDB;
import ehu.isad.controllers.db.DBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    @FXML
    private PieChart serverC;

    @FXML
    private LineChart<String, Integer> scanC;

    @FXML
    private PieChart cmsC;

    private XYChart.Series series;

    private ChartController(){}
    private static ChartController instance = new ChartController();
    public static ChartController getInstance() { return instance; }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.chartSetup();
        this.serverSetup();
        try {
            this.cmsSetup();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void cmsSetup() throws SQLException {
        //Metodo para saber que tipos de cms aparecen
        //ArrayList<String> data = ChartDB.getInstance().cmsTypes();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        //for(String d:data){
           // pieChartData.add(new PieChart.Data(d, ChartDB.getInstance().getCount(d)));
        //}
        cmsC.setData( FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30)));
        cmsC.setTitle("Cms types");
        cmsC.setLabelLineLength(1);

    }

    private void serverSetup() {
        serverC.setData( FXCollections.observableArrayList(
                new PieChart.Data("Grapefruit", 13),
                new PieChart.Data("Oranges", 25),
                new PieChart.Data("Plums", 10),
                new PieChart.Data("Pears", 22),
                new PieChart.Data("Apples", 30)));
        serverC.setTitle("Server types");
        serverC.setLabelLineLength(1);

    }

    public void chartSetup() {
        scanC.setCreateSymbols(false);
        ChartDB chartDB = ChartDB.getInstance();
        scanC.setTitle("Daily usage");
        XYChart.Series series = new XYChart.Series();
        YearMonth yearMonthObject = YearMonth.of(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH));
        int i=1;
        scanC.getData().clear();
        while(i<= yearMonthObject.lengthOfMonth()){
            series.getData().add(new XYChart.Data(Integer.toString(i),chartDB.getHowMany(i)));
            i++;
        }
        scanC.getData().add(series);
    }
}
