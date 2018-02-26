package servlete.user;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.CheckUserNameReq;
import org.json.JSONObject;
import resq.StandardResponse;
import servlete.CodeConstant;
import tools.JSONTools;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckUserNameServlete extends HttpServlet{

    private static final String SQL_CHECK_USER = "SELECT USER_NAME FROM user_info WHERE USER_NAME = ?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        JSONObject result = null;
        Gson gson = new Gson();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        CheckUserNameReq checkUserNameReq = gson.fromJson(JSONTools.getRequestPostStr(req), CheckUserNameReq.class);
        try {
            connection = DataBaseTools.connectionDataSource();
            preparedStatement = connection.prepareStatement(SQL_CHECK_USER);
            preparedStatement.setString(1, checkUserNameReq.userName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.wasNull()){
                jsonObject.put("code", CodeConstant.SUCCESS);
                jsonObject.put("msg", "用户名可用");
                result = StandardResponse.respJSON(jsonObject, "0", "", new JSONObject());
            }else{
                jsonObject.put("code", CodeConstant.FAIUL);
                jsonObject.put("msg", "用户名不可用，请更换");
                result = StandardResponse.respJSON(jsonObject, "200", "", new JSONObject());
            }
        } catch (NamingException e) {
            e.printStackTrace();
            jsonObject.put("code", CodeConstant.ERROR);
            jsonObject.put("msg", e.getMessage());
            result = StandardResponse.respJSON(jsonObject, "404", e.getMessage(), new JSONObject());
        } catch (SQLException e) {
            e.printStackTrace();
            jsonObject.put("code", CodeConstant.ERROR);
            jsonObject.put("msg", e.getMessage());
            result = StandardResponse.respJSON(jsonObject, "404", e.getMessage(), new JSONObject());
        }finally {
            DataBaseTools.closeResultSet(resultSet);
            DataBaseTools.closePrepStmt(preparedStatement);
            DataBaseTools.closeConnection(connection);
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(result.toString());
        printWriter.flush();
        printWriter.close();
    }
}
