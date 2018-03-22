package servlete.teacher;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.TeacherRegistReq;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class TeacherRegisterServlete extends HttpServlet {

    private static final String IDENTITY = "teacher";

    Random rand;



    //user Info
    private static final String SQL_USER_INFO = "insert " +
            "into user_info (USER_NAME, PASS_WORD, USER_ID, REMARKS, IDENTITY_INFO, PHOTO_URL)VALUES (?, ?, ?, ?, ?, ?)";
    //teacher Info
    private static final String SQL_TEACHER_INFO = "insert " +
            "into teach_info (TEACHER_NAME, TEACHER_ID, TEL_PHONE, ADDRESS, CURRICULUM, WORK_STATE, REMARKS)VALUES(?,?,?,?,?,?,?)";

    private static final String SQL_SEARCH_USER = "SELECT USER_ID FROM user_info WHERE USER_ID = ?";


    @Override
    public void init(){
        rand = new Random();
    }

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
        TeacherRegistReq registReq = gson.fromJson(JSONTools.getRequestPostStr(req), TeacherRegistReq.class);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        JSONObject respJSON = new JSONObject();
        JSONObject result = null;

        String userId = "" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10)
                + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10);


        try {
            connection = DataBaseTools.connectionDataSource();

            preparedStatement = connection.prepareStatement(SQL_SEARCH_USER);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.wasNull()){
                userId = "" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10)
                        + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10);
                preparedStatement.setString(1, userId);
                resultSet = preparedStatement.executeQuery();
            }
            DataBaseTools.closeResultSet(resultSet);
            DataBaseTools.closePrepStmt(preparedStatement);

            preparedStatement = connection.prepareStatement(SQL_USER_INFO);
            preparedStatement.setString(1, registReq.userName);
            preparedStatement.setString(2, registReq.passWord);
            preparedStatement.setString(5, registReq.identity);
            preparedStatement.setString(3, userId);
            preparedStatement.setString(4, registReq.remarks);
            preparedStatement.setString(6, "");
            preparedStatement.execute();
            connection.commit();
            DataBaseTools.closePrepStmt(preparedStatement);

            preparedStatement = connection.prepareStatement(SQL_TEACHER_INFO);
            preparedStatement.setString(1, registReq.userName);
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3, registReq.telephone);
            preparedStatement.setString(4, registReq.address);
            preparedStatement.setString(5, registReq.curriculum);
            preparedStatement.setString(6, registReq.work_state);
            preparedStatement.setString(7, registReq.remarks);
            preparedStatement.execute();
            connection.commit();
            DataBaseTools.closePrepStmt(preparedStatement);

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
