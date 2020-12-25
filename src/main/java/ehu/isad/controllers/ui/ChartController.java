package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.ChartDB;
import ehu.isad.controllers.db.DBController;
import ehu.isad.model.ServerCMSModel;
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

    private ObservableList<PieChart.Data> listC = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> listS = FXCollections.observableArrayList();

    private ServerCMSController serverCMSController = ServerCMSController.getInstance();
    private ChartController(){}
    private static ChartController instance = new ChartController();
    public static ChartController getInstance() { return instance; }

    public void uploadCharts(){
        chartSetup();
        pieChartSetup();
    }

    private void pieChartSetup(){
        listC.clear();
        listS.clear();
        ObservableList<ServerCMSModel> list = serverCMSController.getServerCMSList();
        ServerCMSModel model = null;
        for(int i=0; i<list.size(); i++){
            model = list.get(i);
            addCMS(model);
            addServer(model);
        }
        cmsC.setData(listC);
        serverC.setData(listS);
        cmsC.setTitle("Cms types");
        cmsC.setLabelLineLength(1);
        serverC.setTitle("Server types");
        serverC.setLabelLineLength(1);
    }

    private void addCMS(ServerCMSModel model){
        boolean finish = false;
        int i = 0;
        PieChart.Data data = null;
        if(listC.isEmpty()){
            listC.add(new PieChart.Data(model.getCms(),1));
        }else{
            while(!finish && i < listC.size()){
                data = listC.get(i);
                if(data.getName().equals(model.getCms())){
                    finish=true;
                }
                i++;
            }
            if(!finish)listC.add(new PieChart.Data(model.getCms(),1));
            else{
                listC.remove(data);
                listC.add(new PieChart.Data(data.getName(),data.getPieValue()+1));
            }
        }
    }

    private void addServer(ServerCMSModel model){
        boolean finish = false;
        int i = 0;
        PieChart.Data data = null;
        if(listS.isEmpty()){
            listS.add(new PieChart.Data(model.getServer(), 1));
        }else{
            while(!finish && i < listS.size()){
                data = listS.get(i);
                if(data.getName().equals(model.getServer())){
                    finish=true;
                }
                i++;
            }
            if(!finish)listS.add(new PieChart.Data(model.getServer(),1));
            else{
                listS.remove(data);
                listS.add(new PieChart.Data(data.getName(),data.getPieValue()+1));
            }
        }
    }

    public void clearCharts(){
        listS.clear();
        listC.clear();
        chartSetup();
        pieChartSetup();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.chartSetup();
        pieChartSetup();
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
