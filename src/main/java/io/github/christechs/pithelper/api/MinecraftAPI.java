package io.github.christechs.pithelper.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MinecraftAPI {
    public static String resolveUuid(String nameOrUuid) throws Exception {
        if (nameOrUuid.length() > 16) return nameOrUuid.replace("-", "");

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + nameOrUuid).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject response = new JsonParser().parse(reader).getAsJsonObject();
                reader.close();
                return response.get("id").getAsString();
            }
        } catch (Exception ignored) {
        }

        HttpURLConnection fallbackConn = (HttpURLConnection) new URL("https://api.ashcon.app/mojang/v2/user/" + nameOrUuid).openConnection();
        fallbackConn.setConnectTimeout(5000);
        fallbackConn.setReadTimeout(5000);
        if (fallbackConn.getResponseCode() == 200) {
            InputStreamReader reader = new InputStreamReader(fallbackConn.getInputStream());
            JsonObject response = new JsonParser().parse(reader).getAsJsonObject();
            reader.close();
            return response.get("uuid").getAsString().replace("-", "");
        }

        throw new RuntimeException("Minecraft player not found or all APIs are rate-limited.");
    }
}
