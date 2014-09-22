package com.nowopen.encrypt.web.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by pxie on 9/19/2014.
 */
public class ReponseGenerator {

    public static JsonObject getResponse(final int responseCode, String[] response){
        JsonObject reponse = new JsonObject();
        reponse.addProperty("respCode", responseCode);

        if(null != response) {
            reponse.addProperty("valueCount", response.length);

            JsonArray jsonArray = new JsonArray();
            reponse.add("values", jsonArray);

            JsonObject jsonElemenetTemp = null;
            for (int index = 0; index < response.length; index++) {
                jsonElemenetTemp = new JsonObject();
                jsonElemenetTemp.addProperty("" + index, response[index]);
                jsonArray.add(jsonElemenetTemp);
            }
        }

        return reponse;

    }
}
