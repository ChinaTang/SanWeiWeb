package tools;

import com.google.gson.Gson;
import netbean.resp.BaseResp;
import org.json.JSONObject;

public class ResponseToJSON{

    private Gson gson;

    public static ResponseToJSON _instance = null;

    private ResponseToJSON(){
        gson = new Gson();
    }

    public static ResponseToJSON getInstance(){
        if(_instance == null){
            _instance = new ResponseToJSON();
        }
        return _instance;
    }



    public String Response(BaseResp baseResp){
        return gson.toJson(baseResp);
    }

}
