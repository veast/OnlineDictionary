/**
 * Created by 贾晓磊 on 2016/12/9.
 */
/*
import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.InputStream;


public class Client extends Application{

    public String currentuser = "";
    public ObjectOutputStream toServer;
    public ObjectInputStream fromServer;
    public static Socket server;

    private Stage stage = new Stage();
    private Scene scene;

    private void InitClient() throws Exception{
        server = new Socket(InetAddress.getLocalHost(),9999);
        toServer = new ObjectOutputStream(server.getOutputStream());
        fromServer = new ObjectInputStream(server.getInputStream());

        Parent root;

        FXMLLoader loader_login = new FXMLLoader();
        loader_login.setLocation(this.getClass().getResource("Login.fxml"));
        loader_login.load();
        Login login = ((Login)loader_login.getController());
        login.setClient(this);

        FXMLLoader loader_UI = new FXMLLoader();
        loader_UI.setLocation(this.getClass().getResource("UI.fxml"));
        loader_UI.load();
        UI ui = ((UI)loader_UI.getController());
        ui.setClient(this);

        scene = new Scene(loader_login.getRoot());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        InitClient();

        primaryStage.setScene(scene);
        primaryStage.setTitle("OnlineDictionary");
        stage = primaryStage;
        stage.show();
    }

    public Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Client.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Client.class.getResource(fxml));
        Pane page;
        try {
            page = (Pane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable)loader.getController();
    }

    public void toLogin() throws Exception{
        Login login = (Login) replaceSceneContent("Login.fxml");
        login.setClient(this);
    }

    public void toUI() throws Exception{
        UI ui = (UI) replaceSceneContent("UI.fxml");
        ui.setClient(this);
    }

    public void toVisit() throws Exception{
        Visit visit = (Visit) replaceSceneContent("Visit.fxml");
        visit.setClient(this);
        visit.showVisiter();
    }

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }
}