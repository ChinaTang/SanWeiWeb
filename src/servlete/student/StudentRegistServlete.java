package servlete.student;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.StudentRegistReq;
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


public class StudentRegistServlete extends HttpServlet{

    private static final String INSERT_STUDENT_INFO = "insert into students_info " +
            "(START_SEMESTER,OVER_SENESTER,TEACHHER_ID,STUDENT_NAME,GUARDIAN_NAME,SCHOOL_NAME,TEL_PHONE" +
            ",ADDRESS,REMARKS,STUDENT_PHOTO,GUARDIAN_PHOTO_URL,STUDENT_ID)VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_USER_INFO = "insert " +
            "into user_info (USER_NAME, PASS_WORD, USER_ID, REMARKS, IDENTITY_INFO, PHOTO_URL)VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_SEARCH_USER = "SELECT USER_ID FROM user_info WHERE USER_ID = ?";

    private static final String IDENTITY = "student";

    Random rand;

    @Override
    public void init(){
        rand = new Random();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Gson gson = new Gson();
        StudentRegistReq registReq = gson.fromJson(JSONTools.getRequestPostStr(req), StudentRegistReq.class);

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

            preparedStatement = connection.prepareStatement(SQL_USER_INFO);
            preparedStatement.setString(1, registReq.userName);
            preparedStatement.setString(2, registReq.passWord);
            preparedStatement.setString(3, userId);
            preparedStatement.setString(4, registReq.remarks);
            preparedStatement.setString(5, IDENTITY);
            preparedStatement.setString(6, "");
            preparedStatement.execute();
            connection.commit();

            preparedStatement = connection.prepareStatement(INSERT_STUDENT_INFO);
            preparedStatement.setString(1, registReq.startSemester);
            preparedStatement.setString(2, registReq.overSenester);
            preparedStatement.setString(3, registReq.teachherId);
            preparedStatement.setString(4, registReq.userName);
            preparedStatement.setString(5, registReq.guardianName);
            preparedStatement.setString(6, registReq.schoolName);
            preparedStatement.setString(7, registReq.telephone);
            preparedStatement.setString(8, registReq.address);
            preparedStatement.setString(9, registReq.remarks);
            preparedStatement.setString(10, registReq.photoForBase64);
            preparedStatement.setString(11, registReq.guardianPhoto);
            preparedStatement.setString(12, userId);
            preparedStatement.execute();
            connection.commit();

            respJSON.put("msg", "success");
            result = StandardResponse.respJSON(respJSON, "0", "", new JSONObject());


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
        }finally {
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
