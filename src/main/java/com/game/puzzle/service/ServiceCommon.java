package com.game.puzzle.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Iterator;

@Component
class ServiceCommon {

    @Autowired
    RequestUtils requestUtils;

    public JsonNode getMutualFriends(String username1, String username2) {

        ObjectNode result = JsonNodeFactory.instance.objectNode();
        JsonNode user1Details = getUserDetails(username1);
        if (user1Details.get("status").asInt() == 0) {
            return user1Details;
        }
        JsonNode user2Details = getUserDetails(username2);
        if (user2Details.get("status").asInt() == 0) {
            return user2Details;
        }

        Iterator<String> user1Friends = user1Details.fieldNames();
        user1Friends.forEachRemaining(
                z -> {
                    if (user2Details.has(z)) {
                        result.set(z, user1Details.get(z));
                    }
                }
        );
        return result;
    }


    public JsonNode getUserDetails(String username1) {
        String urlTemplate = "https://api.twitter.com/1.1/friends/list.json?screen_name=[USERNAME]&skip_status=true&include_user_entities=false&count=200";
        String apiUrl = urlTemplate.replace("[USERNAME]", username1);
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        try {


            long cursor = -1;
            while (true) {
                JsonNode data = requestUtils.getData(apiUrl + "&cursor=" + cursor);
                JsonNode users = data.get("users");
                for (JsonNode user : users) {
                    ObjectNode userNode = JsonNodeFactory.instance.objectNode();
                    userNode.put("name", user.get("name").asText());
                    String screenName = user.get("screen_name").asText();
                    userNode.put("screen_name", screenName);
                    userNode.put("profile_image_url", user.get("profile_image_url").asText());
                    userNode.put("followers_count", user.get("followers_count").asLong());
                    result.set(screenName, userNode);
                }
                cursor = data.get("next_cursor").asLong();
                if (cursor == 0) {
                    break;
                }
            }
            result.put("status", 1);
        } catch (HttpClientErrorException e) {
            result.put("status", 0);
            result.put("error", "User not found " + username1);
        }
        return result;
    }
}
