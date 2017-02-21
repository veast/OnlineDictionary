import javafx.fxml.Initializable;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by 贾晓磊 on 2016/12/16.
 */
public class Login implements Initializable {

    private Client client;

    public void setClient(Client client1){
        this.client = client1;
    }


    @FXML private Pane LoginUI;
    @FXML private TextField NameText;
    @FXML private TextField PasswordText;
    @FXML private Button RegisterBut;
    @FXML private Button LoginBut;
    @FXML private Label State;

    @FXML private void RegisterButton() throws Exception{
        String name = NameText.getText();
        String  password = PasswordText.getText();

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestRegister");
        requestPackage.AddContent(name);
        requestPackage.AddContent(password);

        client.toServer.writeObject(requestPackage);
        client.toServer.flush();


        ResponsePackage responsePackage = (ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseRegister")){
            State.setText(responsePackage.GetContent().elementAt(0));
        }
    }

    @FXML private void VisitLogin()throws Exception{
        client.toUI();
    }

    @FXML private void LoginButton() throws Exception{
        String name = NameText.getText();
        String password = PasswordText.getText();

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestLogin");
        requestPackage.AddContent(name);
        requestPackage.AddContent(password);
        client.toServer.writeObject(requestPackage);
        client.toServer.flush();
        ResponsePackage responsePackage = (ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseLogin")){
            State.setText(responsePackage.GetContent().elementAt(0));
        }
        if(responsePackage.GetContent().elementAt(0).equals("Successfully.")){
            client.currentuser = name;
            client.toUI();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
