package reqhead;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class CheckReqHead {

    //APP版本号
    public static final String APPID = "appId";

    //终端类型
    public static final String OS = "os";

    public static final String KEY = "key";

    //时间戳
    public static final String TS = "ts";

    public static final String DETOKEN = "deviceId";

    public static final String ACCESSTOKEN = "accessToken";

    public static final String REFRESH_TOKEN = "refreshToken";

    public static final String VERSION = "version";

    public static boolean CheckPublicParam(ServletRequest request){
        String appid = request.getParameter(APPID);
        String os = request.getParameter(OS);
        String ts = request.getParameter(TS);
        if(appid != null && !appid.equals("")
                && os != null && !os.equals("")
                && ts != null && !ts.equals("")){
            return true;
        }
        return false;
    }

    public static boolean CheckAccessToken(HttpServletRequest request){
        String accessToken = request.getHeader(ACCESSTOKEN);
        if(accessToken != null){
            return true;
        }
        return false;
    }

}
