package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.FormatterDB;
import ehu.isad.model.Extension;
import ehu.isad.utils.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FormatterController {

    @FXML
    private Pane pane3;

    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    @FXML
    private Button btn_scan;

    @FXML
    private ComboBox<Extension> combo;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_show;

    FormatterDB formatterDB = FormatterDB.getController();
    private final String path= Utils.getProperties().getProperty("pathToFolder");
    Process currentProcess = null;

    @FXML
    void onClick(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        if (btn_scan.equals(btn)) {
            textArea.setPromptText("Loading...");
            textArea.setWrapText(true);
            String newLine = System.getProperty("line.separator");
            Thread thread = new Thread( () -> {
                String result = String.join(newLine, getOutput());
                Platform.runLater( () -> { textArea.setText(result); } );
            });
            thread.start();
        }
        else if (btn_clear.equals(btn)) {
            textArea.clear();
            textField.clear();
        }
        else if (btn_show.equals(btn)) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Desktop.getDesktop().open(new File(path));
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + path);
            } else if (os.contains("linux")) {
                Runtime.getRuntime().exec("xdg-open " + path);
                Runtime.getRuntime().exec("sensible-browser");
            }
        }
    }

    private List<String> getOutput() {
        Extension comboChoice = combo.getValue();
        String domain = textField.getText().replace("/", "").split(":")[1];
        List<String> emaitza = null;
        try {
            formatterDB.addDomainToDB(domain);
            if (!formatterDB.formatExists(domain, comboChoice)) {
                executeCommand(comboChoice, domain); //This will execute and create the file.
                formatterDB.addFormatToDB(domain, comboChoice.getType());
                while (currentProcess.isAlive()){ /* wait for the process to finish */ }
            }
            //This loads the file with the domain name.
            emaitza = readFile(domain);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return emaitza;
    }

    private List<String> readFile(String domain) {
        List<String> emaitza = new LinkedList<>();
        try {
            String pathcache;
            if (System.getProperty("os.name").toLowerCase().contains("win")) { pathcache="cache\\"; } else { pathcache="cache/"; }
            BufferedReader input = new BufferedReader(new FileReader(path + pathcache + domain + combo.getValue().getExtension()));
            String line;
            while ((line = input.readLine()) != null) {
                emaitza.add(line);
            }
            input.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return emaitza;
    }

    private void executeCommand(Extension ext, String domain) throws IOException{
        String target = textField.getText();
        String type = ext.getType();
        String extension = ext.getExtension();
        String command;
        String path2;
        if (System.getProperty("os.name").toLowerCase().contains("win")) { path2=""; } else { path2=path+"cache/"; }
        switch (type) {
            case "shell":
                command = "whatweb --color=never --log-brief=" + path2 + domain + extension + " " + target;
                break;
            case "ruby":
                command = "whatweb --color=never --log-object=" + path2 + domain + extension + " " + target;
                break;
            default:
                command = "whatweb --color=never --log-" + type + "=" + path2 + domain + extension + " " + target;
                break;
        }
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(path+"cache\\"));
            processBuilder.command("cmd.exe", "/C", "wsl " +command);
            currentProcess = processBuilder.start();
        } else {
            currentProcess = Runtime.getRuntime().exec(command);
        }
    }



    @FXML
    void initialize() {
        String[] displayName = {"Verbose output","Brief shell output","JSON format file","XML format file","MySQL INSERT format file","Ruby object inspection format","MagicTree XML format file"};
        String[] extension = {".txt",".out",".json",".xml",".sql",".rb",".magictree.xml"};
        String[] type = {"verbose","shell","json","xml","sql","ruby","magictree"};
        ObservableList<Extension> list = FXCollections.observableArrayList();
        for(int i = 0; i < displayName.length; i++){
            list.add(new Extension(displayName[i],extension[i],type[i]));
        }
        combo.setItems(list);
        textArea.setEditable(false);
    }

}