package com.br.gabrielmartins.syntri.utils.ip;

import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPUtils {

    public static String getCountryByIP(String ip) {
        try {
            String url = "http://ip-api.com/json/" + ip + "?fields=country";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject myResponse = new JSONObject(response.toString());

            if (myResponse.has("country")) {
                String country = myResponse.getString("country");
                return mapCountryToLanguage(country);
            } else {

                return "br";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "br";
        }
    }

    private static String mapCountryToLanguage(String country) {
        switch (country.toLowerCase()) {
            case "brazil":
            case "brasil":
                return "br";
            case "united states":
            case "usa":
                return "us";
            case "united kingdom":
            case "uk":
                return "uk";
            case "japan":
            case "jp":
                return "japao";
            case "china":
            case "cn":
                return "china";
            case "hungary":
            case "hu":
                return "hungria";
            default:
                return "br";
        }
    }

    public static String getPlayerLanguage(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();

        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            return "br";
        }

        return getCountryByIP(ip);
    }

}
