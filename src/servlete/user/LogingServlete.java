package servlete.user;

import org.json.JSONObject;
import resq.StandardResponse;
import tools.JSONTools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogingServlete extends HttpServlet{

    private static final String TAG = "LogingServlete";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        JSONObject jsonObject = new JSONObject(JSONTools.getRequestPostStr(req));
        System.out.println(TAG + "====>" + jsonObject.toString());
        //验证数据库中是否存在账户密码
        JSONObject respJSON = new JSONObject();
        respJSON.put("accessToken", "1234");
        JSONObject result = StandardResponse.respJSON(respJSON, StandardResponse.SUCCESS, "", new JSONObject());
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(result.toString());
        printWriter.flush();
        printWriter.close();
    }
}
