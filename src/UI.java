import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.servlet.jsp.jstl.sql.Result;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.SortedMap;

/**
 * Created by 贾晓磊 on 2016/12/16.
 */
public class UI implements Initializable {
    GuiCamera photo;
    private Client client;
    public void setClient(Client client1){
        this.client = client1;
    }

    private String transBaidu = "";
    private String transJinshan = "";
    private String transYoudao = "";
    private String ENG_Pron = "";
    private String USA_Pron = "";
    private String bingLike = "";
    private String jinshanLike = "";
    private String youdaoLike = "";

    @FXML private Pane UIboader;
    @FXML private TextField WordText;
    @FXML private TextArea BaiduArea;
    @FXML private TextArea JinshanArea;
    @FXML private TextArea YoudaoArea;
    @FXML private Button SearchBut;
    @FXML private Button Layout;
    @FXML private CheckBox BaiduSelect;
    @FXML private CheckBox YoudaoSelect;
    @FXML private CheckBox JinshanSelect;
    @FXML private Button BaiduLike;
    @FXML private Button YoudaoLike;
    @FXML private Button JinshanLike;
    @FXML private Label BaiduLikeNum;
    @FXML private Label JinshanLikeNum;
    @FXML private Label YoudaoLikeNum;
    @FXML private VBox Result;
    @FXML private Pane Bing;
    @FXML private Pane Youdao;
    @FXML private Pane Jinshan;



    @FXML private void VisiterButton()throws Exception{
        client.toVisit();
    }

    @FXML private void WordCard()throws Exception{
        this.photo = new GuiCamera("Word","png");
        this.photo.snapShot();
    }

    @FXML private void SearchButton() throws Exception{
        String word = WordText.getText();
        if(word == null || word.equals("")){
            BaiduArea.setText("");
            YoudaoArea.setText("");
            JinshanArea.setText("");
            BaiduLikeNum.setText("0");
            JinshanLikeNum.setText("0");
            YoudaoLikeNum.setText("0");
            return;
        }
        else{
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setType("requestSearch");
            requestPackage.AddContent(word);
            client.toServer.writeObject(requestPackage);
            client.toServer.flush();
            ResponsePackage responsePackage = (ResponsePackage)client.fromServer.readObject();
            if(responsePackage.getType().equals("responseSearch")){
                this.transBaidu = responsePackage.GetContent().elementAt(0);
                this.transJinshan = responsePackage.GetContent().elementAt(1);
                this.transYoudao = responsePackage.GetContent().elementAt(2);
                this.ENG_Pron = responsePackage.GetContent().elementAt(3);
                this.USA_Pron = responsePackage.GetContent().elementAt(4);

                if(BaiduSelect.isSelected()){
                    BaiduArea.setText("英： " + ENG_Pron + "  美：" + USA_Pron + "\n" + transBaidu);
                    this.bingLike = responsePackage.GetContent().elementAt(5);
                    int temp = Integer.valueOf(bingLike);
                    String theReal = String.valueOf(temp);
                    BaiduLikeNum.setText(theReal);    //  从数据库中获取
                }
                else {
                    BaiduArea.setText("");
                    BaiduLikeNum.setText("0");
                }
                if(JinshanSelect.isSelected()){
                    JinshanArea.setText("英： " + ENG_Pron + "  美：" + USA_Pron + "\n" + transJinshan);
                    this.jinshanLike = responsePackage.GetContent().elementAt(6);
                    JinshanLikeNum.setText(jinshanLike);    //  从数据库中获取
                }
                else {
                    JinshanArea.setText("");
                    JinshanLikeNum.setText("0");
                }
                if(YoudaoSelect.isSelected()){
                    YoudaoArea.setText("英： " + ENG_Pron + "  美：" + USA_Pron + "\n" + transYoudao);
                    this.youdaoLike = responsePackage.GetContent().elementAt(7);
                    YoudaoLikeNum.setText(youdaoLike);    //  从数据库中获取
                }
                else {
                    YoudaoArea.setText("");
                    YoudaoLikeNum.setText("0");
                }

                Pane pane1 = (Pane)Result.getChildren().get(0);//bing
                Pane pane2 = (Pane)Result.getChildren().get(1);//youdao
                Pane pane3 = (Pane)Result.getChildren().get(2);//jinshan


                int num1 = Integer.parseInt(BaiduLikeNum.getText());
                int num2 = Integer.parseInt(YoudaoLikeNum.getText());
                int num3 = Integer.parseInt(JinshanLikeNum.getText());


                if(!BaiduSelect.isSelected()){
                    num1 = -1;
                }
                if(!YoudaoSelect.isSelected()){
                    num2 = -1;
                }
                if(!JinshanSelect.isSelected()){
                    num3 =-1;
                }

                Result.getChildren().clear();
                if(num1 >= num2 && num1 >= num3){
                    Result.getChildren().add(pane1);
                    if(num2 >= num3){
                        Result.getChildren().add(pane2);
                        Result.getChildren().add(pane3);
                    }
                    else {
                        Result.getChildren().add(pane3);
                        Result.getChildren().add(pane2);
                    }
                }
                else if(num2 >= num1 && num2 >= num3){
                    Result.getChildren().add(pane2);
                    if(num1 >= num3) {
                        Result.getChildren().add(pane1);
                        Result.getChildren().add(pane3);
                    }
                    else {
                        Result.getChildren().add(pane3);
                        Result.getChildren().add(pane1);
                    }
                }
                else {
                    Result.getChildren().add(pane3);
                    if(num1 >= num2){
                        Result.getChildren().add(pane1);
                        Result.getChildren().add(pane2);
                    }
                    else {
                        Result.getChildren().add(pane2);
                        Result.getChildren().add(pane1);
                    }
                }
            }
        }


    }

    @FXML private void BaiduLikeButton() throws Exception {
        if(WordText.getText() == null || WordText.getText().equals(""))
            return;
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestLike");
        requestPackage.AddContent(WordText.getText());
        requestPackage.AddContent("baidu");
        client.toServer.writeObject(requestPackage);
        client.toServer.flush();
        ResponsePackage responsePackage = (ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseLike")){
            BaiduLikeNum.setText(responsePackage.GetContent().elementAt(0));
        }
    }

    @FXML private void YoudaoLikeButton() throws Exception {
        if(WordText.getText() == null || WordText.getText().equals(""))
            return;
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestLike");
        requestPackage.AddContent(WordText.getText());
        requestPackage.AddContent("youdao");
        client.toServer.writeObject(requestPackage);
        client.toServer.flush();
        ResponsePackage responsePackage = (ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseLike")){
            BaiduLikeNum.setText(responsePackage.GetContent().elementAt(0));
        }
    }

    @FXML private void JinshanLikeButton() throws Exception {
        if(WordText.getText() == null || WordText.getText().equals(""))
            return;
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestLike");
        requestPackage.AddContent(WordText.getText());
        requestPackage.AddContent("jinshan");
        client.toServer.writeObject(requestPackage);
        client.toServer.flush();
        ResponsePackage responsePackage = (ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseLike")){
            BaiduLikeNum.setText(responsePackage.GetContent().elementAt(0));
        }
    }

    @FXML private void LogoutButton() throws Exception{
        String name = client.currentuser;
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setType("requestLogout");
        requestPackage.AddContent(name);
        client.toServer.writeObject(requestPackage);
        client.toServer.flush();
        ResponsePackage responsePackage = (ResponsePackage) client.fromServer.readObject();
        if(responsePackage.getType().equals("responseLogout")){
            client.currentuser = "";
            client.toLogin();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
