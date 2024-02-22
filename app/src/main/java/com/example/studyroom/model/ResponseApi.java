package com.example.studyroom.model;
import org.json.JSONObject;

public class ResponseApi {
    private int responseCode;
    private JSONObject responseObject;

   /*public ResponseApi(int responseCode, JSONObject responseObject) {
        this.responseCode = responseCode;
        this.responseObject = responseObject;
    }*/

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public JSONObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(JSONObject responseObject) {
        this.responseObject = responseObject;
    }
}
