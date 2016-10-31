package us.swiftex.custominventories.utils.server;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import us.swiftex.custominventories.CustomInventories;

public class PingResponse {

    private boolean isOnline;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;

    public PingResponse(boolean isOnline, String motd, int onlinePlayers, int maxPlayers) {
        this.isOnline = isOnline;
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    public PingResponse(String jsonString, ServerAddress address) {

        if (jsonString == null || jsonString.isEmpty()) {
            motd = "Invalid ping response";
            CustomInventories.getPlugin().getLogger().severe("Received empty Json response from IP \"" + address.toString() + "\"!");
            return;
        }

        Object jsonObject = JSONValue.parse(jsonString);

        if (!(jsonObject instanceof JSONObject)) {
            motd = "Invalid ping response";
            CustomInventories.getPlugin().getLogger().severe("Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
            return;
        }

        JSONObject json = (JSONObject) jsonObject;
        isOnline = true;

        Object descriptionObject = json.get("description");

        if (descriptionObject != null) {
            motd = descriptionObject.toString();
        } else {
            motd = "Invalid ping response";
            CustomInventories.getPlugin().getLogger().severe("Received invalid Json response from IP \"" + address.toString() + "\": " + jsonString);
        }

        Object playersObject = json.get("players");

        if (playersObject instanceof JSONObject) {
            JSONObject playersJson = (JSONObject) playersObject;

            Object onlineObject = playersJson.get("online");
            if (onlineObject instanceof Number) {
                onlinePlayers = ((Number) onlineObject).intValue();
            }

            Object maxObject = playersJson.get("max");
            if (maxObject instanceof Number) {
                maxPlayers = ((Number) maxObject).intValue();
            }
        }
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getMotd() {
        return motd;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}