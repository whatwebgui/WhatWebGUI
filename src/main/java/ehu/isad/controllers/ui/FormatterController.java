package ehu.isad.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FormatterController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField field;

    @FXML
    private TextArea area;

    @FXML
    private Button btn;

    @FXML
    void onclickScan(ActionEvent event) {
        String newLine = System.getProperty("line.separator");
        area.setText(allProcesses().stream().collect(Collectors.joining(newLine)));
        area.setWrapText(true);
    }

    @FXML
    void initialize() {
        area.setEditable(false);
    }

    public List<String> allProcesses() {
        List<String> processes = new LinkedList<String>();
        try {
            String line;
            Process p = null;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                p = Runtime.getRuntime().exec(System.getenv("wsl whatweb --color=never " + field.getText()));
            } else {
                p = Runtime.getRuntime().exec("whatweb --color=never  " + field.getText());
            }
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) { processes.add(line); }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return processes;
    }

    public void onClickWhatWeb(ActionEvent actionEvent) {
    }

    public void onClickMongo(ActionEvent actionEvent) {
    }
}