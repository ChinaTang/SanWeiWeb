package resq;

import org.json.JSONObject;

public class StandardResponse {

    public static final String SUCCESS = "0";

    public static JSONObject respJSON
            (JSONObject result, String code, String message, JSONObject expand){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("message", message);
        jsonObject.put("data", result);
        jsonObject.put("expand", expand);
        return jsonObject;
    }

}
