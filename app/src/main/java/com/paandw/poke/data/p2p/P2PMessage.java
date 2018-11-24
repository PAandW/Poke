package com.paandw.poke.data.p2p;

import org.json.JSONException;
import org.json.JSONObject;

public class P2PMessage {
    public String message;
    public String time;
    public boolean isMine;

    public P2PMessage(String message, String time, boolean isMine) {
        this.message = message;
        this.time = time;
        this.isMine = isMine;
    }

    public P2PMessage(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            message = (String) json.get("message");
            time = (String) json.get("time");
            isMine = (boolean) json.get("is_mine");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAsJsonString() {
        try {
            JSONObject json = new JSONObject()
                    .put("message", message)
                    .put("time", time)
                    .put("is_mine", isMine);

            return json.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
