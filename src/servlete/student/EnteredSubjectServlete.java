package servlete.student;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.EnteredSubjectReq;
import netbean.req.StudentRegistReq;
import org.json.JSONObject;
import tools.JSONTools;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnteredSubjectServlete extends HttpServlet{

    private static final String SQL_ENTERED_CONTINUATION = "UPDATE entered_continuation SET START_SEMESTER = ?, " +
            "OVER_SEMESTER = ?, TEACHER_ID = ?, SUBJECT_NAME = ?, REMARKS = ?, PAY_STATUS = ? WHERE STUDENT_ID = ?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Gson gson = new Gson();
        EnteredSubjectReq registReq = gson.fromJson(JSONTools.getRequestPostStr(req), EnteredSubjectReq.class);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        JSONObject respJSON = new JSONObject();
        JSONObject result = null;

        try {
            connection = DataBaseTools.connectionDataSource();
            preparedStatement = connection.prepareStatement(SQL_ENTERED_CONTINUATION);
            preparedStatement.setString(1, registReq.startSemester);
            preparedStatement.setString(2, registReq.overSemester);
            preparedStatement.setString(3, registReq.teacherId);
            preparedStatement.setString(4, registReq.subjectName);
            preparedStatement.setString(5, registReq.remarks);
            preparedStatement.setString(6, registReq.payStatus);
            preparedStatement.setString(7, registReq.studentId);
            preparedStatement.execute();
            connection.commit();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(preparedStatement != null){
                DataBaseTools.closePrepStmt(preparedStatement);
            }
            if(connection != null){
                DataBaseTools.closeConnection(connection);
            }
        }
    }
}
