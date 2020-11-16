package ehu.isad.controllers.ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.*;
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
    private Pane pane3;

    @FXML
    private TextField textFieldWW;

    @FXML
    private TextArea textAreaWW;

    @FXML
    private Button scanBtnWW;

    @FXML
    private TextField textFieldMongo;

    @FXML
    private TextArea textAreaMongo;

    @FXML
    private Button scanBtnMongo;

    @FXML
    private TextField textFieldXML;

    @FXML
    private TextArea textAreaXML;

    @FXML
    private Button scanBtnXML;

    @FXML
    void onClickScanXML(ActionEvent event) {
        String newLine = System.getProperty("line.separator");
        textAreaXML.setText(executeCommand("xml").stream().collect(Collectors.joining(newLine)));
        textAreaXML.setWrapText(true);
    }

    @FXML
    void onClickScanMongo(ActionEvent event) {
        String newLine = System.getProperty("line.separator");
        textAreaMongo.setText(executeCommand("mongo").stream().collect(Collectors.joining(newLine)));
        textAreaMongo.setWrapText(true);
    }

    @FXML
    void onClickScanWW(ActionEvent event) {
        String newLine = System.getProperty("line.separator");
        textAreaWW.setText(executeCommand("whatweb").stream().collect(Collectors.joining(newLine)));
        textAreaWW.setWrapText(true);
    }

    public List<String> executeCommand(String mota) {
        List<String> emaitza = new LinkedList<String>();
        try {
            String line;
            Process p = null;
            if(mota.equals("whatweb")){
                p = commandWhatWeb();
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) { emaitza.add(line); }
                input.close();
            }else{
                if(mota.equals("mongo")){
                    p = commandMongo();
                }else {
                    p = commandXML();
                }
                Thread.sleep(8500); // FIXME: wait for thread
                BufferedReader input = new BufferedReader(new FileReader("/tmp/b.json"));
                while ((line = input.readLine()) != null) { emaitza.add(line); }
                input.close();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return emaitza;
    }

    private Process commandWhatWeb() throws IOException {
        Process p = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            p = Runtime.getRuntime().exec(System.getenv("wsl whatweb --color=never " + textFieldWW.getText()));
        } else {
            p = Runtime.getRuntime().exec("whatweb --color=never  " + textFieldWW.getText());
        }
        return p;
    }

    private Process commandMongo() throws IOException {//setup.properties-en jarri path
        Process p = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            p = Runtime.getRuntime().exec(System.getenv("wsl whatweb --color=never --log-json=directoriowindows/b.json " + textFieldMongo.getText()));
        } else {
            p = Runtime.getRuntime().exec("whatweb --color=never --log-json=/tmp/b.json " + textFieldMongo.getText());
        }
        return p;
    }

    private Process commandXML() throws IOException {
        Process p = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            p = Runtime.getRuntime().exec(System.getenv("wsl whatweb --color=never --log-xml=directoriowindows/b.xml " + textFieldXML.getText()));
        } else {
            p = Runtime.getRuntime().exec("whatweb --color=never --log-xml=/tmp/b.xml " + textFieldXML.getText());
        }
        return p;
    }


    @FXML
    void initialize() {
        textAreaMongo.setEditable(false);
        textAreaWW.setEditable(false);
        textAreaXML.setEditable(false);
    }

}
