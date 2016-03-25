package com.xdeathcubex.utils;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class UUIDFetcher {

    private static Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static HashMap<String, String> uuidCache = new HashMap<String, String>();
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static Map<String, String> nameCache = new HashMap<String, String>();
    private String name = null;

    public static String getUUID(String username) {
        if (uuidCache.containsKey(username)) {
            return uuidCache.get(username);
        }
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            InputStream stream = url.openStream();
            InputStreamReader inr = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inr);
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            String result = sb.toString();
            JsonElement element = new JsonParser().parse(result);
            JsonObject obj = element.getAsJsonObject();
            String uuid = obj.get("id").toString();
            uuid = uuid.substring(1);
            uuid = uuid.substring(0, uuid.length() - 1);
            uuidCache.put(username, uuid);
            return uuid;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getName(String uuid) {
        if (nameCache.containsKey(uuid)) {
            return nameCache.get(uuid);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, uuid)).openConnection();
            connection.setReadTimeout(5000);
            UUIDFetcher[] nameHistory = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher[].class);
            UUIDFetcher currentNameData = nameHistory[nameHistory.length - 1];
            nameCache.put(uuid, currentNameData.name);
            return currentNameData.name;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}

