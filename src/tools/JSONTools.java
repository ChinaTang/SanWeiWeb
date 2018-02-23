package tools;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JSONTools {
    public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength < 0){
            return null;
        }

        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;){
            int readlen = request.getInputStream().read(buffer, i, contentLength);
            if(readlen == -1){
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if(charEncoding == null){
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }
}
