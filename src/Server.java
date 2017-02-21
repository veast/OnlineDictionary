/**
 * Created by 贾晓磊 on 2016/12/9.
 */

import javax.servlet.jsp.jstl.sql.Result;
import java.net.*;
import java.util.*;
import java.io.*;


public class Server /*implements Runnable */{
    //private Socket client;
    //private ObjectInputStream in;
    //private ObjectOutputStream out;
    private UserDateBase udb = new UserDateBase("jdbc:mysql://localhost:3306/dictionary", "root", "");


    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            int clientNo = 1;

            while (true) {
                Socket socket = serverSocket.accept();
                HandleAClent task = new HandleAClent(socket);
                new Thread(task).start();
                clientNo++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class HandleAClent implements Runnable {
        private Socket socket;

        public HandleAClent(Socket socket) {
            this.socket = socket;
        }

        public ResponsePackage Search(RequestPackage requestPackage) throws Exception{
            ResponsePackage responsePackage = new ResponsePackage();
            responsePackage.setType("responseSearch");
            String word = requestPackage.GetContent().elementAt(0);

            Web_Crawler crawler = new Web_Crawler(word);
            String baidufanyi = "";
            String youdaofanhyi = "";
            String jinshanfanyi = "";

            for(String s : crawler.Get_Tran_baidu()){
                baidufanyi += s +"\n";
            }

            for(String s : crawler.Get_Tran_jinshan()){
                jinshanfanyi += s +"\n";
            }

            for(String s : crawler.Get_Tran_youdao()){
                youdaofanhyi += s +"\n";
            }

            String ENG_Pron = crawler.Get_ENG_Pron();
            String USA_Pron = crawler.Get_USA_Pron();
            responsePackage.AddContent(baidufanyi);
            responsePackage.AddContent(jinshanfanyi);
            responsePackage.AddContent(youdaofanhyi);
            responsePackage.AddContent(ENG_Pron);
            responsePackage.AddContent(USA_Pron);

            Result rs = udb.query("select * from words where word=\"" + word + "\";");
            if(rs.getRowCount() == 0) {
                udb.update("insert into words values('" + word + "'," + 0 + "," + 0 + "," + 0 + ");");
                responsePackage.AddContent("0");
                responsePackage.AddContent("0");
                responsePackage.AddContent("0");
            }
            else{
                SortedMap<String,Integer> map = (SortedMap<String,Integer>) rs.getRows()[0];
                int bingLike = map.get("bingLike");
                int jinshanLike = map.get("jinshanLike");
                int youdaoLike = map.get("youdaoLike");
                String like1 = String.valueOf(bingLike);
                String like2 = String.valueOf(jinshanLike);
                String like3 = String.valueOf(youdaoLike);
                responsePackage.AddContent(like1);
                responsePackage.AddContent(like2);
                responsePackage.AddContent(like3);
            }
            return responsePackage;
        }

        public ResponsePackage Login(RequestPackage requestPackage) throws Exception{
            ResponsePackage responsePackage = new ResponsePackage();
            responsePackage.setType("responseLogin");
            String userName = requestPackage.GetContent().elementAt(0);
            String Password = requestPackage.GetContent().elementAt(1);
            if(userName.equals("") || Password.equals("") || userName == null || Password ==null){
                responsePackage.AddContent("No userName and Password");
                return responsePackage;
            }
     //     数据库
            else {
                Result rs = udb.query("select * from users where username=\"" + userName + "\";");
                if (rs.getRowCount() == 0)
                    responsePackage.AddContent("There is no such UserName.");
                else{
                    SortedMap<String, String> map = (SortedMap<String, String>) rs.getRows()[0];
                    String theRealPSW = map.get("password");
                    if(Password.equals((theRealPSW))){
                        udb.update("update users set online = 1 where username=\"" + userName + "\";");
                        responsePackage.AddContent("Successfully.");
                    }
                    else responsePackage.AddContent("Wrong Password.");
                }
                return responsePackage;
            }


        }

        public ResponsePackage Register (RequestPackage requestPackage) throws Exception{
            ResponsePackage responsePackage = new ResponsePackage();
            responsePackage.setType("responseRegister");
            String userName = requestPackage.GetContent().elementAt(0);
            String Password = requestPackage.GetContent().elementAt(1);

            if(userName == null || userName.equals("")){
                responsePackage.AddContent("No userName");
                return responsePackage;
            }
            if(Password == null||Password.equals("")){
                responsePackage.AddContent("No password");
                return responsePackage;
            }

            //数据库
            else {
                Result rs = udb.query("select * from users where username=\"" + userName + "\";");
                if (rs.getRowCount() != 0)
                    responsePackage.AddContent("The username has been registered.");
                else{
                    udb.update("insert into users values('" + userName + "','" + Password + "'," + 0 + ");");
                    responsePackage.AddContent("Register Successfully.");
                }
                return responsePackage;
            }
        }

        public ResponsePackage Like (RequestPackage requestPackage) throws Exception{
            ResponsePackage responsePackage = new ResponsePackage();
            responsePackage.setType("responseLike");
            String word = requestPackage.GetContent().elementAt(0);
            String OLdict = requestPackage.GetContent().elementAt(1);

            //数据库
            Result rs = udb.query("select * from words where word=\"" + word + "\";");
            SortedMap<String,Integer> map = (SortedMap<String,Integer>) rs.getRows()[0];
            if(OLdict.equals("baidu")){ //bing
                //返回数据库中点赞个数+1
                int bingLike = map.get("bingLike")+1;

                udb.update("update words set bingLike = " + bingLike + " where word=\"" + word + "\";");
                responsePackage.AddContent(Integer.toString(bingLike));
            }
            else if(OLdict.equals("youdao")){
                int youdaoLike = map.get("youdaoLike")+1;
                udb.update("update words set youdaoLike = " + youdaoLike + " where word=\"" + word + "\";");
                responsePackage.AddContent(Integer.toString(youdaoLike));
            }
            else if(OLdict.equals("jinshan")){
                int jinshanLike = map.get("jinshanLike")+1;
                udb.update("update words set jinshanLike = " + jinshanLike + " where word=\"" + word + "\";");
                responsePackage.AddContent(Integer.toString(jinshanLike));
            }
            responsePackage.AddContent("success.");

            return responsePackage;
        }

        public ResponsePackage Logout(RequestPackage requestPackage) throws Exception{
            ResponsePackage responsePackage = new ResponsePackage();
            responsePackage.setType("responseLogout");

            //数据库
            String username = requestPackage.GetContent().elementAt(0);
            udb.update("update users set online=0 where username=\"" + username + "\";");
            return responsePackage;
        }

        public ResponsePackage Visiter(RequestPackage requestPackage) throws Exception{
            ResponsePackage responsePackage = new ResponsePackage();
            responsePackage.setType("responseVisiter");

            //从数据库得到当前在线用户
            Result rs = udb.query("select * from users where online=" + 1 + ';');
            SortedMap<String,String>[] map = (SortedMap<String, String>[])rs.getRows();
            for(SortedMap<String,String>m:map)
                responsePackage.AddContent(m.get("username"));
            return responsePackage;
        }

       //单词卡



        public void run() {
            try {
                // DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                //DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream inputFromClient = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    RequestPackage requestPackage = (RequestPackage) inputFromClient.readObject();
                    if(requestPackage.getType().equals("requestSearch")) {
                        ResponsePackage responsePackage = Search(requestPackage);
                        System.out.println("Search");
                        outputToClient.writeObject(responsePackage);
                        outputToClient.flush();
                    }

                    else if (requestPackage.getType().equals("requestLogin")){
                        ResponsePackage responsePackage = Login(requestPackage);
                        System.out.println("Login");
                        outputToClient.writeObject(responsePackage);
                        outputToClient.flush();
                    }

                    else if (requestPackage.getType().equals("requestRegister")){
                        ResponsePackage responsePackage = Register(requestPackage);
                        System.out.println("Register");
                        outputToClient.writeObject(responsePackage);
                        outputToClient.flush();
                    }

                    else if (requestPackage.getType().equals("requestLike")){
                        ResponsePackage responsePackage = Like(requestPackage);
                        System.out.println("Like");
                        outputToClient.writeObject(responsePackage);
                        outputToClient.flush();
                    }

                    else if (requestPackage.getType().equals("requestLogout")){
                        ResponsePackage responsePackage = Logout(requestPackage);
                        System.out.println("Logout");
                        outputToClient.writeObject(responsePackage);
                        outputToClient.flush();
                    }
                    else if(requestPackage.getType().equals("requestVisiter")){
                        ResponsePackage responsePackage = Visiter(requestPackage);
                        outputToClient.writeObject(responsePackage);
                        outputToClient.flush();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}

