package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.utils.Url;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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
                btnFiles.getScene().setCursor(Cursor.WAIT);
                Thread thread = new Thread( () -> {
                    String[] parts = textField.getText().split("\n");
                    for (String part : parts) {
                        processURL(part);
                    }
                    btnFiles.getScene().setCursor(Cursor.DEFAULT);
                });
                thread.start();
            }else{
                File file = fileChooser.showOpenDialog(null);
                btnFiles.getScene().setCursor(Cursor.WAIT);
                Thread thread = new Thread( () -> {
                    btnFiles.getScene().setCursor(Cursor.WAIT);
                    BufferedReader input=null;
                    try {
                        input = new BufferedReader(new FileReader(file));
                    } catch (Exception e) {
                        btnFiles.getScene().setCursor(Cursor.DEFAULT);
                        e.printStackTrace();
                    }
                    String line=null;
                    while (true) {
                        try {
                            if (!((line = input.readLine()) != null)) break;
                        } catch (IOException ioException) {
                            btnFiles.getScene().setCursor(Cursor.DEFAULT);
                            ioException.printStackTrace();
                        }
                        processURL(line);
                    }
                    try {
                        input.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } finally {
                        btnFiles.getScene().setCursor(Cursor.DEFAULT);
                    }
                });
                thread.start();
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
        textField.clear();
    }
}
