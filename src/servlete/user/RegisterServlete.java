package servlete.user;

import org.json.JSONObject;
import tools.JSONTools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlete extends HttpServlet {

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
        JSONObject jsonObject = new JSONObject(JSONTools.getRequestPostStr(req));
        //数据库操作
    }

}
