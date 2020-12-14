package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.FormatterDB;
import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.model.Extension;
import ehu.isad.utils.Url;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
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
    private Button btn_forcescan;

    @FXML
    private ComboBox<Extension> combo;
    @FXML
    private ProgressIndicator pgr;
    @FXML
    private Button btn_clear;
    @FXML
    private Button btn_show;

    private String target = null;
    FormatterDB formatterDB = FormatterDB.getController();
    Url urlUtils = new Url();
    private final String path = System.getProperty("user.home")+"/"+Utils.getProperties().getProperty("pathToFolder");
    Process currentProcess = null;

    private static FormatterController instance = new FormatterController();

    private FormatterController(){}

    public static FormatterController getInstance() { return instance; }


    @FXML
    void onClick(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        if (btn_scan.equals(btn) || btn_forcescan.equals(btn)) {
            if(!textField.getText().equals("")) {
                textArea.clear();
                try {
                    String url = urlUtils.processUrl(textField.getText());
                    if (url != null) {
                        pgr.setVisible(true);
                        setText(btn, url);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in FORMATTER");
                alert.setHeaderText("Error on reading the provided URL");
                alert.setContentText("Textfield is empty");
                alert.showAndWait();
            }
        } else if (btn_clear.equals(btn)) {
            textArea.clear();
            textField.clear();
        } else if (btn_show.equals(btn)) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Desktop.getDesktop().open(new File(path+ "/cache"));
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + path + "/cache");
            } else if (os.contains("linux")) {
                Runtime.getRuntime().exec("xdg-open " + path+ "/cache");
            }
        }

    }

    @FXML
    void onKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            try {
                String url = urlUtils.processUrl(textField.getText());
                if(url!=null){
                    setText(btn_scan,url);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void setText(Button btn,String url) {
        String newLine = System.getProperty("line.separator");
        Thread thread = new Thread(() -> {
            String result = String.join(newLine, getOutput(btn,url));
            Platform.runLater(() -> {
                pgr.setVisible(false);
                textArea.setText(result);
                textArea.setWrapText(true);
            });
        });
        thread.start();
    }

    private List<String> getOutput(Button btn,String url) {
        Extension comboChoice = combo.getValue();
        String domain;
        target = url;
        if (comboChoice == null) {
            comboChoice = combo.getItems().get(1);//in case there is no selected item, the first one will be selected
        }
        domain = url.replace("/", "").split(":")[1];
        List<String> emaitza = null;
        try {
            formatterDB.addDomainToDB(domain);
            if ((btn.equals(btn_forcescan) || (btn.equals(btn_scan)) && !formatterDB.formatExists(domain, comboChoice))) {
                if (btn.equals(btn_forcescan)) deleteFileIfExists(comboChoice, domain);
                executeCommand(comboChoice, domain); //This will execute and create the file.
                formatterDB.addFormatToDB(domain, comboChoice.getType());
                while (currentProcess.isAlive()) {
                    textArea.setPromptText(""); /* wait for the process to finish */
                }
            }
            HistoryDB.getInstance().addToHistoryDB(target, "Formatter > " + comboChoice.getType());
            emaitza = readFile(domain, comboChoice); //This loads the file with the domain name.
        } catch (Exception err) {
            err.printStackTrace();
        }
        return emaitza;
    }

    private void deleteFileIfExists(Extension comboChoice, String domain) {
        String extension = comboChoice.getExtension();
        String pathcache;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            pathcache = "cache\\" + domain + "\\";
        } else {
            pathcache = "cache/" + domain + "/";
        }
        File file = new File(path + pathcache + domain + extension);
        file.delete();
    }

    private List<String> readFile(String domain, Extension comboChoice) throws IOException {
        List<String> emaitza = new LinkedList<>();
        BufferedReader input = null;
        try {
            String pathcache;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                pathcache = "cache\\" + domain + "\\";
            } else {
                pathcache = "cache/" + domain + "/";
            }
            input = new BufferedReader(new FileReader(path + pathcache + domain + comboChoice.getExtension()));
            String line;
            while ((line = input.readLine()) != null) {
                emaitza.add(line);
            }
            input.close();
        } catch (Exception e) {
            assert input != null;
            input.close();
            e.printStackTrace();
        }
        return emaitza;
    }

    private void executeCommand(Extension ext, String domain) throws IOException {
        String type = ext.getType();
        String extension = ext.getExtension();
        String command;
        String path2;
        File directory;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path2 = "";
            directory = new File(path + "cache\\" + domain + "\\");
        } else {
            path2 = path + "cache/" + domain + "/";
            directory = new File(path2);
        }

        if (!directory.exists()) directory.mkdir();
        switch (type) {
            case "shell":
                command = "whatweb --color=never --log-brief=" + path2 + domain + extension + " " + target+"/";
                break;
            case "ruby":
                command = "whatweb --color=never --log-object=" + path2 + domain + extension + " " + target+"/";
                break;
            default:
                command = "whatweb --color=never --log-" + type + "=" + path2 + domain + extension + " " + target+"/";
                break;
        }
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(directory);
            processBuilder.command("cmd.exe", "/C", "wsl " + command);
            currentProcess = processBuilder.start();
        } else {
            currentProcess = Runtime.getRuntime().exec(command);
        }
    }

    private void loadComboBox(){
        String[] displayName = {"Verbose output", "Brief shell output", "JSON format file", "XML format file", "MySQL INSERT format file", "Ruby object inspection format", "MagicTree XML format file"};
        String[] extension = {".txt", ".out", ".json", ".xml", ".sql", ".rb", ".magictree.xml"};
        String[] type = {"verbose", "shell", "json", "xml", "sql", "ruby", "magictree"};
        ObservableList<Extension> list = FXCollections.observableArrayList();
        for (int i = 0; i < displayName.length; i++) {
            list.add(new Extension(displayName[i], extension[i], type[i]));
        }
        combo.setItems(list);
    }

    @FXML
    void initialize() {
        loadComboBox();
        textArea.setEditable(false);
        pgr.setVisible(false);
    }
}