package ehu.isad.controllers.ui;

import ehu.isad.model.ServerCMSModel;
import ehu.isad.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class CMSController {

    @FXML
    private Pane pane1;

    @FXML
    private TextField textField;

    @FXML
    private ComboBox<?> comboBox;

    @FXML
    private Button scanBtn;

    @FXML
    private TableView<ServerCMSModel> cmsTable;

    @FXML
    private TableColumn<ServerCMSModel, String> urlColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> cmsColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> versionColumn;

    @FXML
    private TableColumn<ServerCMSModel, String> lastUpdatedColumn;
    MainController mainController = new MainController();
    ArrayList<String> extensions;

    {
        try {
            extensions = this.readExtensionLines();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private final ServerCMSController serverCMSController = ServerCMSController.getInstance();
    private final String path= Utils.getProperties().getProperty("pathToFolder");

    @FXML
    void onClick(ActionEvent event) throws IOException, SQLException {
        String target = textField.getText();
        if(target.charAt(target.length()-1)!='/') textField.setText(textField.getText()+"/");
        if(!target.contains(":")){
            textField.setText("http://"+textField.getText());
        }
        this.CMS(textField.getText());

    }
    void CMS(String url) throws IOException {
        String domain = url.replace("/", "").split(":")[1];
        if (validateInput(url)) {
            serverCMSController.click(domain, url);
            cmsTable.setItems(serverCMSController.getCMSList());
        } else {
            mainController.showPopUp(url);
        }
    }

    public void setItems() {
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        cmsColumn.setCellValueFactory(new PropertyValueFactory<>("cms"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("version"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        cmsTable.setItems(serverCMSController.getCMSList());
    }

    @FXML
    void initialize() throws SQLException {
        setItems();
    }
    private boolean validateInput(String url){
        //extension split.
        String[] split = url.split("\\.");
        String keyword = split[split.length - 1];
        if(keyword.charAt(keyword.length() -1) == '/') {
            keyword = keyword.substring(0, keyword.length() - 1);
        }
        //prefix split
        String[] split2 = url.split(":");
        String protocol = split2[0];
        return (extensions.contains(keyword) && (protocol.equals("http") || protocol.equals("https")));
    }



    public ArrayList<String> readExtensionLines() throws IOException {
        BufferedReader br  = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/extensions.txt" )));
        ArrayList<String>  sb = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            sb.add(line.toLowerCase());
            line = br.readLine();
        }
        return sb;
    }
    @FXML
    void onKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            this.CMS(textField.getText());
        }
    }

}