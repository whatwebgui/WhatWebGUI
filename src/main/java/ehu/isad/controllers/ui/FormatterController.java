package ehu.isad.controllers.ui;

import ehu.isad.model.Extension;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FormatterController {
/*






*/

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
        List<String> emaitza = new LinkedList<>();
        try {
            String line;
            executeCommand();
            String target = textField.getText();
            String extension = combo.getValue().getExtension() ;
            BufferedReader input = new BufferedReader(new FileReader(""+target+extension));
            while ((line = input.readLine()) != null) { emaitza.add(line); }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return emaitza;
    }

    private void executeCommand() throws IOException {
        Process p = null;
        String target = textField.getText();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            p = Runtime.getRuntime().exec(System.getenv("wsl whatweb --color=never " + target));
        } else {
            p = Runtime.getRuntime().exec("whatweb --color=never  " + target);
        }
    }

    @FXML
    void initialize() {
        textArea.setEditable(false);
    }

}
