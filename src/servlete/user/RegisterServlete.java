package servlete.user;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.RegistReq;
import org.json.JSONObject;
import resq.StandardResponse;
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
import java.sql.SQLException;
import java.util.UUID;

public class RegisterServlete extends HttpServlet {

    private static final String IDENTITY = "teacher";

    //teach Info
    private static final String SQL_USER_INFO = "insert " +
            "into user_info (USER_NAME, PASS_WORD, USER_ID, REMARKS, IDENTITY_INFO, PHOTO_URL)VALUES (?, ?, ?, ?, ?, ?)";
    /**
     * userName,
     * password(MD5)
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Gson gson = new Gson();
        RegistReq registReq = gson.fromJson(JSONTools.getRequestPostStr(req), RegistReq.class);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        JSONObject respJSON = new JSONObject();
        JSONObject result = null;
        try {
            connection = DataBaseTools.connectionDataSource();
            preparedStatement = connection.prepareStatement(SQL_USER_INFO);
            preparedStatement.setString(1, registReq.userName);
            preparedStatement.setString(2, registReq.passWord);
            preparedStatement.setString(5, registReq.identity);
            if(registReq.identity.equals(IDENTITY)){
                UUID teachId = UUID.randomUUID();
                preparedStatement.setString(3, teachId.toString());
            }else{
                preparedStatement.setString(3, "");
            }
            preparedStatement.setString(4, registReq.remarks);
            preparedStatement.setString(6, "");
            preparedStatement.execute();
            connection.commit();

            respJSON.put("msg", "success");
            result = StandardResponse.respJSON(respJSON, "0", "", new JSONObject());
        } catch (SQLException e) {
            e.printStackTrace();
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            respJSON.put("msg", e.getMessage());
            result = StandardResponse.respJSON(respJSON, "200", "", new JSONObject());

        } catch (NamingException e) {
            e.printStackTrace();
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            respJSON.put("msg", e.getMessage());
            result = StandardResponse.respJSON(respJSON, "200", "", new JSONObject());
        } finally {
            if(preparedStatement != null){
                DataBaseTools.closePrepStmt(preparedStatement);
            }
            if(connection != null){
                DataBaseTools.closeConnection(connection);
            }
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(result.toString());
            printWriter.flush();
            printWriter.close();
        }
    }

}
