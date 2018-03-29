package servlete.student;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.AchievementReq;
import netbean.req.EnteredSubjectReq;
import org.json.JSONObject;
import tools.JSONTools;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentAchievement extends HttpServlet{

    private static final String SQL_ACHIEVEMENT = "insert into achievement (START_SEMESTER,OVER_SEMESTER,SUBJECT_NAME" +
            ",MARK,TEACHER_ID,STUDENT_ID,REMARKS) VALUES (?,?,?,?,?,?,?)";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Gson gson = new Gson();
        AchievementReq registReq = gson.fromJson(JSONTools.getRequestPostStr(req), AchievementReq.class);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        JSONObject respJSON = new JSONObject();
        JSONObject result = null;

        try {
            connection = DataBaseTools.connectionDataSource();
            preparedStatement = connection.prepareStatement(SQL_ACHIEVEMENT);
            preparedStatement.setString(1, registReq.startSemester);
            preparedStatement.setString(2, registReq.overSemester);
            preparedStatement.setString(3, registReq.subjectName);
            preparedStatement.setString(4, registReq.mark);
            preparedStatement.setString(5, registReq.teacherId);
            preparedStatement.setString(6, registReq.studentId);
            preparedStatement.setString(7, registReq.remarks);
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
