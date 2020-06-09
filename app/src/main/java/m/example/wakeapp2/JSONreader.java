package m.example.wakeapp2;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONreader {


    public String readJSONdata(String s, String tab){
        String response = "";
        try {
            JSONObject jObj = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
            response = jObj.getString(tab);
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
        }
        return response;
    }

}
