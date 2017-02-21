import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Think on 2016/12/19.
 */
public class UserDateBase {
    private String url = "";
    private String userName = "";
    private String password = "";

    public UserDateBase(String url, String userName, String password) { //初始化
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public Result query(String statement)throws Exception{  //查询
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn;
        conn = DriverManager.getConnection(url, userName, password);
        PreparedStatement pstmt = conn.prepareStatement(statement);
        ResultSet rs = pstmt.executeQuery();
        Result result = ResultSupport.toResult(rs);
        conn.close();
        return result;
    }

    public int update(String statement) throws Exception {  //更新数据
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn;
        conn = DriverManager.getConnection(url,userName,password);
        PreparedStatement pstmt = conn.prepareStatement(statement);
        int ret = pstmt.executeUpdate();
        conn.close();
        return ret;
    }
}
