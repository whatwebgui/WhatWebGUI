package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.utils.Url;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.TextArea;

public class MultiController implements Initializable {
    FileChooser fileChooser = new FileChooser();
    Url urlUtils = new Url();
    CMSController cms = CMSController.getInstance();
    ServerController server = ServerController.getInstance();
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
            for (String part : parts) {
                processURL(part);
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
        } catch (IOException | SQLException ioException) { ioException.printStackTrace();
        }

        if(target!=null){
            try {
                cms.CMS(target,true);
                server.Server(target,true);
                HistoryDB.getInstance().addToHistoryDB(target,"Multi-add");
            } catch (IOException ioException) { ioException.printStackTrace(); }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
