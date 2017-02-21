/**
 * Created by 贾晓磊 on 2016/12/10.
 */
import java.io.*;
import java.util.*;

public class RequestPackage implements Serializable{
    private String type;
    private Vector<String> content = new Vector<String>();

    public RequestPackage(){
        this.type = "";
        this.content.clear();
    }

    public void setType(String Type){
        this.type = Type;
    }

    public String getType(){
        return this.type;
    }

    public Vector<String> GetContent(){
        return this.content;
    }

    public void AddContent(String con){
        this.content.add(con);
    }
}
