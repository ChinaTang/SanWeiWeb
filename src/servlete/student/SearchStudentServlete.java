package servlete.student;

import com.google.gson.Gson;
import databases.DataBaseTools;
import netbean.req.SearchStudentReq;
import netbean.resp.BaseResp;
import netbean.resp.SearchStudentResp;
import netbean.resp.StudentInfo;
import tools.JSONTools;
import tools.ResponseToJSON;

import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchStudentServlete extends HttpServlet{

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_LIMIT = 10;

    private static final String SQL_COUNT =
            "SELECT COUNT(*) FROM students_info";

    private static final String SQL_SEARCH_NAME =
            "SELECT * FROM students_info WHERE STUDENT_NAME = ? LIMIT ?, ?";

    private static final String SQL_SEARCH_TIME =
            "SELECT * FROM students_info WHERE START_SEMESTER = ? " +
                    "AND OVER_SENESTER = ? LIMIT ?, ?";

    private static final String SQL_SEARCH_ID =
            "SELECT * FROM students_info WHERE STUDENT_ID = ? LIMIT ?, ?";

    private static final String SQL_SEARCH_ALL =
            "SELECT * FROM students_info WHERE START_SEMESTER = ? " +
                    "AND OVER_SENESTER = ? AND STUDENT_ID = ? AND " +
                    "STUDENT_NAME = ? LIMIT ?, ?";

    private static final String SQL_SEARCH =
            "SELECT * FROM students_info LIMIT ?, ?";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        SearchStudentReq searchStudentReq
                = gson.fromJson(JSONTools.getRequestPostStr(request), SearchStudentReq.class);
        int count = 0;
        int totalpages = 0;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet countSet = null;
        ResultSet resultSet = null;
        BaseResp baseResp = new BaseResp();

        try {
            connection = DataBaseTools.connectionDataSource();
            preparedStatement = connection.prepareStatement(SQL_COUNT);
            countSet = preparedStatement.executeQuery();
            if(countSet.next()){
                count = countSet.getInt(1);
            }
            totalpages = (int)Math.ceil(count / (DEFAULT_LIMIT * 1.0));
            int page = searchStudentReq.page;
            if(page == 0 || page < 1){
                page = DEFAULT_PAGE;
            }

            if(page > totalpages){
                page = totalpages;
            }
            int limit = searchStudentReq.limit;
            if(limit == 0 || limit < 1){
                limit = DEFAULT_LIMIT;
            }

            int lastRow = 0;
            if(page != 1){
                lastRow = (page - 1) * limit - 1;
            }else{
                lastRow = (page - 1) * limit;
            }

            if(!searchStudentReq.studentName.equals("")
                    && !searchStudentReq.overTime.equals("")
                    && !searchStudentReq.startTime.equals("")
                    && !searchStudentReq.studentId.equals("")){
                preparedStatement = connection.prepareStatement(SQL_SEARCH_ALL);
                preparedStatement.setString(1, searchStudentReq.startTime);
                preparedStatement.setString(2, searchStudentReq.overTime);
                preparedStatement.setString(3, searchStudentReq.studentId);
                preparedStatement.setString(4, searchStudentReq.studentName);
                preparedStatement.setInt(5, lastRow);
                preparedStatement.setInt(6, limit);
            }else if(!searchStudentReq.studentId.equals("")){
                preparedStatement = connection.prepareStatement(SQL_SEARCH_ID);
                preparedStatement.setString(1, searchStudentReq.studentId);
                preparedStatement.setInt(2, lastRow);
                preparedStatement.setInt(3, limit);
            }else if(!searchStudentReq.studentName.equals("")){
                preparedStatement = connection.prepareStatement(SQL_SEARCH_NAME);
                preparedStatement.setString(1, searchStudentReq.studentName);
                preparedStatement.setInt(2, lastRow);
                preparedStatement.setInt(3, limit);
            }else if(!searchStudentReq.overTime.equals("")
                    && !searchStudentReq.startTime.equals("")){
                preparedStatement = connection.prepareStatement(SQL_SEARCH_TIME);
                preparedStatement.setString(1, searchStudentReq.startTime);
                preparedStatement.setString(2, searchStudentReq.overTime);
                preparedStatement.setInt(3, lastRow);
                preparedStatement.setInt(4, limit);
            }else{
                preparedStatement = connection.prepareStatement(SQL_SEARCH);
                preparedStatement.setInt(1, lastRow);
                preparedStatement.setInt(2, limit);
            }

            resultSet = preparedStatement.executeQuery();

            SearchStudentResp searchStudentResp = new SearchStudentResp();

            while (resultSet.next()){
                StudentInfo studentInfo = new StudentInfo();
                studentInfo.start_semester = resultSet.getString(2);
                studentInfo.over_senester = resultSet.getString(3);
                studentInfo.teachher_id = resultSet.getString(4);
                studentInfo.student_name = resultSet.getString(5);
                studentInfo.guardian_name = resultSet.getString(6);
                studentInfo.school_name = resultSet.getString(7);
                studentInfo.tel_phone = resultSet.getString(8);
                studentInfo.address = resultSet.getString(9);
                studentInfo.remarks = resultSet.getString(10);
                studentInfo.student_photo = resultSet.getString(11);
                studentInfo.guardian_photo_url = resultSet.getString(12);
                studentInfo.student_id = resultSet.getString(13);
                searchStudentResp.studentInfos.add(studentInfo);
            }

            searchStudentResp.limit = limit;
            searchStudentResp.totalPage = totalpages;

            baseResp.data = searchStudentResp;
            baseResp.code = "0";
            baseResp.expand = null;
            baseResp.message = "";


        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseTools.closePrepStmt(preparedStatement);
            DataBaseTools.closeConnection(connection);
            DataBaseTools.closeResultSet(countSet);
            DataBaseTools.closeResultSet(resultSet);
            PrintWriter printWriter = response.getWriter();
            String resultStr = ResponseToJSON.getInstance().Response(baseResp);
            printWriter.write(resultStr);
            printWriter.flush();
            printWriter.close();
        }

    }


}
