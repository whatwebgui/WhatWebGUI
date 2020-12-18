package ehu.isad.controllers.ui;

import ehu.isad.controllers.db.HistoryDB;
import ehu.isad.utils.Url;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.TextArea;


public class MultiController implements Initializable {
    FileChooser fileChooser = new FileChooser();
    Url urlUtils = new Url();
    CMSController cms = CMSController.getInstance();
    ServerController server = ServerController.getInstance();
    private int cont;
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
    void onClick(ActionEvent event){
        Button btn = (Button) event.getSource();
        if(!textField.getText().equals("") || btn.equals(btnFiles)) {
            Scene scene = btnFiles.getScene();
            if (btn.equals(btnOk)) {
                scene.setCursor(Cursor.WAIT);
                Thread thread = new Thread(() -> {
                    String[] parts = textField.getText().split("\n");
                    for (String part : parts) {
                        processURL(part);
                    }
                    scene.setCursor(Cursor.DEFAULT);
                    Platform.runLater( () -> {//once the scan has finished, the textArea will be cleaned
                        textField.setText("");
                    } );

                });
                thread.start();
            } else {
                File file = fileChooser.showOpenDialog(null);
                if(file != null) {//if one file has been selected
                    if(isValid(file)){
                        Thread thread = new Thread(() -> {
                            cont = 0;
                            scene.setCursor(Cursor.WAIT);
                            BufferedReader input = null;
                            try {
                                input = new BufferedReader(new FileReader(file));
                            } catch (Exception e) {
                                scene.setCursor(Cursor.DEFAULT);
                                e.printStackTrace();
                            }
                            String line = null;
                            while (cont <= 4) {
                                try {
                                    assert input != null;
                                    if ((line = input.readLine()) == null) break;
                                } catch (IOException ioException) {
                                    scene.setCursor(Cursor.DEFAULT);
                                    ioException.printStackTrace();
                                }
                                processURL(line);
                            }
                            if(cont > 4){
                                Platform.runLater( () -> {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error on Multi-Add");
                                    alert.setHeaderText("Error on reading the provided file");
                                    alert.setContentText("The chosen file has too many incorrect URLs, please choose another one");
                                    alert.showAndWait();
                                });
                            }
                            try {
                                assert input != null;
                                input.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            } finally {
                                scene.setCursor(Cursor.DEFAULT);
                            }
                        });
                        thread.start();
                    }else{
                        showError();
                    }
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error on MultiAdd");
            alert.setHeaderText("Error on reading the provided URL");
            alert.setContentText("TextArea is empty");
            alert.showAndWait();
        }
    }

    private void showError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error in Multi-Add");
        alert.setHeaderText("Error in file format");
        alert.setContentText("The file introduced is not a text file");
        alert.showAndWait();
    }

    private void processURL(String url){
        Thread thread = new Thread(() -> {
            String target = null;
            try {
                target = urlUtils.processUrlInMulti(url);
            } catch (SQLException ioException) { ioException.printStackTrace();
            }

            if(target!=null){
                try {
                    cms.CMS(target,true);
                    server.Server(target,true);
                    HistoryDB.getInstance().addToHistoryDB(target,"Multi-add");
                } catch (IOException ioException) { ioException.printStackTrace(); }
            }else{
                cont++;
                Platform.runLater( () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error on URL");
                    alert.setHeaderText("Error on reading the provided URL");
                    alert.setContentText("The URL "+ url + "  doesn't seem to exist");
                    alert.showAndWait();
                });
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid(File file) {
        Process p;
        String line;
        boolean ret = false;
        try{
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                String type = Files.probeContentType(file.toPath());
                ret = type.startsWith("text");
            } else {
                p = Runtime.getRuntime().exec("file " + file.getAbsolutePath());
                if(p!=null){
                    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    ret = true;
                    while (ret) ret = ((line = input.readLine()) != null) && line.contains("text");
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return ret;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField.clear();
    }
}


