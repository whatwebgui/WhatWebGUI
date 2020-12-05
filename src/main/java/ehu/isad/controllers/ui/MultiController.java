package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.controllers.db.ServerCMSDB;
import ehu.isad.utils.Url;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextArea;

public class MultiController implements Initializable {
    FileChooser fileChooser = new FileChooser();
    Url urlUtils = new Url();
    CMSController cms = CMSController.getInstance();
    private final Desktop desktop = Desktop.getDesktop();
    @FXML
    private Button btnOk;

    @FXML
    private Button btnFiles;

    @FXML
    private TextArea textField;

    private static final MultiController instance = new MultiController();

    private MultiController(){}

    public static MultiController getInstance() { return instance; }

    @FXML
    void onClick(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        if (btn.equals(btnOk)) {
            String[] parts = textField.getText().split("\n");
            for(int i = 0; i < parts.length; i++){
                processURL(parts[i]);
            }
        }else{
            File file = fileChooser.showOpenDialog(null);
            BufferedReader input;
            input = new BufferedReader(new FileReader(file));
            String line;
            while ((line = input.readLine()) != null) {
                processURL(line);
            }
            input.close();
        }
    }

    private void processURL(String url){
        String target = null;
        try {
            target = urlUtils.processUrl(url);
        } catch (IOException ioException) { ioException.printStackTrace();
        } catch (SQLException throwables) { throwables.printStackTrace(); }

        if(target!=null){
            try {
                cms.CMS(target);
                HistoryDB.getInstance().addToHistoryDB(target,"CMS/SERVER");
            } catch (IOException ioException) { ioException.printStackTrace(); }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void openFile(File file) {
        try {
            //desktop.open(file);
            Runtime.getRuntime().exec("xdg-open " + file);
        } catch (IOException ex) {
            /*Logger.getLogger(
                    MultiController.class.getName()).log(
                    Level.SEVERE, null, ex
            );*/
        }
    }

    public ArrayList<String> readFile(String text) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())));
        ArrayList<String> sb = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            sb.add(line.toLowerCase());
            line = br.readLine();
        }
        return sb;

    }
}
