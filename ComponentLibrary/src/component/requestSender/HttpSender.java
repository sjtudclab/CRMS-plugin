package component.requestSender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpSender {
    private static String URLHEAD = "http://127.0.0.1:8000/";

    public JSONArray getRequest(String url, String param){
    	System.out.println("getRequest...");
        BufferedReader in = null;
        String result = "";

        try{
            String urlString = URLHEAD + url + "?" + param;
            System.out.println("get url:" + urlString);
            URL realUrl = new URL(urlString);

            URLConnection conn = realUrl.openConnection();
            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = in.readLine()) != null){
                result += line;
            }

            return getJSArray(result);

        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    private JSONArray getJSArray(String result) throws JSONException{
        if(result.charAt(0) == '['){
            JSONArray jsArray = new JSONArray(result);
            return jsArray;
        }else{
            JSONObject jsObject = new JSONObject(result);
            JSONArray jsArray = new JSONArray();
            jsArray.put(jsObject);
            return jsArray;
        }
    }
}

