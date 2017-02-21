import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by 贾晓磊 on 2016/12/19.
 */
public class Visit implements Initializable{
    private Client client;
    private Pane VisitBoard;

    @FXML private ListView Visiter;

    public void showVisiter() throws Exception{
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestVisiter");
        client.toServer.writeObject(requestPackage);
        client.toServer.flush();
        ResponsePackage responsePackage =(ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseVisiter")){
            ObservableList<String> list =  FXCollections.observableArrayList();
            list.addAll(responsePackage.GetContent());
            Visiter.setItems(list);
        }
    }

    public void setClient(Client client1) {
        this.client = client1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
