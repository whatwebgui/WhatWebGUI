package ehu.isad.controllers.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    @FXML
    private PieChart serverC;

    @FXML
    private LineChart<String, Number> scanC;

    @FXML
    private PieChart cmsC;

    private ChartController(){}
    private static ChartController instance = new ChartController();
    public static ChartController getInstance() { return instance; }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.chartSetup();
    }

    private void chartSetup() {

    }
}
