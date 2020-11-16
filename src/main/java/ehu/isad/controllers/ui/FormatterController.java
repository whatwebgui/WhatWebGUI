package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.FormatterDB;
import ehu.isad.model.Extension;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    private String path="/tmp/";

    @FXML
    void onClick(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        if (btn_scan.equals(btn)) {
            String newLine = System.getProperty("line.separator");
            textArea.setText(getOutput().stream().collect(Collectors.joining(newLine)));
            textArea.setWrapText(true);
        }
        else if (btn_clear.equals(btn)) {
            textArea.clear();
            textField.clear();
        }
        else if (btn_show.equals(btn)) {
            Desktop.getDesktop().open(new File(path));
        }

    }

    public List<String> getOutput() throws IOException {
        FormatterDB formatterDB = FormatterDB.getController();
        List<String> emaitza = new LinkedList<>();
        try {
            String target = textField.getText();
            String [] prueba = target.split("//");
            String [] line2 =  prueba[1].split("/");
            target = line2[0]; //Target is the URL name. //adeiarias.pw.shell
            if (!formatterDB.formatExists(target,combo.getValue().getExtension())){
                executeCommand(combo.getValue(),target); //This will execute and create the file.
            }
            //This loads the file with the target name.
            BufferedReader input = new BufferedReader(new FileReader("tmp/whatweb/"+target+"."+combo.getValue().getExtension()));
            String line;
            while ((line = input.readLine()) != null) { emaitza.add(line); }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return emaitza;
    }

    private void executeCommand(Extension ext,String target) throws IOException {
        Process p = null;
        String target2 = textField.getText();
        String name = ext.getDisplayName();
        String command = null;
        switch(name) {
            //TODO Put each command.
            case "shell":
                break;
            case "ruby":
                break;
            case "magictree":
                break;
            default:
                break;
        }
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            p = Runtime.getRuntime().exec(System.getenv("wsl " + command));
        } else {
            p = Runtime.getRuntime().exec(command);
        }
    }

    @FXML
    void initialize() {
        String target[] = {"shell","json","xml","mysql","ruby","magictree"};
        String extension[] = {".out",".json",".xml",".sql",".rb",".magictree.xml"};
        ObservableList<Extension> list = FXCollections.observableArrayList();
        for(int i = 0; i < target.length; i++){
            list.add(new Extension(target[i],extension[i]));
        }
        combo.setItems(list);
        textArea.setEditable(false);
    }

}
